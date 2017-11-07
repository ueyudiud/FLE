/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common.gui.container;

import java.util.HashMap;
import java.util.Map;

import fle.core.FLE;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public final class EnumSlotsSize
{
	private static class Key
	{
		short	x;
		short	y;
		
		Key(int x, int y)
		{
			this.x = (short) x;
			this.y = (short) y;
		}
		
		@Override
		public int hashCode()
		{
			return this.x << 16 | this.y;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this || ((obj instanceof Key) && ((Key) obj).x == this.x && ((Key) obj).y == this.y);
		}
	}
	
	private static final Map<Key, EnumSlotsSize> MAP = new HashMap<>(16, 1.0F);
	
	public static final EnumSlotsSize _2x2 = new EnumSlotsSize(2, 2, 71, 26), _3x3 = new EnumSlotsSize(3, 3, 62, 17), _5x3 = new EnumSlotsSize(5, 3, 44, 17);
	
	public static EnumSlotsSize getSize(int x, int y)
	{
		return MAP.get(new Key(x, y));
	}
	
	public final int				x;
	public final int				y;
	public final int				row;
	public final int				column;
	public final int				size;
	public final ResourceLocation	location;
	
	public EnumSlotsSize(int u, int v, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.row = u;
		this.column = v;
		this.size = u * v;
		this.location = new ResourceLocation(FLE.MODID, "textures/gui/container/" + u + "x" + v + ".png");
		MAP.put(new Key(u, v), this);
	}
}
