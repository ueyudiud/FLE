package farcore.lib.tesr;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICoordableBrightnessProvider
{
	@SideOnly(Side.CLIENT)
	int getBrightness(int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	float getAmbientOcclusionLightValue(int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	float getOpaqueness(int x, int y, int z);
}