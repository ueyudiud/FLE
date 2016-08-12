package farcore.lib.block.instance;

import farcore.data.EnumOreAmount;
import farcore.data.M;
import farcore.lib.block.BlockBase;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemOre extends ItemBlockBase
{
	public ItemOre(BlockBase block)
	{
		super(block);
	}
	
	public ItemStack createItemStack(int size, Mat ore, EnumOreAmount amount, Mat rock, RockType type)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStack stack = new ItemStack(this, size, ore.id);
		stack.setTagCompound(setRock(setAmount(nbt, amount), rock, type));
		return stack;
	}
	
	public static EnumOreAmount getAmount(NBTTagCompound nbt)
	{
		return EnumOreAmount.values()[nbt.getByte("amount")];
	}

	public static NBTTagCompound setAmount(NBTTagCompound nbt, EnumOreAmount amount)
	{
		nbt.setByte("amount", (byte) amount.ordinal());
		return nbt;
	}
	
	public static Mat getRockMaterial(NBTTagCompound nbt)
	{
		return Mat.register.get(nbt.getString("rock"), M.VOID);
	}

	public static RockType getRockType(NBTTagCompound nbt)
	{
		return RockType.values()[nbt.getByte("type")];
	}

	public static NBTTagCompound setRock(NBTTagCompound nbt, Mat material, RockType type)
	{
		nbt.setString("rock", material.name);
		nbt.setByte("type", (byte) type.ordinal());
		return nbt;
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		return super.getDamage(stack);
	}

	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		super.setDamage(stack, damage);
	}
}