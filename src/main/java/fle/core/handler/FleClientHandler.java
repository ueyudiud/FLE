package fle.core.handler;

import org.lwjgl.opengl.GL11;

import farcore.event.ClientEvent;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import farcore.lib.render.RenderHelper;
import fle.core.FLE;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FleClientHandler
{
	private static final ResourceLocation LOCATION_SPEAR_ROCKY = new ResourceLocation(FLE.MODID, "textures/entity/spear.rocky.png");
	
	@SubscribeEvent
	public void onProjectileItemRender(ClientEvent.EntityProjectileItemRenderEvent event)
	{
		Item item = event.getStack().getItem();
		if(item instanceof ItemTool)
		{
			int meta = ((ItemTool) item).getBaseDamage(event.getStack());
			if(meta == 6)
			{
				renderSpear(event, meta);
				event.setResult(Result.ALLOW);
			}
		}
	}

	private void renderSpear(ClientEvent.EntityProjectileItemRenderEvent event, int meta)
	{
		final float size = 1F / 16F;
		final float pixel = 1F / 32F;
		ResourceLocation location;
		switch (meta)
		{
		case 6 : location = LOCATION_SPEAR_ROCKY; break;
		default: location = TextureMap.LOCATION_MISSING_TEXTURE; break;
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		
		float pitch = event.pitch() + 90F;
		float yaw = event.yaw() - 90.0F;
		GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(pitch, 0.0F, 0.0F, 1.0F);
		GL11.glScalef(size, size, size);
		event.bindTexture(location);
		RenderHelper.instance.face(
				-1, 0, -1, 1 * pixel, 6 * pixel,
				+1, 0, -1, 3 * pixel, 6 * pixel,
				+1, 0, +1, 3 * pixel, 8 * pixel,
				-1, 0, +1, 1 * pixel, 8 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(
				-1, 24, -1, 1 * pixel, 6 * pixel,
				-1, 24, +1, 1 * pixel, 8 * pixel,
				+1, 24, +1, 3 * pixel, 8 * pixel,
				+1, 24, -1, 3 * pixel, 6 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(
				-1, 0 , -1, 0 * pixel, 1 * pixel,
				-1, 24, -1, 24 * pixel, 1 * pixel,
				+1, 24, -1, 24 * pixel, 3 * pixel,
				+1, 0 , -1, 0 * pixel, 3 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(
				+1, 0 , +1, 0 * pixel, 1 * pixel,
				+1, 24, +1, 24 * pixel, 1 * pixel,
				-1, 24, +1, 24 * pixel, 3 * pixel,
				-1, 0 , +1, 0 * pixel, 3 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(
				-1, 0 , +1, 0 * pixel, 1 * pixel,
				-1, 24, +1, 24 * pixel, 1 * pixel,
				-1, 24, -1, 24 * pixel, 3 * pixel,
				-1, 0 , -1, 0 * pixel, 3 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(
				+1, 0 , -1, 0 * pixel, 1 * pixel,
				+1, 24, -1, 24 * pixel, 1 * pixel,
				+1, 24, +1, 24 * pixel, 3 * pixel,
				+1, 0 , +1, 0 * pixel, 3 * pixel);
		RenderHelper.instance.draw();
		Mat material = ((ItemTool) event.getStack().getItem()).getMaterial(event.getStack(), "head");
		RenderHelper.instance.face(material.RGBa,
				0,  0, -2, 24 * pixel, 0 * pixel,
				0, -4, -2, 28 * pixel, 0 * pixel,
				0, -4, +2, 28 * pixel, 4 * pixel,
				0,  0, +2, 24 * pixel, 4 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(material.RGBa,
				0,  0, +2, 24 * pixel, 4 * pixel,
				0, -4, +2, 28 * pixel, 4 * pixel,
				0, -4, -2, 28 * pixel, 0 * pixel,
				0,  0, -2, 24 * pixel, 0 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(material.RGBa,
				-2,  0, 0, 24 * pixel, 4 * pixel,
				-2, -4, 0, 28 * pixel, 4 * pixel,
				+2, -4, 0, 28 * pixel, 0 * pixel,
				+2,  0, 0, 24 * pixel, 0 * pixel);
		RenderHelper.instance.draw();
		RenderHelper.instance.face(material.RGBa,
				+2,  0, 0, 24 * pixel, 0 * pixel,
				+2, -4, 0, 28 * pixel, 0 * pixel,
				-2, -4, 0, 28 * pixel, 4 * pixel,
				-2,  0, 0, 24 * pixel, 4 * pixel);
		RenderHelper.instance.draw();

		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
}