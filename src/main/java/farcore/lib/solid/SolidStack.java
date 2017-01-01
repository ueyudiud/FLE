package farcore.lib.solid;

import farcore.util.ItemStacks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.RegistryDelegate;

public class SolidStack
{
	public int amount;
	public NBTTagCompound tag;
	private RegistryDelegate<SolidAbstract> solidDelegate;
	
	public SolidStack(SolidAbstract solid, int amount)
	{
		solidDelegate = solid.delegate;
		this.amount = amount;
	}
	public SolidStack(SolidAbstract solid, int amount, NBTTagCompound nbt)
	{
		this(solid, amount);
		if(nbt != null)
		{
			tag = nbt.copy();
		}
	}
	SolidStack(SolidStack stack)
	{
		solidDelegate = stack.solidDelegate;
		amount = stack.amount;
		if(stack.tag != null)
		{
			tag = stack.tag.copy();
		}
	}
	
	public final SolidAbstract getSolid()
	{
		return solidDelegate.get();
	}

	public SolidStack readFromNBT(NBTTagCompound nbt)
	{
		amount = nbt.getInteger("Amount");
		if(nbt.hasKey("Tag"))
		{
			tag = nbt.getCompoundTag("Tag");
		}
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("SolidName", solidDelegate.get().getRegistryName().toString());
		nbt.setInteger("Amount", amount);
		if(tag != null)
		{
			nbt.setTag("Tag", tag);
		}
		return nbt;
	}
	
	public boolean isStackEqual(SolidStack other)
	{
		return other == null ? false : other.getSolid() == getSolid() && ItemStacks.areTagEqual(tag, other.tag);
	}

	public static SolidStack copyOf(SolidStack stack)
	{
		return stack == null ? null : stack.copy();
	}

	public SolidStack copy()
	{
		return new SolidStack(this);
	}
}