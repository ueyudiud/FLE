package fargen.core.worldgen.surface;

import fargen.core.FarGen;
import fargen.core.biome.BiomeBase;
import fargen.core.worldgen.FarWorldType;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FarSurfaceProvider extends WorldProvider
{
	public FarSurfaceProvider()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getWeatherRenderer()
	{
		IRenderHandler handler = super.getWeatherRenderer();
		//		if(handler == null)
		//		{
		//			setWeatherRenderer(handler = new RenderWeatherSurface());
		//		}
		return handler;
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return FarGen.FAR_OVERWORLD;
	}
	
	/**
	 * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
	 * in WorldProviderSurface to prevent spawn chunks from being unloaded.
	 */
	@Override
	public boolean canDropChunk(int x, int z)
	{
		return !this.world.isSpawnChunk(x, z) || !this.world.provider.getDimensionType().shouldLoadSpawn();
	}
	
	/**
	 * The far core override classic ice, prevent vanilla water freeze now.
	 */
	@Override
	public boolean canBlockFreeze(BlockPos pos, boolean byWater)
	{
		//		if(this.world.getWorldType() != FarWorldType.DEFAULT &&
		//				this.world.getWorldType() != FarWorldType.FLAT &&
		//				this.world.getWorldType() != FarWorldType.LARGE_BIOMES)
		return super.canBlockFreeze(pos, byWater);
		//		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(world);
		//		float temp = properties.getTemperature(world, pos);
		//		if (temp > BiomeBase.minSnowTemperature)
		//			return false;
		//		else
		//		{
		//			if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
		//			{
		//				IBlockState state = world.getBlockState(pos);
		//				Block block = state.getBlock();
		//				if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) &&
		//						state.getValue(BlockLiquid.LEVEL).intValue() == 0)
		//				{
		//					if (!byWater)
		//						return true;
		//					boolean flag = Worlds.isBlockNearby(world, pos, Blocks.WATER, true);
		//					if (!flag)
		//						return true;
		//				}
		//			}
		//			return false;
		//		}
		//		return false;
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
		//		if(this.world.getWorldType() != FarWorldType.DEFAULT &&
		//				this.world.getWorldType() != FarWorldType.FLAT &&
		//				this.world.getWorldType() != FarWorldType.LARGE_BIOMES)
		return super.canSnowAt(pos, checkLightAndSnow);
		//		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(this.world);
		//		float temp = properties.getTemperature(this.world, pos);
		//
		//		if (temp > BiomeBase.minSnowTemperature)
		//			return false;
		//		else if (!checkLightAndSnow)
		//			return true;
		//		else
		//		{
		//			if (pos.getY() >= 0 && pos.getY() < 256 && this.world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
		//			{
		//				IBlockState state = this.world.getBlockState(pos);
		//				if (state.getBlock().isAir(state, this.world, pos) &&
		//						Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, pos))
		//					return true;
		//			}
		//			return false;
		//		}
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		if (this.world.isBlockLoaded(pos))
		{
			if (this.world.getWorldType() == FarWorldType.FLAT ||
					(this.world.getWorldType() != FarWorldType.DEFAULT &&
					this.world.getWorldType() != FarWorldType.LARGE_BIOMES))
				return super.getBiomeForCoords(pos);
			Chunk chunk = this.world.getChunkFromBlockCoords(pos);
			try
			{
				int i = pos.getX() & 15;
				int j = pos.getZ() & 15;
				int id = chunk.getBiomeArray()[j << 4 | i] & 0xFF;
				if(id == 255)
				{
					id = chunk.getBiomeArray()[j << 4 | i] = (byte) ((BiomeBase) getBiomeProvider().getBiome(pos, BiomeBase.DEBUG)).biomeID;
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
		{
			return getBiomeProvider().getBiome(pos, BiomeBase.DEBUG);
		}
	}
}