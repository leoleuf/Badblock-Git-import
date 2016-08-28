package fr.badblock.gameapi.utils.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;

/**
 * Classe d'aide � la gestion des diff�rentes cr�atures MineCraft.
 * 
 * @author LeLanN
 */
public class CreatureUtils {
	/**
	 * Fait spawn une entit�.
	 * 
	 * @param location
	 *            La position du spawn
	 * @param clazz
	 *            Le type d'entit� � faire spawn
	 */
	public static void spawn(Location location, Class<? extends Entity> clazz) {
		location.getWorld().spawn(location, clazz);
	}

	/**
	 * Recherche toutes les entit�s proche d'un certain point.
	 * 
	 * @param location
	 *            La position centrale
	 * @param radius
	 *            La distance maximum entre l'entit� et la position
	 * 
	 * @return Les entit�s trouv�es
	 */
	public static Collection<Entity> getNearbyEntities(Location location, double radius) {
		List<Entity> entities = new ArrayList<>();

		for (Entity e : location.getWorld().getEntities()) {
			if (e.getLocation().distance(location) <= radius) {
				entities.add(e);
			}
		}

		return entities;
	}

	/**
	 * Change le yaw et le pitch de la location pour regarder vers une autre
	 * 
	 * @param from
	 *            La location � changer
	 * @param to
	 *            La location � regarder
	 * @param aiming
	 *            La pr�cision (100 = pr�cision totale)
	 * @return La location chang�e
	 */
	public static Location lookAt(Location from, Location to, float aiming) {
		from = from.clone();

		double dx = to.getX() - from.getX();
		double dy = to.getY() - from.getY();
		double dz = to.getZ() - from.getZ();

		if (dx != 0) {
			from.setYaw((float) ((dx < 0 ? 1.5 : 0.5) * Math.PI));
			from.setYaw(from.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0) {
			from.setYaw((float) Math.PI);
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		from.setPitch((float) -Math.atan(dy / dxz));

		float yawAiming = 360 - (aiming * 360 / 100);
		if (yawAiming > 0)
			yawAiming = MathsUtils.integerRandomInclusive((int) yawAiming, (int) -yawAiming);

		float pitchAiming = 180 - (aiming * 180 / 100);
		if (pitchAiming > 0)
			pitchAiming = MathsUtils.integerRandomInclusive((int) pitchAiming, (int) -pitchAiming);

		from.setYaw(-from.getYaw() * 180f / (float) Math.PI + yawAiming);
		from.setPitch(from.getPitch() * 180f / (float) Math.PI + pitchAiming);

		return from;
	}

	/**
	 * Permet de r�cup�rer une entit� par son UUID.
	 * 
	 * @param world
	 *            Le monde o� se trouve l'entit�
	 * @param entityId
	 *            L'UUID de l'entit�.
	 * @return L'entit� si elle est trouv�e (autrement, null).
	 */
	public static Entity getEntityByUUID(World world, UUID entityId) {
		for (Entity entity : world.getEntities()) {
			if (entity.getUniqueId().equals(entityId))
				return entity;
		}

		return null;
	}

	/**
	 * Recherche la version modifi�e (par l'API) d'une cr�ature. Si l'API n'a
	 * pas sa classe de register ou que l'entit� n'est pas g�r�e, retourne null.
	 * 
	 * @param entity
	 *            L'entit�.
	 * @return La version modifi�e de la cr�ature. Attention, peut �tre null !
	 */
	public static CustomCreature getAsCustom(Entity entity) {
		Object handler = ReflectionUtils.getHandle(entity);

		// System.out.println(handler);

		if (handler instanceof CustomCreature)
			return (CustomCreature) handler;
		return null;
	}
}
