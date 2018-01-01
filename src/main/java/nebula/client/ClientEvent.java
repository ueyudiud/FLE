/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * For some custom render.<br>
 * Posted in {@link MinecraftForge#EVENT_BUS}
 * 
 * @author ueyudiud
 */
public class ClientEvent extends Event
{
	public final Minecraft mc = Minecraft.getMinecraft();
	
	public static class RenderEvent extends ClientEvent
	{
		protected RenderManager	manager;
		private Tessellator		tessellator	= Tessellator.getInstance();
		
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
}
