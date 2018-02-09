/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.solid.SolidStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import nebula.client.util.Client;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * @author ueyudiud
 */
public class SolidRender implements IIngredientRenderer<SolidStack>
{
	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, SolidStack stack)
	{
		TextureAtlasSprite solidIcon = stack.getSolid().getIcon(stack);
		if (solidIcon != null)
		{
			minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			int color = stack.getSolid().getColor(stack);
			Renders.drawTexturedModalRect(xPosition, yPosition, solidIcon, 16, 16, 0.0F,
					(color >>> 16 & 0xFF) / 255.0F,
					(color >>>  8 & 0xFF) / 255.0F,
					(color        & 0xFF) / 255.0F,
					(color >>> 24 & 0xFF) / 255.0F);
		}
	}
	
	@Override
	public List<String> getTooltip(Minecraft minecraft, SolidStack stack)
	{
		List<String> list = new ArrayList<>();
		list.add(EnumChatFormatting.WHITE + stack.getSolid().getLocalizedName());
		list.add(LanguageManager.translateToLocal("jei.compat.solid.amount", stack.amount));
		return list;
	}
	
	@Override
	public FontRenderer getFontRenderer(Minecraft minecraft, SolidStack stack)
	{
		return Client.getFontRender();
	}
}
