/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile;

import farcore.lib.solid.SolidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class TE07InventoryArray extends TE07Solid
{
	protected final ItemStack[] itemStacks;
	protected final FluidStack[] fluidStacks;
	protected final SolidStack[] solidStacks;
	
	public TE07InventoryArray(int itemSize, int fluidSize, int solidSize)
	{
		this.itemStacks = new ItemStack[itemSize];
		this.fluidStacks = new FluidStack[fluidSize];
		this.solidStacks = new SolidStack[solidSize];
	}
}
