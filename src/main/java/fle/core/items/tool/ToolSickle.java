package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolSickle extends Tool
{
	public ToolSickle()
	{
		super(EnumToolTypes.SICKLE);
	}
	
	@Override
	public float getAttackSpeed(ItemStack stack, float mutiplier)
	{
		return -1.0F + mutiplier * 0.4F;
	}
	
	@Override
	public float getToolDamagePerBreak(ItemStack stack, EntityLivingBase user, World world, BlockPos pos,
			IBlockState block)
	{
		return 1.0F;
	}
	
	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return 1.5F;
	}
	
	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return 3.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) harvested by (S) as crop";
	}
}