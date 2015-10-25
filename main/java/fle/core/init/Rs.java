package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapedFleRecipe;
import fle.api.recipe.ShapelessFleRecipe;
import fle.api.soild.SolidRegistry;
import fle.api.soild.SolidStack;
import fle.api.util.FLEConfiguration;
import fle.api.util.FleLog;
import fle.api.util.SubTag;
import fle.core.block.BlockRock;
import fle.core.block.ItemDitch;
import fle.core.block.ItemRopeLadder;
import fle.core.cg.FLECastingRecipe;
import fle.core.cg.FLEClayRecipe;
import fle.core.cg.FLEColdForgingRecipe;
import fle.core.cg.FLEShapedRecipe;
import fle.core.cg.FLEShapelessRecipe;
import fle.core.cg.FLEWashingRecipe;
import fle.core.handler.AxeHandler;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemOre;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.recipe.CastingPoolRecipe;
import fle.core.recipe.FLEBoilingHeaterRecipe;
import fle.core.recipe.FLEDryingRecipe;
import fle.core.recipe.FLEOilMillRecipe;
import fle.core.recipe.FLEPolishRecipe;
import fle.core.recipe.FLESifterRecipe;
import fle.core.recipe.FLEStoneMillRecipe;
import fle.core.recipe.RecipeHelper.FakeCraftingInventory;
import fle.core.recipe.crafting.OilLampAddFuelRecipe;
import fle.core.recipe.crafting.RopeLadderCraftingRecipe;
import fle.core.recipe.crafting.ToolCraftingRecipe;
import fle.core.recipe.crafting.TreeCuttingRecipe;
import fle.core.util.DitchInfo;

public class Rs
{
	private static final Object[] toolMaterial = {Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.gold_ingot, Items.diamond, "ingotBronze", "ingotInver", "ingotSteel"};
	private static final String[][] toolCraftingMap = {
		{
			"xxx", 
			" s ", 
			" s "}, 
		{
			"xx ", 
			"xs ", 
			" s "}, 
		{
			" x ", 
			" s ", 
			" s "}, 
		{
			"xx ", 
			" s ", 
			" s "}, 
		{
			" x ",
			" x ",
			" s "}, 
		{
			"xxx", 
			"xxx", 
			" s "}, 
		{
			" x ", 
			" sx", 
			"s  "}, 
		{
			"xxx", 
			"xsx", 
			" s "}
		};
	
	private static List<FakeCraftingInventory> removeList = new ArrayList();
	private static Map<FakeCraftingInventory, ItemStack> logCraftList = new HashMap();
	private static Map<ItemAbstractStack, ItemStack> logList = new HashMap();
	
	public static ItemStack getLogCraftOutput(ItemStack input)
	{
		for(Entry<ItemAbstractStack, ItemStack> e : logList.entrySet())
		{
			if(e.getKey().isStackEqul(input)) return e.getValue().copy();
		}
		return null;
	}
	
	public static void registerRemovedCrafting(FakeCraftingInventory inv)
	{
		removeList.add(inv);
	}
	
