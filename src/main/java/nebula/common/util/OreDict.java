/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ueyudiud
 */
public final class OreDict
{
	private OreDict()
	{
	}
	
	public static void registerValid(String name, Block ore)
	{
		registerValid(name, new ItemStack(ore, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	public static void registerValid(String name, Item ore)
	{
		registerValid(name, new ItemStack(ore, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	public static void registerValid(String name, ItemStack ore)
	{
		if (ItemStacks.valid(ore) == null) return;
		ItemStack register = ore.copy();
		register.stackSize = 1;
		OreDictionary.registerOre(name, ore);
	}
	
	// public static void registerValid(String name, Item item,
	// IDataChecker<ItemStack> function, ItemStack...instances)
	// {
	// OreDictExt.registerOreFunction(name, item, function, instances);
	// }
	
	public static void registerValid(String name, ItemStack ore, boolean autoValid)
	{
		if (autoValid)
		{
			name = Strings.validateOre(false, name);
		}
		registerValid(name, ore);
	}
}
