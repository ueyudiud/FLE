/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.ai;

import java.util.List;

import farcore.lib.entity.animal.IAnimalAccess;
import nebula.base.Judgable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.Vec3d;

/**
 * @author ueyudiud
 */
public class EntityAIAvoidPlayerWhenHitIt<T extends EntityAnimal & IAnimalAccess> extends EntityAIBase
{
	/** The entity we are attached to */
	protected final T entity;
	private final double farSpeed;
	private final double nearSpeed;
	protected EntityPlayer closestLivingEntity;
	private final float avoidDistance;
	private final float fastAvoidDistanceSq;
	/** The PathEntity of our entity */
	private Path entityPathEntity;
	/** The PathNavigate of our entity */
	private final PathNavigate entityPathNavigate;
	private final Judgable<Entity> avoidTargetSelector;
	
	public EntityAIAvoidPlayerWhenHitIt(T eneity, double farSpeed, double nearSpeed, float avoidDistance, float fastAvoidDistance)
	{
		this.entity = eneity;
		this.farSpeed = farSpeed;
		this.nearSpeed = nearSpeed;
		this.avoidDistance = avoidDistance;
		this.fastAvoidDistanceSq = fastAvoidDistance * fastAvoidDistance;
		this.entityPathNavigate = eneity.getNavigator();
		this.avoidTargetSelector = Judgable.<Entity, EntityPlayer>matchAndCast(player->player.isEntityAlive() && this.entity.getEntitySenses().canSee(player) && !player.isCreative() && !player.isSpectator(), EntityPlayer.class);
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (!this.entity.doesAnimalAfraidPlayer()) return false;
		List<EntityPlayer> list = this.entity.world.getEntitiesWithinAABB(EntityPlayer.class, this.entity.getEntityBoundingBox().expand(this.avoidDistance, 3.0D, this.avoidDistance), this.avoidTargetSelector::isTrue);
		
		if (list.isEmpty())
		{
			return false;
		}
		else
		{
			this.closestLivingEntity = list.get(0);
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
			
			if (vec3d == null)
			{
				return false;
			}
			else if (this.closestLivingEntity.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.entity))
			{
				return false;
			}
			else
			{
				this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
				return this.entityPathEntity != null;
			}
		}
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return !this.entityPathNavigate.noPath();
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
	}
	
	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.closestLivingEntity = null;
	}
	
	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		if (this.entity.getDistanceSqToEntity(this.closestLivingEntity) < this.fastAvoidDistanceSq)
		{
			this.entity.getNavigator().setSpeed(this.nearSpeed);
		}
		else
		{
			this.entity.getNavigator().setSpeed(this.farSpeed);
		}
	}
}