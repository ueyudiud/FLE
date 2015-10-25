import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class DrawUtil
{	
	public static void main(String[] arg)
	{
		File file = new File("./src/main/resources/assets/fle/textures/items/resource");
		File r = new File(file, "plate_double");
		File t = new File(file, "plate_quadrupl");
		File rP = new File(file, "plate_double.png");
		File tP = new File(file, "plate_quadrupl.png");
		try 
		{
			BufferedImage imgR = ImageIO.read(rP);
			BufferedImage imgT = ImageIO.read(tP);
			loadModel(imgR, imgT);
			for(File f : r.listFiles())
			{
				try
				{
					//File file3 = new File(file1, f.getName() + ".png");
					BufferedImage img = ImageIO.read(f);
					//drawPlate(file2, f.getName().replaceAll(".png", ""), img);
					//drawIngot(file3, f.getName().replaceAll(".png", ""), img);
					drawModel(t, f.getName().replaceAll(".png", ""), img);
				}
				catch(Throwable e){e.printStackTrace();}
			}
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}

	private static void drawPlate(File file2, String e, BufferedImage img) throws IOException
	{
		BufferedImage img1 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img1.getGraphics();
		
		int col = img.getRGB(0, 6);
		drawRect(g, 9, 2, 2, 1, col);
		drawRect(g, 7, 3, 2, 1, col);
		drawRect(g, 5, 4, 2, 1, col);
		drawRect(g, 3, 5, 2, 1, col);
		drawRect(g, 2, 6, 1, 2, col);
		drawRect(g, 3, 8, 1, 2, col);
		drawRect(g, 4, 10, 1, 2, col);
		drawRect(g, 5, 12, 1, 2, col);
		col = img.getRGB(15, 6);
		drawRect(g, 11, 3, 1, 2, col);
		drawRect(g, 12, 5, 1, 2, col);
		drawRect(g, 13, 7, 1, 2, col);
		drawRect(g, 14, 9, 1, 2, col);
		drawRect(g, 12, 11, 2, 1, col);
		drawRect(g, 10, 12, 2, 1, col);
		drawRect(g, 8, 13, 2, 1, col);
		drawRect(g, 6, 14, 2, 1, col);
		col = img.getRGB(4, 5);
		drawPixcel(g, 3, 6, col);
		drawPixcel(g, 5, 5, col);
		drawPixcel(g, 7, 4, col);
		drawPixcel(g, 9, 3, col);
		col = img.getRGB(2, 6);
		drawPixcel(g, 8, 4, col);
		drawPixcel(g, 6, 5, col);
		drawPixcel(g, 4, 6, col);
		col = img.getRGB(4, 6);
		drawPixcel(g, 4, 7, col);
		drawPixcel(g, 5, 6, col);
		drawPixcel(g, 7, 5, col);
		drawPixcel(g, 9, 4, col);
		drawPixcel(g, 10, 3, col);
		col = img.getRGB(3, 7);
		drawPixcel(g, 10, 4, col);
		drawRect(g, 8, 5, 2, 1, col);
		drawRect(g, 6, 6, 2, 1, col);
		drawPixcel(g, 5, 7, col);
		drawPixcel(g, 5, 9, col);
		drawPixcel(g, 6, 11, col);
		col = img.getRGB(2, 8);
		drawPixcel(g, 10, 5, col);
		drawRect(g, 8, 6, 2, 1, col);
		drawRect(g, 6, 7, 2, 1, col);
		drawRect(g, 5, 8, 2, 1, col);
		drawPixcel(g, 7, 13, col);
		col = img.getRGB(3, 8);
		drawPixcel(g, 11, 6, col);
		drawPixcel(g, 10, 7, col);
		drawRect(g, 8, 8, 2, 1, col);
		drawPixcel(g, 8, 9, col);
		drawRect(g, 7, 9, 1, 3, col);
		drawPixcel(g, 4, 8, col);
		drawPixcel(g, 5, 10, col);
		drawPixcel(g, 6, 12, col);
		col = img.getRGB(4, 9);
		drawPixcel(g, 3, 7, col);
		drawPixcel(g, 4, 9, col);
		drawPixcel(g, 5, 11, col);
		drawPixcel(g, 6, 13, col);
		col = img.getRGB(6, 10);
		drawPixcel(g, 11, 7, col);
		drawPixcel(g, 10, 8, col);
		drawPixcel(g, 9, 9, col);
		drawPixcel(g, 8, 10, col);
		col = img.getRGB(8, 10);
		drawPixcel(g, 12, 7, col);
		drawPixcel(g, 11, 9, col);
		drawPixcel(g, 10, 10, col);
		drawPixcel(g, 9, 11, col);
		col = img.getRGB(7, 10);
		drawPixcel(g, 11, 8, col);
		drawPixcel(g, 10, 9, col);
		drawPixcel(g, 9, 10, col);
		drawPixcel(g, 8, 11, col);
		drawPixcel(g, 8, 12, col);
		col = img.getRGB(6, 11);
		drawPixcel(g, 12, 8, col);
		drawPixcel(g, 12, 9, col);
		drawPixcel(g, 13, 9, col);
		drawPixcel(g, 11, 10, col);
		col = img.getRGB(11, 10);
		drawPixcel(g, 12, 10, col);
		drawPixcel(g, 10, 11, col);
		drawPixcel(g, 9, 12, col);
		col = mixColor(img.getRGB(8, 11), img.getRGB(9, 11));
		drawPixcel(g, 11, 11, col);
		drawPixcel(g, 13, 10, col);
		col = mixColor(img.getRGB(7, 7), img.getRGB(8, 8));
		drawPixcel(g, 11, 5, col);
		drawPixcel(g, 10, 6, col);
		drawPixcel(g, 9, 7, col);
		drawPixcel(g, 8, 7, col);
		drawPixcel(g, 7, 8, col);
		drawPixcel(g, 6, 9, col);
		drawPixcel(g, 6, 10, col);
		drawPixcel(g, 7, 12, col);
		
		File file4 = new File(file2, e + ".png");
		if(!file4.canExecute())
		{
			file4.createNewFile();
		}
		ImageIO.write(img1, "png", file4);
	}
	private static void drawIngot(File file2, String e, BufferedImage img) throws IOException
	{
		BufferedImage img1 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img1.getGraphics();

		int col = img.getRGB(0, 6);
		drawPixcel(g, 10, 0, col);
		drawPixcel(g, 11, 0, col);
		drawPixcel(g, 7, 1, col);
		drawPixcel(g, 8, 1, col);
		drawPixcel(g, 9, 1, col);
		drawPixcel(g, 12, 1, col);
		drawPixcel(g, 4, 2, col);
		drawPixcel(g, 5, 2, col);
		drawPixcel(g, 6, 2, col);
		drawPixcel(g, 13, 2, col);
		drawPixcel(g, 1, 3, col);
		drawPixcel(g, 2, 3, col);
		drawPixcel(g, 3, 3, col);
		drawPixcel(g, 14, 3, col);
		drawRect(g, 0, 4, 1, 9, col);
		drawPixcel(g, 1, 13, col);
		drawPixcel(g, 2, 14, col);
		drawPixcel(g, 3, 15, col);
		col = img.getRGB(1, 6);
		drawPixcel(g, 1, 4, col);
		drawPixcel(g, 2, 5, col);
		col = img.getRGB(1, 7);
		drawPixcel(g, 1, 5, col);
		col = img.getRGB(2, 6);
		drawPixcel(g, 2, 4, col);
		drawPixcel(g, 3, 4, col);
		drawPixcel(g, 5, 3, col);
		col = img.getRGB(4, 5);
		drawPixcel(g, 4, 3, col);
		drawPixcel(g, 7, 2, col);
		drawPixcel(g, 10, 1, col);
		col = img.getRGB(11, 3);
		drawPixcel(g, 11, 1, col);
		col = img.getRGB(8, 4);
		drawPixcel(g, 4, 4, col);
		drawPixcel(g, 6, 3, col);
		drawPixcel(g, 8, 2, col);
		col = img.getRGB(3, 7);
		drawPixcel(g, 3, 5, col);
		drawPixcel(g, 5, 4, col);
		drawPixcel(g, 6, 4, col);
		drawPixcel(g, 7, 3, col);
		drawPixcel(g, 8, 3, col);
		drawPixcel(g, 9, 2, col);
		drawPixcel(g, 10, 2, col);
		col = img.getRGB(2, 8);
		drawPixcel(g, 11, 2, col);
		drawRect(g, 9, 3, 4, 1, col);
		drawRect(g, 7, 4, 4, 1, col);
		drawRect(g, 4, 5, 4, 1, col);
		drawPixcel(g, 4, 6, col);
		drawPixcel(g, 3, 7, col);
		drawPixcel(g, 2, 8, col);
		drawPixcel(g, 2, 8, col);
		drawPixcel(g, 1, 11, col);
		col = img.getRGB(12, 4);
		drawPixcel(g, 12, 2, col);
		drawPixcel(g, 13, 3, col);
		drawPixcel(g, 3, 8, col);
		drawPixcel(g, 3, 9, col);
		drawPixcel(g, 2, 11, col);
		drawPixcel(g, 3, 12, col);
		col = img.getRGB(5, 8);
		drawRect(g, 5, 6, 3, 1, col);
		drawRect(g, 8, 5, 3, 1, col);
		drawRect(g, 11, 4, 4, 1, col);
		col = img.getRGB(1, 8);
		drawPixcel(g, 1, 6, col);
		drawPixcel(g, 1, 7, col);
		drawPixcel(g, 2, 7, col);
		col = img.getRGB(1, 9);
		drawPixcel(g, 2, 6, col);
		drawPixcel(g, 1, 8, col);
		drawPixcel(g, 1, 10, col);
		col = img.getRGB(1, 10);
		drawPixcel(g, 1, 12, col);
		col = img.getRGB(3, 8);
		drawPixcel(g, 3, 6, col);
		drawPixcel(g, 5, 8, col);
		drawRect(g, 6, 7, 3, 1, col);
		drawRect(g, 9, 6, 3, 1, col);
		drawRect(g, 12, 5, 2, 1, col);
		col = img.getRGB(13, 7);
		drawPixcel(g, 5, 9, col);
		drawRect(g, 6, 8, 3, 1, col);
		drawRect(g, 9, 7, 2, 1, col);
		drawRect(g, 12, 6, 2, 1, col);
		col = img.getRGB(14, 7);
		drawPixcel(g, 6, 9, col);
		drawPixcel(g, 6, 11, col);
		drawPixcel(g, 9, 8, col);
		drawPixcel(g, 9, 10, col);
		drawPixcel(g, 11, 7, col);
		drawPixcel(g, 12, 7, col);
		drawPixcel(g, 12, 9, col);
		drawPixcel(g, 14, 5, col);
		drawPixcel(g, 14, 6, col);
		col = img.getRGB(4, 9);
		drawRect(g, 4, 7, 1, 3, col);
		drawRect(g, 4, 12, 1, 2, col);
		col = img.getRGB(4, 12);
		drawRect(g, 4, 10, 1, 2, col);
		drawPixcel(g, 4, 14, col);
		col = img.getRGB(15, 6);
		drawRect(g, 15, 4, 1, 8, col);
		drawRect(g, 12, 12, 3, 1, col);
		drawRect(g, 9, 13, 3, 1, col);
		drawRect(g, 6, 14, 3, 1, col);
		drawRect(g, 4, 15, 2, 1, col);
		col = img.getRGB(8, 10);
		drawPixcel(g, 5, 10, col);
		drawPixcel(g, 5, 12, col);
		drawPixcel(g, 7, 9, col);
		drawPixcel(g, 8, 9, col);
		drawPixcel(g, 7, 11, col);
		drawPixcel(g, 10, 8, col);
		drawPixcel(g, 11, 8, col);
		drawPixcel(g, 10, 10, col);
		drawPixcel(g, 13, 7, col);
		drawPixcel(g, 14, 7, col);
		drawPixcel(g, 13, 9, col);
		col = img.getRGB(6, 11);
		drawRect(g, 6, 12, 3, 1, col);
		drawRect(g, 8, 11, 3, 1, col);
		drawRect(g, 11, 10, 3, 1, col);
		drawPixcel(g, 14, 9, col);
		drawPixcel(g, 5, 13, col);
		col = img.getRGB(8, 11);
		drawRect(g, 11, 11, 3, 1, col);
		drawRect(g, 9, 12, 2, 1, col);
		drawRect(g, 6, 13, 3, 1, col);
		drawPixcel(g, 5, 14, col);
		drawPixcel(g, 14, 10, col);
		col = img.getRGB(11, 10);
		drawPixcel(g, 11, 12, col);
		col = img.getRGB(14, 9);
		drawPixcel(g, 14, 11, col);
		col = img.getRGB(2, 11);
		drawPixcel(g, 2, 13, col);
		col = img.getRGB(3, 12);
		drawPixcel(g, 3, 14, col);
		col = mixColor(img.getRGB(5, 7), img.getRGB(5, 9));
		drawPixcel(g, 5, 7, col);
		drawPixcel(g, 8, 6, col);
		drawPixcel(g, 11, 5, col);
		col = mixColor(img.getRGB(5, 10), img.getRGB(6, 10));
		drawPixcel(g, 2, 9, col);
		drawPixcel(g, 3, 10, col);
		drawPixcel(g, 2, 12, col);
		drawPixcel(g, 3, 13, col);
		col = mixColor(img.getRGB(5, 4), img.getRGB(5, 8));
		drawRect(g, 6, 10, 3, 1, col);
		drawRect(g, 9, 9, 3, 1, col);
		drawRect(g, 12, 8, 3, 1, col);
		drawPixcel(g, 5, 11, col);
		drawPixcel(g, 1, 9, col);
		drawPixcel(g, 2, 10, col);
		drawPixcel(g, 3, 11, col);
		
		File file4 = new File(file2, e + ".png");
		if(!file4.canExecute())
		{
			file4.createNewFile();
		}
		ImageIO.write(img1, "png", file4);
	}

	static Map<Integer, Integer> list1;
	static Map<Integer, List<Integer>> list2;
	
	private static void loadModel(BufferedImage resource, BufferedImage target) throws IOException
	{
		list1 = new HashMap();
		for(int i = 0; i < 16; ++i)
			for(int j = 0; j < 16; ++j)
			{
				int id = i * 16 + j;
				int col = resource.getRGB(j, i);
				if(!list1.containsKey(col))
				{
					list1.put(col, id);
				}
			}
		list2 = new HashMap();
		for(int i = 0; i < 16; ++i)
			for(int j = 0; j < 16; ++j)
			{
				int id = i * 16 + j;
				int col = target.getRGB(j, i);
				if(list1.containsKey(col))
				{
					int rID = list1.get(col);
					if(!list2.containsKey(rID))
					{
						list2.put(rID, new ArrayList());
					}
					list2.get(rID).add(id);
				}
				else throw new RuntimeException("Can't find color at position x: " + j + ", y: " + i);
			}
		list1 = null;
	}
	
	private static void drawModel(File file2, String e, BufferedImage img) throws IOException
	{
		BufferedImage img1 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img1.getGraphics();

		for(Entry<Integer, List<Integer>> entry : list2.entrySet())
		{
			int value = entry.getKey();
			int x = value & 0x0F;
			int y = (value & 0xF0) >> 4;
			int col = img.getRGB(x, y);
			g.setColor(new Color(col, true));
			for(int i : entry.getValue())
			{
				int u = i & 0x0F;
				int v = (i & 0xF0) >> 4;
			    g.drawRect(u, v, 0, 0);
			}
		}
		
		File file4 = new File(file2, e + ".png");
		if(!file4.canExecute())
		{
			file4.createNewFile();
		}
		ImageIO.write(img1, "png", file4);
	}
	
	private static void drawPixcel(Graphics g, int x, int y, int col)
	{
		drawRect(g, x, y, 1, 1, col);
	}
	
	private static void drawRect(Graphics g, int x, int y, int w, int h, int col)
	{
		g.setColor(new Color(col, true));
		g.drawRect(x, y, w - 1, h - 1);
	}
	
	private static int mixColor(int a, int b)
	{
		Color col1 = new Color(a);
		Color col2 = new Color(b);
		return new Color((col1.getRed() + col2.getRed()) / 2, (col1.getGreen() + col2.getGreen()) / 2, (col1.getBlue() + col2.getBlue()) / 2).getRGB();
	}
}