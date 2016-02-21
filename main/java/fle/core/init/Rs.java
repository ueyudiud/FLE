package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.GameRegistry;
import flapi.cg.RecipesTab;
import flapi.recipe.ShapedFleRecipe;
import flapi.recipe.ShapelessFleRecipe;
import flapi.recipe.stack.AbstractStack;
import flapi.util.FleLog;
import fle.FLE;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

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
	
//	private static List<FakeCraftingInventory> removeList = new ArrayList();
//	private static Map<FakeCraftingInventory, ItemStack> logCraftList = new HashMap();
	public static Map<AbstractStack, ItemStack> logList = new HashMap();
	
	public static ItemStack getLogCraftOutput(ItemStack input)
	{
		for(Entry<AbstractStack, ItemStack> e : logList.entrySet())
		{
			if(e.getKey().similar(input)) return e.getValue().copy();
		}
		return null;
	}
	
//	public static void registerRemovedCrafting(FakeCraftingInventory inv)
//	{
//		removeList.add(inv);
//	}
	
	public static void reloadRecipe()
	{
//		removeList.clear();
//		for(ItemStack stack : OreDictionary.getOres("logWood"))
//		{
//			if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
//			{
//				for(int i = 0; i < 16; ++i)
//				{
//					ItemStack tStack = stack.copy();
//					tStack.setItemDamage(i);
//					logCraftList.put(FakeCraftingInventory.init("x", 'x', tStack), tStack);
//				}
//			}
//			logCraftList.put(FakeCraftingInventory.init("x", 'x', stack), stack);
//		}
//		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", 'x', "plankWood"));
//		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", "xx", 'x', Blocks.glass));
//		registerRemovedCrafting(FakeCraftingInventory.init(" xs", "x s", " xs", 'x', "stickWood", 's', Items.string));
//		registerRemovedCrafting(FakeCraftingInventory.init("x", "x", 'x', "plankWood"));
//		registerRemovedCrafting(FakeCraftingInventory.init("x", 'x', Items.bone));
//		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 0), 'x', "stickWood"));
//		registerRemovedCrafting(FakeCraftingInventory.init("oxv", 'o', Items.egg, 'x', Items.sugar, 'v', Blocks.pumpkin));
//		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 1), 'x', "stickWood"));
//		registerRemovedCrafting(FakeCraftingInventory.init("x x", " x ", 'x', "plankWood"));
//		
//		registerRemovedCrafting(FakeCraftingInventory.init(" x", "x ", 'x', Items.iron_ingot));
//
//		for(Object obj : toolMaterial)
//		{
//			try
//			{
//				for(String[] map : toolCraftingMap)
//				{
//					registerRemovedCrafting(FakeCraftingInventory.init(map[0], map[1], map[2], 's', "stickWood", 'x', obj));
//				}
//			}
//			catch(Throwable e)
//			{
//				FleLog.getLogger().info("FLE : Fail to remove recipe of " + obj.toString() + ", does it isn't register or a bug?", e);
//			}
//		}
//		
//		List list = new ArrayList(CraftingManager.getInstance().getRecipeList());
//		for(Object rawTarget : list)
//		{
//			IRecipe recipe = (IRecipe) rawTarget;
//			for(FakeCraftingInventory inv : removeList)
//			{
//				if(recipe.matches(inv, null))
//				{
//					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
//				}
//			}
//			for(FakeCraftingInventory inv : logCraftList.keySet())
//			{
//				if(recipe.matches(inv, null))
//				{
//					ItemStack tStack = recipe.getCraftingResult(inv);
//					ItemStack tStack1 = logCraftList.get(inv);
//					logList.put(new BaseStack(tStack1), tStack);
//					addShapedRecipe(RecipesTab.tabClassic, tStack, new Object[]{"o", "x", 'x', new BaseStack(tStack1), 'o', "craftingToolSaw"});
//					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
//				}
//			}
//		}
	}
	
	public static void init()
	{
//		for(TreeInfo info : BlockFleLog.trees)
//		{
//			if(info.log() != null)
//				addOre("logWood", info.log());
//			if(info.leaves() != null)
//				addOre("leaves", info.leaves());
//		}
//		addOre();
//		MatterDicts.init();
//
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.WATER, 1000), ItemFleSub.a("wood_bucket_0_water"), ItemFleSub.a("wood_bucket_0_empty"));
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(IB.plant_ash_mortar, 1000), ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), ItemFleSub.a("wood_bucket_0_empty"));
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(IB.lime_mortar, 1000), ItemFleSub.a("wood_bucket_0_lime_mortar"), ItemFleSub.a("wood_bucket_0_empty"));
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.WATER, 1000), ItemFleSub.a("bowl_water"), new ItemStack(Items.bowl));
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(IB.wheat_alcohol, 200), ItemFleFood.a("jug_argil_wheat"), ItemFleSub.a("jug_argil"));
//		
//		//MatterReactionRecipe.init();
//		
//		SolidRegistry.registerSolidContainer(new SolidStack(IB.limestone, 108), ItemFleSub.a("dust_quicklime"));
//		SolidRegistry.registerSolidContainer(new SolidStack(IB.plant_ash, 108), ItemFleSub.a("plant_ash"));
//		SolidRegistry.registerSolidContainer(new SolidStack(IB.brown_sugar, 108), ItemFleFood.a("brown_sugar"));
//		SolidRegistry.registerSolidContainer(new SolidStack(IB.millet_c, 144), ItemFleSub.a("millet"));
//		SolidRegistry.registerSolidContainer(new SolidStack(IB.wheat_c, 144), new ItemStack(Items.wheat));
//
//		FluidDictionary.registerFluid("oil", IB.animalOil);
//		FluidDictionary.registerFluid("oil", IB.plantOil);
//		FluidDictionary.registerFluid("mortarLime", IB.lime_mortar);
//		FluidDictionary.registerFluid("oilAnimal", IB.animalOil);
//		FluidDictionary.registerFluid("oilPlant", IB.plantOil);
//		FluidDictionary.registerFluid("dextrin", IB.wheat_dextrin);
//		FluidDictionary.registerFluid("dextrin", IB.millet_dextrin);
//		FluidDictionary.registerFluid("dextrin", IB.potato_dextrin);
//		FluidDictionary.registerFluid("dextrin", IB.sweet_potato_dextrin);
//
//		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_brick"), ItemFleSub.a("argil_brick"), 0.0F);
//		GameRegistry.addSmelting(ItemFleSub.a("argil_unsmelted_plate"), ItemFleSub.a("argil_plate"), 0.0F);
//		GameRegistry.addSmelting(ItemFleSub.a("dust_limestone"), ItemFleSub.a("dust_quicklime"), 0.0F);
//		GameRegistry.addSmelting(ItemFleSub.a("jug_argil_unsmelted"), ItemFleSub.a("jug_argil"), 0.0F);
//		
//		addShapelessRecipe(RecipesTab.tabClassic, ItemFleSub.a("chip_stone"), ItemFleSub.a("stone_a"));
//		addShapelessRecipe(RecipesTab.tabClassic, ItemFleSub.a("fragment_stone"), ItemFleSub.a("stone_b"));
//		
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("branch_bush"), new Object[]{"branchWood"});
//		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_bar_grizzy", Materials.HardWood), new Object[]{"xo", "ox", 'x', "branchWood", 'o', "rope"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"});
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Flint), new Object[]{ItemFleSub.a("flint_c"), ItemFleSub.a("branch_bush"), "tie"});
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("rough_stone_axe", Materials.Obsidian), new Object[]{ItemFleSub.a("chip_obsidian"), ItemFleSub.a("branch_bush"), "tie"});
//		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemToolHead.a("stone_spinning_disk", Materials.Stone), new Object[]{ItemFleSub.a("fragment_stone"), "craftingToolHardHammer", "craftingToolChisel", "craftingToolWhetstone"});
//		addRecipe(new OilLampAddFuelRecipe(100, Items.beef));
//		addRecipe(new OilLampAddFuelRecipe(150, Items.porkchop));
//		addRecipe(new OilLampAddFuelRecipe(75, Items.rotten_flesh));
//		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood), new Object[]{"x", "o", 'x', "logWood", 'o', "stickWood"});
//		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("wooden_hammer", Materials.HardWood, 2), new Object[]{"x", "o", 'x', "logWood", 'o', ItemFleSub.a("branch_bush")});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("argil_ball", 3), new Object[]{Items.clay_ball, Items.clay_ball, ItemFleSub.a("dust_sand"), "dustLimestone"});
//		addShapedRecipe(RecipesTab.tabOldStoneAge, new ItemStack(IB.woodMachine, 1, 0), new Object[]{"x", "o", 'x', "logWood", 'o', ItemFleSub.a("pile_gravel")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.woodMachine1, 1, 0), new Object[]{"xx", "xx", 'x', "stickWood"});
//		addShapedRecipe(RecipesTab.tabOldStoneAge, Blocks.cobblestone, new Object[]{"xx", "xx", 'x', ItemFleSub.a("fragment_stone")});
//		addShapedRecipe(RecipesTab.tabOldStoneAge, ItemTool.a("whetstone", Materials.Stone), new Object[]{"xx", 'x', ItemFleSub.a("fragment_stone")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemTool.a("wooden_drilling_firing", Materials.HardWood), new Object[]{" s", "wl", 's', "stickWood", 'w', "logWood", 'l', ItemFleSub.a("tinder")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_empty"), new Object[]{"rsr", "w w", " w ", 's', "stickWood", 'r', "rope", 'w', "logWood"});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustPlantAsh", "dustPlantAsh", "dustPlantAsh"});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("wood_bucket_0_lime_mortar"), new Object[]{ItemFleSub.a("wood_bucket_0_water"), "dustQuickLime", "dustQuickLime", "dustQuickLime"});
//		
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemRopeLadder.a(4), new Object[]{" s ", "rsr", " s ", 'r', ItemFleSub.a("ramie_bundle_rope"), 's', "stickWood"});
//		for(int i = 2; i < 9; ++i)
//		{
//			Object[] objs = new Object[i];
//			Arrays.fill(objs, IB.ropeLadder);
//			addRecipe(new RopeLadderCraftingRecipe(true, objs));
//		}
//		addRecipe(new RopeLadderCraftingRecipe(false, IB.ropeLadder));
//
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, Items.string, new Object[]{ItemFleSub.a("spinneret")});
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("sinew"), new Object[]{Items.leather});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("straw_rope"), new Object[]{ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry"), ItemFleSub.a("straw_dry")});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("crushed_bone"), new Object[]{Items.bone, "craftingToolHardHammer"});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("chicken_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.chicken});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("pork_kebab_raw", 2), new Object[]{"stickWood", "stickWood", Items.porkchop});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_fiber_dry")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("ramie_rope")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemFleSub.a("ramie_bundle_rope")});
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("sisal_fiber"), new Object[]{ItemFleSub.a("leaves_sisal"), ItemFleSub.a("leaves_sisal"), ItemFleSub.a("leaves_sisal"), ItemFleSub.a("leaves_sisal")});
//		addShapelessRecipe(RecipesTab.tabOldStoneAge, ItemFleSub.a("sisal_rope"), new Object[]{ItemFleSub.a("sisal_fiber"), ItemFleSub.a("sisal_fiber")});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("twine", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("ramie_fiber_debonded")});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_thread", 2), new Object[]{"tx ", "xsx", " x ", 't', "craftingToolSpinning", 's', "stickWood", 'x', ItemFleSub.a("cotton")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("linen"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("ramie_rope"), 'n', "craftingToolNeedle"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("ramie_fiber_debonded", 4), new Object[]{" x ", "xsx", " x ", 'x', ItemFleSub.a("ramie_fiber_dry"), 's', ItemFleSub.a("plant_ash_soap")});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("linen_b"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("twine"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xx ", "xxn", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"xxn", "xx ", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{"nxx", " xx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton_gauze"), new Object[]{" xx", "nxx", 'x', ItemFleSub.a("cotton_thread"), 'n', "craftingToolNeedleTier1"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("sack_empty"), new Object[]{"nxx", "sxx", 'x', ItemFleSub.a("linen_b"), 's', ItemFleSub.a("twine"), 'n', "craftingToolNeedle"});
//		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("cotton", 3), new Object[]{ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), ItemFleSub.a("cotton_rough"), "stickWood"});
//		
//		addShapedRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemFleSub.a("leaves_dry")});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_millet_wholemeal"), new Object[]{"cropMillet", "craftingToolDecortingPlate", "craftingToolDecortingStick"});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleFood.a("groats_wheat_wholemeal"), new Object[]{"cropWheat", "craftingToolDecortingPlate", "craftingToolDecortingStick"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(Items.bowl, 4), new Object[]{"x", "o", 'x', "craftingToolKnife", 'o', "plankWood"});
//		
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("cemented_grit", 2), new Object[]{ItemFleSub.a("dust_sand"), ItemFleSub.a("dust_sand"), Items.clay_ball, Items.clay_ball});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, ItemFleSub.a("chip_stone", 9), new Object[]{Blocks.cobblestone});
//		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("yeast_strain", 4), new Object[]{ItemFleSub.a("pile_dirt"), ItemFleFood.a("flour"), new FluidContainerStack(FluidRegistry.WATER)});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.workbench, 1, 0), new Object[]{"logWood", "logWood", "logWood", "logWood"});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 0), new Object[]{Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone});
//		addShapelessRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 0), new Object[]{"plateStone", "plateStone", "plateStone", "plateStone"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine1, 1, 1), new Object[]{"xox", "xxx", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 1), new Object[]{"xox", "xox", 'x', "plateArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar")});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 2), new Object[]{"xox", "cxc", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 3), new Object[]{"cxc", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'c', "ballArgil"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 4), new Object[]{" p ", "xox", " p ", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"});
//		addShapedRecipe(RecipesTab.tabNewStoneAge, new ItemStack(IB.stoneMachine, 1, 5), new Object[]{"ppp", "xox", 'x', "brickArgil", 'o', ItemFleSub.a("wood_bucket_0_lime_mortar"), 'p', "plateArgil"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 1), new Object[]{"psp", "sss", 's', "stickWood", 'p', "flePlankWood"});
//		addShapelessRecipe(RecipesTab.tabBronzeAge, ItemFleSub.a("millstone"), new Object[]{Blocks.stone, "craftingToolHardHammer", "craftingToolChisel"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 2), new Object[]{"sm ", "ppp", " b ", 's', "stickWood", 'p', "plateStone", 'm', ItemFleSub.a("millstone"), 'b', new ItemStack(IB.woodMachine1, 1, 1)});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 3), new Object[]{" f ", "sfs", " b ", 's', ItemFleSub.a("ramie_rope"), 'f', ItemFleSub.a("linen"), 'b', new ItemStack(IB.woodMachine1, 1, 1)});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.woodMachine1, 1, 4), new Object[]{"sr ", "rr ", " b ", 's', "stickWood", 'r', ItemFleSub.a("ramie_rope"), 'r', Blocks.stone, 'b', new ItemStack(IB.woodMachine1, 1, 1)});
//
//		addShapedRecipe(RecipesTab.tabBronzeAge, IB.fluidTransfer, new Object[]{"ss", "oo", 's', "chipAbstractStone", 'o', "plateStone"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 0), new Object[]{"s s", " h ", "s s", 's', "plateStone", 'h', "craftingToolChisel"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 1), new Object[]{"shs", "s s", 's', "plateStone", 'h', "craftingToolHardHammer"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 2), new Object[]{" s ", "shs", " s ", 's', "plateStone", 'h', "craftingToolHardHammer"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(IB.tank, 1, 3), new Object[]{"shs", "sss", 's', "plateStone", 'h', "craftingToolHardHammer"});
//		
//		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 20), new Object[]{" p ", " o ", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, Materials.ditch_stone, 60), new Object[]{" p ", " o ", "xxx", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//		addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(Materials.ditch_stone, 100), new Object[]{" p ", "xox", " x ", 'x', Blocks.stone, 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//		addShapedRecipe(RecipesTab.tabBronzeAge, new ItemStack(Items.bowl, 4), new Object[]{"x x", " x ", 'x', ItemFleSub.a("rotproof_plank")});
//		DitchInfo[] infos = new DitchInfo[]{Materials.ditch_wood0, Materials.ditch_wood1, Materials.ditch_wood2, Materials.ditch_wood3, Materials.ditch_wood4, Materials.ditch_wood5};
//		for(int i = 0; i < infos.length; ++i)
//		{
//			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 20), new Object[]{" p ", " o ", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(2, infos[i], 60), new Object[]{" p ", " o ", "xxx", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//			addShapedRecipe(RecipesTab.tabCopperAge, ItemDitch.a(infos[i], 100), new Object[]{" p ", "xox", " x ", 'x', infos[i].getBlock(), 'o', "craftingToolChisel", 'p', "craftingToolHardHammer"});
//		}
//		addShapedRecipe(RecipesTab.tabBronzeAge, ItemThermalWire.a(Materials.Copper, 3), new Object[]{" x ", "ooo", 'x', "craftingToolChisel", 'o', "ingotCopper"});
//		addRecipe(new OreDivideRecipe());
//		
		RecipeSorter.register(FLE.MODID + ":shaped", ShapedFleRecipe.class, SHAPED, "before:minecraft:shaped");
		RecipeSorter.register(FLE.MODID + ":shapeless", ShapelessFleRecipe.class, SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");
		
//		WashingRecipe.init();
//		PlayerToolCraftingRecipe.init();
//		CastingPoolRecipe.init();
//		ColdForgingRecipe.init();
//		FLEBoilingHeaterRecipe.init();
//		FLEOilMillRecipe.init();
//		FLESifterRecipe.init();
//		FLEStoneMillRecipe.init();
//		FLEDryingRecipe.init();
//		FLEPolishRecipe.init();
//		FLESoakRecipe.init();
//		MatterMeltingRecipe.init();
//		
//		BarrelDrinkRecipe.init();
//		BarrelDrinkMixRecipe.init();
//		
//		AxeHandler.init();
//		BurnHandler.init();
//		StoneHammerHandler.init();
	}
	
	public static void addOre()
	{
		addOre("wick", Items.string);
		addOre("wick", ItemFleSub.a("ramie_rope"));
		addOre("wick", ItemFleSub.a("sisal_rope"));
		addOre("rope", ItemFleSub.a("ramie_rope"));
		addOre("rope", ItemFleSub.a("rattan"));
		addOre("rope", ItemFleSub.a("sisal_rope"));
		addOre("tie", ItemFleSub.a("ramie_rope"));
		addOre("tie", ItemFleSub.a("rattan"));
		addOre("tie", ItemFleSub.a("sinew"));
		addOre("tie", ItemFleSub.a("straw_rope"));
		addOre("tie", ItemFleSub.a("sisal_rope"));
		addOre("tie", Items.string);
		addOre("cropMillet", ItemFleSub.a("millet"));
		addOre("cropPotato", ItemFleFood.a("potato"));
		addOre("cropCitron", ItemFleFood.a("citron"));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		addOre("craftingToolAxe", new ItemStack(IB.tool, 1, 101));
		addOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 5));
		addOre("craftingToolHardHammer", new ItemStack(IB.tool, 1, 103));
		addOre("craftingToolSoftHammer", new ItemStack(IB.tool, 1, 6));
		addOre("craftingToolDecortingPlate", new ItemStack(IB.tool, 1, 13));
		addOre("craftingToolDecortingStick", new ItemStack(IB.tool, 1, 14));
		addOre("craftingToolFirestarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		addOre("craftingToolFirestarter", new ItemStack(IB.tool, 1, 7));
		addOre("craftingResourceTinder", ItemFleSub.a("tinder_smoldering"));
		addOre("craftingToolChisel", new ItemStack(IB.tool, 1, 105));
		addOre("craftingToolBowsaw", new ItemStack(IB.tool, 1, 106));
		addOre("craftingToolWhetstone", new ItemStack(IB.tool, 1, 8));
		addOre("craftingToolSaw", new ItemStack(IB.tool, 1, 106));
		addOre("craftingToolAdz", new ItemStack(IB.tool, 1, 107));
		addOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 15));
		addOre("craftingToolNeedle", new ItemStack(IB.tool, 1, 108));
		addOre("craftingToolNeedleTier1", new ItemStack(IB.tool, 1, 108));
		addOre("craftingToolSpinning", new ItemStack(IB.tool, 1, 16));
		addOre("craftingToolBarGrizzy", new ItemStack(IB.tool, 1, 17));
		addOre("craftingToolKnife", new ItemStack(IB.tool, 1, 18));
		addOre("branchWood", ItemFleSub.a("branch_oak"));
		addOre("branchWood", ItemFleSub.a("branch_spruce"));
		addOre("branchWood", ItemFleSub.a("branch_birch"));
		addOre("branchWood", ItemFleSub.a("branch_jungle"));
		addOre("branchWood", ItemFleSub.a("branch_acacia"));
		addOre("branchWood", ItemFleSub.a("branch_darkoak"));
		addOre("branchWood", ItemFleSub.a("branch_beech"));
		addOre("flePlankWood", new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		addOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));
