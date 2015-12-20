package fle.core.block;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import flapi.util.FleLog;
import fle.core.util.Util;

public class BlockMaterial extends Material
{
	private static final Method setTranslucent;
	
	static
	{
		setTranslucent = Util.getMethod(Material.class, Arrays.asList("setTranslucent", "func_76223_p"));
		if(setTranslucent != null)
		{
			setTranslucent.setAccessible(true);
		}
	}
	
	public BlockMaterial(MapColor col)
	{
		super(col);
	}
	
	protected BlockMaterial setTranslucent()
	{
		try
		{
			setTranslucent.invoke(this, new Object[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		return this;
	}
}