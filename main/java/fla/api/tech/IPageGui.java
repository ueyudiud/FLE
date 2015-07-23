package fla.api.tech;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public interface IPageGui 
{
	public void bindTexture(ResourceLocation locate);
	
	public void drawTexturedRect(int x, int y, int u, int v, int xSize, int ySize);
	
	public void drawString(int x, int y, int color, String str);
	
	public RenderItem getItemRender();

	public void drawTexturedModelRectFromIcon(int x, int y, IIcon iicon, int u,
			int v);
}
