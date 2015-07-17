package fla.core.world.generate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos.ChunkPos;
import fla.core.util.MathHelper;

public class FlaWorldGenerateHelper 
{
	private static long seed;
	private static Map<String, Map<Long, Double>> cache = new HashMap();
	
	public static int[] getHeight(long seed, int x, int z)
	{
		init(seed);
		double posMax1 =  h("height1", seed, x, z, 256, 48, -16, 0, 421410183L),
				posMax2 = h("height1", seed, x, z, 256, 48, -16, 1, 421410183L),
				posMax3 = h("height1", seed, x, z, 256, 48, -16, 2, 421410183L),
				posMax4 = h("height1", seed, x, z, 256, 48, -16, 3, 421410183L),
				posMid1 = h("height2", seed, x, z, 16 , 12, -4 , 0, 435251619L),
				posMid2 = h("height2", seed, x, z, 16 , 12, -4 , 1, 435251619L),
				posMid3 = h("height2", seed, x, z, 16 , 12, -4 , 2, 435251619L),
				posMid4 = h("height2", seed, x, z, 16 , 12, -4 , 3, 435251619L),
				posMin1 = h("height3", seed, x, z, 1  , 3 , -1 , 0, 382112517L),
				posMin2 = h("height3", seed, x, z, 1  , 3 , -1 , 1, 382112517L),
				posMin3 = h("height3", seed, x, z, 1  , 3 , -1 , 2, 382112517L),
				posMin4 = h("height3", seed, x, z, 1  , 3 , -1 , 3, 382112517L);
		
		int[] rets = new int[256];
		for(int cX = 0; cX < 16; ++cX)
			for(int cZ = 0; cZ < 16; ++cZ)
			{
				int aX = MathHelper.mode(x * 16 + cX, 4096),
					bX = MathHelper.mode(x * 16 + cX, 256),
					aZ = MathHelper.mode(z * 16 + cZ, 4096),
					bZ = MathHelper.mode(z * 16 + cZ, 256);
				double max = (posMax1 - posMax2 - posMax3 + posMax4) / 0x1000000 * aX * aZ + (posMax3 - posMax1) / 0X1000 * aZ + (posMax2 - posMax1) / 0x1000 * aX + posMax1;
				double mid = (posMid1 - posMid2 - posMid3 + posMid4) / 0x10000   * bX * bZ + (posMid3 - posMid1) / 0X100  * bZ + (posMid2 - posMid1) / 0x100  * bX + posMid1;
				double min = (posMin1 - posMin2 - posMin3 + posMin4) / 0x100     * cX * cZ + (posMin3 - posMin1) / 0X10   * cZ + (posMin2 - posMin1) / 0x10   * cX + posMin1;
				rets[cX * 16 + cZ] = (int)Math.round(max + mid + min + FlaValue.SEALEVEL);
			}
		return rets;
	}
	public static int getHeight(long seed, int x, int z, int u, int v)
	{
		init(seed);
		double posMax1 =  h("height1", seed, x, z, 256, 48, -16, 0, 421410183L),
				posMax2 = h("height1", seed, x, z, 256, 48, -16, 1, 421410183L),
				posMax3 = h("height1", seed, x, z, 256, 48, -16, 2, 421410183L),
				posMax4 = h("height1", seed, x, z, 256, 48, -16, 3, 421410183L),
				posMid1 = h("height2", seed, x, z, 16 , 12, -4 , 0, 435251619L),
				posMid2 = h("height2", seed, x, z, 16 , 12, -4 , 1, 435251619L),
				posMid3 = h("height2", seed, x, z, 16 , 12, -4 , 2, 435251619L),
				posMid4 = h("height2", seed, x, z, 16 , 12, -4 , 3, 435251619L),
				posMin1 = h("height3", seed, x, z, 1  , 3 , -1 , 0, 382112517L),
				posMin2 = h("height3", seed, x, z, 1  , 3 , -1 , 1, 382112517L),
				posMin3 = h("height3", seed, x, z, 1  , 3 , -1 , 2, 382112517L),
				posMin4 = h("height3", seed, x, z, 1  , 3 , -1 , 3, 382112517L);
		
		int aX = MathHelper.mode(x, 256) * 16 + u,
			bX = MathHelper.mode(x, 16) * 16 + v,
			aZ = MathHelper.mode(z, 256) * 16 + u,
			bZ = MathHelper.mode(z, 16) * 16 + v;
		double max = (posMax1 - posMax2 - posMax3 + posMax4) / 0x1000000 * aX * aZ + (posMax3 - posMax1) / 0X1000 * aZ + (posMax2 - posMax1) / 0x1000 * aX + posMax1;
		double mid = (posMid1 - posMid2 - posMid3 + posMid4) / 0x10000   * bX * bZ + (posMid3 - posMid1) / 0X100  * bZ + (posMid2 - posMid1) / 0x100  * bX + posMid1;
		double min = (posMin1 - posMin2 - posMin3 + posMin4) / 0x100     * u  * v  + (posMin3 - posMin1) / 0X10   * v  + (posMin2 - posMin1) / 0x10   * u  + posMin1;
		return (int)Math.round(max + mid + min + FlaValue.SEALEVEL);
	}

