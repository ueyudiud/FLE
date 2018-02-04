/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks.terria;

import farcore.data.EnumOreAmount;
import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.lib.material.Mat;
import nebula.common.block.BlockBase;
import nebula.common.block.ItemBlockBase;
import nebula.common.util.NBTs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemOre extends ItemBlockBase
{
	public ItemOre(BlockBase block)
	{
		super(block);
	}
	
	public ItemStack createItemStack(int size, Mat ore, EnumOreAmount amount, Mat rock, EnumRockType type)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStack stack = new ItemStack(this, size, ore.id);
		stack.setTagCompound(setRock(setAmount(nbt, amount), rock, type));
		return stack;
	}
	
	public static Mat getOre(ItemStack stack)
	{
		return Mat.material(stack.getItemDamage());
	}
	
	public static EnumOreAmount getAmount(NBTTagCompound nbt)
	{
		return NBTs.getEnumOrDefault(nbt, "amount", EnumOreAmount.normal);
	}
	
	public static NBTTagCompound setAmount(NBTTagCompound nbt, EnumOreAmount amount)
	{
		NBTs.setEnum(nbt, "amount", amount);
		return nbt;
	}
	
	public static Mat getRockMaterial(NBTTagCompound nbt)
	{
		return Mat.material(nbt.getString("rock"), M.stone);
	}
	
	public static EnumRockType getRockType(NBTTagCompound nbt)
	{
		return NBTs.getEnumOrDefault(nbt, "type", EnumRockType.resource);
	}
	
	public static NBTTagCompound setRock(NBTTagCompound nbt, Mat material, EnumRockType type)
	{
		nbt.setString("rock", material.name);
		NBTs.setEnum(nbt, "type", type);
		return nbt;
	}
}
