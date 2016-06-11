package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.FleCraftingManager;
import farcore.lib.recipe.IFleRecipe;
import farcore.lib.recipe.ShapedFleRecipe;
import farcore.lib.recipe.ShapelessFleRecipe;
import fle.api.recipe.FoodUseCraftingRecipe;
import fle.api.recipe.LogCutRecipe;
import fle.api.recipe.ToolSingleCraftingRecipe;
import fle.load.BlockItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Crafting
{
	public static void init()
	{
		registerRecipe(new LogCutRecipe(new Ety(EnumToolType.axe.stack(), null)));
		registerShapelessRecipe(EnumItem.plant_production.instance(1, "vine_rope"), 40, "vine", "vine", "vine", "vine");
		Object object = EnumItem.plant_production.instance(1, "dry_ramie_fiber");
		registerShapelessRecipe(EnumItem.plant_production.instance(5, "ramie_rope"), 80, object, object, object, object);
		object = EnumItem.plant_production.instance(1, "ramie_rope");
		registerShapelessRecipe(EnumItem.plant_production.instance(1, "ramie_bundle"), 90, object, object, object, object);
		object = EnumItem.plant_production.instance(1, "ramie_bundle");
		registerShapelessRecipe(EnumItem.plant_production.instance(4, "ramie_rope"), 90, object);
		registerShapelessRecipe(EnumItem.stone_production.instance(1, "lime_dust"), 40, new Ety(EnumToolType.hammer_digable.stack(), null), "chipLimestone");
		registerShapelessRecipe(EnumItem.stone_production.instance(4, "argil_ball"), 50, "dustLimestone", "dustSand", Items.clay_ball, Items.clay_ball, Items.clay_ball, Items.clay_ball);
		registerShapedRecipe(new ItemStack(BlockItems.machineIAlpha, 1, 0), 180, "x", "o", 'x', "logWood", 'o', "pileGravel");
		registerShapedRecipe(new ItemStack(BlockItems.machineIAlpha, 1, 1), 240, "xx", "oo", 'o', "logWood", 'x', "plateStone");
		
		Object[][] inputs2 = {
				{"stone", "Stone"},
				{"andesite", "Andesite"},
				{"basalt", "Basalt"},
				{"peridotite", "Peridotite"},
				{"rhyolite", "Rhyolite"},
				{"stone-compact", "StoneCompact"}};
				
		for(Object[] element : inputs2)
		{
			registerShapelessRecipe(EnumItem.cobble_block.instance(1, element[0]), 8, "chip" + element[1], "chip" + element[1], "chip" + element[1], "chip" + element[1]);
			registerShapelessRecipe(EnumItem.tool.instance(1, "whetstone", (String) element[0]), 12, "fragment" + element[1], "fragment" + element[1], "fragment" + element[1]);
		}
		
		registerShapedRecipe(EnumItem.tool.instance(1, "bar_grizzly", "simple_bar_grizzly"), 120, true, "xo", "ox", 'x', "branchWood", 'o', "rope");
		Object[][] inputs1 = {
					{"flint", "SharpFlint"},
					{"obsidian", "Obsidian"}};
		for(Object[] element : inputs1)
		{
			registerShapelessRecipe(EnumItem.tool.instance(1, "rough_stone_adz", element[0]), 100, "branchWood", "rope", "chip" + element[1]);
		}
		registerShapedRecipe(EnumItem.bowl.instance(4), 80, new Ety(EnumToolType.knife.stack(), null), "x", 'x', "logWood");
		registerRecipe(new FoodUseCraftingRecipe(EnumItem.food_smeltable.instance(1, "beef_kebab_raw"), 4.0F, 40, EnumItem.food_divide_smeltable.instance(1, "beef_raw"), "stickWood"));
		registerRecipe(new FoodUseCraftingRecipe(EnumItem.food_smeltable.instance(1, "chicken_kebab_raw"), 4.0F, 40, EnumItem.food_divide_smeltable.instance(1, "chicken_raw"), "stickWood"));
		registerRecipe(new FoodUseCraftingRecipe(EnumItem.food_smeltable.instance(1, "fish_kebab_raw"), 4.0F, 40, EnumItem.food_divide_smeltable.instance(1, "fish_raw"), "stickWood"));
		registerRecipe(new FoodUseCraftingRecipe(EnumItem.food_smeltable.instance(1, "pork_kebab_raw"), 4.0F, 40, EnumItem.food_divide_smeltable.instance(1, "pork_raw"), "stickWood"));
		registerRecipe(new FoodUseCraftingRecipe(EnumItem.food_smeltable.instance(1, "squid_kebab_raw"), 4.0F, 40, EnumItem.food_divide_smeltable.instance(1, "squid_raw"), "stickWood"));
		registerRecipe(new ToolSingleCraftingRecipe("flint_hammer", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("flint_hammer", 1.0F, 100, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("flint_knife", 0.7F, 100, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("flint_knife", 1.0F, 60, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_axe", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_axe", 1.0F, 100, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_shovel", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_shovel", 1.0F, 100, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_hammer", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_hammer", 1.0F, 100, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_spade_hoe", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_spade_hoe", 1.0F, 100, "stickWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_sickle", 0.7F, 140, "branchWood", "rope"));
		registerRecipe(new ToolSingleCraftingRecipe("stone_sickle", 1.0F, 100, "stickWood", "rope"));
		registerShapelessRecipe(EnumItem.tool.instance(1, "wood_hammer", "wood_hard-void"), 160, "branchWood", "logWood");
	}
	
	public static void registerShapedRecipe(ItemStack output, int tick, Object...objects)
	{
		FleCraftingManager.registerRecipe(new ShapedFleRecipe(output, tick, objects));
	}
	
	public static void registerShapelessRecipe(ItemStack output, int tick, Object...objects)
	{
		FleCraftingManager.registerRecipe(new ShapelessFleRecipe(output, tick, objects));
	}
	
	public static void registerRecipe(IFleRecipe recipe)
	{
		FleCraftingManager.registerRecipe(recipe);
	}
}