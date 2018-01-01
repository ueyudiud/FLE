/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * I don't know why ModelResourceLocation use this such a xxx
 * constructor and let the basic one has the protected modifier.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelLocation extends ModelResourceLocation
{
	public ModelLocation(String dormain, String path, String variant)
	{
		super(0, dormain, path, variant);
	}
	
	public ModelLocation(String location, String variant)
	{
		this(new ResourceLocation(location), variant);
	}
	
	public ModelLocation(String path)
	{
		super(path);
	}
	
	public ModelLocation(ResourceLocation location, String variant)
	{
		this(location.getResourceDomain(), location.getResourcePath(), variant);
	}
}
