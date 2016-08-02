package farcore.lib.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FontRenderExtend extends FontRenderer
{
	private static final List<IFontMap> FONT_MAPS = new ArrayList();
	
	/**
	 * Put custom font map to font render.
	 * @param map
	 */
	public static void addFontMap(IFontMap map)
	{
		FONT_MAPS.add(map);
	}
	
	private static final String MC_CODE_LIST =
			"ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";
	private static final ResourceLocation LOCATION = new ResourceLocation("textures/font/ascii.png");

	public FontRenderExtend()
	{
		this(Minecraft.getMinecraft().gameSettings, LOCATION, Minecraft.getMinecraft().renderEngine, false);
	}
	public FontRenderExtend(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn,
			boolean unicode)
	{
		super(gameSettingsIn, location, textureManagerIn, unicode);
		char[] chrs = MC_CODE_LIST.toCharArray();
		for(int i = 0; i < chrs.length; charWidth[i] = Minecraft.getMinecraft().fontRendererObj.getCharWidth(chrs[i]), ++i)
		{
			;
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		readFontTexture();
	}
	
	private void readFontTexture()
	{
		IResource iresource = null;
		BufferedImage bufferedimage;

		for(IFontMap map : FONT_MAPS)
		{
			try
			{
				iresource = getResource(map.getSource());
				bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
			}
			catch (IOException ioexception)
			{
				throw new RuntimeException(ioexception);
			}
			finally
			{
				IOUtils.closeQuietly(iresource);
			}
			map.initalizeResource(bufferedimage);
		}
	}

	@Override
	public int getCharWidth(char character)
	{
		for(IFontMap map : FONT_MAPS)
			if(map.shouldRender(character))
				return map.characterWidth(character);
		return super.getCharWidth(character);
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic)
	{
		char chr = MC_CODE_LIST.charAt(ch);
		for(IFontMap map : FONT_MAPS)
			if(map.shouldRender(chr))
				return map.renderCharacter(chr, italic, this);
		return super.renderDefaultChar(ch, italic);
	}

	@Override
	protected float renderUnicodeChar(char ch, boolean italic)
	{
		if(!getUnicodeFlag())
		{
			for(IFontMap map : FONT_MAPS)
				if(map.shouldRender(ch))
					return map.renderCharacter(ch, italic, this);
		}
		return super.renderUnicodeChar(ch, italic);
	}

	@Override
	public void bindTexture(ResourceLocation location)
	{
		super.bindTexture(location);
	}

	public float[] getPosition()
	{
		return new float[]{posX, posY};
	}
}