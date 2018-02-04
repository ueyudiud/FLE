/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.entity.monster;

import farcore.lib.entity.IEntityDamageEffect;
import fle.core.entity.pathfinding.PathNavigateGroundExt;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class EntityFLESkeleton extends EntitySkeleton implements IEntityDamageEffect
{
	public EntityFLESkeleton(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected PathNavigate createNavigator(World worldIn)
	{
		return new PathNavigateGroundExt(this, worldIn);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		return data;
	}
}
