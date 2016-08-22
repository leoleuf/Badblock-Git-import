package fr.badblock.permissions;

import lombok.Getter;

/**
 * Représente une permission
 * Classe compatible Spigot/BungeeCord/Ladder
 * @author LeLanN
 */
@Getter public class Permission {
	private boolean antiPermission;
	private String permission;
	private boolean all;
	
	public Permission(String permission){
		if(permission.startsWith("-")){
			antiPermission = true;
			permission = permission.substring(1);
		} else antiPermission = false;
		
		if(permission.endsWith("*")){
			all = true;
			permission = permission.substring(0, permission.length() - 1);
		} else all = false;
		
		if(permission.endsWith("."))
			permission = permission.substring(0, permission.length() - 1);

		this.permission = permission.toLowerCase();
	}
	
	/**
	 * Vérifie si la permission correspond, et, dans ce cas, si  le joueur à la permission :
	 * Si la permission est sous la forme -permission, la réponse sera 'NO'
	 * Si la permission est introuvable, la réponse sera 'UNKNOW'
	 * Sinon, la réponse sera 'YES'
	 * 
	 * La permission correspond, si la permission est la même, ou si la permission donnée
	 * en paramètre est une sous-partie de la permission donnée, et qu'elle est sous la forme
	 * permission.*
	 * 
	 * Par exemple, sera reconnu comme même permission :
	 * - example.* (Permission) et example.command (paramètre)
	 * - * (Permission) et n'importe quelle permission
	 * 
	 * Ne sera pas reconnu comme même permission :
	 * - example2.* (Permission) et example.command (paramètre)
	 * - example.test (Permission) et example.command (paramètre)
	 * 
	 * @param perm La permission a tester
	 * @return La réponse
	 */
	public Reponse has(Permission perm){
		if(perm.permission.equals(permission)
			|| (perm.permission.startsWith(permission) && all)){
			return antiPermission == perm.antiPermission ? Reponse.YES : Reponse.NO;
		}
		
		return Reponse.UNKNOW;
	}
	
	@Override
	public String toString(){
		return (antiPermission ? "-" : "") + permission + (all ? (permission.isEmpty() ? "*" : ".*") : "");
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	/**
	 * Représente la réponse de Permission lorsque l'on check si une autre permission correspond
	 * Classe compatible Spigot/BungeeCord/Ladder
	 * @author LeLanN
	 */
	public enum Reponse {
		/**
		 * Les deux permissions sont correspondent [voir Permission.has()] et le joueur a bien la permission
		 */
		YES,
		/**
		 * Les deux permissions ne correspondent pas [voir Permission.has()], mais une anti-permission [voir Permission.has()] fait que le joueur n'a pas la permission
		 */
		NO,
		/**
		 * Les deux permissions ne correspondent pas [voir Permission.has()]
		 */
		UNKNOW;
	}
}
