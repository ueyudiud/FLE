package flapi.solid;

import net.minecraft.nbt.NBTTagCompound;
import flapi.collection.abs.Stack;

public final class SolidStack extends Stack<Solid>
{
	public NBTTagCompound nbt;
	
	public SolidStack(Solid solid)
	{
		super(solid);
	}
	public SolidStack(Solid solid, int amount)
	{
		super(solid, amount);
	}
	public SolidStack(Solid solid, int amount, NBTTagCompound aNBT)
	{
		this(solid, amount);
		nbt = aNBT;
	}
	
	public NBTTagCompound getTagCompound()
	{
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		return nbt;
	}
	
	public static SolidStack readFromNBT(NBTTagCompound aNBT)
	{
		String solidName = aNBT.getString("Solid");
		int amount = aNBT.getInteger("Amount");
		if(SolidRegistry.register.contain(solidName) && amount > 0)
		{
			Solid solid = SolidRegistry.getSolidFromName(solidName);
			SolidStack ret = new SolidStack(solid, amount);
			if(aNBT.hasKey("NBTData"))
			{
				ret.nbt = aNBT.getCompoundTag("NBTData");
			}
			return ret;
		}
		return null;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound aNBT)
	{
		aNBT.setString("Solid", get().getSolidName());
		aNBT.setInteger("Amount", size());
		if(nbt != null)
		{
			aNBT.setTag("NBTData", nbt);
		}
		return aNBT;
	}

	public boolean isStackEqul(SolidStack aStack)
	{
		return getSolidName().equals(aStack.getSolidName());
	}
	
	public boolean isStackTagEqul(SolidStack aStack)
	{
		return aStack.nbt == null && nbt == null ? true : aStack.nbt == null ? nbt.hasNoTags() : nbt == null ? aStack.nbt.hasNoTags() : aStack.nbt.equals(nbt);
	}
	
	private String getSolidName()
	{
		return get().getSolidName();
	}

	@Override
	public SolidStack copy()
	{
		return new SolidStack(get(), size(), nbt);
	}
	
	public static boolean areStackEquls(SolidStack s1, SolidStack s2)
	{
		if(s1 == null || s2 == null)
		{
			return s1 == null && s2 == null;
		}
		return s1.isStackEqul(s2) && s1.isStackTagEqul(s2);
	}
	
	public boolean contain(SolidStack solid)
	{
		return isStackEqul(solid) ? solid.size() <= size() : false;
	}
	
	public boolean contain(Solid solid)
	{
		return contain(new SolidStack(solid));
	}
	
	public int getSolidID()
	{
		return obj == null ? -1 : SolidRegistry.getSolidID(obj);
	}
	
	public Solid getSolid()
	{
		return obj;
	}
	
	public int getSize()
	{
		return size;
	}
}