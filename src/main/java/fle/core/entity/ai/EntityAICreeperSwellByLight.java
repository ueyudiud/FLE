/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

/**
 * @author ueyudiud
 */
public class EntityAICreeperSwellByLight extends EntityAIBase
{
	/** The creeper that is swelling. */
	EntityCreeper	swellingCreeper;
	BlockPos		target;
	
	public EntityAICreeperSwellByLight(EntityCreeper entitycreeperIn)
	{
		this.swellingCreeper = entitycreeperIn;
		setMutexBits(1);
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (this.swellingCreeper.getAttackTarget() != null) return false;
		return checkLight() && this.swellingCreeper.world.getClosestPlayerToEntity(this.swellingCreeper, 64.0) != null;
	}
	
	private boolean checkLight()
	{
		int light = this.swellingCreeper.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.swellingCreeper));
		return light >= 10;
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.swellingCreeper.getNavigator().clearPathEntity();
	}
	
	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
	}
	
	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		if (this.swellingCreeper.getAttackTarget() != null || !checkLight())
		{
			this.swellingCreeper.setCreeperState(-1);
		}
		else
		{
			this.swellingCreeper.setCreeperState(2);
		}
	}
}
