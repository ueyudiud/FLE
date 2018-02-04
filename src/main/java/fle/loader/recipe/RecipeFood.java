/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import farcore.data.MC;
import fle.api.recipes.SingleInputMatch;
import fle.core.items.ItemSimpleFluidContainer;
import fle.loader.IBFS;
import nebula.base.function.Selector;
import nebula.common.fluid.container.FluidContainerHandler;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class RecipeFood
{
	public static void init()
	{
		ItemStack output = IBFS.iCropRelated.getSubItem("plant_waste");
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("citrus")), 250, output, new FluidStack(IBFS.fsJuice[1], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("bitter_orange")), 250, output, new FluidStack(IBFS.fsJuice[2], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("lemon")), 250, output, new FluidStack(IBFS.fsJuice[3], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("tangerine")), 250, output, new FluidStack(IBFS.fsJuice[4], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("lime")), 250, output, new FluidStack(IBFS.fsJuice[6], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("orange")), 250, output, new FluidStack(IBFS.fsJuice[7], 50));
		addSqueezingRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("grapefruit")), 250, output, new FluidStack(IBFS.fsJuice[8], 50));
	}
	
	private static void addSqueezingRecipe(AbstractStack input, int bychance, ItemStack byproduct, FluidStack output)
	{
		addShapelessRecipe(IBFS.iFluidContainer.getSubItem("bowl_wooden"), new SingleInputMatch(new AbstractStack()
		{
			@Override
			public int size(ItemStack stack)
			{
				return 1;
			}
			
			@Override
			public boolean similar(ItemStack stack)
			{
				FluidStack f;
				return stack.getItem() instanceof ItemSimpleFluidContainer && ((f = ItemSimpleFluidContainer.getFluid(stack)) == null || f.isFluidEqual(output));
			}
			
			@Override
			public ItemStack instance()
			{
				return IBFS.iFluidContainer.getSubItem("bowl_wooden");
			}
		}, (i, o) -> FluidContainerHandler.fillContainer(o, FluidStacks.sizeOf(output, ItemSimpleFluidContainer.getFluidAmount(i) + output.amount)), null), MC.chip_rock.orePrefix,
				new SingleInputMatch(input, Selector.forChance(null, byproduct, bychance).anyTo().andThen(ItemStacks.COPY_ITEMSTACK).anyTo()));
	}
}
