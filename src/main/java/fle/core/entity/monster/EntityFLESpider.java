/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.entity.monster;

import farcore.data.EnumPhysicalDamageType;
import farcore.lib.entity.IEntityDamageEffect;
import fle.core.entity.pathfinding.PathNavigateClimberExt;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class EntityFLESpider extends EntitySpider implements IEntityDamageEffect
{
	public EntityFLESpider(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected PathNavigate createNavigator(World worldIn)
	{
		return new PathNavigateClimberExt(this, worldIn);
	}
	
	@Override
	public float getDamageMultiplier(EnumPhysicalDamageType type)
	{
		switch (type)
		{
		case SMASH:
			return 1.1F;
		default:
		case CUT:
			return 1.0F;
		case PUNCTURE:
			return 1.1F;
		}
	}
}
