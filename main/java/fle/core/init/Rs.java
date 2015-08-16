package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.recipe.ShapedFleRecipe;
import fle.api.recipe.ShapelessFleRecipe;
import fle.core.block.BlockRock;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemSub;
import fle.core.item.ItemTool;
import fle.core.recipe.RecipeHelper.FakeCraftingInventory;
import fle.core.recipe.crafting.OilLampAddFuelRecipe;
import fle.core.recipe.crafting.StoneToolCraftingRecipe;
import fle.core.recipe.crafting.TreeCuttingRecipe;

public class Rs
{
	private static List<FakeCraftingInventory> removeList = new ArrayList();
	
	public static void registerRemovedCrafting(FakeCraftingInventory inv)
	{
		removeList.add(inv);
	}
	
	public static void reloadRecipe()
	{
		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init(" xs", "x s", " xs", 'x', "stickWood", 's', Items.string));
		registerRemovedCrafting(FakeCraftingInventory.init("x", "x", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 0), 'x', "stickWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 1), 'x', "stickWood"));
		
		List list = new ArrayList(CraftingManager.getInstance().getRecipeList());
		for(Object rawTarget : list)
		{
			IRecipe recipe = (IRecipe) rawTarget;
			for(FakeCraftingInventory inv : removeList)
			{
				if(recipe.matches(inv, null))
				{
					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
				}
			}
		}
	}
	
	public static void init()
	{
		OreDictionary.registerOre("wick", Items.string);
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(IB.tool, 1, 7));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_oak"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_spruce"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_birch"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_jungle"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_acacia"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_darkoak"));
		OreDictionary.registerOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("rockCompactStone", BlockRock.a(Materials.CompactStone));
		OreDictionary.registerOre("dustLimestone", ItemFleSub.a("dust_limestone"));
		
		FleAPI.fluidDictionary.registerFluid("oilAnimal", IB.animalOil);
		
		GameRegistry.addRecipe(new ShapelessFleRecipe(ItemFleSub.a("branch_bush"), new Object[]{"branchWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(new ItemStack(IB.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"}));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("rough_stone_axe", ItemFleSub.a("branch_bush")));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_axe", "stickWood"));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_shovel", "stickWood"));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_hammer", "stickWood"));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("flint_arrow", 2, "stickWood", Items.feather));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(100, Items.beef));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(150, Items.porkchop));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(75, Items.rotten_flesh));
		GameRegistry.addRecipe(new ShapedFleRecipe(ItemTool.a("wooden_hammer", Materials.HardWood), new Object[]{"x", "o", 'x', "logWood", 'o', "stickWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(ItemFleSub.a("argil_ball", 3), new Object[]{Items.clay_ball, Items.clay_ball, Blocks.sand, "dustLimestone"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(new ItemStack(IB.woodMachine, 1, 0), new Object[]{"x", "o", 'x', "logWood", 'o', Blocks.gravel}));
		GameRegistry.addRecipe(new ShapedFleRecipe(new ItemStack(IB.woodMachine1, 1, 0), new Object[]{"xx", "xx", 'x', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemTool.a("whetstone", Materials.Stone), new Object[]{"xx", "xx", 'x', ItemFleSub.a("stone_b")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemTool.a("wooden_drilling_firing", Materials.HardWood), new Object[]{" s", "wl", 's', "stickWood", 'w', "logWood", 'l', ItemFleSub.a("tinder")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemFleSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_fiber_dry")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemFleSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_rope")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemFleSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemFleSub.a("ramie_bundle_rope")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemFleSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("leaves_dry")}));
		GameRegistry.addRecipe(new TreeCuttingRecipe());

		RecipeSorter.register(FLE.MODID + ":shaped", ShapedFleRecipe.class, SHAPED, "after:forge:shapedore before:minecraft:shapeless");
		RecipeSorter.register(FLE.MODID + ":stonetoolcrafting", StoneToolCraftingRecipe.class, SHAPED, "after:" + FLE.MODID + ":stonetoolcrafting");
		RecipeSorter.register(FLE.MODID + ":stonetoolcrafting", TreeCuttingRecipe.class, SHAPED, "after:" + FLE.MODID + ":stonetoolcrafting");
		RecipeSorter.register(FLE.MODID + ":shapeless", ShapelessFleRecipe.class, SHAPELESS, "after:forge:shapelessore");
	}
}