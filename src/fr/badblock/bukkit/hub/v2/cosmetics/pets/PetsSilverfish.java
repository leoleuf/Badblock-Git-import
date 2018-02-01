package fr.badblock.bukkit.hub.v2.cosmetics.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Silverfish;

public class PetsSilverfish extends CustomPet
{
	
	public PetsSilverfish()
	{
		super(Silverfish.class, true);
	}

	@Override
	public void onSpawn(LivingEntity livingEntity)
	{
		// Nothing there yet
	}

}