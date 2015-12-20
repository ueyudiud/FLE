package fle.core.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import flapi.enums.EnumTool;
import flapi.fluid.FluidBase;
import flapi.material.IChemCondition.EnumOxide;
import flapi.material.IChemCondition.EnumPH;
import flapi.material.PropertyInfo;
import flapi.recipe.stack.OreStack;
import flapi.solid.Solid;
import flapi.solid.Solid.SolidState;
import flapi.util.FleValue;
import fle.core.block.BlockAsh;
import fle.core.block.BlockCharcoal;
import fle.core.block.BlockFirewood;
import fle.core.block.BlockFleFarmland;
import fle.core.block.BlockPlacedItem;
import fle.core.block.machine.BlockClayInventory;
import fle.core.block.machine.BlockClayInventory1;
import fle.core.block.machine.BlockStoneMachine;
import fle.core.block.machine.BlockStoneMachine1;
import fle.core.block.machine.BlockWoodMachine;
import fle.core.block.machine.BlockWoodMachine1;
import fle.core.block.plant.BlockPlant;
import fle.core.block.plant.BlockPlantRattan;
import fle.core.block.tank.BlockMultiTank;
import fle.core.item.ItemBowl;
import fle.core.item.ItemDebug;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.solid.SolidFlour;
import fle.core.util.FleCreativeTab;
import fle.energy.block.BlockThermal;
import fle.energy.block.BlockThermalWire;
import fle.resource.block.BlockFleCrop;
import fle.resource.block.BlockFleRock;
import fle.resource.block.BlockOre;
import fle.resource.block.BlockOreCobble;
import fle.resource.block.BlockPeat;
import fle.resource.item.ItemOre;
import fle.resource.item.ItemTreeLog;
import fle.tool.block.BlockDitch;
import fle.tool.block.BlockOilLamp;
import fle.tool.block.BlockRopeLadder;
import fle.tool.block.BlockWorkbench;
import fle.tool.item.ItemTool;
import fle.tool.item.ItemToolHead;

public class IB 
{
	public static void reloadIB()
	{
		FluidRegistry.LAVA.setTemperature(950);
		ForgeHooks.canToolHarvestBlock(Blocks.obsidian, 0, new ItemStack(Items.wooden_pickaxe));//Init forge hook.
		Blocks.torch.setLightLevel(0.5F);
		Item.getItemFromBlock(Blocks.torch).setMaxStackSize(16);
		Blocks.fire.setLightLevel(0.625F);
		Blocks.lit_pumpkin.setLightLevel(0.625F);
		Blocks.lit_furnace.setLightLevel(0.5F);
		Blocks.obsidian.setResistance(20.0F);
		Blocks.obsidian.setHardness(6.0F);
		Blocks.stone.setHardness(4.0F);
		Blocks.netherrack.setHardness(4.5F);
		Blocks.end_stone.setHardness(5.0F);
		Blocks.enchanting_table.setHardness(6.0F);
		Blocks.yellow_flower.setHardness(0.5F);
		Blocks.red_flower.setHardness(0.5F);
		OreStack treeSapling = new OreStack("treeSapling");
		for(Object obj : Block.blockRegistry.getKeys())
		{
			Block block = (Block) Block.blockRegistry.getObject(obj);
			if(treeSapling.equal(new ItemStack(block)));
			{
				block.setHardness(1.0F);
			}
		}
		Items.arrow.setMaxStackSize(16);
		Blocks.obsidian.setHarvestLevel(EnumTool.pickaxe.name(), 1);
	}

