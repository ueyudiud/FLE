/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import org.lwjgl.opengl.GL11;

import nebula.client.EntityProjectileItemRenderEvent;
import nebula.common.entity.EntityProjectileItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectileItem extends Render<EntityProjectileItem>
{
	@SideOnly(Side.CLIENT)
	public static enum Factory implements IRenderFactory<EntityProjectileItem>
	{
		instance;
		
		@Override
		public Render<EntityProjectileItem> createRenderFor(RenderManager manager)
		{
			return new RenderProjectileItem(manager);
		}
	}
	
	private Minecraft minecraft = Minecraft.getMinecraft();
	private RenderItem render;
	
	protected RenderProjectileItem(RenderManager renderManager)
	{
		super(renderManager);
		this.render = this.minecraft.getRenderItem();
	}
	
	@Override
	public void doRender(EntityProjectileItem entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		ItemStack stack = entity.currentItem;
		if(stack != null)
		{
			bindEntityTexture(entity);
			
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glScalef(.5F, .5F, .5F);
			GlStateManager.disableLighting();
			
			EntityProjectileItemRenderEvent event = new EntityProjectileItemRenderEvent(entity, this.renderManager, partialTicks);
			
			MinecraftForge.EVENT_BUS.post(event);
			
			if(event.getResult() == Result.DEFAULT)
			{
				//The rotation use only in item state, the custom render should initialized rotation.
				GL11.glRotatef(event.yaw(), 0, 1, 0);
				EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack.copy());
				item.getEntityItem().stackSize = 1;
				item.hoverStart = 1.0F;
				
				if (!this.render.shouldRenderItemIn3D(stack))
				{
					GL11.glRotatef(180F, 0, 1, 0);
				}
				
				GlStateManager.pushAttrib();
				RenderHelper.enableStandardItemLighting();
				this.render.renderItem(stack, TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popAttrib();
			}
			
			GlStateManager.enableLighting();
			GL11.glPopMatrix();
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityProjectileItem entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}