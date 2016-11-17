package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPropHandler
{
	private static final Map<Integer, IWorldPropProvider> WORLD_PROPERTIES = new HashMap();
	private static final IWorldPropProvider DEFAULT = new IWorldPropProvider()
	{
		@Override
		public float getTemperature(World world, BlockPos pos)
		{
			return world.getBiomeForCoordsBody(pos).getTemperature();
		}
		@Override
		public float getAverageTemperature(World world, BlockPos pos)
		{
			return getTemperature(world, pos);
		}
		@Override
		public float getSunshine(World world, BlockPos pos)
		{
			return 0.0F;
		}
		@Override
		public float getSkylight(World world)
		{
			return world.provider.getHasNoSky() ? 0F : 0.8F;
		}
		@Override
		public float getRainstrength(World world, BlockPos pos)
		{
			return U.Worlds.isCatchingRain(world, pos) ? world.getRainStrength(0F) : 0F;
		}
		@Override
		public float getHumidity(World world, BlockPos pos)
		{
			return world.getBiomeGenForCoords(pos).getRainfall();
		}
		@Override
		public float getAverageHumidity(World world, BlockPos pos)
		{
			return getHumidity(world, pos);
		}
		@Override
		public Block getMainFluidBlock()
		{
			return Blocks.WATER;
		}
		@Override
		public boolean canMainFluidBlockFreeze(World world, BlockPos pos)
		{
			return world.canBlockFreezeBody(pos, false);
		}
		@Override
		public void freezeMainFluidAt(World world, BlockPos pos)
		{
			world.setBlockState(pos, Blocks.ICE.getDefaultState(), 3);
		}
	};
	
	public static void addWorldProperty(int dimID, IWorldPropProvider provider)
	{
		WORLD_PROPERTIES.put(dimID, provider);
	}
	
	public static IWorldPropProvider getWorldProperty(World world)
	{
		return getWorldProperty(world.provider.getDimension());
	}
	
	public static IWorldPropProvider getWorldProperty(int dimID)
	{
		return WORLD_PROPERTIES.getOrDefault(dimID, DEFAULT);
	}
}