	public static Item debug;
	public static Item tool;
	public static Item toolHead;
	public static Item subItem;
	public static Item treeLog;
	public static Item cropSeed;
	public static Item food;
	public static Item oreChip;
	public static Block crop;
	//public static Block rock;
	public static Block ore;
	//public static Block leaf;
	//public static Block log;
	public static Block ropeLadder;
	public static Block workbench;
	public static Block woodMachine;
	public static Block woodMachine1;
	public static Block stoneMachine;
	public static Block stoneMachine1;
	public static Block oilLamp;
	public static Block firewood;
	public static Block charcoal;
	public static Block ash;
	public static Block argil_unsmelted;
	public static Block argil_smelted;
	public static Block ore_cobble;
	public static Block ditch;
	public static Block farmland;
	public static Block peat;
	public static Block tank;
	public static Block plant;
	public static Block plant_rattan;
	public static Block thermalWire;
	public static Block placedItem;
	public static Fluid animalOil;
	public static Fluid plant_ash_mortar;
	public static Fluid lime_mortar;
	public static Fluid copper;
	public static Fluid lead;
	public static Fluid zinc;
	public static Fluid tin;
	public static Fluid cu_as_0;
	public static Fluid cu_as_1;
	public static Fluid cu_pb_0;
	public static Fluid cu_pb_1;
	public static Fluid cu_sn_0;
	public static Fluid cu_sn_1;
	public static Fluid cu_pb_sn;
	public static Fluid plantOil;
	public static Fluid sugarcane_juice;
	public static Fluid apple_juice;
	public static Fluid brown_sugar_aqua;
	public static Fluid wheat_dextrin;
	public static Fluid millet_dextrin;
	public static Fluid potato_dextrin;
	public static Fluid sweet_potato_dextrin;
	public static Fluid saccharified_dextrin;
	public static Fluid wheat_alcohol;
	public static Solid limestone;
	public static Solid wheat;
	public static Solid wheat_b;
	public static Solid wheat_c;
	public static Solid millet;
	public static Solid millet_b;
	public static Solid millet_c;
	public static Solid plant_ash;
	public static Solid brown_sugar;
	public static Solid Ca_P_fertilizer;
	
