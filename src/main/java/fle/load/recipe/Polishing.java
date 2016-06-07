package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.item.ItemSubTool;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import farcore.lib.substance.SubstanceRock;
import farcore.lib.substance.SubstanceTool;
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
		PolishRecipe.registerToolCrafting(EnumToolType.awl, ' ', 'c');
		PolishRecipe.registerToolCrafting(EnumToolType.whetstone, ' ', 'p');
		PolishRecipe.registerTool(EnumToolType.wooden_hammer, 
				(ItemStack stack) -> {
					return OreDictionary.itemMatches(EnumItem.tool.instance(1, "wood_hammer"), stack, false) ? 
						ItemSubTool.getToolMaterial(stack).harvestLevel : -1;});
		PolishRecipe.registerTool(EnumToolType.awl, 
				(ItemStack stack) -> {
					return OreDictionary.itemMatches(EnumItem.tool.instance(1, "flint_awl"), stack, false) ? 
						ItemSubTool.getToolMaterial(stack).harvestLevel : -1;});
		PolishRecipe.registerTool(EnumToolType.whetstone, 
				(ItemStack stack) -> {
					return OreDictionary.itemMatches(EnumItem.tool.instance(1, "whetstone"), stack, false) ? 
						ItemSubTool.getToolMaterial(stack).harvestLevel : -1;});
		
		PolishRecipe.registerResource(new BaseStack(EnumItem.stone_fragment.instance(1, "flint")), 5);
		
		SubstanceTool tool = SubstanceTool.getSubstance("flint");
		OreStack input = new OreStack("fragmentFlint");
		PolishRecipe.registerRecipe(input, "c c      ", new ItemStack(Items.flint));
		PolishRecipe.registerRecipe(input, " c ccc c ", EnumItem.stone_chip.instance(4, "flintSmall"));
		PolishRecipe.registerRecipe(input, "     cccc", EnumItem.tool_head.instance(1, "flint_hammer", tool));
		PolishRecipe.registerRecipe(input, "c ccccccc", EnumItem.tool_head.instance(1, "stone_shovel", tool));
		PolishRecipe.registerRecipe(input, "   c cc c", EnumItem.tool.instance(1, "flint_awl", tool));
		PolishRecipe.registerRecipe(input, "c cc cc c", EnumItem.tool_head.instance(1, "flint_knife", tool));
		PolishRecipe.registerRecipe(input, " c  c  c ", EnumItem.tool_head.instance(2, "flint_knife", tool));
		Object[][] inputs1 = {
				{"stone", "Stone"},
				{"andesite", "Andesite"},
				{"basalt", "Basalt"},
				{"peridotite", "Peridotite"},
				{"rhyolite", "Rhyolite"},
				{"stone-compact", "StoneCompact"}};

		for(Object[] element : inputs1)
		{
			SubstanceRock rock = SubstanceRock.getSubstance((String) element[0]);
			tool = rock.toolBelong;
			OreStack chip = new OreStack("chip" + element[1]);
			PolishRecipe.registerResource(chip, rock.harvestLevel);
			PolishRecipe.registerRecipe(chip, "p cpccccc", EnumItem.tool_head.instance(1, "stone_axe", tool));
			PolishRecipe.registerRecipe(chip, "c pcpcccc", EnumItem.tool_head.instance(1, "stone_axe", tool));
			PolishRecipe.registerRecipe(chip, "c pcpcccc", EnumItem.tool_head.instance(1, "stone_axe", tool));
			PolishRecipe.registerRecipe(chip, "c ccpcccc", EnumItem.tool_head.instance(1, "stone_shovel", tool));
			PolishRecipe.registerRecipe(chip, "      ccc", EnumItem.tool_head.instance(1, "stone_hammer", tool));
			PolishRecipe.registerRecipe(chip, "ppp   ppp", EnumItem.tool.instance(1, "whetstone", tool));
		}
	}
}