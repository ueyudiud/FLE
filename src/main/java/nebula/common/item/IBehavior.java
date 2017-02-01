package nebula.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

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

public interface IBehavior
{
	List<IBehavior> NONE = ImmutableList.of();
	
	boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity);
	
	boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player);
	
	boolean onEntityItemUpdate(EntityItem entity);
	
	ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand);
	
	EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
	
	EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand);
	
	boolean onRightClickEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target,
			EnumHand hand);
	
	boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);
	
	void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft);
	
	@Nullable
	ItemStack onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected);
	
	void onUsingTick(ItemStack stack, EntityLivingBase player, int count);
	
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack stack, EntityPlayer player, UnlocalizedList list,
			boolean advanced);
}