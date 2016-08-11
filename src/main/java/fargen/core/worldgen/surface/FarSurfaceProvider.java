package fargen.core.worldgen.surface;

import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.FarGen;
import fargen.core.biome.BiomeBase;
import fargen.core.render.RenderWeatherSurface;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class FarSurfaceProvider extends WorldProvider
{
	public FarSurfaceProvider()
	{
		setWeatherRenderer(new RenderWeatherSurface());
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return FarGen.FAR_OVERWORLD;
	}
	
	@Override
	protected void createBiomeProvider()
	{
		biomeProvider = new FarSurfaceBiomeProvider(worldObj.getWorldInfo());
	}

	/**
	 * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
	 * in WorldProviderSurface to prevent spawn chunks from being unloaded.
	 */
	@Override
	public boolean canDropChunk(int x, int z)
	{
		return !worldObj.isSpawnChunk(x, z) || !worldObj.provider.getDimensionType().shouldLoadSpawn();
	}

	/**
	 * The far core override classic ice, prevent vanilla water freeze now.
	 */
	@Override
	public boolean canBlockFreeze(BlockPos pos, boolean byWater)
	{
		return false;
		//		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(worldObj);
		//		float temp = properties.getTemperature(worldObj, pos);
		//		if (temp > BiomeBase.minSnowTemperature)
		//			return false;
		//		else
		//		{
		//			if (pos.getY() >= 0 && pos.getY() < 256 && worldObj.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
		//			{
		//				IBlockState state = worldObj.getBlockState(pos);
		//				Block block = state.getBlock();
		//				if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) &&
		//						state.getValue(BlockLiquid.LEVEL).intValue() == 0)
		//				{
		//					if (!byWater)
		//						return true;
		//					boolean flag = Worlds.isBlockNearby(worldObj, pos, Blocks.WATER, true);
		//					if (!flag)
		//						return true;
		//				}
		//			}
		//			return false;
		//		}
	}
	
	/**
	 *
	 * @param pos
	 * @param checkLightAndSnow Might this method also check whether
	 * snow can place on side?
	 * @return
	 */
	@Override
	public boolean canSnowAt(BlockPos pos, boolean checkLightAndSnow)
	{
		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(worldObj);
		float temp = properties.getTemperature(worldObj, pos);
		
		if (temp > BiomeBase.minSnowTemperature)
			return false;
		else if (!checkLightAndSnow)
			return true;
		else
		{
			if (pos.getY() >= 0 && pos.getY() < 256 && worldObj.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
			{
				IBlockState state = worldObj.getBlockState(pos);
				if (state.getBlock().isAir(state, worldObj, pos) &&
						Blocks.SNOW_LAYER.canPlaceBlockAt(worldObj, pos))
					return true;
			}
			return false;
		}
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		if (worldObj.isBlockLoaded(pos))
		{
			Chunk chunk = worldObj.getChunkFromBlockCoords(pos);
			try
			{
				int i = pos.getX() & 15;
				int j = pos.getZ() & 15;
				int id = chunk.getBiomeArray()[j << 4 | i] & 0xFF;
				if(id == 255)
				{
					id = chunk.getBiomeArray()[j << 4 | i] = (byte) ((BiomeBase) getBiomeProvider().getBiomeGenerator(pos, BiomeBase.DEBUG)).biomeID;
				}
				return BiomeBase.getBiomeFromID(id);
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
				crashreportcategory.setDetail("Location", () -> CrashReportCategory.getCoordinateInfo(pos));
				throw new ReportedException(crashreport);
			}
		}
		else
			return getBiomeProvider().getBiomeGenerator(pos, BiomeBase.DEBUG);
	}
}