/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import javax.annotation.Nullable;

import nebula.common.fluid.FluidStackExt;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;

/**
 * Use to drain or fill fluid from player.
 * 
 * @author ueyudiud
 * @see nebula.common.inventory.InventoryHelper#drainOrFillTank(IFluidHandlerIO,
 *      EntityPlayer, EnumHand, Direction, ItemStack, byte)
 */
public interface IFluidHandlerIO
{
	/**
	 * Match side can extract item.
	 * 
	 * @return
	 */
	boolean canExtractFluid(Direction to);
	
	/**
	 * Match side can insert item.
	 * 
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canInsertFluid(Direction from, @Nullable FluidStack stack);
	
	FluidStackExt extractFluid(int amount, Direction to, boolean simulate);
	
	FluidStackExt extractFluid(FluidStack suggested, Direction to, boolean simulate);
	
	int insertFluid(FluidStack stack, Direction from, boolean simulate);
}
