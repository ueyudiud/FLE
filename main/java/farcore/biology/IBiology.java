package farcore.biology;

import net.minecraft.nbt.NBTTagCompound;

import farcore.world.IObjectInWorld;

/**
 * Biology instance. Provide a biology and useful method. May changed in the
 * future.
 * 
 * @author ueyudiud
 * 		
 */
public interface IBiology
{
	DNA getDNA();
	
	ISpecies getSpecie();
	
	int getDeformity();
	
	void update(IObjectInWorld object);
	
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
}