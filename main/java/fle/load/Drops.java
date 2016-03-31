package fle.load;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.DropHandler;
import farcore.lib.recipe.ToolDestoryDropRecipes;
import farcore.lib.stack.BaseStack;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;
import fle.api.util.BlockConditionMatcherIS;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Drops
{
	public static void init()
	{
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			ItemStack drop = EnumItem.stone_chip.instance(3, rock.getName());
			if(drop == null)
			{
				drop = EnumItem.cobble_block.instance(1, rock);
			}
			ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("stone" + U.Lang.validateOre(true, rock.getName())), 
					new DropHandler(1, new Ety(new BaseStack(drop), 1)));
			ToolDestoryDropRecipes.add(EnumToolType.hammer_digable.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
					new DropHandler(1, new Ety(new BaseStack(drop), 1)));
			ToolDestoryDropRecipes.add(EnumToolType.hammer_digable_basic.stack(), new BlockConditionMatcherIS("cobble" + U.Lang.validateOre(true, rock.getName())), 
					new DropHandler(1, new Ety(new BaseStack(drop), 1)));
		}
	}
}