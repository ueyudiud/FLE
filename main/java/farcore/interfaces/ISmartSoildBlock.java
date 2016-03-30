package farcore.interfaces;

import farcore.enums.Direction;
import net.minecraft.world.IBlockAccess;

public interface ISmartSoildBlock
{
	boolean canSustainPlant(IBlockAccess world, int x, int y, int z, Direction direction, ISmartPlantableBlock block);
}