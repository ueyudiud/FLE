/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.monster;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.entity.IEntityDamageEffect;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import fle.loader.IBF;
import nebula.common.util.L;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class EntityFLEZombie extends EntityZombie implements IEntityDamageEffect
{
	public EntityFLEZombie(World worldIn)
	{
		super(worldIn);
	}
	
	//	@Override
	//	protected PathNavigate createNavigator(World worldIn)
	//	{
	//		return new PathNavigateGroundExt(this, worldIn);
	//	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		if (getHeldItemMainhand() == null && getRNG().nextInt(4) == 0)
		{
			ItemStack stack = new ItemStack(IBF.tool, 1, 4);
			ItemTool.setMaterialToItem(stack, "head", L.random(Mat.filt(MC.hard_hammer_flint), getRNG()));
			ItemTool.setMaterialToItem(stack, "tie", L.random(Mat.filt(SubTags.ROPE), getRNG()));
			ItemTool.setMaterialToItem(stack, "handle", L.random(Mat.filt(SubTags.HANDLE), getRNG()));
		}
		return data;
	}
	
	@Override
	public float getDamageMultiplier(EnumPhysicalDamageType type)
	{
		switch (type)
		{
		case CUT : return 1.2F;
		default :
		case PUNCTURE : return 1.0F;
		case SMASH : return 0.8F;
		}
	}
}