package farcore.lib.matter;

import farcore.util.Part;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MatterStack
{
	public IMatter matter;
	public Part part;
	public long size;
	private NBTTagCompound nbt;

	public MatterStack(IMatter matter, long size)
	{
		this(matter, size, null);
	}
	public MatterStack(IMatter matter, long size, NBTTagCompound nbt)
	{
		this.matter = matter;
		this.size = size;
		this.nbt = nbt;
	}

	public NBTTagCompound getStackNBT()
	{
		return nbt;
	}
	public NBTTagCompound getStackNBT(boolean create)
	{
		if(create && nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		return nbt;
	}
	
	public ItemStack example()
	{
		return null;
	}
}