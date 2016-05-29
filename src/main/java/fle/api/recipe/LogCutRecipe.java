package fle.api.recipe;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ObjectArrays;

import farcore.enums.EnumItem;
import farcore.lib.recipe.ICraftingInventoryMatching;
import farcore.lib.recipe.ISingleInputRecipe;
import farcore.lib.recipe.ShapelessFleRecipe;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class LogCutRecipe extends ShapelessFleRecipe
{
	public LogCutRecipe(Object...objects)
	{
		super(-1, ObjectArrays.concat(objects, new LogCutSingleInputRecipe()));
	}
	
	@Override
	public int getRecipeTick(ICraftingInventoryMatching inventory)
	{
		for(int i = 0; i < inventory.getCraftingMatrixSize(); ++i)
		{
			if(inventory.getStackInMatrix(i) == null) continue;
			if(EnumItem.log.item() == inventory.getStackInMatrix(i).getItem())
			{
				ItemStack stack = inventory.getStackInMatrix(i);
				SubstanceWood wood = SubstanceWood.getSubstance(stack.getItemDamage());
				int length = U.Inventorys.setupNBT(stack, false).getShort("length");
				return 10 + (int) ((1F + wood.hardness / 10F) * (length - 1) * 30);
			}
		}
		return 10;
	}
	
	private static class LogCutSingleInputRecipe implements ISingleInputRecipe
	{
		public LogCutSingleInputRecipe()
		{
			
		}

		@Override
		public boolean similar(ItemStack stack)
		{
			return stack != null && EnumItem.log.item() == stack.getItem();
		}

		@Override
		public boolean match(ItemStack stack)
		{
			return similar(stack) && stack.stackSize > 0;
		}

		@Override
		public ItemStack[] output(ItemStack stack)
		{
			int length = U.Inventorys.setupNBT(stack, false).getShort("length");
			ItemStack stack2 = EnumItem.log_block.instance(length, SubstanceWood.getSubstance(stack.getItemDamage()));
			stack = stack.copy();
			if(--stack.stackSize <= 0)
			{
				stack = null;
			}
			return new ItemStack[]{stack2, stack};
		}

		@Override
		public ItemStack instance()
		{
			return EnumItem.log_block.instance(1);
		}
		
		private List<ItemStack> display;

		@Override
		public List<ItemStack> display()
		{
			if(display == null)
			{
				Builder<ItemStack> builder = ImmutableList.builder();
				for(SubstanceWood wood : SubstanceWood.getWoods())
				{
					if(wood != SubstanceWood.WOOD_VOID)
					{
						builder.add(EnumItem.log.instance(1, wood));
					}
				}
				display = builder.build();
			}
			return display;
		}

		@Override
		public boolean valid()
		{
			return EnumItem.log.initialised();
		}		
	}
}