/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.worldgen.surface;

import java.util.Random;

import javax.annotation.Nonnull;

import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.data.MP;
import farcore.lib.block.terria.BlockRock;
import farcore.lib.block.terria.BlockSand;
import farcore.lib.block.terria.BlockSoil;
import farcore.lib.block.terria.BlockSoil.EnumCoverType;
import farcore.lib.crop.ICrop;
import farcore.lib.material.Mat;
import farcore.lib.tree.ITreeGenerator;
import farcore.lib.tree.TreeGenAbstract;
import fargen.core.biome.BiomeBase;
import fargen.core.util.DataCacheCoord;
import fle.core.tree.TreeGenAcacia;
import fle.core.tree.TreeGenClassic;
import fle.core.tree.TreeGenJungle;
import fle.core.tree.TreeGenPine;
import fle.core.tree.TreeGenShrub;
import nebula.base.IRegister;
import nebula.base.Register;
import nebula.base.function.WeightedRandomSelector;
import nebula.common.util.A;
import nebula.common.util.noise.NoiseBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class FarSurfaceDataGenerator
{
	static final TreeGenAbstract
	CEIBA1 = new TreeGenJungle(M.ceiba.getProperty(MP.property_tree), 0.01F),
	OAK1 = new TreeGenClassic(M.oak.getProperty(MP.property_tree), 0.03F),
	OAK2 = new TreeGenShrub(M.oak.getProperty(MP.property_tree), 0.025F),
	BIRCH = new TreeGenClassic(M.birch.getProperty(MP.property_tree), 0.03F),
	ACACIA = new TreeGenAcacia(M.acacia.getProperty(MP.property_tree), 0.03F),
	SPRUCE1 = new TreeGenPine(M.spruce.getProperty(MP.property_tree), 0.03F);
	
	static void addVanillaTrees(BiomeBase biome, World world, int x, int z, NoiseBase noise, float temp, float rain, WeightedRandomSelector<ITreeGenerator> selector)
	{
		double d1, d2;
		int value;
		if (temp > 0.7F && rain > 0.7F)
		{
			d2 = noise.noise(x, 38274.0, z);
			d1 = temp > 0.8F ? 1.0F : (temp - 0.7F) * 10.0F;
			d1 *= rain > 0.8F ? 1.0F : (rain - 0.7F) * 10.0F;
			value = (int) (d1 * d2 * d2 * 128);
			if (value > 0)
				selector.add(CEIBA1, value);
		}
		if (temp > 0.4F && rain > 0.4F && rain < 0.95F)
		{
			d2 = noise.noise(x, 17274.0, z);
			d1 = temp > 0.9F ? (1.0F - temp) * 10.0F : temp > 0.5F ? 1.0F : (temp - 0.4F) * 10.0F;
			d1 *= rain > 0.9F ? (1.0F - rain) * 20.0F : rain > 0.5F ? 1.0F : (rain - 0.5F) * 10.0F;
			value = (int) (d1 * d2 * d2 * 256);
			if (value > 0)
				selector.add(OAK1, value);
		}
		if (temp > 0.5F && rain < 0.45F * temp)
		{
			d2 = noise.noise(x, 15628.0, z);
			d1 = temp > 0.6F ? 1.0F : (temp - 0.5F) * 10.0F;
			d1 *= rain > 0.35F * temp ? (rain - 0.35F * temp) * 10.0F / temp : 1.0F;
			value = (int) (d1 * d2 * d2 * 96);
			if (value > 0)
				selector.add(OAK2, value);
		}
		if (temp > 0.7F && rain > 0.05F && rain < 0.45F)
		{
			d2 = noise.noise(x, 23841.0, z);
			d1 = temp > 0.8F ? 1.0F : (temp - 0.7F) * 10.0F;
			d1 *= rain < 0.15F ? (rain - 0.05F) * 10.0F : rain > 0.35F ? (0.45F - rain) * 10.0F : 1.0F;
			value = (int) (d1 * d2 * d2 * 384);
			if (value > 0)
				selector.add(ACACIA, value);
		}
		if (temp < 0.9F && temp > 0.3F && rain > 0.4F && rain < 0.8F)
		{
			d2 = noise.noise(x, 47247.0, z);
			d1 = temp > 0.9F ? (1.0F - temp) * 10.0F : temp > 0.4F ? 1.0F : (temp - 0.3F) * 10.0F;
			d1 *= rain > 0.7F ? (1.0F - rain) * 10.0F : rain > 0.5F ? 1.0F : (rain - 0.4F) * 10.0F;
			value = (int) (d1 * d2 * d2 * 192);
			if (value > 0)
				selector.add(BIRCH, value);
		}
		if (temp < 0.3F && temp > -0.4F && rain > 0.5F)
		{
			d2 = noise.noise(x, 47247.0, z);
			d1 = temp > 0.2F ? (0.3F - temp) * 10.0F : temp > -0.3F ? 1.0F : (temp + 0.3F) * 10.0F;
			d1 *= rain > 0.7F ? 1.0F : (rain - 0.5F) * 5.0F;
			value = (int) (d1 * d2 * d2 * 64);
			if (value > 0)
				selector.add(SPRUCE1, value);
		}
	}
	
	static final ICrop
	WHEAT = Mat.material("wheat").getProperty(MP.property_crop),
	SOYBEAN = Mat.material("soybean").getProperty(MP.property_crop),
	POTATO = Mat.material("potato").getProperty(MP.property_crop),
	SWEET_POTATO = Mat.material("sweetpotato").getProperty(MP.property_crop),
	REED = Mat.material("reed").getProperty(MP.property_crop),
	WILD_CABBAGE = Mat.material("wild_cabbage").getProperty(MP.property_crop);
	
	static void addVanillaCrops(int x, int z, Random rand, NoiseBase noise, float temp, float rain, WeightedRandomSelector<ICrop> selector)
	{
		if (WHEAT != ICrop.VOID && rand.nextInt(31) == 0)
		{
			int weight;
			weight = c(16, x, z, 29371.0, noise, temp, 30.0F, 8.0F, rain, 1.4F, 1.2F);
			if (weight > 0)
			{
				selector.add(WHEAT, weight);
			}
			weight = c(24, x, z, 26382.0, noise, temp, 25.0F, 12.0F, rain, 0.9F, 1.5F);
			if (weight > 0)
			{
				selector.add(SOYBEAN, weight);
			}
			weight = c(16, x, z, 183723.0, noise, temp, 22.0F, 18.0F, rain, 0.7F, 1.4F);
			if (weight > 0)
			{
				selector.add(POTATO, weight);
			}
			weight = c(16, x, z, 174837.0, noise, temp, 24.0F, 14.0F, rain, 0.7F, 1.4F);
			if (weight > 0)
			{
				selector.add(SWEET_POTATO, weight);
			}
			if (rain > 0.7F && temp > 0.64F)
			{
				weight = c(64, x, z, 164834.0, noise, temp, 32.0F, 6.0F, rain, 1.0F, 0.9F);
				if (weight > 0)
				{
					selector.add(REED, weight);
				}
			}
			weight = c(12, x, z, 164837.0, noise, temp, 32.0F, 8.0F, rain, 0.6F, 0.9F);
			if (weight > 0)
			{
				selector.add(WILD_CABBAGE, weight);
			}
		}
	}
	
	private static int c(int baseMultiplier, int x, int z, final double yNoise, NoiseBase noise,
			float temp, final float tempArange, final float tempSigma,
			float rain, final float rainArange, final float rainSigma)
	{
		double result = baseMultiplier * noise.noise(x, yNoise, z);
		temp -= tempArange;
		result *= tempSigma / (tempSigma + temp * temp);
		rain -= rainArange;
		result *= rainSigma / (rainSigma + rain * rain);
		return (int) result;
	}
	
	private static IBlockState[] getRockLayer(int id)
	{
		IBlockState[] states = new IBlockState[1];
		int i = (id & 0x3FF) % ROCK_LAYER_REGISTER.size();
		states[0] = ROCK_LAYER_REGISTER.get(i);
		return states;
	}
	
	private static IBlockState[] getSoilLayer(int id)
	{
		return SOIL_LAYER_REGISTER.get(id, SOIL_DEFAULT);
	}
	
	static final IBlockState
	ROCK = M.stone.getProperty(MP.property_rock).block.getDefaultState(),
	SOIL_DEFAULT[] = {Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState()},
	ROCK_DEFAULT[] = {ROCK};
	
	static final IRegister<IBlockState> ROCK_LAYER_REGISTER = new Register<>();
	static final IRegister<IBlockState[]> SOIL_LAYER_REGISTER = new Register<>(256);
	
	public static @Nonnull IBlockState[] getSoilLayerByName(String name)
	{
		return SOIL_LAYER_REGISTER.get(name, SOIL_DEFAULT);
	}
	
	public static int getSoilLayerIDByName(String name)
	{
		return SOIL_LAYER_REGISTER.id(name);
	}
	
	protected final FarSurfaceBiomeProvider biomeProvider;
	
	private DataCacheCoord<IBlockState[]> rockDataCache;
	private DataCacheCoord<IBlockState[]> topDataCache;
	
	public FarSurfaceDataGenerator(FarSurfaceBiomeProvider provider, long seed)
	{
		this.biomeProvider = provider;
		this.rockDataCache = new DataCacheCoord<>((x, z)-> A.transform(
				provider.layers[2].getInts(x << 4, z << 4, 16, 16),
				IBlockState[].class, FarSurfaceDataGenerator::getRockLayer), 4);
		this.topDataCache = new DataCacheCoord<>((x, z)-> A.transform(
				provider.layers[3].getInts(x << 4, z << 4, 16, 16),
				IBlockState[].class, FarSurfaceDataGenerator::getSoilLayer), 4);
	}
	
	void cleanCache()
	{
		this.rockDataCache.clean();
		this.topDataCache.clean();
	}
	
	public IBlockState[][] getCoverLayer(int x, int z)
	{
		return this.topDataCache.get(x, z);
	}
	
	public IBlockState[][] getRockLayer(int x, int z)
	{
		return this.rockDataCache.get(x, z);
	}
	
	public static boolean isGrass(IBlockState state)
	{
		return state.getBlock() instanceof BlockSoil && state.getValue(BlockSoil.COVER_TYPE).getNoCover() == EnumCoverType.GRASS;
	}
	
	public static boolean isSand(IBlockState state)
	{
		return state.getBlock() instanceof BlockSand;
	}
	
	public static IBlockState getGrassStateWithTemperatureAndRainfall(IBlockState state, float temp, float rain)
	{
		Block block = state.getBlock();
		if (block instanceof BlockSoil)
		{
			if (temp < 0.6F)
			{
				state = state.withProperty(BlockSoil.COVER_TYPE, BlockSoil.EnumCoverType.TUNDRA);
			}
			else
			{
				state = state.withProperty(BlockSoil.COVER_TYPE, BlockSoil.EnumCoverType.GRASS);
			}
		}
		return state;
	}
	
	public static IBlockState getMossy(IBlockState state)
	{
		Block block = state.getBlock();
		if (block == Blocks.COBBLESTONE)
		{
			return Blocks.MOSSY_COBBLESTONE.getDefaultState();
		}
		else if (block.getBlockState().getProperties().contains(BlockRock.TYPE))
		{
			state = state.withProperty(BlockRock.TYPE, EnumRockType.mossy);
		}
		return state;
	}
	
	static
	{
		final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
		
		for (Mat rock : new Mat[]{M.andesite, M.basalt, M.diorite, M.gabbro, M.granite, M.kimberlite,
				M.limestone, M.marble, M.peridotite, M.rhyolite, M.graniteP})
		{
			ROCK_LAYER_REGISTER.register(rock.name, rock.getProperty(MP.property_rock).block.getDefaultState());
		}
		
		for (Mat soil : new Mat[]{M.latrosol, M.latroaluminosol, M.ruboloam, M.ruboaluminoloam,
				M.flavoloam, M.peatsol, M.aterosol, M.podzol, M.pheosol, M.aterocalcosol,
				M.brunnocalcosol, M.brunnodesertosol, M.cinerodesertosol, M.flavobrunnoloam,
				M.brunnoloam, M.tundrosol, M.moraine, M.aterobrunnoloam, M.spaticocalcosol,
				M.palosol})
		{
			IBlockState state = soil.getProperty(MP.property_soil).block.getDefaultState();
			SOIL_LAYER_REGISTER.register(soil.name, new IBlockState[] {
					state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.GRASS),
					state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.NONE),
					GRAVEL
			});
		}
		for (Mat sand : new Mat[]{M.sand, M.redsand})
		{
			IBlockState state = sand.getProperty(MP.property_sand).block.getDefaultState();
			SOIL_LAYER_REGISTER.register(sand.name, new IBlockState[]{state, state, GRAVEL});
		}
		
		CEIBA1.setHeight(32, 10);
		OAK1.setHeight(4, 3);
		ACACIA.setHeight(5, 7);
		BIRCH.setHeight(4, 3);
		SPRUCE1.setHeight(7, 5);
	}
}