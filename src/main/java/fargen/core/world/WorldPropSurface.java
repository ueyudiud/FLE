package fargen.core.world;

import farcore.lib.util.NoiseBase;
import farcore.lib.util.NoisePerlin;
import farcore.lib.world.CalendarHandler;
import farcore.lib.world.ICalendarWithMonth;
import farcore.util.U.Maths;
import fargen.core.util.ClimaticZone;
import fargen.core.util.IWorldPropProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPropSurface implements IWorldPropProvider
{
	public static final ICalendarWithMonth calendar = CalendarHandler.overworld;
	private final NoiseBase temperatureOffsetNoise = new NoisePerlin(0L, 8, 76, 1.2, 3.0);
	
	private void setData(World world)
	{
		long seed = world.getSeed();
		temperatureOffsetNoise.setSeed(seed);
	}
	
	@Override
	public float getTemperature(World world, BlockPos pos)
	{
		setData(world);
		double offset = temperatureOffsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		return Maths.lerp(zone.temperature[a], zone.temperature[b], d);
	}

	@Override
	public float getSunshine(World world, BlockPos pos)
	{
		setData(world);
		double offset = temperatureOffsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
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
		double offset = temperatureOffsetNoise.noise(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		double l = offset + calendar.dProgressInYear(world.getWorldTime()) * 12D;
		int a = (int) l;
		int b = (a + 1) % 12;
		float d = (float) (l - a);
		ClimaticZone zone = ClimaticZone.temperate_plain;
		return Maths.lerp(zone.rain[a], zone.rain[b], d);
	}
}