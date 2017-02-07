/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.monster;

import farcore.data.EnumPhysicalDamageType;
import farcore.lib.entity.IEntityDamageEffect;
import fle.core.entity.ai.EntityAICreeperSwellByLight;
import fle.core.pathfinding.PathNavigateGroundExt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class EntityFLECreeper extends EntityCreeper implements IEntityDamageEffect
{
	public EntityFLECreeper(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(3, new EntityAICreeperSwellByLight(this));
	}
	
	@Override
	protected PathNavigate createNavigator(World worldIn)
	{
		return new PathNavigateGroundExt(this, worldIn);
	}
	
	@Override
	public float getDamageMultiplier(EnumPhysicalDamageType type)
	{
		switch (type)
		{
		default : return 1.0F;
		case CUT : return 0.9F;
		case SMASH : return 0.9F;
		case PUNCTURE : return 1.2F;
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.isExplosion())
		{
			setCreeperState(100);
		}
		else if (!source.isProjectile())
		{
			setCreeperState(10);
		}
		return super.attackEntityFrom(source, amount);
	}
}