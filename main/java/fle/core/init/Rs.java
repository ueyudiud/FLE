package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import fle.cg.recipe.FLEClayRecipe;
import fle.cg.recipe.FLEShapedRecipe;
import fle.cg.recipe.FLEShapelessRecipe;
import fle.cg.recipe.FLEWashingRecipe;
import fle.core.block.BlockRock;
import fle.core.block.ItemDitch;
import fle.core.block.ItemRopeLadder;
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
					GameRegistry.addRecipe(new ShapedFleRecipe(tStack, new Object[]{"o", "x", 'x', new ItemBaseStack(tStack1), 'o', "craftingToolSaw"}));
					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
				}
			}
		}
	}
	
	public static void init(FLEConfiguration cfg)
	{
		OreDictionary.registerOre("wick", Items.string);
		OreDictionary.registerOre("wick", ItemFleSub.a("ramie_rope"));
		OreDictionary.registerOre("rope", ItemFleSub.a("ramie_rope"));
		OreDictionary.registerOre("tie", ItemFleSub.a("ramie_rope"));
		OreDictionary.registerOre("tie", ItemFleSub.a("rattan"));
		OreDictionary.registerOre("tie", Items.string);
		OreDictionary.registerOre("cropMillet", ItemFleSub.a("millet"));
		OreDictionary.registerOre("cropPotato", ItemFleFood.a("potato"));
		OreDictionary.registerOre("cropCitron", ItemFleFood.a("citron"));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 101));
		OreDictionary.registerOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 5));
		OreDictionary.registerOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 103));
		OreDictionary.registerOre("craftingToolDecortingPlate", new ItemStack(IB.tool, 1, 13));
		OreDictionary.registerOre("craftingToolDecortingStick", new ItemStack(IB.tool, 1, 14));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolFirestarter", new ItemStack(IB.tool, 1, 7));
		OreDictionary.registerOre("craftingToolChisel", new ItemStack(IB.tool, 1, 105));
		OreDictionary.registerOre("craftingToolBowsaw", new ItemStack(IB.tool, 1, 106));
		OreDictionary.registerOre("craftingToolWhetstone", new ItemStack(IB.tool, 1, 8));
		OreDictionary.registerOre("craftingToolSaw", new ItemStack(IB.tool, 1, 106));
		OreDictionary.registerOre("craftingToolAdz", new ItemStack(IB.tool, 1, 107));
		OreDictionary.registerOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 15));
		OreDictionary.registerOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 108));
		OreDictionary.registerOre("craftingToolNeedleTier1", new ItemStack(IB.tool, 1, 108));
		OreDictionary.registerOre("craftingToolSpinning", new ItemStack(IB.tool, 1, 16));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_oak"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_spruce"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_birch"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_jungle"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_acacia"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_darkoak"));
		OreDictionary.registerOre("flePlankWood", new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("rockCompactStone", BlockRock.a(Materials.CompactStone));
		OreDictionary.registerOre("dustLimestone", ItemFleSub.a("dust_limestone"));
		OreDictionary.registerOre("dustQuickLime", ItemFleSub.a("dust_quicklime"));
		OreDictionary.registerOre("dustPlantAsh", ItemFleSub.a("plant_ash"));
		OreDictionary.registerOre("plateStone", ItemFleSub.a("stone_plate"));
		OreDictionary.registerOre("plateArgil", ItemFleSub.a("argil_plate"));
		OreDictionary.registerOre("brickArgil", ItemFleSub.a("argil_brick"));
		OreDictionary.registerOre("ballArgil", ItemFleSub.a("argil_ball"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_as_0"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_as_1"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_0"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_1"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_sn_0"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_sn_1"));
		OreDictionary.registerOre("ingotAbstractBronze", ItemFleSub.a("ingot_cu_pb_sn"));
		for(EnumAtoms atom : EnumAtoms.values())
		{
			if(atom.contain(SubTag.ATOM_metal))
				if(ItemFleSub.a("ingot_" + atom.name().toLowerCase()) != null)
					OreDictionary.registerOre("ingot" + atom.getName(), ItemFleSub.a("ingot_" + atom.name().toLowerCase()));
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
		
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("branch_bush"), new Object[]{"branchWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("wooden_wedge"), new Object[]{"xo", 'x', "flePlankWood", 'o', "craftingToolAdz"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Flint), new Object[]{ItemFleSub.a("flint_c"), ItemFleSub.a("branch_bush"), "tie"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Obsidian), new Object[]{ItemFleSub.a("chip_obsidian"), ItemFleSub.a("branch_bush"), "tie"}));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "rough_stone_axe", 1, ItemFleSub.a("branch_bush"), "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_hammer", 1, ItemFleSub.a("branch_bush"), "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_axe", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_shovel", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_hammer", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_spade_hoe", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabOldStoneAge, "flint_arrow", 2, "stickWood", Items.feather));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabNewStoneAge, "stone_sickle", 1, "stickWood", "tie"));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabBronzeAge, ItemToolHead.a("stone_spinning_disk", Materials.Stone), new Object[]{ItemFleSub.a("stone_b"), "craftingToolHardHammer", "craftingToolChisel", "craftingToolWhetstone"}));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabBronzeAge, "stone_spinning_disk", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_axe", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_pickaxe", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_shovel", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_chisel", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_bowsaw", "stickWood"));
		GameRegistry.addRecipe(new ToolCraftingRecipe(RecipesTab.tabCopperAge, "metal_adz", "stickWood"));
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
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_empty"), new Object[]{"rsr", "w w", " w ", 's', "stickWood", 'r', "rope", 'w', "logWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustPlantAsh", "dustPlantAsh", "dustPlantAsh"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_lime_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustQuickLime", "dustQuickLime", "dustQuickLime"}));

		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemRopeLadder.a(4), new Object[]{" s ", "rsr", " s ", 'r', ItemFleSub.a("ramie_bundle_rope"), 's', "stickWood"}));
		for(int i = 2; i < 9; ++i)
		{
			Object[] objs = new Object[i];
			Arrays.fill(objs, IB.ropeLadder);
			GameRegistry.addRecipe(new RopeLadderCraftingRecipe(true, objs));
		}
		GameRegistry.addRecipe(new RopeLadderCraftingRecipe(false, IB.ropeLadder));

		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabOldStoneAge, Items.string, new Object[]{ItemFleSub.a("spinneret")}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("crushed_bone"), new Object[]{Items.bone, "craftingToolHardHammer"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("chicken_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.chicken}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("pork_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.porkchop}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_fiber_dry")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_rope")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemFleSub.a("ramie_bundle_rope")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("twine", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("ramie_fiber_debonded")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_thread", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("cotton")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("sack_empty"), new Object[]{"nxx", "sxx", 'x', ItemFleSub.a("linen_b"), 's', ItemFleSub.a("twine"), 'n', "craftingToolNeedle"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton", 3), new Object[]{ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), "stickWood"}));
		
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("leaves_dry")}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_millet_wholemeal"), new Object[]{"cropMillet", "craftingToolDecortingPlate", "craftingToolDecortingStick"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_wheat_wholemeal"), new Object[]{"cropWheat", "craftingToolDecortingPlate", "craftingToolDecortingStick"}));
		
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("cemented_grit", 8), new Object[]{Blocks.sand, Blocks.sand, Blocks.clay, Blocks.clay}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("stone_a", 9), new Object[]{Blocks.cobblestone}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.workbench, 1, 0), new Object[]{"logWood", "logWood", "logWood", "logWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 0), new Object[]{Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 0), new Object[]{"plateStone", "plateStone", "plateStone", "plateStone"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 1), new Object[]{"xox", "xxx", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 1), new Object[]{"xox", "xox", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 2), new Object[]{"xox", "cxc", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 3), new Object[]{"cxc", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 4), new Object[]{" p ", "xox", " p ", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 5), new Object[]{"ppp", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 1), new Object[]{"psp", "sss", 's', "stickWood", 'p', "flePlankWood"}));
		GameRegistry.addRecipe(new ShapelessFleRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("millstone"), new Object[]{Blocks.stone, "craftingToolHardHammer", "craftingToolChisel"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 2), new Object[]{"sm ", "ppp", " b ", 's', "stickWood", 'p', "plateStone", 'm', ItemFleSub.a("millstone"), 'b', new ItemStack(IB.woodMachine1, 1, 1)}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 3), new Object[]{" f ", "sfs", " b ", 's', ItemFleSub.a("ramie_rope"), 'f', ItemFleSub.a("linen"), 'b', new ItemStack(IB.woodMachine1, 1, 1)}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 4), new Object[]{"sr ", "rr ", " b ", 's', "stickWood", 'r', ItemFleSub.a("ramie_rope"), 'r', Blocks.stone, 'b', new ItemStack(IB.woodMachine1, 1, 1)}));
		
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 20), new Object[]{" p ", " o ", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, Materials.ditch_stone, 60), new Object[]{" p ", " o ", "xxx", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
		GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 100), new Object[]{" p ", "xox", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
		DitchInfo[] infos = new DitchInfo[]{Materials.ditch_wood0, Materials.ditch_wood1, Materials.ditch_wood2, Materials.ditch_wood3, Materials.ditch_wood4, Materials.ditch_wood5};
		for(int i = 0; i < infos.length; ++i)
		{
			GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 20), new Object[]{" p ", " o ", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
			GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, infos[i], 60), new Object[]{" p ", " o ", "xxx", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
			GameRegistry.addRecipe(new ShapedFleRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 100), new Object[]{" p ", "xox", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"}));
		}
		GameRegistry.addRecipe(new TreeCuttingRecipe());

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
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.cg.recipe.FLEPolishRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEWashingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.cg.recipe.FLEDryingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEClayRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new fle.cg.recipe.FLEPolishRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new FLEWashingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new fle.cg.recipe.FLEDryingRecipe());
		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new FLEClayRecipe());
		//Map<ItemAbstractStack, DropInfo> washRecipes = WashingRecipe.getRecipes();
		//for(ItemAbstractStack tStack : washRecipes.keySet())
		{
			//CraftGuide.instance.registerRecipe(RecipesTab.tabOldStoneAge, new fle.cg.recipe.WashingRecipe(tStack, washRecipes.get(tStack)));
			//CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new fle.cg.recipe.WashingRecipe(tStack, washRecipes.get(tStack)));
		}
		//for(DryingRecipe recipe : FLEDryingRecipe.getInstance().getRecipes())
		{
			//CraftGuide.instance.registerRecipe(RecipesTab.tabNewStoneAge, new fle.cg.recipe.DryingRecipe(recipe));
			//CraftGuide.instance.registerRecipe(RecipesTab.tabClassic, new fle.cg.recipe.DryingRecipe(recipe));
		}
	}
}