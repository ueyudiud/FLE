package fle.core.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fle.api.FleValue;

public class RenderFleArrow extends RenderArrow
{
	private final ResourceLocation texture;
	
	public RenderFleArrow(String aTextureName)
	{
		texture = new ResourceLocation(FleValue.TEXTURE_FILE + ":textures/entity/" + aTextureName + ".png");
	}
	  
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return texture;
	}
}