	public static void reloadRecipe()
	{
		removeList.clear();
		for(ItemStack stack : OreDictionary.getOres("logWood"))
		{
			if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				for(int i = 0; i < 16; ++i)
				{
					ItemStack tStack = stack.copy();
					tStack.setItemDamage(i);
					logCraftList.put(FakeCraftingInventory.init("x", 'x', tStack), tStack);
				}
			}
			logCraftList.put(FakeCraftingInventory.init("x", 'x', stack), stack);
		}
		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", "xx", 'x', Blocks.glass));
		registerRemovedCrafting(FakeCraftingInventory.init(" xs", "x s", " xs", 'x', "stickWood", 's', Items.string));
		registerRemovedCrafting(FakeCraftingInventory.init("x", "x", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("x", 'x', Items.bone));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 0), 'x', "stickWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("oxv", 'o', Items.egg, 'x', Items.sugar, 'v', Blocks.pumpkin));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 1), 'x', "stickWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("x x", " x ", 'x', "plankWood"));
		
		registerRemovedCrafting(FakeCraftingInventory.init(" x", "x ", 'x', Items.iron_ingot));

		for(Object obj : toolMaterial)
		{
			try
			{
				for(String[] map : toolCraftingMap)
				{
					registerRemovedCrafting(FakeCraftingInventory.init(map[0], map[1], map[2], 's', "stickWood", 'x', obj));
				}
			}
			catch(Throwable e)
			{
				FleLog.getLogger().info("FLE : Fail to remove recipe of " + obj.toString() + ", does it isn't register or a bug?", e);
			}
		}
		
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
			for(FakeCraftingInventory inv : logCraftList.keySet())
			{
				if(recipe.matches(inv, null))
				{
					ItemStack tStack = recipe.getCraftingResult(inv);
					ItemStack tStack1 = logCraftList.get(inv);
					logList.put(new ItemBaseStack(tStack1), tStack);
					addShapedRecipe(RecipesTab.tabClassic, tStack, new Object[]{"o", "x", 'x', new ItemBaseStack(tStack1), 'o', "craftingToolSaw"});
					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
				}
			}
		}
	}
	
	public static void init(FLEConfiguration cfg)
	{
		addOre("wick", Items.string);
		addOre("wick", ItemFleSub.a("ramie_rope"));
		addOre("rope", ItemFleSub.a("ramie_rope"));
		addOre("rope", ItemFleSub.a("straw_rope"));
		addOre("tie", ItemFleSub.a("ramie_rope"));
		addOre("tie", ItemFleSub.a("rattan"));
		addOre("tie", ItemFleSub.a("sinew"));
		addOre("tie", ItemFleSub.a("straw_rope"));
		addOre("tie", Items.string);
		addOre("cropMillet", ItemFleSub.a("millet"));
		addOre("cropPotato", ItemFleFood.a("potato"));
		addOre("cropCitron", ItemFleFood.a("citron"));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 101));
		addOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 5));
		addOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 103));
		addOre("craftingToolDecortingPlate", new ItemStack(IB.tool, 1, 13));
		addOre("craftingToolDecortingStick", new ItemStack(IB.tool, 1, 14));
		addOre("craftingToolFirestarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		addOre("craftingToolFirestarter", new ItemStack(IB.tool, 1, 7));
		addOre("craftingToolChisel", new ItemStack(IB.tool, 1, 105));
		addOre("craftingToolBowsaw", new ItemStack(IB.tool, 1, 106));
		addOre("craftingToolWhetstone", new ItemStack(IB.tool, 1, 8));
		addOre("craftingToolSaw", new ItemStack(IB.tool, 1, 106));
		addOre("craftingToolAdz", new ItemStack(IB.tool, 1, 107));
		addOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 15));
		addOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 108));
		addOre("craftingToolNeedleTier1", new ItemStack(IB.tool, 1, 108));
		addOre("craftingToolSpinning", new ItemStack(IB.tool, 1, 16));
		addOre("branchWood", ItemFleSub.a("branch_oak"));
		addOre("branchWood", ItemFleSub.a("branch_spruce"));
		addOre("branchWood", ItemFleSub.a("branch_birch"));
		addOre("branchWood", ItemFleSub.a("branch_jungle"));
		addOre("branchWood", ItemFleSub.a("branch_acacia"));
		addOre("branchWood", ItemFleSub.a("branch_darkoak"));
		addOre("flePlankWood", new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		addOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));
		addOre("rockCompactStone", BlockRock.a(Materials.CompactStone));
		addOre("dustLimestone", ItemFleSub.a("dust_limestone"));
		addOre("dustQuickLime", ItemFleSub.a("dust_quicklime"));
		addOre("dustPlantAsh", ItemFleSub.a("plant_ash"));
		addOre("plateStone", ItemFleSub.a("stone_plate"));
		addOre("plateArgil", ItemFleSub.a("argil_plate"));
		addOre("brickArgil", ItemFleSub.a("argil_brick"));
		addOre("ballArgil", ItemFleSub.a("argil_ball"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_as_0"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_as_1"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_0"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_1"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_sn_0"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_sn_1"));
		addOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_sn"));
		for(EnumAtoms atom : EnumAtoms.values())
		{
			if(atom.contain(SubTag.ATOM_metal))
				if(ItemFleSub.a("ingot_" + atom.name().toLowerCase()) != null)
					addOre("ingot" + atom.getName(), ItemFleSub.a("ingot_" + atom.name().toLowerCase()));
		}

		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.WATER, 1000), ItemFleSub.a("wood_bucket_0_water"), ItemFleSub.a("wood_bucket_0_empty"));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(IB.plant_ash_mortar, 1000), ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), ItemFleSub.a("wood_bucket_0_empty"));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(IB.lime_mortar, 1000), ItemFleSub.a("wood_bucket_0_lime_mortar"), ItemFleSub.a("wood_bucket_0_empty"));

		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.NativeCopper)), EnumAtoms.Cu.asMatter(), 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Enargite)), Matter.mCu3AsS4, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Cuprite)), Matter.mCu2O, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Chalcocite)), Matter.mCu2S, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Covellite)), Matter.mCuS, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Tenorite)), Matter.mCuO, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Orpiment)), Matter.mAs2S3, 88, 681, 60000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Malachite)), Matter.mCu_OH2_CO3, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Azurite)), Matter.mCu_OH2_2CO3, 88, (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Gelenite)), Matter.mPbS, 88, (int) Materials.Lead.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Cassiterite)), Matter.mSnO2, 88, (int) Materials.Tin.getPropertyInfo().getMeltingPoint(), 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Stannite)), Matter.mCu2FeSnS4, 88, 718, 20000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu")), EnumAtoms.Cu.asMatter(), 75, (int) Materials.Copper.getPropertyInfo().getMeltingPoint(), 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_pb")), EnumAtoms.Pb.asMatter(), 75, (int) Materials.Lead.getPropertyInfo().getMeltingPoint(), 60000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_zn")), EnumAtoms.Zn.asMatter(), 75, (int) Materials.Zinc.getPropertyInfo().getMeltingPoint(), 70000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_sn")), EnumAtoms.Sn.asMatter(), 75, (int) Materials.Tin.getPropertyInfo().getMeltingPoint(), 60000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_as_0")), Materials.CuAs.getMatter(), 75, 684, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_as_1")), Materials.CuAs2.getMatter(), 75, 573, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_0")), Materials.CuPb.getMatter(), 75, 671, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_1")), Materials.CuPb2.getMatter(), 75, 628, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_sn_0")), Materials.CuSn.getMatter(), 75, 648, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_sn_1")), Materials.CuSn2.getMatter(), 75, 629, 100000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_sn")), Materials.CuSnPb.getMatter(), 75, 629, 100000);
		MatterDictionary.registerFluid(IB.copper, EnumAtoms.Cu.asMatter());
		MatterDictionary.registerFluid(IB.lead, EnumAtoms.Pb.asMatter());
		MatterDictionary.registerFluid(IB.zinc, EnumAtoms.Zn.asMatter());
		MatterDictionary.registerFluid(IB.tin, EnumAtoms.Sn.asMatter());
		MatterDictionary.registerFluid(IB.cu_as_0, Materials.CuAs.getMatter());
		MatterDictionary.registerFluid(IB.cu_as_1, Materials.CuAs2.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_0, Materials.CuPb.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_1, Materials.CuPb2.getMatter());
		MatterDictionary.registerFluid(IB.cu_sn_0, Materials.CuSn.getMatter());
		MatterDictionary.registerFluid(IB.cu_sn_1, Materials.CuSn2.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_sn, Materials.CuSnPb.getMatter());
		
		SolidRegistry.registerSolidContainer(new SolidStack(IB.limestone, 108), ItemFleSub.a("dust_quicklime"));
		SolidRegistry.registerSolidContainer(new SolidStack(IB.plant_ash, 108), ItemFleSub.a("plant_ash"));
		SolidRegistry.registerSolidContainer(new SolidStack(IB.brown_sugar, 108), ItemFleFood.a("brown_sugar"));
		SolidRegistry.registerSolidContainer(new SolidStack(IB.millet_c, 144), ItemFleSub.a("millet"));
		SolidRegistry.registerSolidContainer(new SolidStack(IB.wheat_c, 144), new ItemStack(Items.wheat));

		FleAPI.fluidDictionary.registerFluid("oil", IB.animalOil);
		FleAPI.fluidDictionary.registerFluid("oil", IB.plantOil);
		FleAPI.fluidDictionary.registerFluid("mortarLime", IB.lime_mortar);
		FleAPI.fluidDictionary.registerFluid("oilAnimal", IB.animalOil);
		FleAPI.fluidDictionary.registerFluid("oilPlant", IB.plantOil);
		FleAPI.fluidDictionary.registerFluid("dextrin", IB.wheat_dextrin);
		FleAPI.fluidDictionary.registerFluid("dextrin", IB.millet_dextrin);
		FleAPI.fluidDictionary.registerFluid("dextrin", IB.potato_dextrin);
		FleAPI.fluidDictionary.registerFluid("dextrin", IB.sweet_potato_dextrin);

		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_brick"), ItemFleSub.a("argil_brick"), 0.0F);
		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_plate"), ItemFleSub.a("argil_plate"), 0.0F);
		GameRegistry.addSmelting(ItemFleSub.a("dust_limestone"), ItemFleSub.a("dust_quicklime"), 0.0F);
		
		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("branch_bush"), new Object[]{"branchWood"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("wooden_wedge"), new Object[]{"xo", 'x', "flePlankWood", 'o', "craftingToolAdz"});
		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Flint), new Object[]{ItemFleSub.a("flint_c"), ItemFleSub.a("branch_bush"), "tie"});
		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Obsidian), new Object[]{ItemFleSub.a("chip_obsidian"), ItemFleSub.a("branch_bush"), "tie"});
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "rough_stone_axe", 1, ItemFleSub.a("branch_bush"), "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_hammer", 1, ItemFleSub.a("branch_bush"), "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_axe", 1, "stickWood", "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_shovel", 1, "stickWood", "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_hammer", 1, "stickWood", "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", 1, "stickWood", "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_spade_hoe", 1, "stickWood", "tie"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_arrow", 2, "stickWood", Items.feather));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", 1, "stickWood", "tie"));
		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemToolHead.a("stone_spinning_disk", Materials.Stone), new Object[]{ItemFleSub.a("stone_b"), "craftingToolHardHammer", "craftingToolChisel", "craftingToolWhetstone"});
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabBronzeAge, "stone_spinning_disk", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_axe", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_pickaxe", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_shovel", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_chisel", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_bowsaw", "stickWood"));
		addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_adz", "stickWood"));
		addRecipe(new OilLampAddFuelRecipe(100, Items.beef));
		addRecipe(new OilLampAddFuelRecipe(150, Items.porkchop));
		addRecipe(new OilLampAddFuelRecipe(75, Items.rotten_flesh));
		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood), new Object[]{"x", "o", 'x', "logWood", 'o', "stickWood"});
		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood, 2), new Object[]{"x", "o", 'x', "logWood", 'o', ItemFleSub.a("branch_bush")});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("argil_ball", 3), new Object[]{Items.clay_ball, Items.clay_ball, Blocks.sand, "dustLimestone"});
		addShapedRecipe(RecipesTab.tabOldStoneAge, new ItemStack(IB.woodMachine, 1, 0), new Object[]{"x", "o", 'x', "logWood", 'o', Blocks.gravel});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.woodMachine1, 1, 0), new Object[]{"xx", "xx", 'x', "stickWood"});
		addShapedRecipe(RecipesTab.tabOldStoneAge, Blocks.cobblestone, new Object[]{"xx", "xx", 'x', ItemFleSub.a("stone_b")});
		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("whetstone", Materials.Stone), new Object[]{"xx", 'x', ItemFleSub.a("stone_b")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemTool.a("wooden_drilling_firing", Materials.HardWood), new Object[]{" s", "wl", 's', "stickWood", 'w', "logWood", 'l', ItemFleSub.a("tinder")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_empty"), new Object[]{"rsr", "w w", " w ", 's', "stickWood", 'r', "rope", 'w', "logWood"});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustPlantAsh", "dustPlantAsh", "dustPlantAsh"});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_lime_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustQuickLime", "dustQuickLime", "dustQuickLime"});

		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemRopeLadder.a(4), new Object[]{" s ", "rsr", " s ", 'r', ItemFleSub.a("ramie_bundle_rope"), 's', "stickWood"});
		for(int i = 2; i < 9; ++i)
		{
			Object[] objs = new Object[i];
			Arrays.fill(objs, IB.ropeLadder);
			addRecipe(new RopeLadderCraftingRecipe(true, objs));
		}
		addRecipe(new RopeLadderCraftingRecipe(false, IB.ropeLadder));

		addShapelessRecipe(RecipesTab.tabOldStoneAge, Items.string, new Object[]{ItemFleSub.a("spinneret")});
		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("sinew"), new Object[]{Items.leather});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("straw_rope"), new Object[]{ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry")});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("crushed_bone"), new Object[]{Items.bone, "craftingToolHardHammer"});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("chicken_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.chicken});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("pork_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.porkchop});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_fiber_dry")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_rope")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemFleSub.a("ramie_bundle_rope")});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("twine", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("ramie_fiber_debonded")});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_thread", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("cotton")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("ramie_fiber_debonded", 4), new Object[]{" x ", "xsx", " x ", 'x', ItemFleSub.a("ramie_fiber_dry"), 's', ItemFleSub.a("plant_ash_soap")});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("sack_empty"), new Object[]{"nxx", "sxx", 'x', ItemFleSub.a("linen_b"), 's', ItemFleSub.a("twine"), 'n', "craftingToolNeedle"});
		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton", 3), new Object[]{ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), "stickWood"});
		
		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("leaves_dry")});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_millet_wholemeal"), new Object[]{"cropMillet", "craftingToolDecortingPlate", "craftingToolDecortingStick"});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_wheat_wholemeal"), new Object[]{"cropWheat", "craftingToolDecortingPlate", "craftingToolDecortingStick"});
		
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("cemented_grit", 8), new Object[]{Blocks.sand, Blocks.sand, Blocks.clay, Blocks.clay});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("stone_a", 9), new Object[]{Blocks.cobblestone});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.workbench, 1, 0), new Object[]{"logWood", "logWood", "logWood", "logWood"});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 0), new Object[]{Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone});
		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 0), new Object[]{"plateStone", "plateStone", "plateStone", "plateStone"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 1), new Object[]{"xox", "xxx", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 1), new Object[]{"xox", "xox", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 2), new Object[]{"xox", "cxc", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 3), new Object[]{"cxc", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 4), new Object[]{" p ", "xox", " p ", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"});
		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 5), new Object[]{"ppp", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"});
		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 1), new Object[]{"psp", "sss", 's', "stickWood", 'p', "flePlankWood"});
		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("millstone"), new Object[]{Blocks.stone, "craftingToolHardHammer", "craftingToolChisel"});
		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 2), new Object[]{"sm ", "ppp", " b ", 's', "stickWood", 'p', "plateStone", 'm', ItemFleSub.a("millstone"), 'b', new ItemStack(IB.woodMachine1, 1, 1)});
		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 3), new Object[]{" f ", "sfs", " b ", 's', ItemFleSub.a("ramie_rope"), 'f', ItemFleSub.a("linen"), 'b', new ItemStack(IB.woodMachine1, 1, 1)});
		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 4), new Object[]{"sr ", "rr ", " b ", 's', "stickWood", 'r', ItemFleSub.a("ramie_rope"), 'r', Blocks.stone, 'b', new ItemStack(IB.woodMachine1, 1, 1)});

		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 0), new Object[]{"s s", " h ", "s s", 's', "plateStone", 'h', "craftingToolChisel"});
		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 1), new Object[]{"shs", "s s", 's', "plateStone", 'h', "craftingToolHardHammer"});
		
		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 20), new Object[]{" p ", " o ", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, Materials.ditch_stone, 60), new Object[]{" p ", " o ", "xxx", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 100), new Object[]{" p ", "xox", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
		DitchInfo[] infos = new DitchInfo[]{Materials.ditch_wood0, Materials.ditch_wood1, Materials.ditch_wood2, Materials.ditch_wood3, Materials.ditch_wood4, Materials.ditch_wood5};
		for(int i = 0; i < infos.length; ++i)
		{
			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 20), new Object[]{" p ", " o ", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, infos[i], 60), new Object[]{" p ", " o ", "xxx", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 100), new Object[]{" p ", "xox", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
		}
		addRecipe(new TreeCuttingRecipe());

		RecipeSorter.register(FLE.MODID + ":shaped", ShapedFleRecipe.class, SHAPED, "before:minecraft:shaped");
		RecipeSorter.register(FLE.MODID + ":toolcrafting", ToolCraftingRecipe.class, SHAPED, "after:" + FLE.MODID + ":shaped");
		RecipeSorter.register(FLE.MODID + ":tree", TreeCuttingRecipe.class, SHAPED, "after:" + FLE.MODID + ":shaped");
		RecipeSorter.register(FLE.MODID + ":shapeless", ShapelessFleRecipe.class, SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");
		
		CastingPoolRecipe.init();
		FLEBoilingHeaterRecipe.init();
		FLEOilMillRecipe.init();
		FLESifterRecipe.init();
		FLEStoneMillRecipe.init();
		FLEDryingRecipe.init();
		FLEPolishRecipe.init();
		FLEBoilingHeaterRecipe.postInit(cfg);
		FLEOilMillRecipe.postInit(cfg);
		FLESifterRecipe.postInit(cfg);
		FLEStoneMillRecipe.postInit(cfg);
		FLEDryingRecipe.postInit(cfg);
		FLEPolishRecipe.postInit(cfg);
		AxeHandler.init();
	}

	private static void addOre(String str, Item obj)
	{
		OreDictionary.registerOre(str, new ItemStack(obj, 1, OreDictionary.WILDCARD_VALUE));
	}
	private static void addOre(String str, Block obj)
	{
		OreDictionary.registerOre(str, new ItemStack(obj, 1, OreDictionary.WILDCARD_VALUE));
	}		
	private static void addOre(String str, ItemStack obj)
	{
		OreDictionary.registerOre(str, obj);
	}
	
	private static void addShapedRecipe(RecipesTab aTab, Object output, Object...inputs)
	{
		GameRegistry.addRecipe(new ShapedFleRecipe(aTab, output, inputs));
	}
	
	private static void addShapelessRecipe(RecipesTab aTab, Object output, Object...inputs)
	{
		GameRegistry.addRecipe(new ShapelessFleRecipe(aTab, output, inputs));
	}
	
	private static void addRecipe(IRecipe recipe)
	{
		GameRegistry.addRecipe(recipe);
	}

	public static void completeInit()
	{
		for(Object obj : CraftingManager.getInstance().getRecipeList())
		{
			IRecipe recipe = (IRecipe) obj;
			if(recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe || recipe instanceof ShapedFleRecipe)
			{
				FLEShapedRecipe.register(recipe);
			}
			else if(recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe || recipe instanceof ShapelessFleRecipe)
			{
				FLEShapelessRecipe.register(recipe);
			}
		}
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEShapedRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEShapelessRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEPolishRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEWashingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEDryingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEClayRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLECastingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEStoneMillRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLESifterRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEColdForgingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEOilMillRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new fle.core.cg.FLEPolishRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new FLEWashingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new fle.core.cg.FLEDryingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new FLEClayRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabCopperAge, new FLECastingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabCopperAge, new FLEColdForgingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLEStoneMillRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLESifterRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLEOilMillRecipe());
	}
}