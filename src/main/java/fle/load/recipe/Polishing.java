package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.item.ItemSubTool;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import fle.api.recipe.machine.PolishRecipe;
import fle.api.recipe.machine.PolishRecipe.PolishCondition;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Polishing 
{
	public static void init()
	{
		new PolishCondition("polish", "Polish", 'p').setTextureName("fle:polish");
		new PolishCondition("crush", "Crush", 'c').setTextureName("fle:crush");
		
		PolishRecipe.registerToolCrafting(EnumToolType.wooden_hammer, ' ', 'c');
		PolishRecipe.registerTool(EnumToolType.wooden_hammer, 
				(ItemStack stack) -> {
					return OreDictionary.itemMatches(EnumItem.tool.instance(1, "wood_hammer"), stack, false) ? 
						ItemSubTool.getToolMaterial(stack).harvestLevel : -1;});
		
		PolishRecipe.registerResource(new BaseStack(EnumItem.stone_fragment.instance(1, "flint")), 5);
		
		PolishRecipe.registerRecipe(new OreStack("fragmentFlint"), "c c      ", new ItemStack(Items.flint));
	}
}