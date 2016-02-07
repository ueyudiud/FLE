package flapi.enums;

import farcore.util.IUnlocalized;

public enum EnumRockSize implements IUnlocalized
{
	small(0.125F, 0.0625F), medium(0.25F, 0.125F), large(0.5F, 0.25F);
	
	float w;
	float h;
	
	private EnumRockSize(float width, float height)
	{
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
}