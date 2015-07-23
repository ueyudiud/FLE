package fla.core.init;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import fla.api.recipe.IItemChecker.OreChecker;
import fla.api.util.FlaValue;
import fla.core.FlaBlocks;
import fla.core.FlaCreativeTab;
import fla.core.FlaItems;
import fla.core.block.BlockArgil;
import fla.core.block.BlockArgilUnsmelted;
import fla.core.block.BlockAsh;
import fla.core.block.BlockCharcoal;
import fla.core.block.BlockDryingTable;
import fla.core.block.BlockFirewood;
import fla.core.block.BlockFlaCrop;
import fla.core.block.BlockOilLamp;
import fla.core.block.BlockPolishTable;
import fla.core.block.cobble.BlockCobble1;
import fla.core.block.item.ItemSubBlock;
import fla.core.block.ore.BlockOre1;
import fla.core.block.rock.BlockLimestone;
import fla.core.item.ItemArgil;
import fla.core.item.ItemDebug;
import fla.core.item.ItemExtinguishTorch;
import fla.core.item.ItemFlaSeed;
import fla.core.item.ItemLog;
import fla.core.item.ItemOreChip;
import fla.core.item.ItemSub;
import fla.core.item.tool.ItemAwl;
import fla.core.item.tool.ItemDrillingFiring;
import fla.core.item.tool.ItemFlaAxe;
import fla.core.item.tool.ItemFlaShovel;
import fla.core.item.tool.ItemOilLamp;
import fla.core.item.tool.ItemRoughAxe;
import fla.core.item.tool.ItemSpadeHoe;
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
		for(Object obj : Block.blockRegistry.getKeys())
		{
			Block block = (Block) Block.blockRegistry.getObject(obj);
			if(new OreChecker("treeSapling").match(new ItemStack(block)));
			{
				block.setHardness(1.0F);
			}
		}
	}
	
	public static void init()
	{
		FlaItems.debug = registerItem(new ItemDebug().setUnlocalizedName("debug").setCreativeTab(FlaCreativeTab.fla_other_tab), "debug");
    	FlaItems.log = (ItemLog) registerItem(new ItemLog().setUnlocalizedName("logs").setCreativeTab(FlaCreativeTab.fla_other_tab), "logs");
    	FlaItems.subs = (ItemSub) registerItem(new ItemSub().setUnlocalizedName("fla"), "subs");
    	FlaItems.ore_chip = registerItem(new ItemOreChip().setUnlocalizedName("oreChip").setTextureName(FlaValue.TEXT_FILE_NAME + ":iconsets/chip"), "oreChip");
    	FlaItems.wooden_hammer = registerItem(new ItemWoodenHammer().setUnlocalizedName("wooden_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/wooden/wood_hammer").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "wooden_hammer");
    	FlaItems.wooden_firestarter = registerItem(new ItemDrillingFiring(32, 0.3F).setUnlocalizedName("wooden_drilling_firing").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/wooden/wood_drilling_firing").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "wooden_firestarter");
    	FlaItems.rough_flint_axe = registerItem(new ItemRoughAxe().setUnlocalizedName("rough_flint_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/rough_flint_axe").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "rough_flint_axe");
    	FlaItems.flint_axe = registerItem(new ItemFlaAxe(FlaValue.flint_b).setUnlocalizedName("flint_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_axe").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "flint_axe");
    	FlaItems.flint_hammer = registerItem(new ItemStoneHammer(FlaValue.flint_a).setUnlocalizedName("flint_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_hammer").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "flint_hammer");
    	FlaItems.flint_shovel = registerItem(new ItemFlaShovel(FlaValue.flint_b).setUnlocalizedName("flint_shovel").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_shovel").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "flint_shovel");
    	FlaItems.flint_awl = registerItem(new ItemAwl(32).setUnlocalizedName("flint_awl").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/flint/flint_awl").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "flint_awl");
    	FlaItems.stone_axe = registerItem(new ItemFlaAxe(FlaValue.stone_b).setUnlocalizedName("stone_axe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_axe").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "stone_axe");
    	FlaItems.stone_hammer = registerItem(new ItemStoneHammer(FlaValue.stone_a).setUnlocalizedName("stone_hammer").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_hammer").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "stone_hammer");
    	FlaItems.stone_shovel = registerItem(new ItemFlaShovel(FlaValue.stone_b).setUnlocalizedName("stone_shovel").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_shovel").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "stone_shovel");
    	FlaItems.stone_spade_hoe = registerItem(new ItemSpadeHoe(FlaValue.stone_b).setUnlocalizedName("stone_spade_hoe").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/stone_spade_hoe"), "stone_spade_hoe");
    	FlaItems.stone_oil_lamp = (ItemOilLamp) registerItem(new ItemOilLamp().setUnlocalizedName("oil_lamp").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/lamp").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "stone_oil_lamp");
    	FlaItems.whetstone = registerItem(new ItemWhetstone(32).setUnlocalizedName("whetstone").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/stone/whetstone").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "whetstone");
    	FlaItems.extinguishTorch = registerItem(new ItemExtinguishTorch().setUnlocalizedName("extinguish_torch").setTextureName(FlaValue.TEXT_FILE_NAME + ":tools/1").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "extinguishTorch");
    	FlaBlocks.crops = registerBlock(new BlockFlaCrop().setBlockName("fle.crop"), "crops");
    	FlaItems.seeds = registerItem(new ItemFlaSeed(FlaBlocks.crops, Blocks.farmland).setUnlocalizedName("seeds"), "seed");
    	FlaBlocks.rock1 = registerBlock(new BlockLimestone().setBlockName("limestone").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":rock/limestone"), "rock1");
    	FlaBlocks.ore1 = registerBlock(new BlockOre1().setBlockName("ore_copper"), ItemSubBlock.class, "ore1");
    	FlaBlocks.cobble1 = registerBlock(new BlockCobble1().setBlockName("cobble1"), ItemSubBlock.class, "cobble1");
    	FlaBlocks.polishTable = registerBlock(new BlockPolishTable().setBlockName("polishTable").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":machine/polish").setCreativeTab(FlaCreativeTab.fla_old_stone_age_tab), "polishTable");
    	FlaBlocks.oilLamp = registerBlock(new BlockOilLamp().setBlockName("oilLamp"), "oil_lamp");
    	FlaBlocks.dryingTable = registerBlock(new BlockDryingTable().setBlockName("dryTable").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":machine/drying_table").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "drying_table");
    	FlaBlocks.firewood = registerBlock(new BlockFirewood().setBlockName("firewood").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":wood/firewood/firewood_oak").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "firewood");
    	FlaBlocks.charcoal = registerBlock(new BlockCharcoal().setBlockName("charcoal").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":wood/firewood/coal").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), "charcoal");
    	FlaBlocks.plantAsh = registerBlock(new BlockAsh().setBlockName("ash_plant").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":wood/firewood/ash").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab), ItemSubBlock.class, "plantAsh");
    	FlaBlocks.argil_unsmelted = registerBlock(new BlockArgilUnsmelted().setBlockName("argil_unsmelted").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":iconsets/argil"), ItemSubBlock.class, "argil_unsmelted");
    	FlaBlocks.argil_smelted = (BlockArgil) registerBlock(new BlockArgil().setBlockName("argil_smelted").setBlockTextureName(FlaValue.TEXT_FILE_NAME + ":iconsets/argil_smelted"), ItemSubBlock.class, "argil_smelted");
    	FlaItems.argil_unsmelted = registerItem(new ItemArgil(FlaBlocks.argil_unsmelted).setUnlocalizedName("argil_un"), "item_argil_unsmelted");
    	FlaItems.argil_smelted = registerItem(new ItemArgil(FlaBlocks.argil_smelted).setUnlocalizedName("argil"), "item_argil_smelted");
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