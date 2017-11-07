/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block;

import java.util.Random;

import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author ueyudiud
 *
 * @param <T> The tile entity class.
 */
public interface ITileEntityBehavior<T>
{
	default void breakBlock(T tile, IBlockState state)
	{
	}
	
	default void causeUpdate(T tile, IBlockState state, boolean isTileUpdate)
	{
	}
	
	default void onUpdateTick(T tile, IBlockState state, Random random, boolean isTickRandomly)
	{
	}
	
	@SideOnly(Side.CLIENT)
	default void randomDisplayTick(T tile, IBlockState stateIn, Random rand)
	{
	}
	
	default EnumActionResult onBlockActivated(T tile, EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}
	
	default boolean onBlockClicked(T tile, EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	default void onEntityCollidedWithBlock(T tile, IBlockState state, Entity entity)
	{
	}
	
	default void onEntityWalk(T tile, Entity entity)
	{
	}
	
	default void onEntityFallenUpon(T tile, Entity entityIn, float fallDistance)
	{
	}
	
	default void onEntityLanded(T tile, Entity entity)
	{
	}
	
	default void onPlantGrow(T tile, IBlockState state, BlockPos source)
	{
	}
	
	default ActionResult<Float> onToolClick(T tile, EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	default ActionResult<Float> onToolUse(T tile, EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ, long tick)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
}
