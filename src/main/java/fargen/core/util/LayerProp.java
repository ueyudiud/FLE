package fargen.core.util;

import java.util.Arrays;

import farcore.data.EnumTerrain;
import fargen.core.layer.Layer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerProp
{
	public Layer terrainLayer;
	public Layer heightLayer;
	public Layer biomeLayer1;
	public GenLayer biomeLayer2;

	public EnumTerrain[] terrain(EnumTerrain[] list, int x, int y, int w, int h)
	{
		if(list == null || list.length < w * h)
		{
			list = new EnumTerrain[w * h];
		}
		IntCache.resetIntCache();
		int[] array = terrainLayer.getInts(x, y, w, h);
		try
		{
			for(int i = 0; i < w * h; ++i)
			{
				list[i] = EnumTerrain.values()[array[i]];
			}
		}
		catch(Exception exception)
		{
			CrashReport report = new CrashReport("Fail to get terrain.", exception);
			CrashReportCategory category = report.getCategory();
			category.addCrashSection("LayerIndexs", Arrays.toString(array));
			category.addCrashSection("LayerWarpper", terrainLayer);
			throw new ReportedException(report);
		}
		return list;
	}

	public void markZoom()
	{
	}
}