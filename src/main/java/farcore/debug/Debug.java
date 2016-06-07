package farcore.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import farcore.enums.EnumCharacter;
import farcore.lib.bio.DNA;
import farcore.lib.bio.DNACol;
import farcore.lib.bio.DNAHandler;
import farcore.lib.bio.DNAPart;
import farcore.lib.world.biome.BiomeBase;

@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		DNAHandler.map.put("debuging", 
				Arrays.asList(new DNACol('d', new DNAPart('s', EnumCharacter.Dominance), new DNAPart('m', EnumCharacter.Recessive))));
		DNA dna = new DNA();
		DNAPart part = DNAHandler.map.get("debuging").get(0).randomGet(new Random());
		dna.species = "debuging";
		dna.map.put('d', new DNAPart[]{part, part});
		String string = dna.encode();
		System.out.println(string);
	}
	
	public static void draw(String string, int w, int h, int[] range)
	{
		File file = new File("./" + string + ".png");
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int idx = 0;
		Graphics graphics = image.getGraphics();
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				graphics.setColor(new Color(BiomeBase.getBiome(range[i + j * w]).color));
				graphics.drawRect(i, j, 1, 1);
			}
		try
		{
			ImageIO.write(image, "png", file);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static double max(double[] value)
	{
		double i = value[0];
		for(double d : value)
		{
			if(d > i)
				i = d;
		}
		return i;
	}
	
	private static double min(double[] value)
	{
		double i = value[0];
		for(double d : value)
		{
			if(d < i)
				i = d;
		}
		return i;
	}
}