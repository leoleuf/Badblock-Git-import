package fr.badblock.game.v1_8_R3.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.PathfinderGoalTargetNearestPlayer;
import net.minecraft.server.v1_8_R3.World;

public class NMSGhast extends EntityGhast implements NMSCustomCreature {
	@Getter@Setter
	private ControllerMove    normalController;
	
	@Getter@Setter
	public CreatureBehaviour  creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;

	public NMSGhast(World world) {
		super(world);

		flags = new ArrayList<>();
		EntityUtils.prepare(this);
	}
	
	@Override
	public boolean isInvulnerable(DamageSource damageSource){
		if(hasCreatureFlag(CreatureFlag.INVINCIBLE))
			return true;
		else return super.isInvulnerable(damageSource);
	}

	@Override
	public boolean a(EntityHuman entityhuman){
		return EntityUtils.rightClick(this, entityhuman);
	}

	@Override
	protected void E() {
		EntityUtils.moveFlying(this);
	}

	@Override
	public void g(float sideMot, float forMot){
		EntityUtils.move(this, sideMot, forMot);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		return EntityUtils.damageEntity(this, damagesource, f);
	}
	
	@Override
	public void regenerateAttributes(){
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		buildGoalSelector(5, EntityGhast.class, "PathfinderGoalGhastIdleMove", new Class[]{EntityGhast.class}, this);
		
	    if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) return;
		
		buildGoalSelector(7, EntityGhast.class, "PathfinderGoalGhastMoveTowardsTarget", new Class[]{EntityGhast.class}, this);
		buildGoalSelector(7, EntityGhast.class, "PathfinderGoalGhastAttackTarget", new Class[]{EntityGhast.class}, this);
	    
	    this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));		
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
	}
}
