/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.load;

import farcore.blocks.terria.BlockSand;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.tree.Tree;
import nebula.common.LanguageManager;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.ModCompator.ICompatible;

/**
 * @author ueyudiud
 */
public class JEICompact implements ICompatible
{
	@Override
	public void call(String phase) throws Exception
	{
		switch (phase)
		{
		case "post" :
			LanguageManager.registerLocal("jei.info.chance", "Chance : %s");
			//Adding recipes.
			for (RockBehavior<?> behavior : Mat.filtAndGet(SubTags.ROCK, MP.property_rock))
			{
				behavior.addDropRecipe();
			}
			final AbstractStack[] notool = new AbstractStack[0];
			
			for (PropertyBlockable<?> property : Mat.filtAndGet(SubTags.SAND, MP.property_sand))
			{
				ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(property.block),
						notool,
						new AbstractStack[] { new BaseStack(ItemMulti.createStack(property.material, MC.pile, BlockSand.MAX_HEIGHT))},
						null);
			}
			for (PropertyBlockable<?> property : Mat.filtAndGet(SubTags.CLAY, MP.property_soil))
			{
				ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(property.block),
						notool,
						new AbstractStack[] { new BaseStack(ItemMulti.createStack(property.material, MC.clayball))},
						new int[][] {{10000, 10000, 10000, 10000, 8000, 6000, 4000, 2000}});
			}
			for (PropertyBlockable<?> property : Mat.filtAndGet(SubTags.DIRT, MP.property_soil))
			{
				ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(property.block),
						notool,
						new AbstractStack[] { new BaseStack(ItemMulti.createStack(property.material, MC.pile))},
						new int[][] {{10000, 10000, 10000, 10000, 10000, 10000, 6667, 3333}});
			}
			for (Tree tree : Mat.filtAndGet(SubTags.TREE, MP.property_tree))
			{
				tree.addDropRecipe();
			}
			break;
		}
	}
}
