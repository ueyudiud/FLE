package fargen.core.world;

import farcore.data.EnumBlock;
import farcore.data.V;
import farcore.lib.world.CalendarHandler;
import farcore.lib.world.ICalendarWithMonth;
import farcore.lib.world.IWorldPropProvider;
import fargen.core.biome.BiomeBase;
import fargen.core.util.ClimaticZone;
import nebula.common.util.Maths;
import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * The surface properties wrapper.
 * @author ueyudiud
 * @see farcore.lib.world.WorldPropHandler
 */
public class WorldPropSurface implements IWorldPropProvider
{
	public static final ICalendarWithMonth calendar = CalendarHandler.OVERWORLD;
	private final NoiseBase offsetNoise = new NoisePerlin(0L, 8, 76, 1.2, 3.0);
	private final NoiseBase temperatureUndulateNoise = new NoisePerlin(0L, 3, 31.0, 1.6, 2.2);
	private final NoiseBase rainfallUndulateNoise = new NoisePerlin(0L, 3, 31.0, 1.4, 2.2);
	
	private void setData(World world)
	{
		long seed = world.getSeed();
		this.offsetNoise.setSeed(seed);
		this.temperatureUndulateNoise.setSeed(seed);
		this.rainfallUndulateNoise.setSeed(seed);
	}
	
	private float getTemperatureLocal(World world, BlockPos pos, int a, int b, float d)
	{
		Biome biome = world.getBiome(pos);
		if(biome instanceof BiomeBase)
		{
			ClimaticZone zone = ((BiomeBase) biome).zone;
			return Maths.lerp(zone.temperature[a], zone.temperature[b], d);
		}
		else
		{
			return biome.getFloatTemperature(pos);
		}
	}
	
	@Override
	public float getTemperature(World world, BlockPos pos)
	{
		setData(world);
		double offset = this.offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		PooledMutableBlockPos pos2 = PooledMutableBlockPos.retain();
		int c = 0;
		float tempTotal = 0F;
		for(int i = -3; i <= 3; ++i)
		{
			for(int j = -3; j <= 3; ++j)
			{
				pos2.setPos(x + i, y, z + j);
				float t = getTemperatureLocal(world, pos2, a, b, d);
				if(t >= 0)
				{
					tempTotal += t;
					++c;
				}
			}
		}
		float undulate = (float) this.temperatureUndulateNoise.noise(x, y, z) * 0.06F - 0.03F;
		return c == 0 ? 0 : tempTotal / c + undulate;
	}
	
	@Override
	public float getAverageTemperature(World world, BlockPos pos)
	{
		setData(world);
		Biome biome = world.getBiome(pos);
		if(biome instanceof BiomeBase)
		{
			ClimaticZone zone = ((BiomeBase) biome).zone;
			float undulate = (float) this.temperatureUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
			return zone.temperatureAverage + undulate;
		}
		else
			return biome.getTemperature();
	}
	
	@Override
	public float getSunshine(World world, BlockPos pos)
	{
		setData(world);
		double offset = this.offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone;
		Biome biome = world.getBiome(pos);
		if(biome instanceof BiomeBase)
		{
			zone = ((BiomeBase) biome).zone;
		}
		else
		{
			zone = ClimaticZone.temperate_plain;
		}
		return Maths.lerp(zone.sunshine[a], zone.sunshine[b], d) * V.sq2f;// * world.getSunBrightnessFactor(0F);
	}
	
	@Override
	public float getRainstrength(World world, BlockPos pos)
	{
		return getHumidity(world, pos) * V.sq2f * world.rainingStrength;
	}
	
	@Override
	public float getSkylight(World world)
	{
		return world.getSunBrightnessFactor(0F);
	}
	
	private float getHumidityLocal(World world, BlockPos pos, int a, int b, float d)
	{
		if(!world.isBlockLoaded(pos))
			return -1;
		Biome biome = world.getBiome(pos);
		if(biome instanceof BiomeBase)
		{
			ClimaticZone zone = ((BiomeBase) biome).zone;
			return Math.max(0, Maths.lerp(zone.rain[a], zone.rain[b], d));
		}
		else
			return biome.getRainfall();
	}
	
	@Override
	public float getHumidity(World world, BlockPos pos)
	{
		setData(world);
		double offset = this.offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		PooledMutableBlockPos pos2 = PooledMutableBlockPos.retain();
		int c = 0;
		float humTotal = 0F;
		for(int i = -3; i <= 3; ++i)
		{
			for(int j = -3; j <= 3; ++j)
			{
				pos2.setPos(x + i, y, z + j);
				float t = getHumidityLocal(world, pos2, a, b, d);
				if(t >= 0)
				{
					humTotal += t;
					++c;
				}
			}
		}
		float undulate = (float) this.rainfallUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
		return c == 0 ? 0 : humTotal / c + undulate;
	}
	
	@Override
	public float getAverageHumidity(World world, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		if(biome instanceof BiomeBase)
		{
			ClimaticZone zone = ((BiomeBase) biome).zone;
			float undulate = (float) this.rainfallUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
			return zone.rainAverage + undulate;
		}
		else
			return biome.getRainfall();
	}
	
	@Override
	public Block getMainFluidBlock()
	{
		return EnumBlock.water.block;
	}
	
	@Override
	public boolean canMainFluidBlockFreeze(World world, BlockPos pos)
	{
		return getTemperature(world, pos) < BiomeBase.minSnowTemperature;
	}
	
	@Override
	public void freezeMainFluidAt(World world, BlockPos pos)
	{
		world.setBlockState(pos, Blocks.ICE.getDefaultState(), 3);
	}
}