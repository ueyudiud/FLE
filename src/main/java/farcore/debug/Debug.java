package farcore.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import farcore.lib.util.Log;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * This type is only use to debugging,
 * if here are some method or field dose not exist,
 * delete this package is allowance.
 * @author ueyudiud
 *
 */
@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		//		MC.init();
		//		DebugMaterial.init();
		//		String sourceLocate = "D:/Program Files/minecraft/f/forge-1.10.2-12.18.1.2011-mdk/src/main/resources/assets";
		//		//		String srcDirName = "";
		//		//		String destDirName = "";
		//		//		String formatName = "chiseled.png";
		//		//		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		//		ModelFileCreator.provideGroupItemInfo(sourceLocate, MC.fragment);
	}

	public static void checkAccess(GenLayer layer)
	{
		layer.initWorldGenSeed(382947292L);
		IntCache.resetIntCache();
		int[] i1 = layer.getInts(0, 0, 256, 256);
		int[] i2 = layer.getInts(1, 1, 256, 256);
		for(int i = 0; i < 255; ++i)
		{
			for(int j = 0; j < 255; ++j)
			{
				if(i1[(i + 1) * 256 + j + 1] != i2[i * 256 + j])
				{
					Log.info("Checking fail.");
				}
			}
		}
	}
	
	public static void drawImage(int size, double[] values, String name)
	{
		try
		{
			File outFile = new File(name + ".png");
			if (outFile.exists())
				return;
			BufferedImage outBitmap = new BufferedImage(size, size, 1);
			Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
			graphics.clearRect(0, 0, size, size);
			Log.info(name + ".png");
			for (int x = 0; x < size; x++)
			{
				for (int z = 0; z < size; z++)
				{
					int v = (int) (values[(x * size + z)] * 256);
					int c = v * 0x010101;
					graphics.setColor(new Color(c));
					graphics.drawRect(x, z, 1, 1);
				}
			}
			ImageIO.write(outBitmap, "png", outFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}