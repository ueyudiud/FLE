package fle.loader;

import farcore.data.CT;
import farcore.data.M;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.material.Mat;
import fle.api.FLEAPI;
import fle.core.blocks.BlockDitch;
import fle.core.blocks.BlockGear;
import fle.core.blocks.BlockWoodenSimpleWorkbench;
import fle.core.items.ItemMiscResources;
import fle.core.items.ItemSimpleFluidContainer;
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
	public static Block ditch;
	public static Block gear;
	public static Block simple_wooden_workbench;
	
	public static void registerItemsAndBlocks()
	{
		FLEAPI.tabSimpleMachinery = new CreativeTabBase("fle.simple.machinery", "Simple Machinery", () -> {
			ItemStack stack = new ItemStack(gear, 1, GearSize.MID_16.ordinal());
			Mat.setMaterialToStack(stack, "material", M.oak);
			return stack;
		});
		
		tool = (ItemToolFar) new ItemToolFar().setCreativeTab(CT.tabTool);
		fluidContainer = (ItemSimpleFluidContainer) new ItemSimpleFluidContainer().setCreativeTab(CT.tabTool);
		miscResources = new ItemMiscResources();
		ditch = new BlockDitch();
		gear = new BlockGear();
		simple_wooden_workbench = new BlockWoodenSimpleWorkbench().setCreativeTab(CT.tabMachine);
	}
	
	public static void setBlocksItemsProperties()
	{
		ItemStoneChip.shootStoneChipExp = 0.7F;
	}
}