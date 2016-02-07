package fle.core.enums;

import farcore.util.ColorMap;
import farcore.util.IColorMapHandler;
import farcore.util.IColorMapProvider;
import farcore.util.IUnlocalized;
import flapi.util.Values;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.biome.BiomeGenBase;
import scala.inline;

public enum EnumDirtState implements IUnlocalized, IColorMapProvider
{
	dirt, grass, mycelium, moss, farmland;
	
	private ColorMap map;
	
	public String getUnlocalized()
	{
		return "state." + name();
	}

	@Override
	public void registerColorMap(IColorMapHandler handler)
	{
		map = handler.registerColorMap(Values.TEXTURE_FILE + ":textures/colormap/dirtcover/" + name());
	}
	
	public int getTopColor(IBlockAccess world, int x, int y, int z)
	{
		if(map == null) return 0xFFFFFF;
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		float temp = 
				MathHelper.clamp_float(biome.getFloatTemperature(x, y, z), 0.0F, 1.0F);
		float rain =
				MathHelper.clamp_float(biome.getFloatRainfall(), 0.0F, 1.0F) * temp;
		return map.getColorFromCrood(
				(int) (255 - temp * 255), 
				(int) (255 - rain * 255));
	}

	public int getDefaultColor()
	{
		return map.getColorFromCrood(127, 127);
	}
}