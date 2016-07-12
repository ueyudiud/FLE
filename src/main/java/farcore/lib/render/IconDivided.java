package farcore.lib.render;

import farcore.util.U;
import net.minecraft.util.IIcon;

/**
 * Use for classical texture map.
 * @author ueyudiud
 *
 */
public class IconDivided implements IIcon
{
	private static final int textureSize = 16;
	
	IIcon parent;
	int x;
	int y;
	
	public IconDivided(IIcon icon, int x, int y)
	{
		this.parent = icon;
		this.x = x;
		this.y = y;
	}

	@Override
	public int getIconWidth()
	{
		return parent.getIconWidth() / textureSize;
	}

	@Override
	public int getIconHeight()
	{
		return parent.getIconHeight() / textureSize;
	}

	@Override
	public float getMinU()
	{
		return parent.getInterpolatedU(x);
	}

	@Override
	public float getMaxU()
	{
		return parent.getInterpolatedU(x + 1);
	}

	@Override
	public float getInterpolatedU(double u)
	{
		return getMinU() + (float) u * (getMaxU() - getMinU()) / (float) textureSize;
	}

	@Override
	public float getMinV()
	{
		return parent.getInterpolatedV(y);
	}

	@Override
	public float getMaxV()
	{
		return parent.getInterpolatedV(y + 1);
	}

	@Override
	public float getInterpolatedV(double v)
	{
		return getMinV() + (float) v * (getMaxV() - getMinV()) / (float) textureSize;
	}

	@Override
	public String getIconName()
	{
		return parent.getIconName();
	}
}