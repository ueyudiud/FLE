/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import java.awt.image.BufferedImage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Registered in font render.
 * @author ueyudiud
 *
 */
@SideOnly(Side.CLIENT)
public interface IFontMap
{
	/**
	 * Provided source map to use.
	 * @return
	 */
	ResourceLocation getSource();
	
	/**
	 * Make custom render.
	 * @param chr
	 * @return
	 */
	boolean shouldRender(char chr);
	
	void initalizeResource(BufferedImage stream);
	
	int characterWidth(char chr);
	
	/**
	 * Render the character.
	 * @param chr
	 * @return
	 */
	int renderCharacter(char chr, boolean italic, FontRenderExtend render);
}