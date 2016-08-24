//package fargen.core.worldgen.surface.text;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//import com.google.common.collect.HashBasedTable;
//import com.google.common.collect.Table;
//
//import farcore.lib.util.NoiseBase;
//import farcore.lib.util.NoiseCell;
//import farcore.lib.util.NoisePerlin;
//import farcore.lib.world.IBiomeRegetter;
//import fargen.core.biome.BiomeBase;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.BiomeCache;
//import net.minecraft.world.biome.BiomeProvider;
//import net.minecraft.world.gen.layer.IntCache;
//
//public abstract class FarSurfacableBiome extends BiomeProvider implements IBiomeRegetter
//{
//	protected BiomeCache biomeCache;
//	private final int temp;
//	private final int hum;
//	private NoiseBase noiseTemperatureBig;
//	private NoiseBase noiseTemperatureSmall;
//	private NoiseBase noiseHumidityBig;
//	private NoiseBase noiseHumiditySmall;
//	private NoiseBase noiseBiomeBig;
//	private NoiseBase noiseBiomeSmall;
//	private double[] cache;
//	private Table<Integer, Integer, List<BiomeBase>> biomeTable;
//
//	public FarSurfacableBiome(World world, int temperature, int humidity)
//	{
//		biomeCache = new BiomeCache(this);
//		Random random = new Random(world.getSeed());
//		temp = temperature;
//		hum = humidity;
//		biomeTable = HashBasedTable.<Integer, Integer, List<BiomeBase>>create(temperature, humidity);
//		noiseTemperatureBig = new NoisePerlin(random, 5, 64.0, 2.5, 1.75);
//		noiseBiomeSmall = new NoiseCell(random.nextLong(), 1, 2, 8.0);
//		noiseTemperatureBig = new NoisePerlin(random, 8, 38.0, 2.0, 1.20);
//		noiseTemperatureSmall = new NoiseCell(random.nextLong(), 2, 2, 4.0);
//		noiseHumidityBig = new NoisePerlin(random, 8, 27.0, 2.1, 1.30);
//		noiseHumiditySmall = new NoiseCell(random.nextLong(), 2, 2, 4.0);
//	}
//
//	protected void putBiomeInTable(int temp, int hum, BiomeBase biome)
//	{
//		List<BiomeBase> list;
//		if(!biomeTable.contains(temp, hum))
//		{
//			list = new ArrayList();
//			list.add(biome);
//			biomeTable.put(temp, hum, list);
//		}
//		else
//		{
//			biomeTable.get(temp, hum).add(biome);
//		}
//	}
//	protected void putBiomeInTable(int temp, int hum, BiomeBase...biomes)
//	{
//		List<BiomeBase> list;
//		if(!biomeTable.contains(temp, hum))
//		{
//			list = new ArrayList();
//			list.addAll(Arrays.asList(biomes));
//			biomeTable.put(temp, hum, list);
//		}
//		else
//		{
//			biomeTable.get(temp, hum).addAll(Arrays.asList(biomes));
//		}
//	}
//
//	@Override
//	public BiomeBase getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn)
//	{
//		return getBiomeAtPos(pos.getX(), pos.getY(), pos.getZ());
//	}
//
//	protected BiomeBase getBiomeAtPos(int x, int y, int z)
//	{
//		float temp = (float) (
//				noiseTemperatureBig.noise(x, 0, z) * 0.92 +
//				noiseTemperatureSmall.noise(x, 0, z) * 0.08);
//		float humidity = (float) (
//				noiseHumidityBig.noise(x, 0, z) * 0.92 +
//				noiseHumiditySmall.noise(x, 0, z) * 0.08);
//		int t1 = (int) (temp * this.temp);
//		int h1 = (int) (humidity * hum);
//		List<BiomeBase> biomes = biomeTable.get(t1, h1);
//		int size = biomes.size();
//		int i = (int) (size * (noiseBiomeBig.noise(x, 0, z) * 0.75 + noiseBiomeSmall.noise(x, 0, z) * 0.25));
//		return biomes.get(i);
//	}
//
//	@Override
//	public Biome[] getBiomeGenAt(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
//	{
//		IntCache.resetIntCache();
//
//		if (listToReuse == null || listToReuse.length < width * length)
//		{
//			listToReuse = new Biome[width * length];
//		}
//
//		if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
//		{
//			Biome[] abiome = biomeCache.getCachedBiomes(x, z);
//			System.arraycopy(abiome, 0, listToReuse, 0, width * length);
//			return listToReuse;
//		}
//		else
//		{
//			for(int i = 0; i < width; ++i)
//			{
//				for(int j = 0; j < length; ++j)
//				{
//					listToReuse[j * width + i] = getBiomeAtPos(x + i, 0, z + j);
//				}
//			}
//
//			return listToReuse;
//		}
//	}
//
//	@Override
//	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
//	{
//		return null;
//	}
//
//	@Override
//	public abstract Biome getBiome(int saveID, BlockPos pos);
//}