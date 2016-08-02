package fargen.core.world;

import java.util.HashMap;
import java.util.Map;

import fargen.core.util.IWorldPropProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPropHandler
{
	private static final Map<Integer, IWorldPropProvider> worldProperties = new HashMap();
	private static final IWorldPropProvider DEFAULT = new IWorldPropProvider()
	{
		@Override
		public float getTemperature(World world, BlockPos pos){return 0.0F;}
		@Override
		public float getSunshine(World world, BlockPos pos){return 0.0F;}
		@Override
		public float getSkylight(World world){return 0.0F;}
		@Override
		public float getRainstrength(World world, BlockPos pos){return 0.0F;}
		@Override
		public float getHumidity(World world, BlockPos pos){return 0.0F;}
	};

	public static void addWorldProperty(int dimID, IWorldPropProvider provider)
	{
		worldProperties.put(dimID, provider);
	}

	public static IWorldPropProvider getWorldProperty(World world)
	{
		return getWorldProperty(world.provider.getDimension());
	}

	public static IWorldPropProvider getWorldProperty(int dimID)
	{
		return worldProperties.getOrDefault(dimID, DEFAULT);
	}
}
