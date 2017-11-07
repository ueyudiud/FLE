package fargen.core.worldgen.end;

import javax.annotation.Nullable;

import fargen.core.FarGen;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FarEndProvider extends WorldProvider
{
	private DragonFightManager dragonFightManager;
	
	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	@Override
	public void createBiomeProvider()
	{
		this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
		this.hasNoSky = true;
		NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(DimensionType.THE_END);
		this.dragonFightManager = this.world instanceof WorldServer ? new DragonFightManager((WorldServer) this.world, nbttagcompound.getCompoundTag("DragonFight")) : null;
	}
	
	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified
	 * time (usually worldTime)
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
	
	/**
	 * Return Vec3D with biome specific fog color
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		int i = 10518688;
		float f = MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		float f1 = 0.627451F;
		float f2 = 0.5019608F;
		float f3 = 0.627451F;
		f1 = f1 * (f * 0.0F + 0.15F);
		f2 = f2 * (f * 0.0F + 0.15F);
		f3 = f3 * (f * 0.0F + 0.15F);
		return new Vec3d(f1, f2, f3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored()
	{
		return false;
	}
	
	/**
	 * True if the player can respawn in this dimension (true = overworld, false
	 * = nether).
	 */
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the
	 * Nether or End dimensions.
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
	 * Will check if the x, z position specified is alright to be set as the map
	 * spawn point
	 */
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return this.world.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
	}
	
	@Override
	public BlockPos getSpawnCoordinate()
	{
		return new BlockPos(100, 50, 0);
	}
	
	@Override
	public int getAverageGroundLevel()
	{
		return 50;
	}
	
	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return FarGen.FAR_END;
	}
	
	/**
	 * Called when the world is performing a save. Only used to save the state
	 * of the Dragon Boss fight in WorldProviderEnd in Vanilla.
	 */
	@Override
	public void onWorldSave()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		
		if (this.dragonFightManager != null)
		{
			nbttagcompound.setTag("DragonFight", this.dragonFightManager.getCompound());
		}
		
		this.world.getWorldInfo().setDimensionData(FarGen.FAR_END, nbttagcompound);
	}
	
	/**
	 * Called when the world is updating entities. Only used in WorldProviderEnd
	 * to update the DragonFightManager in Vanilla.
	 */
	@Override
	public void onWorldUpdateEntities()
	{
		if (this.dragonFightManager != null)
		{
			this.dragonFightManager.tick();
		}
	}
	
	@Nullable
	public DragonFightManager getDragonFightManager()
	{
		return this.dragonFightManager;
	}
}
