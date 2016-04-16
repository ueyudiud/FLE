package farcore.interfaces;

import java.util.List;

import net.minecraft.world.World;

public interface IDebugableBlock
{
	void addInformation(World world, int x, int y, int z, List<String> list);
}