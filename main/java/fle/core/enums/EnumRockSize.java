package fle.core.enums;

import farcore.item.enums.EnumParticleSize;
import farcore.util.IUnlocalized;
import farcore.util.Part;
import scala.collection.generic.BitOperations.Int;

public enum EnumRockSize implements IUnlocalized
{
	small(144, 0.1875F, 0.0625F), 
	medium(216, 0.25F, 0.125F), 
	large(288, 0.375F, 0.25F);
	
	static
	{
		small.damage = 0.8F;
		medium.damage = 1.1F;
		large.damage = 1.7F;
	}
	
	int size;
	float w;
	float h;
	double damage;
	private Part part;
	
	private EnumRockSize(int size, float width, float height)
	{
		this.size = size;
		w = width;
		h = height;
	}
	
	public float getWidth()
	{
		return w;
	}
	
	public float getHeiht()
	{
		return h;
	}
	
	public String getUnlocalized()
	{
		return "rock.size." + name();
	}

	public Part part()
	{
		if(part == null)
		{
			part = Part.part("chip.stone." + name(), 
					this == EnumRockSize.small ? EnumParticleSize.nuggetic : EnumParticleSize.ingotic, size);
			part.setBasePart(Part.ingot);
		}
		return part;
	}

	public double getDamage()
	{
		return damage;
	}
}