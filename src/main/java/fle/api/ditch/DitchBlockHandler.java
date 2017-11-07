/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.ditch;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import farcore.lib.material.Mat;
import nebula.Log;
import nebula.Nebula;
import nebula.base.Judgable;

/**
 * @author ueyudiud
 */
public final class DitchBlockHandler implements Judgable<Mat>
{
	public static final List<Mat> DITCH_ALLOWED_MATERIALS = new ArrayList();
	
	public static final DitchBlockHandler HANDLER = new DitchBlockHandler();
	
	private static final List<DitchFactory>	FACTORIES	= new ArrayList();
	public static DitchFactory				rawFactory;
	
	public static void addMaterial(Mat material)
	{
		DITCH_ALLOWED_MATERIALS.add(material);
	}
	
	public static void addFactory(DitchFactory factory)
	{
		FACTORIES.add(factory);
	}
	
	public static DitchFactory getFactory(@Nullable Mat material)
	{
		if (material == null) return rawFactory;
		DitchFactory select = null;
		for (DitchFactory factory : FACTORIES)
		{
			if (factory.access(material))
			{
				if (select != null)
				{
					RuntimeException exception = new RuntimeException("The factory " + select + " and " + factory + " both want to handle material " + material.name + ".");
					if (Nebula.debug) throw exception;
					Log.warn("Catching an exception during getting ditch factory.", exception);
				}
				select = factory;
			}
		}
		if (rawFactory.access(material))
		{
			select = rawFactory;
		}
		return select == null ? null : select;
	}
	
	DitchBlockHandler()
	{
	}
	
	@Override
	public boolean isTrue(Mat target)
	{
		return DITCH_ALLOWED_MATERIALS.contains(target);
	}
}
