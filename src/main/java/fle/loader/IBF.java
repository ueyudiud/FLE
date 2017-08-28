package fle.loader;

import farcore.data.CT;
import farcore.data.M;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.material.Mat;
import fle.api.FLEAPI;
import fle.core.blocks.BlockDirtMixture;
import fle.core.blocks.BlockDitch;
import fle.core.blocks.BlockGear;
import fle.core.blocks.BlockPottery;
import fle.core.blocks.BlockWoodenMiscMachine;
import fle.core.blocks.BlockWoodenSimpleWorkbench;
import fle.core.blocks.container.BlockChest;
import fle.core.blocks.container.BlockRockyTank;
import fle.core.items.ItemMiscResources;
import fle.core.items.ItemSimpleFluidContainer;
import fle.core.items.ItemSubCropRelated;
import fle.core.items.ItemToolFar;
import fle.core.tile.kinetic.TEGear.GearSize;
import nebula.client.CreativeTabBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlocksItems
{
	public static ItemToolFar tool;
	public static ItemSimpleFluidContainer fluidContainer;
	public static ItemMiscResources miscResources;
	public static ItemSubCropRelated crop;
	public static Block ditch;
	public static Block gear;
	public static Block simple_wooden_workbench;
	public static Block misc_wooden_machine;
	public static Block pottery;
	public static Block dirt_mixture;
	public static Block rocky_tank;
	public static Block chest;
	
	public static void registerItemsAndBlocks()
	{
		FLEAPI.tabSimpleMachinery = new CreativeTabBase("fle.simple.machinery", "Simple Machinery", () -> {
			ItemStack stack = new ItemStack(gear, 1, GearSize.MID_16.ordinal());
			Mat.setMaterialToStack(stack, "material", M.oak);
			return stack;
		});
		crop = (ItemSubCropRelated) new ItemSubCropRelated().setCreativeTab(CT.CROP_AND_WILD_PLANTS);
		
		tool = (ItemToolFar) new ItemToolFar().setCreativeTab(CT.TOOL);
		fluidContainer = (ItemSimpleFluidContainer) new ItemSimpleFluidContainer().setCreativeTab(CT.TOOL);
		miscResources = (ItemMiscResources) new ItemMiscResources().setCreativeTab(CT.RESOURCE_ITEM);
		ditch = new BlockDitch();
		gear = new BlockGear();
		simple_wooden_workbench = new BlockWoodenSimpleWorkbench().setCreativeTab(CT.MACHINE);
		misc_wooden_machine = new BlockWoodenMiscMachine().setCreativeTab(CT.MACHINE);
		pottery = new BlockPottery().setCreativeTab(CT.MACHINE);
		dirt_mixture = new BlockDirtMixture().setCreativeTab(CT.MACHINE);
		rocky_tank = new BlockRockyTank().setCreativeTab(CT.MACHINE);
		chest = new BlockChest().setCreativeTab(CT.MACHINE);
	}
	
	public static void setBlocksItemsProperties()
	{
		ItemStoneChip.shootStoneChipExp = 0.7F;
	}
}