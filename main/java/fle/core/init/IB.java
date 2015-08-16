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
import fle.core.block.BlockFirewood;
import fle.core.block.BlockFleCrop;
import fle.core.block.BlockLeaves;
import fle.core.block.BlockLog;
import fle.core.block.BlockOilLamp;
import fle.core.block.BlockOre;
import fle.core.block.BlockRock;
import fle.core.block.machine.BlockClayInventory;
import fle.core.block.machine.BlockClayInventory1;
import fle.core.block.machine.BlockWoodMachine;
import fle.core.block.machine.BlockWoodMachine1;
import fle.core.item.ItemDebug;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
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
		Blocks.obsidian.setHarvestLevel(EnumTool.abstract_hammer.name(), 0);
	}

	public static Item debug;
	public static Item tool;
	public static Item toolHead;
	public static Item subItem;
	public static Item treeLog;
	public static Item cropSeed;
	public static Item food;
	public static Block crop;
	public static Block rock;
	public static Block ore;
	public static Block leaf;
	public static Block log;
	public static Block woodMachine;
	public static Block woodMachine1;
	public static Block oilLamp;
	public static Block firewood;
	public static Block charcoal;
	public static Block ash;
	public static Block argil_unsmelted;
	public static Block argil_smelted;
	public static Fluid animalOil;
	
	public static void init()
	{
		animalOil = new FluidBase("oil_a", new PropertyInfo(0xFFFFFF, 313, 773, 293, 3, 1500, 0.2F, 0.2F, 0.7F, 0.0F, 100, 0.0F, 1.8F)).setTextureName(FleValue.TEXTURE_FILE + ":fluids/oil");
		debug = new ItemDebug("debug").setCreativeTab(CreativeTabs.tabRedstone).setTextureName(FleValue.TEXTURE_FILE + ":fle");
		crop = new BlockFleCrop();
		rock = new BlockRock().setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeStone);
		ore = new BlockOre().setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeStone);
		log = new BlockLog("log").setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeWood);
		leaf = new BlockLeaves("leaves").setCreativeTab(CreativeTabs.tabDecorations).setStepSound(Block.soundTypeGrass);
		ash = new BlockAsh().setCreativeTab(CreativeTabs.tabDecorations).setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/ash").setStepSound(Block.soundTypeSand);
		oilLamp = new BlockOilLamp("oilLamp").setCreativeTab(CreativeTabs.tabDecorations);
		woodMachine = new BlockWoodMachine("woodMachine").init().setCreativeTab(CreativeTabs.tabDecorations);
		woodMachine1 = new BlockWoodMachine1("woodMachine1").init().setCreativeTab(CreativeTabs.tabDecorations);
		argil_unsmelted = new BlockClayInventory("argil_unsmelted").init().setCreativeTab(CreativeTabs.tabDecorations);
		argil_smelted = new BlockClayInventory1("argil_smelted").init().setCreativeTab(CreativeTabs.tabDecorations);
		firewood = new BlockFirewood().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/firewood_oak").setCreativeTab(CreativeTabs.tabBlock);
		charcoal = new BlockCharcoal().setBlockTextureName(FleValue.TEXTURE_FILE + ":wood/firewood/coal").setCreativeTab(CreativeTabs.tabBlock);
		tool = new ItemTool("tool", "tool").init().setCreativeTab(CreativeTabs.tabTools);
		toolHead = new ItemToolHead("tool.head", "toolHead").init().setCreativeTab(CreativeTabs.tabMaterials);
		food = new ItemFleFood("food", "foods").init().setCreativeTab(CreativeTabs.tabFood);
		subItem = new ItemFleSub("sub", "subs").init().setCreativeTab(CreativeTabs.tabMaterials);
		cropSeed = new ItemFleSeed(crop, Blocks.farmland).init().setCreativeTab(CreativeTabs.tabMaterials);
		treeLog = new ItemTreeLog("tree.log", "log").init().setCreativeTab(CreativeTabs.tabMaterials);
	}
}