	public static void init()
	{
		new FleCreativeTab("fle");
		try
		{
			MinecraftForge.EVENT_BUS.register(new ItemBowl());
		}
		catch(Throwable e)
		{
			;
		}
		
		animalOil = new FluidBase("oil_a", "Animal Oil", new PropertyInfo(0xFFFFFF, 313, 773, 293, 1831, 1500, 0.8F, -1F, 1.0F, 0.7F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/oil").setTemperature(FleValue.WATER_FREEZE_POINT + 25);
		plantOil = new FluidBase("oil_b", "Plant Oil", new PropertyInfo(0xFFFFFF, 267, 781, 294, 1472, 1300, 0.8F, -1F, 0.93F, 0.68F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/plant_oil").setTemperature(FleValue.WATER_FREEZE_POINT + 25);
		sugarcane_juice = new FluidBase("sugarcane_juice", "Sugarcane Juice").setTextureName(FleValue.TEXTURE_FILE + ":fluids/sugarcane_juice").setTemperature(295).setViscosity(1100);
		apple_juice = new FluidBase("apple_juice", "Apple Juice").setTextureName(FleValue.TEXTURE_FILE + ":fluids/apple_juice").setTemperature(FleValue.WATER_FREEZE_POINT + 25).setViscosity(1050);
		brown_sugar_aqua = new FluidBase("brown_sugar_aqua", "Brown Sugar Aqua").setTextureName(FleValue.TEXTURE_FILE + ":fluids/brown_sugar").setTemperature(295).setViscosity(1050);
		copper = new FluidBase("copper", "Copper Liquid", Materials.Copper.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		lead = new FluidBase("lead", "Lead Liquid", Materials.Lead.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		zinc = new FluidBase("zinc", "Zinc Liquid", Materials.Zinc.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		tin = new FluidBase("tin", "Tin Liquid", Materials.Tin.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		plant_ash_mortar = new FluidBase("plant_ash_mortar", "Plant Ash Mortar", new PropertyInfo(0xFFFFFF, 264, 360, 212, 1842, 1800, 1.0F, 3.2F, 0.7F, 1.7F)).setFluidPH(EnumPH.Weak_Alkali).setTextureName(FleValue.TEXTURE_FILE + ":fluids/plant_ash_mortar");
		lime_mortar = new FluidBase("lime_mortar", "Lime Mortar", new PropertyInfo(0xFFFFFF, 264, 360, 212, 1842, 2200, 1.0F, 1.9F, 0.67F, 1.8F)).setFluidPH(EnumPH.Weak_Alkali).setTextureName(FleValue.TEXTURE_FILE + ":fluids/lime_mortar");
		cu_as_0 = new FluidBase("cu_as_0", "Arsenic Bronze Liquid", Materials.CuAs.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_as_1 = new FluidBase("cu_as_1", "High Arsenic Bronze Liquid", Materials.CuAs2.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_pb_0 = new FluidBase("cu_pb_0", "Lead Bronze Liquid", Materials.CuPb.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_pb_1 = new FluidBase("cu_pb_1", "High Lead Bronze Liquid", Materials.CuPb2.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_sn_0 = new FluidBase("cu_sn_0", "Tin Bronze Liquid", Materials.CuSn.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_sn_1 = new FluidBase("cu_sn_1", "High Tin Bronze Liquid", Materials.CuSn2.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		cu_pb_sn = new FluidBase("cu_pb_sn", "Lead Tin Bronze Liquid", Materials.CuSnPb.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		wheat_dextrin = new FluidBase("wheat_dextrin", "Wheat Dextrin").setTextureName(FleValue.TEXTURE_FILE + ":fluids/dextrin").setFluidOxide(EnumOxide.C).setColor(0xFFFCE8).setViscosity(1300).setTemperature(FleValue.WATER_FREEZE_POINT + 105);
		millet_dextrin = new FluidBase("millet_dextrin", "Millet Dextrin").setTextureName(FleValue.TEXTURE_FILE + ":fluids/dextrin").setFluidOxide(EnumOxide.C).setColor(0xF2F1ED).setViscosity(1300).setTemperature(FleValue.WATER_FREEZE_POINT + 105);
		potato_dextrin = new FluidBase("potato_dextrin", "Potato Dextrin").setTextureName(FleValue.TEXTURE_FILE + ":fluids/dextrin").setFluidOxide(EnumOxide.C).setColor(0xFEF0D8).setViscosity(1300).setTemperature(FleValue.WATER_FREEZE_POINT + 105);
		sweet_potato_dextrin = new FluidBase("sweet_potato_dextrin", "Sweet Potato Dextrin").setTextureName(FleValue.TEXTURE_FILE + ":fluids/dextrin").setFluidOxide(EnumOxide.C).setColor(0xFFE7DB).setViscosity(1300).setTemperature(FleValue.WATER_FREEZE_POINT + 105);
		saccharified_dextrin = new FluidBase("saccharified_dextrin", "Saccharified Dextrin").setTextureName(FleValue.TEXTURE_FILE + ":fluids/saccharified_dextrin").setViscosity(1500);
		wheat_alcohol = new FluidBase("wheat_alcohol", "Wheat Alcohol").setTextureName(FleValue.TEXTURE_FILE + ":fluids/alcohol_drink").setFluidOxide(EnumOxide.C).setColor(0xD9E8BB);
		
		limestone = new Solid("limestone", "Limestone").setType(SolidState.Dust).setTextureName(FleValue.TEXTURE_FILE + ":solid/limestone");
		millet = new SolidFlour("millet", "Wholemeal Millet Groats").setTextureName(FleValue.TEXTURE_FILE + ":solid/millet");
		wheat = new SolidFlour("wheat", "Wholemeal Wheat Groats").setTextureName(FleValue.TEXTURE_FILE + ":solid/wheat");
		millet_b = new SolidFlour("millet_b", "Millet Groats").setTextureName(FleValue.TEXTURE_FILE + ":solid/millet_b");
		wheat_b = new SolidFlour("wheat_b", "Wheat Groats").setTextureName(FleValue.TEXTURE_FILE + ":solid/wheat_b");
		millet_c = new Solid("millet_raw", "Millet").setTextureName(FleValue.TEXTURE_FILE + ":solid/millet_c").setType(SolidState.Chip);
		wheat_c = new Solid("wheat_raw", "Wheat").setTextureName(FleValue.TEXTURE_FILE + ":solid/wheat_c").setType(SolidState.Chip);
		plant_ash = new Solid("plant_ash", "Plant Ash").setType(SolidState.Dust).setTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/ash");
		brown_sugar = new Solid("brown_sugar", "Brown Sugar").setType(SolidState.Sick_Dust).setTextureName(FleValue.TEXTURE_FILE + ":solid/brown_sugar");
		Ca_P_fertilizer = new Solid("fertilizer1", "Ca P Fertilizer").setType(SolidState.Sick_Dust).setTextureName(FleValue.TEXTURE_FILE + ":solid/fertilizer1");
		
		debug = new ItemDebug("debug").setMaxStackSize(1).setCreativeTab(FleValue.tabFLE).setTextureName(FleValue.TEXTURE_FILE + ":fle");
		new BlockThermal().setCreativeTab(FleValue.tabFLE).setBlockTextureName(FleValue.TEXTURE_FILE + ":iconsets/debug_thermal").setMaxStackSize(1);
		
		farmland = new BlockFleFarmland("fle_farmland", "Farmland").setBlockTextureName(FleValue.TEXTURE_FILE + ":iconsets/farmland");
		crop = new BlockFleCrop();
		//rock = new BlockRock().setCreativeTab(FleValue.tabFLE).setStepSound(Block.soundTypeStone);
		BlockFleRock.init();
		ore = new BlockOre().setCreativeTab(FleValue.tabFLE).setStepSound(Block.soundTypeStone);
		ore_cobble = new BlockOreCobble("ore.cobble").init();
		oreChip = new ItemOre("ore.chip", "ore").init().setCreativeTab(FleValue.tabFLE);
		//log = new BlockLog("log").setCreativeTab(FleValue.tabFLE).setStepSound(Block.soundTypeWood);
		//leaf = new BlockLeaves("leaves").setCreativeTab(FleValue.tabFLE).setStepSound(Block.soundTypeGrass);
		ash = new BlockAsh().setCreativeTab(FleValue.tabFLE).setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/ash").setStepSound(Block.soundTypeSand);
		oilLamp = new BlockOilLamp("oilLamp").setCreativeTab(FleValue.tabFLE);
		workbench = new BlockWorkbench("workbench", "Workbench").setCreativeTab(FleValue.tabFLE);
		tank = new BlockMultiTank("fle_tank").setCreativeTab(FleValue.tabFLE).setBlockTextureName(FleValue.TEXTURE_FILE + ":iconsets/tank");
		woodMachine = new BlockWoodMachine("woodMachine").init().setCreativeTab(FleValue.tabFLE);
		woodMachine1 = new BlockWoodMachine1("woodMachine1").init().setCreativeTab(FleValue.tabFLE);
		stoneMachine = new BlockStoneMachine("stoneMachine").init().setCreativeTab(FleValue.tabFLE);
		stoneMachine1 = new BlockStoneMachine1("stoneMachine1").init().setCreativeTab(FleValue.tabFLE);
		argil_unsmelted = new BlockClayInventory("argil_unsmelted").init().setCreativeTab(FleValue.tabFLE);
		argil_smelted = new BlockClayInventory1("argil_smelted").init().setCreativeTab(FleValue.tabFLE);
		firewood = new BlockFirewood().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/firewood_oak").setCreativeTab(FleValue.tabFLE);
		charcoal = new BlockCharcoal().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/coal").setCreativeTab(FleValue.tabFLE);
		ropeLadder = new BlockRopeLadder("ropeLadder", "Rope Ladder").setBlockTextureName(FleValue.TEXTURE_FILE + ":tools/rope_ladder").setCreativeTab(FleValue.tabFLE);
		ditch = new BlockDitch().setCreativeTab(CreativeTabs.tabDecorations);
		peat = new BlockPeat("peat", "Peat").setBlockTextureName(FleValue.TEXTURE_FILE + ":dirt/peat").setCreativeTab(FleValue.tabFLE);
		plant = new BlockPlant();
		plant_rattan = new BlockPlantRattan("plant_rattan", "Rattan").setCreativeTab(FleValue.tabFLE);
		thermalWire = new BlockThermalWire("thermalWire", "Thermal Wire").setBlockTextureName(FleValue.TEXTURE_FILE + ":wire/wire").setCreativeTab(FleValue.tabFLE);
		placedItem = new BlockPlacedItem("placedItem");
		tool = new ItemTool("tool", "tool").init().setCreativeTab(FleValue.tabFLE);
		toolHead = new ItemToolHead("tool.head", "toolHead").init().setCreativeTab(FleValue.tabFLE);
		food = new ItemFleFood("food", "foods").init().setCreativeTab(FleValue.tabFLE);
		subItem = new ItemFleSub("sub", "subs").init().setCreativeTab(FleValue.tabFLE);
		cropSeed = new ItemFleSeed(crop, Blocks.farmland).init().setCreativeTab(FleValue.tabFLE);
		treeLog = new ItemTreeLog("tree.log", "log").init().setCreativeTab(FleValue.tabFLE);
	}
}