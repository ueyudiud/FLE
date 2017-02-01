package fle.core.items.tool;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolSpear extends Tool
{
	public ToolSpear()
	{
		super(EnumToolTypes.SPEAR);
	}
	
	@Override
	public EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.PUNCTURE;
	}
	
	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return -0.1F;
	}
	
	@Override
	public float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos,
			IBlockState block)
	{
		return 2.0F;
	}
	
	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return 0.8F;
	}
	
	@Override
	public float getDamageVsEntity(ItemStack stack)
	{
		return 2.0F;
	}
	
	@Override
	public boolean isShootable()
	{
		return true;
	}
	
	@Override
	public boolean isWeapon()
	{
		return true;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) prodded by (S)";
	}
}