package farcore.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;

import javax.imageio.ImageIO;

import farcore.data.MC;
import farcore.lib.material.Mat;
import farcore.lib.util.Log;
import farcore.lib.util.SubTag;

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
		MC.init();
		DebugMaterial.init();
		String sourceLocate = "D:/Program Files/minecraft/f/forge-1.10.2-12.18.1.2011-mdk/src/main/resources/assets";
		//		String srcDirName = "";
		//		String destDirName = "";
		//		String formatName = "chiseled.png";
		//		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		for(Mat material : Mat.filt(SubTag.ROCK))
		{
			ModelFileCreator.provideRockInfo(sourceLocate, material);
			ModelFileCreator.provideRockSlabInfo(sourceLocate, material);
		}
		//				ModelFileCreator.provideGroupItemInfo(sourceLocate, MC.lo);
		//		TextureFormater formater = TextureFormater.INSTANCE;
		//		try
		//		{
		//			File fileA = new File("D:/Program Files/minecraft/f/textures/ores/instances/Ore_a3");
		//			File file1 = new File("D:/Program Files/minecraft/f/textures/ores/Ore_a3.png");
		//			Map<Integer, Integer> map = formater.loadPositionSource(file1);
		//			File file2 = new File("D:/Program Files/minecraft/f/textures/ores/Ore_a1.png");
		//			formater.loadPositionTarget(file2, map);
		//			File fileB = new File("D:/Program Files/minecraft/f/textures/ores/instances/Ore_a1");
		//			if(!fileB.exists())
		//			{
		//				fileB.mkdirs();
		//			}
		//			for(File file3 : fileA.listFiles(filter))
		//			{
		//				String name = file3.getName();
		//				formater.copyColor(new File(fileB, name), file3);
		//			}
		//			file2 = new File("D:/Program Files/minecraft/f/textures/ores/Ore_a2.png");
		//			fileB = new File("D:/Program Files/minecraft/f/textures/ores/instances/Ore_a2");
		//			if(!fileB.exists())
		//			{
		//				fileB.mkdirs();
		//			}
		//			formater.loadPositionTarget(file2, map);
		//			for(File file3 : fileA.listFiles(filter))
		//			{
		//				String name = file3.getName();
		//				formater.copyColor(new File(fileB, name), file3);
		//			}
		//			file2 = new File("D:/Program Files/minecraft/f/textures/ores/Ore_a4.png");
		//			fileB = new File("D:/Program Files/minecraft/f/textures/ores/instances/Ore_a4");
		//			if(!fileB.exists())
		//			{
		//				fileB.mkdirs();
		//			}
		//			formater.loadPositionTarget(file2, map);
		//			for(File file3 : fileA.listFiles(filter))
		//			{
		//				String name = file3.getName();
		//				formater.copyColor(new File(fileB, name), file3);
		//			}
		//			file2 = new File("D:/Program Files/minecraft/f/textures/ores/Ore_a5.png");
		//			fileB = new File("D:/Program Files/minecraft/f/textures/ores/instances/Ore_a5");
		//			if(!fileB.exists())
		//			{
		//				fileB.mkdirs();
		//			}
		//			formater.loadPositionTarget(file2, map);
		//			for(File file3 : fileA.listFiles(filter))
		//			{
		//				String name = file3.getName();
		//				formater.copyColor(new File(fileB, name), file3);
		//			}
		//		}
		//		catch(IOException exception)
		//		{
		//
		//		}
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