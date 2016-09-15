package farcore.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;

import javax.imageio.ImageIO;

import farcore.lib.util.Log;

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
	private static final FileFilter filter = (File file) ->
	{
		return file.getName().endsWith(".png");
	};

	public static void main(String[] args)
	{
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