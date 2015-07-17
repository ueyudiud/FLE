package fla.api.recipe;

import net.minecraft.nbt.NBTTagCompound;

public final class FuelStack<T extends Fuel>
{
	private T fuel;
	private int contain;
	
	public FuelStack(T fuel){this(fuel, 1000);}
	public FuelStack(T fuel, int contain)
	{
		this.fuel = fuel;
		this.contain = contain;
	}
	private FuelStack() 
	{
		
	}
	
	public T getFuel()
	{
		return fuel;
	}
	
	public int getContain()
	{
		return contain;
	}
	
	/**
	 * Drain, etc. To get stack size out.
	 * @param size
	 * @return if stack is empty.
	 */
	public boolean decrStack(int size)
	{
		contain -= size;
		if(contain <= 0) return true;
		return false;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("FuelType", fuel.getName());
		nbt.setShort("Contain", (short) contain);
	}
	
	private void readFromNBT(NBTTagCompound nbt)
	{
		Fuel f = Fuel.getFuel(nbt.getString("FuelType"));
		fuel = f == null ? null : (T) f;
		contain = nbt.getShort("Contain");
	}
	
	public static FuelStack getFuelStackFromNBT(NBTTagCompound nbt)
	{
		FuelStack fs = new FuelStack();
		fs.readFromNBT(nbt);
		return fs.fuel != null ? fs : null;
	}
	
	public FuelStack copy() 
	{
		return new FuelStack(fuel, contain);
	}
}
