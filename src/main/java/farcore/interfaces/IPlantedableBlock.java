package farcore.interfaces;

import farcore.enums.EnumBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.fluids.BlockFluidBase;

public interface IPlantedableBlock
{
	default int countWater(World world, int x, int y, int z, int rangeXZ, int rangeY, boolean checkSea)
	{
		int c = 0;
		int x1 = x + rangeXZ, y1 = y - rangeY, z1 = z + rangeXZ;
		for(int i = x - rangeXZ; i < x1; ++i)
			for(int k = z - rangeXZ; k < z1; ++k)
			{
				if(!checkSea && world.getBiomeGenForCoords(i, k).getTempCategory() == TempCategory.OCEAN)
					continue;
				for(int j = y - 1; j >= y1; --j)
				{
					Block block = world.getBlock(i, j, k);
					if(block == EnumBlock.water.block())
					{
						c += ((BlockFluidBase) block).getQuantaValue(world, i, j, k);
					}
				}
			}
		return c;
	}
	
	int getWaterLevel(World world, int x, int y, int z, boolean checkSea);
	
	int absorbWater(World world, int x, int y, int z, int maxOutput, boolean ableSaltyWater);
	
	int fillWater(World world, int x, int y, int z, int maxInput, boolean ableSaltyWater);
}