package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.cg.CraftGuide;
import fle.api.cg.RecipesTab;
import fle.api.enums.EnumAtoms;
import fle.api.material.Matter;
import fle.api.material.MatterDictionary;
import fle.api.recipe.IFleRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapedFleRecipe;
import fle.api.recipe.ShapelessFleRecipe;
import fle.api.util.DropInfo;
import fle.api.util.SubTag;
import fle.cg.recipe.ShapedRecipe;
import fle.cg.recipe.ShapelessRecipe;
import fle.core.block.BlockRock;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemOre;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.recipe.CastingPoolRecipe;
import fle.core.recipe.PolishRecipe;
import fle.core.recipe.RecipeHelper.FakeCraftingInventory;
import fle.core.recipe.WashingRecipe;
import fle.core.recipe.crafting.OilLampAddFuelRecipe;
import fle.core.recipe.crafting.ToolCraftingRecipe;
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
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log, 1, 0)));
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log, 1, 1)));
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log, 1, 2)));
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log, 1, 3)));
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log2, 1, 0)));
		registerRemovedCrafting(FakeCraftingInventory.init("o", 'o', new ItemStack(Blocks.log2, 1, 1)));
		
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
		OreDictionary.registerOre("cropMillet", ItemFleSub.a("millet"));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 101));
		OreDictionary.registerOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 5));
		OreDictionary.registerOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 103));
		OreDictionary.registerOre("craftingToolDecortingPlate", new ItemStack(IB.tool, 1, 13));
		OreDictionary.registerOre("craftingToolDecortingStick", new ItemStack(IB.tool, 1, 14));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(IB.tool, 1, 7));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_oak"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_spruce"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_birch"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_jungle"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_acacia"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_darkoak"));
		OreDictionary.registerOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("rockCompactStone", BlockRock.a(Materials.CompactStone));
		OreDictionary.registerOre("dustLimestone", ItemFleSub.a("dust_limestone"));
		OreDictionary.registerOre("plateStone", ItemFleSub.a("stone_plate"));
		for(EnumAtoms atom : EnumAtoms.values())
		{
			if(atom.contain(SubTag.ATOM_metal))
				if(ItemFleSub.a("ingot_" + atom.name().toLowerCase()) != null)
					OreDictionary.registerOre("ingot" + atom.getName(), ItemFleSub.a("ingot_" + atom.name().toLowerCase()));
		}

		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.NativeCopper)), Matter.mCu, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu")), Matter.mCu, 100, (int) Materials.Copper.getPropertyInfo().getMeltingPoint(), 100000);
		MatterDictionary.registerFluid(IB.copper, Matter.mCu);
		
		FleAPI.fluidDictionary.registerFluid("oilAnimal", IB.animalOil);

		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_brick"), ItemFleSub.a("argil_brick"), 0.0F);
		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_plate"), ItemFleSub.a("argil_plate"), 0.0F);
		
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("branch_bush"), new Object[]{"branchWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemToolHead.a("rough_stone_axe", Materials.Flint), new Object[]{ItemFleSub.a("flint_c")}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemToolHead.a("rough_stone_axe", Materials.Obsidian), new Object[]{ItemFleSub.a("chip_obsidian")}));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "rough_stone_axe", ItemFleSub.a("branch_bush")));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_hammer", ItemFleSub.a("branch_bush")));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_axe", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_shovel", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_hammer", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_spade_hoe", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_arrow", 2, "stickWood", Items.feather));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_axe", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_chisel", "stickWood"));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(100, Items.beef));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(150, Items.porkchop));
		GameRegistry.addRecipe(new OilLampAddFuelRecipe(75, Items.rotten_flesh));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood), new Object[]{"x", "o", 'x', "logWood", 'o', "stickWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood, 2), new Object[]{"x", "o", 'x', "logWood", 'o', ItemFleSub.a("branch_bush")}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("argil_ball", 3), new Object[]{Items.clay_ball, Items.clay_ball, Blocks.sand, "dustLimestone"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabOldStoneAge, new ItemStack(IB.woodMachine, 1, 0), new Object[]{"x", "o", 'x', "logWood", 'o', Blocks.gravel}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.woodMachine1, 1, 0), new Object[]{"xx", "xx", 'x', "stickWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabOldStoneAge, Blocks.cobblestone, new Object[]{"xx", "xx", 'x', ItemFleSub.a("stone_b")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("whetstone", Materials.Stone), new Object[]{"xx", 'x', ItemFleSub.a("stone_b")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemTool.a("wooden_drilling_firing", Materials.HardWood), new Object[]{" s", "wl", 's', "stickWood", 'w', "logWood", 'l', ItemFleSub.a("tinder")}));
		
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_fiber_dry")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_rope")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemFleSub.a("ramie_bundle_rope")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("leaves_dry")}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_millet_wholemeal"), new Object[]{"cropMillet", "craftingToolDecortingPlate", "craftingToolDecortingStick"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_wheat_wholemeal"), new Object[]{"cropWheat", "craftingToolDecortingPlate", "craftingToolDecortingStick"}));
		
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("cemented_grit", 8), new Object[]{Blocks.sand, Blocks.sand, Blocks.clay, Blocks.clay}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("stone_a", 9), new Object[]{Blocks.cobblestone}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.woodMachine, 1, 1), new Object[]{"flePlankWood", "flePlankWood", "flePlankWood", "flePlantWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 0), new Object[]{Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 0), new Object[]{"plateStone", "plateStone", "plateStone", "plateStone"}));
		GameRegistry.addRecipe(new TreeCuttingRecipe());

		RecipeSorter.register(FLE.MODID + ":shaped", ShapedFleRecipe.class, SHAPED, "before:minecraft:shaped");
		RecipeSorter.register(FLE.MODID + ":toolcrafting", ToolCraftingRecipe.class, SHAPED, "after:" + FLE.MODID + ":shaped");
		RecipeSorter.register(FLE.MODID + ":tree", TreeCuttingRecipe.class, SHAPED, "after:" + FLE.MODID + ":shaped");
		RecipeSorter.register(FLE.MODID + ":shapeless", ShapelessFleRecipe.class, SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");
		
		CastingPoolRecipe.init();
	}

	public static void completeInit()
	{
		for(Object obj : CraftingManager.getInstance().getRecipeList())
		{
			IRecipe recipe = (IRecipe) obj;
			if(recipe instanceof ShapedRecipes)
			{
				CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapedRecipe((ShapedRecipes) recipe));
			}
			else if(recipe instanceof ShapedOreRecipe)
			{
				CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapedRecipe((ShapedOreRecipe) recipe));
			}
			else if(recipe instanceof ShapedFleRecipe)
			{
				CraftGuide.instance.registerRecipe(((IFleRecipe) recipe).getRecipeTab(), new ShapedRecipe((ShapedFleRecipe) recipe));
				if(((IFleRecipe) recipe).getRecipeTab() != RecipesTab.tabClassic)
					CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapedRecipe((ShapedFleRecipe) recipe));
			}
			else if(recipe instanceof ShapelessRecipes)
			{
				CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapelessRecipe((ShapelessRecipes) recipe));
			}
			else if(recipe instanceof ShapelessOreRecipe)
			{
				CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapelessRecipe((ShapelessOreRecipe) recipe));
			}
			else if(recipe instanceof ShapelessFleRecipe)
			{
				CraftGuide.instance.registerRecipe(((IFleRecipe) recipe).getRecipeTab(), new ShapelessRecipe((ShapelessFleRecipe) recipe));
				if(((IFleRecipe) recipe).getRecipeTab() != RecipesTab.tabClassic)
					CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new ShapelessRecipe((ShapelessFleRecipe) recipe));
			}
		}
		Map<ItemAbstractStack, DropInfo> washRecipes = WashingRecipe.getRecipes();
		for(ItemAbstractStack tStack : washRecipes.keySet())
		{
			CraftGuide.instance.registerRecipe(RecipesTab.tabOldStoneAge, new fle.cg.recipe.WashingRecipe(tStack, washRecipes.get(tStack)));
			CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new fle.cg.recipe.WashingRecipe(tStack, washRecipes.get(tStack)));
		}
		for(PolishRecipe recipe : PolishRecipe.getRecipeList())
		{
			CraftGuide.instance.registerRecipe(recipe.getTab(), new fle.cg.recipe.PolishRecipe(recipe));
			if(recipe.getTab() != RecipesTab.tabClassic)
				CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new fle.cg.recipe.PolishRecipe(recipe));
		}
	}
}