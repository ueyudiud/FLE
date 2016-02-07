package fle.core.init;

import static fle.init.Blocks.*;

import farcore.util.FleCreativeTab;
import flapi.util.Values;
import fle.core.block.BlockDebug;
import fle.core.item.ItemDebug;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class IBF
{
	public static void reloadIB()
	{
//		FluidRegistry.LAVA.setTemperature(950);
//		//Forced to initialize forge hook.
//		ForgeHooks.canToolHarvestBlock(Blocks.obsidian, 0, new ItemStack(Items.wooden_pickaxe));
//		Item.getItemFromBlock(Blocks.torch).setMaxStackSize(16);
//		Blocks.stone             .setHarvestLevel(EnumTool.pickaxe.name(), 6 );
//		Blocks.sandstone         .setHarvestLevel(EnumTool.pickaxe.name(), 9 );
//		Blocks.netherrack        .setHarvestLevel(EnumTool.pickaxe.name(), 10);
//		Blocks.end_stone         .setHarvestLevel(EnumTool.pickaxe.name(), 39);
//		Blocks.stonebrick        .setHarvestLevel(EnumTool.pickaxe.name(), 8 );
//		Blocks.nether_brick      .setHarvestLevel(EnumTool.pickaxe.name(), 13);
//		Blocks.stone_brick_stairs.setHarvestLevel(EnumTool.pickaxe.name(), 8 );
//		Blocks.stone_stairs      .setHarvestLevel(EnumTool.pickaxe.name(), 9 );
//		Blocks.obsidian          .setHarvestLevel(EnumTool.pickaxe.name(), 13);
//
//		Blocks.brown_mushroom.setLightLevel(0.0F);
//		Blocks.torch         .setLightLevel(0.5F);
//		Blocks.lit_furnace   .setLightLevel(0.5F);
//		Blocks.fire          .setLightLevel(0.625F);
//		Blocks.lit_pumpkin   .setLightLevel(0.625F);
//		
//		Blocks.obsidian.setResistance(20.0F);
//		
//		Blocks.obsidian        .setHardness(6.0F);
//		Blocks.stone           .setHardness(4.0F);
//		Blocks.netherrack      .setHardness(4.5F);
//		Blocks.end_stone       .setHardness(5.0F);
//		Blocks.enchanting_table.setHardness(6.0F);
//		Blocks.yellow_flower   .setHardness(0.5F);
//		Blocks.red_flower      .setHardness(0.5F);
//		OreStack treeSapling = new OreStack("treeSapling");
//		for(Object obj : Block.blockRegistry.getKeys())
//		{
//			Block block = (Block) Block.blockRegistry.getObject(obj);
//			if(treeSapling.equal(new ItemStack(block)));
//			{
//				block.setHardness(1.0F);
//			}
//		}
//		Items.arrow.setMaxStackSize(16);
	}

	public static Item debugI;
	public static Block debugB;
	
	public static void init()
	{
		Values.tabFLE = new FleCreativeTab("fle", "FLE Main")
		{
			@Override
			public Item getTabIconItem()
			{
				return IBF.debugI;
			}		
		};
		
		debugI = new ItemDebug().setMaxStackSize(1).setCreativeTab(Values.tabFLE).setTextureName(Values.TEXTURE_FILE + ":fle");
		debugB = new BlockDebug().setCreativeTab(Values.tabFLE);
	}
}