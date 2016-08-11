package fargen.core.world;

import farcore.data.EnumBlock;
import farcore.lib.util.NoiseBase;
import farcore.lib.util.NoisePerlin;
import farcore.lib.world.CalendarHandler;
import farcore.lib.world.ICalendarWithMonth;
import farcore.lib.world.IWorldPropProvider;
import farcore.util.U.Maths;
import fargen.core.biome.BiomeBase;
import fargen.core.util.ClimaticZone;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPropSurface implements IWorldPropProvider
{
	public static final ICalendarWithMonth calendar = CalendarHandler.overworld;
	private final NoiseBase offsetNoise = new NoisePerlin(0L, 8, 76, 1.2, 3.0);
	private final NoiseBase temperatureUndulateNoise = new NoisePerlin(0L, 3, 31.0, 1.6, 2.2);
	private final NoiseBase rainfallUndulateNoise = new NoisePerlin(0L, 3, 31.0, 1.4, 2.2);
	
	private void setData(World world)
	{
		long seed = world.getSeed();
		offsetNoise.setSeed(seed);
		temperatureUndulateNoise.setSeed(seed);
		rainfallUndulateNoise.setSeed(seed);
	}
	
	@Override
	public float getTemperature(World world, BlockPos pos)
	{
		setData(world);
		double offset = offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		float undulate = (float) temperatureUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
		return Maths.lerp(zone.temperature[a], zone.temperature[b], d) + undulate;
	}

	@Override
	public float getAverageTemperature(World world, BlockPos pos)
	{
		setData(world);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		float undulate = (float) temperatureUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
		return zone.temperatureAverage + undulate;
	}

	@Override
	public float getSunshine(World world, BlockPos pos)
	{
		setData(world);
		double offset = offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		return Maths.lerp(zone.sunshine[a], zone.sunshine[b], d) * (float) Maths.sq2 * world.getSunBrightnessFactor(0F);
	}

	@Override
	public float getRainstrength(World world, BlockPos pos)
	{
		return getHumidity(world, pos) * (float) Maths.sq2 * world.rainingStrength;
	}
	
	@Override
	public float getSkylight(World world)
	{
		return world.getSunBrightnessFactor(0F);
	}

	@Override
	public float getHumidity(World world, BlockPos pos)
	{
		setData(world);
		double offset = offsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		float undulate = (float) rainfallUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
		return Math.max(0, Maths.lerp(zone.rain[a], zone.rain[b], d));
	}

	@Override
	public float getAverageHumidity(World world, BlockPos pos)
	{
		ClimaticZone zone = ClimaticZone.temperate_plain;
		float undulate = (float) rainfallUndulateNoise.noise(pos.getX(), pos.getY(), pos.getZ()) * 0.06F - 0.03F;
		return zone.rainAverage + undulate;
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