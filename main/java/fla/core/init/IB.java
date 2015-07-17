package fla.core.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.registry.GameRegistry;
import fla.api.util.FlaValue;
import fla.core.FlaBlocks;
import fla.core.FlaItems;
import fla.core.block.BlockDryingTable;
import fla.core.block.BlockFlaCrop;
import fla.core.block.BlockOilLamp;
import fla.core.block.BlockPolishTable;
import fla.core.block.cobble.BlockCobble1;
import fla.core.block.item.ItemSubBlock;
import fla.core.block.ore.BlockOre1;
import fla.core.item.ItemDebug;
import fla.core.item.ItemExtinguishTorch;
import fla.core.item.ItemFlaSeed;
import fla.core.item.ItemLog;
import fla.core.item.ItemSub;
import fla.core.item.tool.ItemAwl;
import fla.core.item.tool.ItemDrillingFiring;
import fla.core.item.tool.ItemFlaAxe;
import fla.core.item.tool.ItemFlaShovel;
import fla.core.item.tool.ItemOilLamp;
import fla.core.item.tool.ItemRoughAxe;
import fla.core.item.tool.ItemStoneHammer;
import fla.core.item.tool.ItemWhetstone;
import fla.core.item.tool.ItemWoodenHammer;

public class IB 
{
	public static void reloadIB()
	{
		Blocks.torch.setLightLevel(0.5F);
		Item.getItemFromBlock(Blocks.torch).setMaxStackSize(16);
		Blocks.fire.setLightLevel(0.625F);
	}
	
	public static void init()
	{
		FlaItems.debug = registerItem(new ItemDebug().setUnlocalizedName("debug").setCreativeTab(CreativeTabs.tabRedstone), "debug");
    	FlaItems.log = (ItemLog) registerItem(new ItemLog().setUnlocalizedName("logs").setCreativeTab(CreativeTabs.tabMaterials), "logs");
    	FlaItems.subs = (ItemSub) registerItem(new ItemSub().setUnlocalizedName("fla").setCreativeTab(CreativeTabs.tabMaterials), "subs");
    	FlaItems.wooden_hammer = registerItem(new ItemWoodenHammer().setUnlocalizedName("wooden_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/wooden/wood_hammer"), "wooden_hammer");
    	FlaItems.wooden_firestarter = registerItem(new ItemDrillingFiring(32, 0.3F).setUnlocalizedName("wooden_drilling_firing").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/wooden/wood_drilling_firing"), "wooden_firestarter");
    	FlaItems.rough_flint_axe = registerItem(new ItemRoughAxe().setUnlocalizedName("rough_flint_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/rough_flint_axe"), "rough_flint_axe");
    	FlaItems.flint_axe = registerItem(new ItemFlaAxe(FlaValue.flint_b).setUnlocalizedName("flint_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_axe"), "flint_axe");
    	FlaItems.flint_hammer = registerItem(new ItemStoneHammer(FlaValue.flint_a).setUnlocalizedName("flint_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_hammer"), "flint_hammer");
    	FlaItems.flint_shovel = registerItem(new ItemFlaShovel(FlaValue.flint_b).setUnlocalizedName("flint_shovel").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_shovel"), "flint_shovel");
    	FlaItems.flint_awl = registerItem(new ItemAwl(32).setUnlocalizedName("flint_awl").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_awl"), "flint_awl");
    	FlaItems.stone_axe = registerItem(new ItemFlaAxe(FlaValue.stone_b).setUnlocalizedName("stone_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_axe"), "stone_axe");
    	FlaItems.stone_hammer = registerItem(new ItemStoneHammer(FlaValue.stone_a).setUnlocalizedName("stone_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_hammer"), "stone_hammer");
    	FlaItems.stone_shovel = registerItem(new ItemFlaShovel(FlaValue.stone_b).setUnlocalizedName("stone_shovel").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_shovel"), "stone_shovel");
    	FlaItems.stone_oil_lamp = (ItemOilLamp) registerItem(new ItemOilLamp().setUnlocalizedName("oil_lamp").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/lamp"), "stone_oil_lamp");
    	FlaItems.whetstone = registerItem(new ItemWhetstone(32).setUnlocalizedName("whetstone").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/whetstone"), "whetstone");
    	FlaItems.extinguishTorch = registerItem(new ItemExtinguishTorch().setUnlocalizedName("extinguish_torch").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/1"), "extinguishTorch");
    	FlaBlocks.crops = registerBlock(new BlockFlaCrop(), "crops");
    	FlaItems.seeds = registerItem(new ItemFlaSeed(FlaBlocks.crops, Blocks.farmland).setUnlocalizedName("seeds"), "seed");
    	FlaBlocks.ore1 = registerBlock(new BlockOre1().setBlockName("ore_copper"), ItemSubBlock.class, "ore1");
    	FlaBlocks.cobble1 = registerBlock(new BlockCobble1().setBlockName("cobble1"), ItemSubBlock.class, "cobble1");
    	FlaBlocks.polishTable = registerBlock(new BlockPolishTable().setBlockName("polishTable").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":machine/polish"), "polishTable");
    	FlaBlocks.oilLamp = registerBlock(new BlockOilLamp().setBlockName("oilLamp"), "oil_lamp");
    	FlaBlocks.dryingTable = registerBlock(new BlockDryingTable().setBlockName("dryTable").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":machine/drying_table"), "drying_table");
	}

	private static Item registerItem(Item item, String name)
	{
		GameRegistry.registerItem(item, name);
		return item;
	}
	
	private static Block registerBlock(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		return block;
	}
	
	private static Block registerBlock(Block block, Class<? extends ItemBlock> clazz, String name)
	{
		GameRegistry.registerBlock(block, clazz, name);
		return block;
	}
}