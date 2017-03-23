/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.worldgen.surface;

import static fargen.core.FarGenBiomes.beach;
import static fargen.core.FarGenBiomes.boreal_forest;
import static fargen.core.FarGenBiomes.frozen_tundra;
import static fargen.core.FarGenBiomes.gigafteral_forest;
import static fargen.core.FarGenBiomes.gigafungal_forest;
import static fargen.core.FarGenBiomes.glacispical_land;
import static fargen.core.FarGenBiomes.grassland;
import static fargen.core.FarGenBiomes.mangrove;
import static fargen.core.FarGenBiomes.meadow;
import static fargen.core.FarGenBiomes.ocean_f;
import static fargen.core.FarGenBiomes.ocean_f_deep;
import static fargen.core.FarGenBiomes.ocean_sf;
import static fargen.core.FarGenBiomes.ocean_sf_deep;
import static fargen.core.FarGenBiomes.ocean_st;
import static fargen.core.FarGenBiomes.ocean_st_deep;
import static fargen.core.FarGenBiomes.ocean_t;
import static fargen.core.FarGenBiomes.ocean_t_deep;
import static fargen.core.FarGenBiomes.ocean_te;
import static fargen.core.FarGenBiomes.ocean_te_deep;
import static fargen.core.FarGenBiomes.river;
import static fargen.core.FarGenBiomes.rockland;
import static fargen.core.FarGenBiomes.rocky_desert;
import static fargen.core.FarGenBiomes.savanna;
import static fargen.core.FarGenBiomes.sclerophyll_forest;
import static fargen.core.FarGenBiomes.sequoia_forest;
import static fargen.core.FarGenBiomes.shrubland;
import static fargen.core.FarGenBiomes.subtropical_broadleaf_forest;
import static fargen.core.FarGenBiomes.subtropical_coniferous_forest;
import static fargen.core.FarGenBiomes.swamp;
import static fargen.core.FarGenBiomes.temperate_broadleaf_forest;
import static fargen.core.FarGenBiomes.temperate_desert;
import static fargen.core.FarGenBiomes.temperate_mixed_forest;
import static fargen.core.FarGenBiomes.temperate_rainforest;
import static fargen.core.FarGenBiomes.tropical_desert;
import static fargen.core.FarGenBiomes.tropical_monsoon_forest;
import static fargen.core.FarGenBiomes.tropical_rainforest;
import static fargen.core.FarGenBiomes.tropical_thorny_forest;
import static fargen.core.FarGenBiomes.tundra;

import farcore.data.M;
import farcore.data.MP;
import farcore.lib.block.instance.BlockSoil;
import farcore.lib.block.instance.BlockSoil.EnumCoverType;
import farcore.lib.tree.ITreeGenerator;
import fargen.core.biome.BiomeBase;
import fargen.core.util.DataCache;
import fargen.core.util.DataCacheCoord;
import nebula.common.base.Selector;
import nebula.common.util.A;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;

/**
 * @author ueyudiud
 */
public class FarSurfaceDataGenerator
{
	static final IBlockState
	DIRT = M.latosol.getProperty(MP.property_soil).block.getDefaultState(),
	GRASS = DIRT.withProperty(BlockSoil.COVER_TYPE, BlockSoil.EnumCoverType.GRASS),
	GRAVEL = Blocks.GRAVEL.getDefaultState(),
	ROCK = M.stone.getProperty(MP.property_rock).block.getDefaultState();
	static final ITreeGenerator
	TREE_GENERATOR1 = M.oak.getProperty(MP.property_tree);
	
	static final
	IBlockState[] STATES1 = {GRASS, DIRT, GRAVEL},
	STATES2 = {ROCK};
	
	private final FarSurfaceBiomeProvider biomeProvider;
	
	private DataCacheCoord<IBlockState[]> rockDataCache;
	private DataCache<Selector<ITreeGenerator>> treeDataCache;
	private DataCacheCoord<IBlockState[]> topDataCache;
	
