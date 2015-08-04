package fle.api.block;

import net.minecraft.world.World;

public interface IWorldNBT
{
	int getMetadata(World world, int x, int y, int z);

	void setMetadata(World world, int x, int y, int z, int metadata);
}