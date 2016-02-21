package flapi.enums;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum EnumFLERock
{
	Rhyolite(-1, 1, 0),
	Obsidian(-1, 0, 0),
	Andesite(0, 1, 0),
	Basalt(1, 1, 0),
	
	Granite(-1, 1, 2),
	Granite_Pegmatite(-1, 0, 2),
	Diorite(0, 1, 2),
	Gabbro(1, 1, 2),
	Peridotite(2, 1, 2),
	
	Kimberlite(2, 1, 1), 
	Unknown;

	public static Block[] getRockBlock(double ph, double weather)
	{
		return new Block[]{getRock(ph, weather, 0).normal,
				getRock(ph, weather, 1).normal,
				getRock(ph, weather, 2).normal,
				getRock(ph, weather, 3).normal};
	}
	
	public static EnumFLERock getRock(double PH, double weather, int deepLevel)
	{
		switch(deepLevel)
		{
		case 0 : ;
		if(weather < -0.7)
		{
			if(PH > 0.6) return Obsidian;
			else if(PH > 0.0) return Andesite;
			else if(PH > -0.6) return Basalt;
			else return Peridotite;
		}
		else
		{
			if(PH > 0.6) return Rhyolite;
			else if(PH > 0.0) return Andesite;
			else if(PH > -0.6) return Basalt;
			else return Peridotite;
		}
		case 1 : ;
		if(weather < -0.7)
		{
			if(PH > 0.6) return Granite_Pegmatite;
			else if(PH > 0.0) return Diorite;
			else if(PH > -0.6) return Gabbro;
			else return Peridotite;
		}
		else
		{
			if(PH > 0.6) return Granite;
			else if(PH > 0.0) return Diorite;
			else if(PH > -0.6) return Gabbro;
			else return Kimberlite;
		}
		case 2 : ;
		case 3 : ;
		if(weather < -0.7)
		{
			if(PH > 0.6) return Granite_Pegmatite;
			else if(PH > 0.0) return Diorite;
			else if(PH > -0.6) return Gabbro;
			else return Peridotite;
		}
		else
		{
			if(PH > 0.6) return Granite;
			else if(PH > 0.0) return Diorite;
			else if(PH > -0.6) return Gabbro;
			else return Peridotite;
		}
		default : return Unknown;
		}
	}
	
	public Block normal = Blocks.stone;
	public Block cobble = Blocks.cobblestone;
	public Block brick = Blocks.brick_block;
	
	int ph;
	int wl;
	int dl;
	
	EnumFLERock()
	{
		this(0, -1, -1);
	}
	EnumFLERock(int PH, int weatheringLevel, int deepLevel)
	{
		ph = PH;
		wl = weatheringLevel;
		dl = deepLevel;
	}
	
	public int getPH()
	{
		return ph;
	}
	
	public int getWeatheringLevel()
	{
		return wl;
	}
	
	public int getDeepLevel()
	{
		return dl;
	}
}