package farcore.event;

import farcore.lib.entity.EntityProjectileItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEvent extends Event
{
	public final Minecraft mc = Minecraft.getMinecraft();
	
	@SideOnly(Side.CLIENT)
	public static class RenderEvent extends ClientEvent
	{
		protected RenderManager manager;
		private Tessellator tessellator = Tessellator.getInstance();
		
		public void bindTexture(ResourceLocation location)
		{
			manager.renderEngine.bindTexture(location);
		}
		
		public VertexBuffer vertexBuffer()
		{
			return tessellator.getBuffer();
		}

		public void draw()
		{
			tessellator.draw();
		}
	}
	
	@HasResult
	@SideOnly(Side.CLIENT)
	public static class EntityProjectileItemRenderEvent extends RenderEvent
	{
		private float partialTicks;
		private EntityProjectileItem projectileItem;

		public EntityProjectileItemRenderEvent(EntityProjectileItem item, RenderManager manager, float partialTicks)
		{
			this.partialTicks = partialTicks;
			projectileItem = item;
			this.manager = manager;
		}
		
		public EntityProjectileItem getProjectileItem()
		{
			return projectileItem;
		}
		
		public ItemStack getStack()
		{
			return projectileItem.currentItem;
		}
		
		public float yaw()
		{
			return projectileItem.prevRotationYaw + (projectileItem.rotationYaw - projectileItem.prevRotationYaw) * partialTicks;
		}
		
		public float pitch()
		{
			return projectileItem.prevRotationPitch + (projectileItem.rotationPitch - projectileItem.prevRotationPitch) * partialTicks;
		}
	}
}