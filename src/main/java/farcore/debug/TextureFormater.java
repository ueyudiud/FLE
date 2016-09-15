package farcore.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TextureFormater
{
	public static final TextureFormater INSTANCE = new TextureFormater();
	private static final int EMPTY_COLOR = 0xFFFFFFFF;

	private short[] targetCoords = new short[256];
	
	private TextureFormater(){	}

	public Map<Integer, Integer> loadPositionSource(File file) throws IOException
	{
		BufferedImage image = ImageIO.read(file);
		if(image.getHeight() != 16 || image.getWidth() != 16)
			throw new RuntimeException();
		Map<Integer, Integer> map = new HashMap();
		for(int j = 0; j < 16; ++j)
		{
			for(int i = 0; i < 16; ++i)
			{
				int rgb = image.getRGB(i, j);
				if(rgb == EMPTY_COLOR || map.containsKey(rgb))
				{
					continue;
				}
				int coord = j << 4 | i;
				map.put(rgb, coord);
			}
		}
		return map;
	}
	
	public void loadPositionTarget(File file, Map<Integer, Integer> map) throws IOException
	{
		BufferedImage image = ImageIO.read(file);
		if(image.getHeight() != 16 || image.getWidth() != 16)
			throw new RuntimeException();
		Arrays.fill(targetCoords, (short) -1);
		for(int j = 0; j < 16; ++j)
		{
			for(int i = 0; i < 16; ++i)
			{
				int rgb = image.getRGB(i, j);
				if((rgb >> 24 & 0xFF) == 0)
				{
					continue;
				}
				if(rgb != EMPTY_COLOR && map.containsKey(rgb))
				{
					short coord = map.get(rgb).shortValue();
					targetCoords[j << 4 | i] = coord;
				}
			}
		}
	}

	public void copyColor(File target, File source) throws IOException
	{
		copyColor(target, ImageIO.read(source));
	}
	
	public void copyColor(File target, BufferedImage source) throws IOException
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		copyColor(image, source);
		ImageIO.write(image, "png", target);
	}

	public void copyColor(BufferedImage target, BufferedImage source)
	{
		int c = 0;
		Graphics graphics = target.getGraphics();
		graphics.setColor(new Color(0xFF, 0xFF, 0xFF, 0));
		graphics.drawRect(0, 0, 16, 16);
		for(int j = 0; j < 16; ++j)
		{
			for(int i = 0; i < 16; ++i)
			{
				int coord = targetCoords[j << 4 | i];
				if(coord != -1)
				{
					int rgb = source.getRGB(coord & 0xF, coord >> 4);
					Color color = new Color(rgb);
					graphics.setColor(color);
					graphics.drawRect(i, j, 0, 0);
				}
			}
		}
	}
}