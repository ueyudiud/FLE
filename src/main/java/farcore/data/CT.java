/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.blocks.BlockRedstoneCircuit;
import farcore.lib.item.ItemMulti;
import nebula.client.CreativeTabBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Creative tabs.
 * 
 * @author ueyudiud
 */
public class CT
{
	public static final CreativeTabs	TERRIA;
	public static final CreativeTabs	BUILDING;
	public static final CreativeTabs	RESOURCE_ITEM;
	public static final CreativeTabs	CROP_AND_WILD_PLANTS;
	public static final CreativeTabs	TOOL;
	public static final CreativeTabs	MACHINE;
	public static final CreativeTabs	MATERIAL;
	public static final CreativeTabs	TREE;
	public static final CreativeTabs	REDSTONE;
	
	static
	{
		CROP_AND_WILD_PLANTS = new CreativeTabBase("farcore.crop.plants", "Far Crop And Wild Plant", () -> new ItemStack(Items.WHEAT));
		TREE = new CreativeTabBase("farcore.tree", "Far Tree", () -> new ItemStack(M.oak.getProperty(MP.property_wood).block));
		TERRIA = new CreativeTabBase("farcore.terria", "Far Terria", () -> new ItemStack(M.peridotite.getProperty(MP.property_rock).block));
		BUILDING = new CreativeTabBase("farcore.building", "Far Building Blocks", () -> new ItemStack(M.marble.getProperty(MP.property_rock).block, 1, EnumRockType.brick.ordinal()));
		RESOURCE_ITEM = new CreativeTabBase("farcore.resource.item", "Far Resource Item", () -> ItemMulti.createStack(M.peridotite, MC.chip_rock));
		MACHINE = new CreativeTabBase("farcore.machine", "Far Machine", () -> new ItemStack(Blocks.CRAFTING_TABLE));
		MATERIAL = new CreativeTabBase("farcore.material", "Far Material", () -> ItemMulti.createStack(M.copper, MC.ingot));
		TOOL = new CreativeTabBase("farcore.tool", "Far Tool", () -> new ItemStack(EnumItem.debug.item));
		REDSTONE = new CreativeTabBase("farcore.redstone", "Far Redstone", () -> BlockRedstoneCircuit.createItemStack(1, M.stone));
	}
}
