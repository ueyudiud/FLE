package fle.api.block;

import java.util.List;

import net.minecraft.world.World;

public interface IDebugableBlock
{
	void addInfomationToList(World aWorld, int x, int y, int z, List aList);
}
