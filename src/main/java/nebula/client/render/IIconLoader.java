/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The custom icon loader.<p>
 * Minecraft removed icon loading method in Block and
 * Item after 1.7.10, but there is still some places
 * needed register icon, such as fluid icon, custom
 * icon in TESR, etc.<p>
 * Register loader instance at {@link nebula.client.NebulaTextureHandler#addIconLoader(IIconLoader)}
 * and the method {@link #registerIcon(IIconRegister)}
 * will be called when resource reloading.
 * @author ueyudiud
 * @see nebula.client.NebulaTextureHandler
 */
public interface IIconLoader
{
	/**
	 * Called when resource reloading.<p>
	 * Use to register icon.
	 * @param register the icon register.
	 * @see nebula.client.render.IIconRegister
	 */
	@SideOnly(Side.CLIENT)
	void registerIcon(IIconRegister register);
}