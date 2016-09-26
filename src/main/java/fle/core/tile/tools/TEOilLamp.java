package fle.core.tile.tools;

import farcore.lib.material.Mat;
import farcore.lib.tile.TESynchronization;
import net.minecraft.nbt.NBTTagCompound;

public class TEOilLamp extends TESynchronization
{
	public Mat material;

	public TEOilLamp()
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = Mat.material(nbt.getString("material"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
	}
}