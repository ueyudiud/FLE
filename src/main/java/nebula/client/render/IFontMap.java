/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import java.awt.image.BufferedImage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Registered in
 * {@link nebula.client.render.FontRenderExtend#addFontMap(IFontMap)}.
 * <p>
 * Added custom font render.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IFontMap
{
	/**
	 * Provided source map to use.
	 * 
	 * @return
	 */
	ResourceLocation getSource();
	
	/**
	 * Make custom render.
	 * 
	 * @param chr
	 * @return
	 */
	boolean shouldRender(char chr);
	
	/**
	 * Initialize character property from image.
	 * <p>
	 * The image will be 256x256 empty image if source is missing.
	 * 
	 * @param image the image.
	 */
	void initalizeResource(BufferedImage image);
	
	/**
	 * Get the character width.
	 * 
	 * @param chr
	 * @return
	 */
	int characterWidth(char chr);
	
	/**
	 * Render the character to screen.
	 * 
	 * @param chr the character.
	 * @param italic is character render in italic mode.
	 * @param render the font render.
	 * @return the character width.
	 * @see #characterWidth(char)
	 */
	int renderCharacter(char chr, boolean italic, FontRenderExtend render);
}
