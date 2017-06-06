package fargen.core.biome.layer.surface;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.biome.BiomeBase;
import fargen.core.biome.layer.BiomeLayerGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class BLGSurfaceBase extends BiomeLayerGenerator
{
	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
	protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
	protected static final IBlockState ICE = EnumBlock.ice.block.getDefaultState();
	
	protected abstract int generateLayer(World world, Random rand, ChunkPrimer primer, int x, int y, int z, int submeta, double length, boolean underWater);

	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal, BiomeBase biome, int submeta)
	{
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(world);
		float temperature = properties.getTemperature(world, pos.setPos(x, world.getSeaLevel(), z));
		int i = world.getSeaLevel();
		double k = noiseVal * 4.0 + 5.0 + rand.nextDouble() * 0.4 - rand.nextDouble() * 0.4;
		byte lay = 0;
		int x0 = x & 15;
		int z0 = z & 15;
		
		primer.setBlockState(x0, 0, z0, BEDROCK);
		for (int y = 255; y >= 1; --y)
		{
			if (y <= 8)
			{
				primer.setBlockState(x0, y, z0, LAVA);
			}
			else if(y <= 16)
			{
				primer.setBlockState(x0, y, z0, AIR);
			}
			else
			{
				IBlockState state = primer.getBlockState(x0, y, z0);
				
				if (state.getMaterial() == Material.AIR)
				{
					lay &= ~0x2;
				}
				else if (isRock(state.getBlock()) || state.getBlock() == Blocks.STONE)
				{
					if((lay & 0x2) == 0)
					{
						if(k < 0)
						{
							continue;
						}
						else
						{
							y -= generateLayer(world, rand, primer, x0, y, z0, submeta, k, (lay & 0x1) != 0);
							lay |= 0x2;
						}
					}
				}
				else if(state.getMaterial() == Material.WATER)
				{
					if((lay & 0x1) == 0)
					{
						if(temperature < BiomeBase.minSnowTemperature)
						{
							primer.setBlockState(x0, y, z0, ICE);
						}
						lay |= 0x1;
					}
				}
			}
		}
	}
}