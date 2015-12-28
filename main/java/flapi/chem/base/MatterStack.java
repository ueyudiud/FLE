package flapi.chem.base;

import net.minecraft.nbt.NBTTagCompound;

public class MatterStack
{
	Matter matter;
	Part part;
	public int size;
	
	public MatterStack(Matter matter)
	{
		this(matter, null);
	}
	
	public MatterStack(Matter matter, Part part)
	{
		this(matter, part, 1);
	}
	
	public MatterStack(Matter matter, Part part, int size)
	{
		this.matter = matter;
		this.part = part;
		this.size = size;
	}
	
	public Matter getMatter()
	{
		return matter;
	}
	
	public int getContain()
	{
		return part.resolution * size;
	}
	
	public Part getPart()
	{
		return part;
	}
	
	public MatterStack copy()
	{
		return new MatterStack(matter, part, size);
	}
	
	public static MatterStack readFromNBT(NBTTagCompound nbt)
	{
		Matter m = Matter.getMatterFromName(nbt.getString("Matter"));
		if(m != null)
		{
			Part p = Part.get(nbt.getString("Type"));
			int size = nbt.getInteger("Size");
			return new MatterStack(m, p, size);
		}
		else return null;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Matter", matter.getSaveName());
		nbt.setString("Type", part.name);
		nbt.setInteger("Size", size);
		return nbt;
	}
	
	@Override
	public int hashCode()
	{
		int hash = matter.getSaveName().hashCode();
		hash *= 31;
		hash += part.name.hashCode();
		hash *= 31;
		hash += size;
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof MatterStack ? 
				((MatterStack) obj).matter.equals(matter) && ((MatterStack) obj).part.name.equals(part.name) && 
				((MatterStack) obj).size == size : false;
	}
	
	@Override
	public String toString()
	{
		return "[" + matter.toString() + "]" + part.name + "x" + size;
	}
}