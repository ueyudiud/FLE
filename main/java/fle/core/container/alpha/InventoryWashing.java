package fle.core.container.alpha;

import farcore.FarCore;
import farcore.enums.EnumDamageResource;
import farcore.enums.EnumToolType;
import farcore.inventory.Inventory;
import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import fle.api.recipe.WashingRecipe;
import fle.load.Langs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryWashing extends Inventory
{
	static final int input = 0;
	static final int tool = 1;
	static final int output_min = 2;
	static final int output_max = 11;
	
	private WashingRecipe.$Recipe recipe;
	int progress;
	
	public InventoryWashing()
	{
		super(11, Langs.inventoryWashing, 64);
	}
	
	public void tryWashingRecipe(EntityPlayer player)
	{
		if(EnumToolType.bar_grizzly.stack().similar(stacks[tool]))
		{
			if(recipe == null)
			{
				recipe = WashingRecipe.matchRecipe(stacks[input]);
				progress = 0;
				if(recipe != null)
				{
					decrStack(input, recipe.input, true);
					U.Inventorys.damage(stacks[tool], player, 1, EnumDamageResource.USE, false);
					++progress;
				}
			}
			else
			{
				U.Inventorys.damage(stacks[tool], player, 1, EnumDamageResource.USE, false);
				if(++progress >= 8)
				{
					if(addStacks(output_min, output_max, U.Lang.cast(recipe.output.maxDrop(), ItemStack.class), false))
					{
						addStacks(output_min, output_max, U.Lang.cast(recipe.output.randomDrops(rand), ItemStack.class), true);
						progress = 0;
						recipe = null;
					}
					else
					{
						progress = 8;
					}
				}
			}
		}
	}
	
	public int getWashingProgress(int scale)
	{
		return (int) ((double) progress * scale / 7D);
	}
	
	public boolean isWashing()
	{
		return progress > 0;
	}
}