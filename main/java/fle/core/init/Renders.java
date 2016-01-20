package fle.core.init;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import farcore.entity.EntityFleFallingBlock;
import fle.core.render.RenderFallingBlock;
import fle.core.render.model.RenderLoaderFleFluid;

@SideOnly(Side.CLIENT)
public class Renders
{
	public static void preinit()
	{
		ModelLoaderRegistry.registerLoader(new RenderLoaderFleFluid());
	}
	
	public static void init()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityFleFallingBlock.class, new RenderFallingBlock(Minecraft.getMinecraft().getRenderManager()));
	}
}