package fle.core.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import fle.api.FleValue;
import fle.api.enums.EnumTool;
import fle.api.fluid.FluidBase;
import fle.api.material.PropertyInfo;
import fle.api.recipe.ItemOreStack;
import fle.core.block.BlockAsh;
import fle.core.block.BlockCharcoal;
import fle.core.block.BlockDitch;
import fle.core.block.BlockFirewood;
import fle.core.block.BlockFleCrop;
import fle.core.block.BlockLeaves;
import fle.core.block.BlockLog;
import fle.core.block.BlockOilLamp;
import fle.core.block.BlockOre;
import fle.core.block.BlockOreCobble;
import fle.core.block.BlockRock;
import fle.core.block.BlockWorkbench;
import fle.core.block.machine.BlockClayInventory;
import fle.core.block.machine.BlockClayInventory1;
import fle.core.block.machine.BlockStoneMachine;
import fle.core.block.machine.BlockStoneMachine1;
import fle.core.block.machine.BlockWoodMachine;
import fle.core.block.machine.BlockWoodMachine1;
import fle.core.item.ItemDebug;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemOre;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.item.ItemTreeLog;

public class IB 
{
	public static void reloadIB()
	{
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
		for(Object obj : Block.blockRegistry.getKeys())
		{
			Block block = (Block) Block.blockRegistry.getObject(obj);
			if(new ItemOreStack("treeSapling").isStackEqul(new ItemStack(block)));
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
	public static Block rock;
	public static Block ore;
	public static Block leaf;
	public static Block log;
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
	public static Fluid animalOil;
	public static Fluid plant_ash_mortar;
	public static Fluid lime_mortar;
	public static Fluid copper;
	public static Fluid cu_as_0;
	
	public static void init()
	{
		animalOil = new FluidBase("oil_a", new PropertyInfo(0xFFFFFF, 313, 773, 293, 1831, 1500, 1.0F, -1F, 1.0F, 0.7F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/oil");
		copper = new FluidBase("copper", Materials.Copper.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		plant_ash_mortar = new FluidBase("plant_ash_mortar", new PropertyInfo(0xFFFFFF, 264, 360, 212, 1842, 1800, 1.0F, 3.2F, 0.7F, 1.7F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/plant_ash_mortar");
		lime_mortar = new FluidBase("lime_mortar", new PropertyInfo(0xFFFFFF, 264, 360, 212, 1842, 2200, 1.0F, 1.9F, 0.67F, 1.8F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/lime_mortar");
		cu_as_0 = new FluidBase("cu_as_0", Materials.CuAs.getPropertyInfo()).setTextureName(FleValue.TEXTURE_FILE + ":fluids/metal_a");
		debug = new ItemDebug("debug").setCreativeTab(CreativeTabs.tabRedstone).setTextureName(FleValue.TEXTURE_FILE + ":fle");
		crop = new BlockFleCrop();
		rock = new BlockRock().setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeStone);
		ore = new BlockOre().setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeStone);
		ore_cobble = new BlockOreCobble("ore.cobble").init().setCreativeTab(CreativeTabs.tabBlock);
		oreChip = new ItemOre("ore.chip", "ore").init().setCreativeTab(CreativeTabs.tabMaterials);
		log = new BlockLog("log").setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeWood);
		leaf = new BlockLeaves("leaves").setCreativeTab(CreativeTabs.tabDecorations).setStepSound(Block.soundTypeGrass);
		ash = new BlockAsh().setCreativeTab(CreativeTabs.tabDecorations).setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/ash").setStepSound(Block.soundTypeSand);
		oilLamp = new BlockOilLamp("oilLamp").setCreativeTab(CreativeTabs.tabDecorations);
		workbench = new BlockWorkbench("workbench", "Workbench").setCreativeTab(CreativeTabs.tabDecorations);
		woodMachine = new BlockWoodMachine("woodMachine").init().setCreativeTab(CreativeTabs.tabDecorations);
		woodMachine1 = new BlockWoodMachine1("woodMachine1").init().setCreativeTab(CreativeTabs.tabDecorations);
		stoneMachine = new BlockStoneMachine("stoneMachine").init().setCreativeTab(CreativeTabs.tabDecorations);
		stoneMachine1 = new BlockStoneMachine1("stoneMachine1").init().setCreativeTab(CreativeTabs.tabDecorations);
		argil_unsmelted = new BlockClayInventory("argil_unsmelted").init().setCreativeTab(CreativeTabs.tabDecorations);
		argil_smelted = new BlockClayInventory1("argil_smelted").init().setCreativeTab(CreativeTabs.tabDecorations);
		firewood = new BlockFirewood().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/firewood_oak").setCreativeTab(CreativeTabs.tabBlock);
		charcoal = new BlockCharcoal().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/coal").setCreativeTab(CreativeTabs.tabBlock);
		ditch = new BlockDitch().setCreativeTab(CreativeTabs.tabDecorations);
		tool = new ItemTool("tool", "tool").init().setCreativeTab(CreativeTabs.tabTools);
		toolHead = new ItemToolHead("tool.head", "toolHead").init().setCreativeTab(CreativeTabs.tabMaterials);
		food = new ItemFleFood("food", "foods").init().setCreativeTab(CreativeTabs.tabFood);
		subItem = new ItemFleSub("sub", "subs").init().setCreativeTab(CreativeTabs.tabMaterials);
		cropSeed = new ItemFleSeed(crop, Blocks.farmland).init().setCreativeTab(CreativeTabs.tabMaterials);
		treeLog = new ItemTreeLog("tree.log", "log").init().setCreativeTab(CreativeTabs.tabMaterials);
	}
}