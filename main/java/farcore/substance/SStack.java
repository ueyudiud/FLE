package farcore.substance;

import farcore.util.Part;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class SStack
{
	Substance substance;
	public Part part;
	/**
	 * The substance size.
	 */
	public int size;

	private SStack(Substance substance)
	{
		this(substance, null);
	}
	public SStack(Substance substance, Part part)
	{
		this(substance, part, 1);
	}
	public SStack(Substance substance, Part part, int size)
	{
		this.substance = substance;
		this.part = part.getEquivalencePart();
		this.size = size;
	}
	
	public Substance getSubstance()
	{
		return substance;
	}
	
	public boolean contain(Substance substance)
	{
		return this.substance == substance;
	}
	
	public SStack splitStack(int size)
	{
		SStack stack = new SStack(substance, part, Math.min(this.size, size));
		this.size -= stack.size;
		return stack;
	}
	
	public ItemStack toIS()
	{
		if(substance == null || part == null) return null;
		if(size < part.resolution) return null;
		ItemStack stack = 
				SubstanceRegistry.getItemStack(substance, part);
		if(stack == null) return null;
		stack.stackSize = size / part.resolution;
		return stack;
	}
	
	public Object[] toISWithAmount()
	{
		if(substance == null || part == null) return null;
		if(size < part.resolution) return null;
		ItemStack stack = 
				SubstanceRegistry.getItemStack(substance, part);
		if(stack == null) return null;
		stack.stackSize = size / part.resolution;
		return new Object[]{stack, stack.stackSize * part.resolution};
	}
	
	public ItemStack splitItemStack(int max)
	{
		if(substance == null || part == null) return null;
		if(size < part.resolution) return null;
		ItemStack stack = 
				SubstanceRegistry.getItemStack(substance, part);
		if(stack == null) return null;
		stack.stackSize = Math.min(size / part.resolution, max);
		size -= stack.stackSize * part.resolution;
		return stack;
	}
	
	public static SStack readFromNBT(NBTTagCompound nbt)
	{
		Substance substance = Substance.getRegister().get(nbt.getString("substance"));
		Part part = Part.get(nbt.getString("part"));
		int size = nbt.getInteger("size");
		if(substance == null || part == null || size == 0) return null;
		return new SStack(substance, part, size);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("substance", substance.getName());
		nbt.setString("part", part.name);
		nbt.setInteger("size", size);
		return nbt;
	}
}