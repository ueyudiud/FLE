/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.world;

import nebula.client.render.ICoordableBrightnessProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockCoordQuarterProperties extends ICoordableBrightnessProvider, ICoord
{
	default boolean canSeeSky(int x, int z)
	{
		if (x >= 0 && x < 4 && z >= 0 && z < 4)
			return canSeeSkyLocal(x, z);
		int X = x >> 2;
		int Z = z >> 2;
		TileEntity tile = getTE(X, 0, Z);
		if (tile instanceof IBlockCoordQuarterProperties)
			return ((IBlockCoordQuarterProperties) tile).canSeeSkyLocal(x & 0x3, z & 0x3);
		return world().canSeeSky(pos().add(X, 0, Z));
	}
	
	boolean canSeeSkyLocal(int x, int z);
	
	@Override
	@SideOnly(Side.CLIENT)
	default float getAmbientOcclusionLightValue(int x, int y, int z)
	{
		if (x >= 0 && x < 4 && y >= 0 && y < 4 && z >= 0 && z < 4)
			return getAmbientOcclusionLightValueLocal(x, y, z);
		int X = x >> 2;
		int Y = y >> 2;
		int Z = z >> 2;
		TileEntity tile = getTE(X, Y, Z);
		if (tile instanceof IBlockCoordQuarterProperties)
			return ((IBlockCoordQuarterProperties) tile).getAmbientOcclusionLightValueLocal(x & 0x3, y & 0x3, z & 0x3);
		return getBlockState(X, Y, Z).getAmbientOcclusionLightValue();
	}
	
	@SideOnly(Side.CLIENT)
	float getAmbientOcclusionLightValueLocal(int x, int y, int z);
	
	@Override
	@SideOnly(Side.CLIENT)
	default int getBrightness(int x, int y, int z)
	{
		if (x >= 0 && x < 4 && y >= 0 && y < 4 && z >= 0 && z < 4)
			return getBrightnessLocal(x, y, z);
		int X = x >> 2;
		int Y = y >> 2;
		int Z = z >> 2;
		BlockPos pos = pos().add(X, Y, Z);
		TileEntity tile = world().getTileEntity(pos);
		if (tile instanceof IBlockCoordQuarterProperties)
			return ((IBlockCoordQuarterProperties) tile).getBrightnessLocal(x & 0x3, y & 0x3, z & 0x3);
		return world().getCombinedLight(pos, world().getBlockState(pos).getLightValue(world(), pos));
	}
	
	@SideOnly(Side.CLIENT)
	int getBrightnessLocal(int x, int y, int z);
	
	@Override
	@SideOnly(Side.CLIENT)
	default float getOpaqueness(int x, int y, int z)
	{
		if (x >= 0 && x < 4 && y >= 0 && y < 4 && z >= 0 && z < 4)
			return getOpaquenessLocal(x, y, z);
		int X = x >> 2;
		int Y = y >> 2;
		int Z = z >> 2;
		TileEntity tile = getTE(X, Y, Z);
		if (tile instanceof IBlockCoordQuarterProperties)
			return ((IBlockCoordQuarterProperties) tile).getOpaquenessLocal(x & 0x3, y & 0x3, z & 0x3);
		return getBlockLightOpacity(X, Y, Z) / 255F;
	}
	
	@SideOnly(Side.CLIENT)
	float getOpaquenessLocal(int x, int y, int z);
}
