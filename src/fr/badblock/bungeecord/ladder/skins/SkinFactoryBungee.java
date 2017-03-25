package fr.badblock.bungeecord.ladder.skins;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

import fr.badblock.bungeecord.UserConnection;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.connection.InitialHandler;
import fr.badblock.bungeecord.connection.LoginResult;
import fr.badblock.bungeecord.connection.LoginResult.Property;
import fr.badblock.bungeecord.ladder.LadderBungee;
import fr.badblock.bungeecord.ladder.skins.format.SkinProfile;
import fr.badblock.bungeecord.ladder.skins.format.SkinProperty;
import fr.badblock.bungeecord.ladder.skins.storage.SkinStorage;

public class SkinFactoryBungee {

	private static Field profileField = null;
	public static SkinFactoryBungee skinfactory;
	public SkinFactoryBungee(){
		skinfactory = this;
		profileField = getProfileField();
	}
	public static SkinFactoryBungee getFactory(){
		return skinfactory;
	}
	private static Field getProfileField() {
		try {
			Field profileField = InitialHandler.class.getDeclaredField("loginProfile"); 
			profileField.setAccessible(true);
			return profileField;
		} catch (Throwable t) {
			System.err.println("Failed to get method handle for initial handel loginProfile field");
			t.printStackTrace();
		}
		return null;
	}
	
	//Apply the skin to the player.
	public void applySkin(ProxiedPlayer player) {
		applySkin(player, player.getUniqueId());
	}
	
	public void applySkin(final ProxiedPlayer player, final UUID uuid){
		ProxyServer.getInstance().getScheduler().runAsync(LadderBungee.getInstance(), new Runnable() {
			@Override
			public void run() {


				SkinProfile skinprofile = SkinStorage.getInstance().getOrCreateSkinData(player.getName().toLowerCase());
				skinprofile.applySkin(new SkinProfile.ApplyFunction() {
					@Override
					public void applySkin(SkinProperty property) {
						try {
							Property textures = new Property(property.getName(), property.getValue(), property.getSignature());
							InitialHandler handler = (InitialHandler) player.getPendingConnection();

							LoginResult profile = new LoginResult(uuid.toString(), new Property[] { textures });
							Property[] present = profile.getProperties();
							Property[] newprops = new Property[present.length + 1];
							System.arraycopy(present, 0, newprops, 0, present.length);
							newprops[present.length] = textures;
							profile.getProperties()[0].setName(newprops[0].getName());
							profile.getProperties()[0].setValue(newprops[0].getValue());
							profile.getProperties()[0].setSignature(newprops[0].getSignature());
							profileField.set(handler, profile);
							updateSkin(player, profile, false);

						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
				});
			}
		}
				);
	}

	//Remove skin from player
	public void removeSkin(ProxiedPlayer player){
		//	if (SkinsRestorer.getInstance().isAutoInEnabled()){
		//	if (ConfigStorage.getInstance().USE_AUTOIN_SKINS==true&&SkinsRestorer.getInstance().getAutoInAPI().getPremiumStatus(player.getName())==com.gmail.bartlomiejkmazur.autoin.api.PremiumStatus.PREMIUM){
		//			return;
		//		}
		//	}
		LoginResult profile = ((UserConnection) player).getPendingConnection().getLoginProfile();
		updateSkin(player, profile, true); //Removing the skin.
	}

	public void updateSkin(final ProxiedPlayer player, final LoginResult profile, final boolean removeSkin){
		ProxyServer.getInstance().getScheduler().runAsync(LadderBungee.getInstance(), new Runnable() {
			@Override
			public void run() {

				final UserConnection user = (UserConnection) player;
				String[][] props = new String[ profile.getProperties().length ][];
				for ( int i = 0; i < props.length; i++ )
				{
					props[ i ] = new String[]
							{
									profile.getProperties()[ i ].getName(),
									profile.getProperties()[ i ].getValue(),
									profile.getProperties()[ i ].getSignature()
							};
				}
				if (player.getServer()==null){
					return;
				}
				sendUpdateRequest(user, removeSkin);
			}});
	}
	public void sendUpdateRequest( UserConnection p, boolean removeSkin ) {
		final ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("ForwardToPlayer");
			out.writeUTF(p.getName());
			if (removeSkin==true){
				out.writeBoolean(true);
			}else{
				out.writeBoolean(false);
			}

			p.getServer().sendData("SkinUpdate", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// Refletion stuff down there. 
	protected static void setValue(Object owner, Field field, Object value) throws Exception { 
		makeModifiable(field); 
		field.set(owner, value); 
	}

	protected static void makeModifiable(Field nameField) throws Exception {
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}
	@SuppressWarnings("unused")
	private Object getValue(Object instance, String field) throws Exception {
		Field f = instance.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.get(instance);
	}
}