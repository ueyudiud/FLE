/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid;

import nebula.client.NebulaTextureHandler;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class SolidTextureLoader implements IIconLoader
{
	public static void init()
	{
		NebulaTextureHandler.addIconLoader(new SolidTextureLoader());
	}
	
	private SolidTextureLoader()
	{
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		for (SolidAbstract solid : SolidAbstract.REGISTRY)
		{
			solid.registerIcon(register);
		}
	}
}
