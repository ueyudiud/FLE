/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.worldgen.surface;

import java.util.Random;

import javax.annotation.Nonnull;

import farcore.blocks.terria.BlockRock;
import farcore.blocks.terria.BlockSand;
import farcore.blocks.terria.BlockSoil;
import farcore.blocks.terria.EnumCoverType;
import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.data.MP;
import farcore.lib.crop.ICrop;
import farcore.lib.material.Mat;
import farcore.lib.tree.ITreeGenerator;
import farcore.lib.tree.TreeGenAbstract;
import fargen.api.util.SelectorEntryListProvider;
import fargen.core.biome.BiomeBase;
import fargen.core.util.DataCacheCoord;
import fle.core.tree.TreeGenAcacia;
import fle.core.tree.TreeGenClassic;
import fle.core.tree.TreeGenJungle;
import fle.core.tree.TreeGenPine;
import fle.core.tree.TreeGenShrub;
import fle.core.tree.TreeGenSimple;
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
	static final TreeGenAbstract CEIBA1 = new TreeGenJungle(M.ceiba.getProperty(MP.property_tree), 0.01F).enableVineGen(), OAK1 = new TreeGenClassic(M.oak.getProperty(MP.property_tree), 0.03F), OAK2 = new TreeGenShrub(M.oak.getProperty(MP.property_tree), 0.025F),
			BIRCH = new TreeGenClassic(M.birch.getProperty(MP.property_tree), 0.03F), ACACIA = new TreeGenAcacia(M.acacia.getProperty(MP.property_tree), 0.03F), SPRUCE1 = new TreeGenPine(M.spruce.getProperty(MP.property_tree), 0.03F),
			WILLOW = new TreeGenSimple(M.willow.getProperty(MP.property_tree), 0.08F, true).setTreeLeavesShape(1, 6, 2, 3.6F);
	
	static final SelectorEntryListProvider<ITreeGenerator> TREE_GEN_PROVIDER;
	
	static void addVanillaTrees(BiomeBase biome, World world, int x, int z, NoiseBase noise, float temp, float rain, WeightedRandomSelector<ITreeGenerator> selector)
	{
		TREE_GEN_PROVIDER.addToSelector(x, z, temp, rain, noise, selector);
	}
	
	static final ICrop WHEAT = Mat.propertyOf("wheat", MP.property_crop);
	
	static final SelectorEntryListProvider<ICrop> CROP_PROVIDER;
	
	static void addVanillaCrops(int x, int z, Random rand, NoiseBase noise, float temp, float rain, WeightedRandomSelector<ICrop> selector)
	{
		if (WHEAT != ICrop.VOID && rand.nextInt(31) == 0)
		{
			CROP_PROVIDER.addToSelector(x, z, temp, rain, noise, selector);
		}
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
	
	static final IBlockState ROCK = M.stone.getProperty(MP.property_rock).block.getDefaultState(), SOIL_DEFAULT[] = { Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState() }, ROCK_DEFAULT[] = { ROCK };
	
	static final IRegister<IBlockState>		ROCK_LAYER_REGISTER	= new Register<>();
	static final IRegister<IBlockState[]>	SOIL_LAYER_REGISTER	= new Register<>(256);
	
	public static @Nonnull IBlockState[] getSoilLayerByName(String name)
	{
		return SOIL_LAYER_REGISTER.get(name, SOIL_DEFAULT);
	}
	
	public static int getSoilLayerIDByName(String name)
	{
		return SOIL_LAYER_REGISTER.id(name);
	}
	
	protected final FarSurfaceBiomeProvider biomeProvider;
	
	private DataCacheCoord<IBlockState[]>	rockDataCache;
	private DataCacheCoord<IBlockState[]>	topDataCache;
	
	public FarSurfaceDataGenerator(FarSurfaceBiomeProvider provider, long seed)
	{
		this.biomeProvider = provider;
		this.rockDataCache = new DataCacheCoord<>((x, z) -> A.transform(provider.layers[2].getInts(x << 4, z << 4, 16, 16), IBlockState[].class, FarSurfaceDataGenerator::getRockLayer), 4);
		this.topDataCache = new DataCacheCoord<>((x, z) -> A.transform(provider.layers[3].getInts(x << 4, z << 4, 16, 16), IBlockState[].class, FarSurfaceDataGenerator::getSoilLayer), 4);
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
				state = state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.TUNDRA);
			}
			else
			{
				state = state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.GRASS);
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
		
		for (Mat rock : new Mat[] { M.andesite, M.basalt, M.diorite, M.gabbro, M.granite, M.kimberlite, M.limestone, M.marble, M.peridotite, M.rhyolite, M.graniteP })
		{
			ROCK_LAYER_REGISTER.register(rock.name, rock.getProperty(MP.property_rock).block.getDefaultState());
		}
		
		for (Mat soil : new Mat[] { M.latrosol, M.latroaluminosol, M.ruboloam, M.ruboaluminoloam, M.flavoloam, M.peatsol, M.aterosol, M.podzol, M.pheosol, M.aterocalcosol, M.brunnocalcosol, M.brunnodesertosol, M.cinerodesertosol, M.flavobrunnoloam, M.brunnoloam, M.tundrosol, M.moraine, M.aterobrunnoloam, M.spaticocalcosol, M.palosol })
		{
			IBlockState state = soil.getProperty(MP.property_soil).block.getDefaultState();
			SOIL_LAYER_REGISTER.register(soil.name, new IBlockState[] { state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.GRASS), state.withProperty(BlockSoil.COVER_TYPE, EnumCoverType.NONE), GRAVEL });
		}
		for (Mat sand : new Mat[] { M.sand, M.redsand })
		{
			IBlockState state = sand.getProperty(MP.property_sand).block.getDefaultState();
			SOIL_LAYER_REGISTER.register(sand.name, new IBlockState[] { state, state, GRAVEL });
		}
		
		CEIBA1.setHeight(32, 10);
		OAK1.setHeight(4, 3);
		ACACIA.setHeight(5, 7);
		BIRCH.setHeight(4, 3);
		SPRUCE1.setHeight(7, 5);
		WILLOW.setHeight(4, 4);
		
		SelectorEntryListProvider.Builder
		
		builder = SelectorEntryListProvider.builder();
		builder.add2(WHEAT, 29371, 16, 30.0F, 8.0F, 1.4F, 1.2F);
		builder.add2(Mat.propertyOf("soybean", MP.property_crop), 26382, 24, 25.0F, 12.0F, 0.9F, 1.5F);
		builder.add2(Mat.propertyOf("potato", MP.property_crop), 183723, 16, 22.0F, 18.0F, 0.7F, 1.4F);
		builder.add2(Mat.propertyOf("sweetpotato", MP.property_crop), 174837, 16, 24.0F, 14.0F, 0.7F, 1.4F);
		builder.add2(Mat.propertyOf("reed", MP.property_crop), 164834, 64, 32.0F, 6.0F, 1.0F, 0.9F);
		builder.add2(Mat.propertyOf("wild_cabbage", MP.property_crop), 47374, 12, 32.0F, 8.0F, 0.6F, 0.9F);
		CROP_PROVIDER = builder.build();
		
		builder = SelectorEntryListProvider.builder();
		builder.add1(CEIBA1, 38274, 128, 32.0F, 39.0F, 0.7F, 1.2F);
		builder.add1(OAK1, 17274, 256, 18.0F, 32.0F, 0.4F, 0.95F);
		builder.add1(OAK2, 15628, 96, 27.0F, 36.0F, 0.3F, 0.5F);
		builder.add1(ACACIA, 23841, 384, 26.0F, 35.0F, 0.05F, 0.45F);
		builder.add1(BIRCH, 47247, 192, 12.0F, 29.0F, 0.4F, 0.8F);
		builder.add1(SPRUCE1, 56628, 64, -3.0F, 19.0F, 0.5F, 1.2F);
		builder.add1(WILLOW, 31537, 128, 20.0F, 40.0F, 0.025F, 0.6F);
		TREE_GEN_PROVIDER = builder.build();
	}
}