//		addOre("rockCompactStone", BlockRock.a(Materials.CompactStone));
		addOre("dustLimestone", ItemFleSub.a("dust_limestone"));
		addOre("dustQuickLime", ItemFleSub.a("dust_quicklime"));
		addOre("dustPlantAsh", ItemFleSub.a("plant_ash"));
		addOre("plateStone", ItemFleSub.a("stone_plate"));
		addOre("plateArgil", ItemFleSub.a("argil_plate"));
		addOre("brickArgil", ItemFleSub.a("argil_brick"));
		addOre("ballArgil", ItemFleSub.a("argil_ball"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_stone"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_limestone"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_sandstone"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_netherstone"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_obsidian"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_rhyolite"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_andesite"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_basalt"));
		addOre("chipAbstractStone", ItemFleSub.a("chip_peridotite"));
		addOre("chipHardStone", ItemFleSub.a("chip_stone"));
		addOre("chipHardStone", ItemFleSub.a("chip_rhyolite"));
		addOre("chipHardStone", ItemFleSub.a("chip_andesite"));
		addOre("chipHardStone", ItemFleSub.a("chip_basalt"));
		addOre("chipHardStone", ItemFleSub.a("chip_peridotite"));
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
		try
		{
//			File file = new File(FLE.fle.getPlatform().getMinecraftDir(), "config/fle/recipe");
//			if(!file.canExecute()) file.mkdirs();
//			JsonHandler loader0 = new JsonHandler("fle/recipe/boiling_heater.json");
//			JsonHandler loader1 = new JsonHandler("fle/recipe/oil_mill.json");
//			JsonHandler loader2 = new JsonHandler("fle/recipe/sifter.json");
//			JsonHandler loader3 = new JsonHandler("fle/recipe/stone_mill.json");
//			JsonHandler loader4 = new JsonHandler("fle/recipe/drying.json");
//			JsonHandler loader5 = new JsonHandler("fle/recipe/polish.json");
//			JsonHandler loader6 = new JsonHandler("fle/recipe/soak.json");
//			JsonHandler loader7 = new JsonHandler("fle/recipe/washing.json");
//			MachineRecipes.boilingRecipeHandler.reloadRecipes(loader0);
//			MachineRecipes.oilMillRecipeHandler.reloadRecipes(loader1);
//			MachineRecipes.sifterRecipeHandler.reloadRecipes(loader2);
//			MachineRecipes.stoneMillRecipeHandler.reloadRecipes(loader3);
//			MachineRecipes.dryingRecipeHandler.reloadRecipes(loader4);
//			MachineRecipes.polishRecipeHandler.reloadRecipes(loader5);
//			MachineRecipes.soakRecipeHandler.reloadRecipes(loader6);
//			WashingRecipe.postInit(loader7);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().error("Fail to load recipe", e);
		}
				
//		for(Object obj : CraftingManager.getInstance().getRecipeList())
//		{
//			IRecipe recipe = (IRecipe) obj;
//			if(recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe || recipe instanceof ShapedFleRecipe)
//			{
//				FLEShapedRecipe.register(recipe);
//			}
//			else if(recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe || recipe instanceof ShapelessFleRecipe)
//			{
//				FLEShapelessRecipe.register(recipe);
//			}
//		}
//		FLEPlantGuide.init();
		//CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEShapedRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEShapelessRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEPolishRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEWashingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEDryingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEClayRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLECastingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEStoneMillRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLESifterRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEColdForgingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLEOilMillRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new fle.core.cg.FLESoakRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new fle.core.cg.FLEPolishRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabOldStoneAge, new FLEWashingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new fle.core.cg.FLEDryingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabNewStoneAge, new FLEClayRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabCopperAge, new FLECastingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabCopperAge, new FLEColdForgingRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLEStoneMillRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLESifterRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLEOilMillRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabBronzeAge, new fle.core.cg.FLESoakRecipe());
//		CraftGuide.instance.registerGuideType(RecipesTab.tabClassic, new FLEPlantGuide());
	}
}