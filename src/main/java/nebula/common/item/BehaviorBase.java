/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.item;

import nebula.client.util.UnlocalizedList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BehaviorBase implements IBehavior
{
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity)
	{
		return false;
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entity)
	{
		return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}
	
	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		return EnumActionResult.PASS;
	}
	
	@Override
	public boolean onRightClickEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
	{
		return false;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return false;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		
	}
	
	@Override
	public ItemStack onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		return stack;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean advanced)
	{
		
	}
}
