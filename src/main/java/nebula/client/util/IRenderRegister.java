package nebula.client.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Called by block or item, called during pre-initialized. Use
 * <tt>FarCoreSetup.ClientProxy.registerRenderObject()</tt>
 * 
 * @author ueyudiud
 * @see farcore.FarCoreSetup.ClientProxy
 */
public interface IRenderRegister
{
	/**
	 * Register rendering object.
	 */
	@SideOnly(Side.CLIENT)
	void registerRender();
}