	public FarSurfaceDataGenerator(FarSurfaceBiomeProvider provider, long seed)
	{
		this.biomeProvider = provider;
		this.rockDataCache = new DataCacheCoord<>((x, z)-> A.createArray(256, STATES2), 2);
		this.treeDataCache = new DataCache<>((x, z)-> Selector.single(TREE_GENERATOR1));
		this.topDataCache = new DataCacheCoord<>((x, z)-> A.createArray(256, STATES1), 2);
	}
	
	public Selector<ITreeGenerator> getTreeGenerator(int x, int z)
	{
		return this.treeDataCache.get(x, z);
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
	
	public static IBlockState getWithWaterState(IBlockState state)
	{
		Block block = state.getBlock();
		if (block instanceof BlockSoil)
		{
			state = state.withProperty(BlockSoil.COVER_TYPE, state.getValue(BlockSoil.COVER_TYPE).getWaterCover());
		}
		return state;
	}
	
	
	public BiomeBase getBiome(int index, double temperature, double rainfall)
	{
		switch (index)
		{
		case 0 ://deep ocean
			return temperature > 0.8F ? ocean_t_deep :
				temperature > 0.6F ? ocean_st_deep :
					temperature > 0.4F ? ocean_te_deep :
						temperature > 0.2F ? ocean_sf_deep :
							ocean_f_deep;
		case 1 ://ocean
			return temperature > 0.8F ? ocean_t :
				temperature > 0.6F ? ocean_st :
					temperature > 0.4F ? ocean_te :
						temperature > 0.2F ? ocean_sf :
							ocean_f;
		case 8://beach
		case 9://gravel beach
			return beach[MathHelper.ceil(4.99999 * (1.0F - temperature))];
		case 10://river
			return river[MathHelper.ceil(4.99999 * (1.0F - temperature))];
		default:
			return
					temperature > 0.875F ? (rainfall < 0.1F ? tropical_desert :
						rainfall < 0.4F ? savanna :
							rainfall < 0.6F ? tropical_thorny_forest :
								rainfall < 0.8F ? tropical_monsoon_forest :
									tropical_rainforest)
							: temperature > 0.75F ? (rainfall < 0.2F ? tropical_desert :
								rainfall < 0.4F ? savanna :
									rainfall < 0.55F ? subtropical_coniferous_forest :
										rainfall < 0.7F ? subtropical_broadleaf_forest :
											rainfall < 0.8F ? mangrove :
												gigafteral_forest)
									: temperature > 0.625F ? (rainfall < 0.2F ? temperate_desert :
										rainfall < 0.4F ? shrubland :
											rainfall < 0.55F ? subtropical_coniferous_forest :
												rainfall < 0.7F ? subtropical_broadleaf_forest :
													rainfall < 0.8F ? mangrove :
														temperate_rainforest)
											: temperature > 0.5F ? (rainfall < 0.2F ? temperate_desert :
												rainfall < 0.35F ? shrubland :
													rainfall < 0.45F ? sclerophyll_forest :
														rainfall < 0.65F ? temperate_broadleaf_forest :
															rainfall < 0.85F ? swamp :
																gigafungal_forest)
													: temperature > 0.375F ? (rainfall < 0.2F ? temperate_desert :
														rainfall < 0.25F ? rockland :
															rainfall < 0.35F ? grassland :
																rainfall < 0.45F ? sclerophyll_forest :
																	rainfall < 0.55F ? temperate_broadleaf_forest :
																		rainfall < 0.65F ? temperate_mixed_forest :
																			rainfall < 0.85F ? swamp :
																				gigafungal_forest)
															: temperature > 0.25F ?
																	(rainfall < 0.2F ? rocky_desert :
																		rainfall < 0.5F ? grassland :
																			rainfall < 0.7F ? boreal_forest :
																				sequoia_forest)
																	: temperature > 0.125F ?
																			(rainfall < 0.2F ? rocky_desert :
																				rainfall < 0.5F ? meadow :
																					rainfall < 0.7F ? boreal_forest :
																						rainfall < 0.9F ? sequoia_forest :
																							glacispical_land)
																			: (rainfall < 0.2F ? rocky_desert :
																				rainfall < 0.5F ? tundra :
																					rainfall < 0.7F ? frozen_tundra :
																						glacispical_land);
		}
	}
}