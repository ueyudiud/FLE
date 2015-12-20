package fle.core.world.dim;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FLENetherProvider extends WorldProvider
{
	public FLENetherProvider()
	{
		isHellWorld = true;
		hasNoSky = true;
		dimensionId = -1;
	}

    /**
     * Return Vec3D with biome specific fog color
     */
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float x, float z)
    {
        return Vec3.createVectorHelper(0.7D, 0.03D, 0.03D);
    }

    /**
     * Creates the light to brightness table
     */
    protected void generateLightBrightnessTable()
    {
        float f = 0.1F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }
	
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return false;
    }
	
	@Override
	public String getDimensionName()
	{
		return "FLE Nether";
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return "Entering ths Nether";
	}
	
	@Override
	public String getDepartMessage()
	{
		return "Leaving this Nether";
	}
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new FLENetherChunkProvider(worldObj, worldObj.getSeed());
	}

	@Override
	public float getCloudHeight()
	{
		return 256.0F;
	}

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float calculateCelestialAngle(long tick, float f)
    {
        return 0.5F;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean canRespawnHere()
    {
        return false;
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return true;
    }
	
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return false;
	}
	
	@Override
	public boolean canSnowAt(int x, int y, int z, boolean checkLight)
	{
		return false;
	}
	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return false;
	}
}