	public static double getTempreture(long seed, int x, int z)
	{
		init(seed);
		double posMax1 = h("tem1", seed, x, z, 4096, 3, -4, 0, 2759273521L),
				posMax2 = h("tem1", seed, x, z, 4096, 3, -4, 1, 2759273521L),
				posMax3 = h("tem1", seed, x, z, 4096, 3, -4, 2, 2759273521L),
				posMax4 = h("tem1", seed, x, z, 4096, 3, -4, 3, 2759273521L),
				posMin1 = h("tem2", seed, x, z, 256, 1, -2, 0, 532805869L),
				posMin2 = h("tem2", seed, x, z, 256, 1, -2, 1, 532805869L),
				posMin3 = h("tem2", seed, x, z, 256, 1, -2, 2, 532805869L),
				posMin4 = h("tem2", seed, x, z, 256, 1, -2, 3, 532805869L);
		int aX = x  & 0xFFF;
		int bX = aX & 0XFF;
		int aZ = z  & 0XFFF;
		int bZ = aZ % 0XFF;
		
		double max = (posMax1 - posMax2 - posMax3 + posMax4) / 0x1000000 * aX * aZ + (posMax3 - posMax1) / 0X1000 * aZ + (posMax2 - posMax1) / 0x1000 * aX + posMax1;
		double min = (posMin1 - posMin2 - posMin3 + posMin4) / 0x10000   * bX * bZ + (posMin3 - posMin1) / 0X100  * bZ + (posMin2 - posMin1) / 0x100  * bX + posMin1;
		
		return (max + min + FlaValue.BASE_TEMPRETURE) / 10;
	}

	public static double getDampness(long seed, int x, int z)
	{
		init(seed);
		double posMax1 = h("damp1", seed, x, z, 4096, 3, -4, 0, 832572309L),
				posMax2 = h("damp1", seed, x, z, 4096, 3, -4, 1, 832572309L),
				posMax3 = h("damp1", seed, x, z, 4096, 3, -4, 2, 832572309L),
				posMax4 = h("damp1", seed, x, z, 4096, 3, -4, 3, 832572309L),
				posMin1 = h("damp2", seed, x, z, 256, 1, -2, 0, 710582472L),
				posMin2 = h("damp2", seed, x, z, 256, 1, -2, 1, 710582472L),
				posMin3 = h("damp2", seed, x, z, 256, 1, -2, 2, 710582472L),
				posMin4 = h("damp2", seed, x, z, 256, 1, -2, 3, 710582472L);
		int aX = x  % 0x1000;
		int bX = aX % 0x100;
		int aZ = z  % 0x1000;
		int bZ = aZ % 0x100;

		double max = (posMax1 - posMax2 - posMax3 + posMax4) / 0x1000000 * aX * aZ + (posMax3 - posMax1) / 0X1000 * aZ + (posMax2 - posMax1) / 0x1000 * aX + posMax1;
		double min = (posMin1 - posMin2 - posMin3 + posMin4) / 0x10000   * bX * bZ + (posMin3 - posMin1) / 0X100  * bZ + (posMin2 - posMin1) / 0x100  * bX + posMin1;
		
		return (max + min + FlaValue.BASE_TEMPRETURE) / 10;
	}
	
	private static double h(String str, long seed, int x, int z, int chunkSize, int max, int min, int dir, long s)
	{
		Random rand = new Random(s);
		int a = (int) MathHelper.floor(x, chunkSize) + ((dir & 1) != 0 ? chunkSize : 0);
		int b = (int) MathHelper.floor(z, chunkSize) + ((dir & 2) != 0 ? chunkSize : 0);
		return setupRandom(str, seed, s * (a * rand.nextInt() + b * rand.nextInt()), max, min);
	}
	
	private static void init(long seed)
	{
		if(FlaWorldGenerateHelper.seed != seed)
		{
			cache.clear();
			FlaWorldGenerateHelper.seed = seed;
		}
	}

	private static double setupRandom(String tag, long seed1, long seed2, double maxValue)
	{
		if(!cache.containsKey(tag))
		{
			cache.put(tag, new HashMap());
		}
		if(cache.get(tag).containsKey(seed2))
		{
			return cache.get(tag).get(seed2);
		}
		double ret = new Random(seed1 * seed2).nextDouble() * maxValue;
		cache.get(tag).put(seed2, ret);
		return ret;
	}
	private static double setupRandom(String tag, long seed1, long seed2, double maxValue, double minValue)
	{
		if(!cache.containsKey(tag))
		{
			cache.put(tag, new HashMap());
		}
		if(cache.get(tag).containsKey(seed2))
		{
			return cache.get(tag).get(seed2);
		}
		double ret = new Random(seed1 * seed2).nextDouble() * (maxValue - minValue) + minValue;
		cache.get(tag).put(seed2, ret);
		return ret;
	}
}
