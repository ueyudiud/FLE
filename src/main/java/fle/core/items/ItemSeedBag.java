/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items;

import fle.core.FLE;
import nebula.common.item.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemSeedBag extends ItemBase
{
	public ItemSeedBag()
	{
		super(FLE.MODID, "seedbag");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
