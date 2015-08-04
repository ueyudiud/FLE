package fle.core.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import fle.api.FleValue;
import fle.api.recipe.ItemOreStack;
import fle.core.block.BlockLeaves;
import fle.core.block.BlockLog;
import fle.core.block.BlockOre;
import fle.core.block.machine.BlockWoodMachine;
import fle.core.item.ItemDebug;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.item.ItemTreeLog;

public class IB 
{
	public static void reloadIB()
	{
		Blocks.torch.setLightLevel(0.5F);
		Item.getItemFromBlock(Blocks.torch).setMaxStackSize(16);
		Blocks.fire.setLightLevel(0.625F);
		for(Object obj : Block.blockRegistry.getKeys())
		{
			Block block = (Block) Block.blockRegistry.getObject(obj);
			if(new ItemOreStack("treeSapling").isStackEqul(new ItemStack(block)));
			{
				block.setHardness(1.0F);
			}
		}
	}

	public static Item debug;
	public static Item tool;
	public static Item toolHead;
	public static Item subItem;
	public static Item treeLog;
	public static Block ore;
	public static Block leaf;
	public static Block log;
	public static Block woodMachine;
	
	public static void init()
	{
		debug = new ItemDebug("debug").setCreativeTab(CreativeTabs.tabRedstone).setTextureName(FleValue.TEXTURE_FILE + ":fle");
		ore = new BlockOre().setCreativeTab(CreativeTabs.tabBlock);
		log = new BlockLog("log").setCreativeTab(CreativeTabs.tabBlock);
		leaf = new BlockLeaves("leaves").setCreativeTab(CreativeTabs.tabDecorations);
		woodMachine = new BlockWoodMachine("woodMachine").init().setCreativeTab(CreativeTabs.tabDecorations);
		tool = new ItemTool("tool", "tool").init().setCreativeTab(CreativeTabs.tabTools);
		toolHead = new ItemToolHead("tool.head", "toolHead").init().setCreativeTab(CreativeTabs.tabMaterials);
		subItem = new ItemFleSub("sub", "subs").init().setCreativeTab(CreativeTabs.tabMaterials);
		treeLog = new ItemTreeLog("tree.log", "log").init().setCreativeTab(CreativeTabs.tabMaterials);
	}
}