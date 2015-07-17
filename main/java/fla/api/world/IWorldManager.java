package fla.api.world;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IWorldManager 
{
	public int getBlockMeta(BlockPos pos);
	
	public void setBlockMeta(BlockPos pos, int meta);
}
