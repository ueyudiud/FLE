package fargen.core.worldgen.v;

import fargen.core.FarGen;
import fargen.core.FarGenBiomes;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FarVoidProvider extends WorldProvider
{
	public FarVoidProvider()
	{

	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new FarVoidChunkGenerator(worldObj, worldObj.getSeed());
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return FarGen.FAR_VOID;
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
		return false;
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		return FarGenBiomes.v_void;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return new Vec3d(0.01F, 0.01F, 0.01F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float f1, float f2)
	{
		return new Vec3d(0F, 0F, 0F);
	}

	@Override
	protected void generateLightBrightnessTable()
	{
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0.0F;
	}

	/**
	 * Returns array with sunrise/sunset colors
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored()
	{
		return false;
	}
	
	/**
	 * True if the player can respawn in this dimension (true = overworld, false = nether).
	 */
	@Override
	public boolean canRespawnHere()
	{
		return true;
	}
	
	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
	 */
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	/**
	 * the y level at which clouds are rendered.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight()
	{
		return 8.0F;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return x == 0 && z == 0;
	}

	@Override
	public boolean getHasNoSky()
	{
		return true;
	}
	
	@Override
	public long getWorldTime()
	{
		return worldObj.getWorldInfo().getWorldTime();
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		return new BlockPos(0, 128, 0);
	}
	
	@Override
	public double getHorizon()
	{
		return 0;
	}
}