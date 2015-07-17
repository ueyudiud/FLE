package fla.api.tech;

import net.minecraft.util.ResourceLocation;

public interface IPageGui 
{
	public void bindTexture(ResourceLocation locate);
	
	public void drawTexturedRect(int x, int y, int u, int v, int xSize, int ySize);
}
