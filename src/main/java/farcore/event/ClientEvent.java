package farcore.event;

import farcore.lib.entity.EntityProjectileItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For some custom render.<br>
 * Posted in {@link MinecraftForge#EVENT_BUS}
 * @author ueyudiud
 */
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
			this.manager.renderEngine.bindTexture(location);
		}
		
		public VertexBuffer vertexBuffer()
		{
			return this.tessellator.getBuffer();
		}
		
		public void draw()
		{
			this.tessellator.draw();
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
			this.projectileItem = item;
			this.manager = manager;
		}
		
		public EntityProjectileItem getProjectileItem()
		{
			return this.projectileItem;
		}
		
		public ItemStack getStack()
		{
			return this.projectileItem.currentItem;
		}
		
		public float yaw()
		{
			return this.projectileItem.prevRotationYaw + (this.projectileItem.rotationYaw - this.projectileItem.prevRotationYaw) * this.partialTicks;
		}
		
		public float pitch()
		{
			return this.projectileItem.prevRotationPitch + (this.projectileItem.rotationPitch - this.projectileItem.prevRotationPitch) * this.partialTicks;
		}
	}
}