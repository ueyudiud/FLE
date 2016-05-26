package farcore.handler;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.util.FarFoodStats;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class FarCoreGuiHandler
{
	private static final ResourceLocation override = new ResourceLocation("farcore", "textures/gui/override.png");
	private static final DecimalFormat formatK = new DecimalFormat("#0.0K");
	
	@SubscribeEvent
	public void onItemToolTip(ItemTooltipEvent event)
	{
		if(V.debug)
		{
			int[] ids = OreDictionary.getOreIDs(event.itemStack);
			if(ids.length > 0)
			{
				event.toolTip.add("Ore Dictionary Name:");
				for(int id : ids)
				{
					event.toolTip.add(OreDictionary.getOreName(id));
				}
			}
			if(event.itemStack.getItem() instanceof IThermalItem)
			{
				IThermalItem item = (IThermalItem) event.itemStack.getItem();
				event.toolTip.add("====Thermal Info====");
				event.toolTip.add("Temperature : " + EnumChatFormatting.RED + formatK.format(item.getTemperature(event.itemStack)));
				event.toolTip.add("Thermal Conductivity : " + EnumChatFormatting.WHITE + item.getThermalConductivity(event.itemStack) + "W/m");
				event.toolTip.add("====================");
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderHUD(RenderGameOverlayEvent.Text event)
	{
		if(V.debug) return;
		String string;
		label:
		for(int i = 0; i < event.right.size(); ++i)
		{
			string = event.right.get(i);
			while(string != null)
			{
				if(string.startsWith("Used memory: ") || string.startsWith("Allocated memory: "))
				{
					string = null;
				}
				else
				{
					event.right.remove(i);
					if(event.right.size() <= i) break label;
					string = event.right.get(i);
				}
			}
		}
		label:
		for(int i = 0; i < event.left.size(); ++i)
		{
			string = event.left.get(i);
			while(string != null)
			{
				if(string.startsWith("Minecraft ") || string.startsWith("shader:"))
				{
					string = null;
				}
				else
				{
					event.left.remove(i);
					if(event.left.size() <= i) break label;
					string = event.left.get(i);
				}
			}
		}
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
            for (String brand : FMLCommonHandler.instance().getBrandings(false))
            {
                event.right.add(brand);
            }
		EntityPlayer player = U.Worlds.player();
		int x = (int) player.posX;
		int y = (int) player.posY;
		int z = (int) player.posZ;
	}

	@SubscribeEvent
	public void onRenderHUD(RenderGameOverlayEvent.Pre event)
	{
		int width, height;
		int level, left, top;
		if(event.type == ElementType.FOOD)
		{
			width = event.resolution.getScaledWidth();
			height = event.resolution.getScaledHeight();
			FarFoodStats stats = (FarFoodStats) U.Worlds.player().getFoodStats();
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_BLEND);
			if(V.debug)
			{
				event.setCanceled(true);
		        left = width / 2 + 91 - 89;
		        top = height - GuiIngameForge.right_height;
		        GuiIngameForge.right_height += 10;
				String info = "Hunger:" + (int) (stats.getFoodLevelFar()) + "/" + 200;
				Minecraft.getMinecraft().fontRenderer.drawString(info, left + 1, top + 1, 0xFF113322);
				Minecraft.getMinecraft().fontRenderer.drawString(info, left, top, 0xFF00FF88);
		        level = stats.getWaterProgress();
		        left = width / 2 + 91 - 89;
		        top = height - GuiIngameForge.right_height;
		        GuiIngameForge.right_height += 10;
		        info = "Water:" + (int) (stats.getWaterLevel()) + "/" + 200;
				Minecraft.getMinecraft().fontRenderer.drawString(info, left + 1, top + 1, 0xFF111133);
				Minecraft.getMinecraft().fontRenderer.drawString(info, left, top, 0xFF0000FF);
			}
			else
			{
//		        level = stats.getFoodLevel();
//		        left = width / 2 + 91;
//		        top = height - GuiIngameForge.right_height;
//		        GuiIngameForge.right_height += 10;

//		        for (int i = 0; i < 10; ++i)
//		        {
//		            int idx = i * 2 + 1;
//		            int x = left - i * 8 - 9;
//		            int y = top;
//		            int icon = 16;
//		            byte backgound = 0;

//		            if (thePlayer.isPotionActive(Potion.hunger))
//		            {
//		                icon += 36;
//		                backgound = 13;
//		            }		            
//		            if (thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && evt.resolution % (level * 3 + 1) == 0)
//		            {
//		                y = top + (rand.nextInt(3) - 1);
//		            }
//		            drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);
//		            if (idx < level)
//		            {
//		            	drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
//		            }
//		            else if (idx == level)
//		            {
//		            	drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
//		            }
//		        }
		        Minecraft.getMinecraft().renderEngine.bindTexture(override);
		        level = stats.getWaterProgress();
		        left = width / 2 + 91;
		        top = height - GuiIngameForge.right_height;
		        GuiIngameForge.right_height += 10;

		        for (int i = 0; i < 10; ++i)
		        {
		            int idx = i * 2 + 1;
		            int x = left - i * 8 - 9;
		            int y = top;
		            byte backgound = 0;
		            byte state = 0;

		            drawTexturedModalRect(x, y, backgound * 9, 0, 9, 9);
		            if (idx < level)
		            {
		            	drawTexturedModalRect(x, y, 0, 9 + state * 9, 9, 9);
		            }
		            else if (idx == level)
		            {
		            	drawTexturedModalRect(x, y, 9, 9 + state * 9, 9, 9);
		            }
		        }
				Minecraft.getMinecraft().renderEngine.bindTexture(Gui.icons);
			}
	        GL11.glEnable(GL11.GL_BLEND);
		}
	}
	
	private void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double) 0, (double)((float)(u + 0) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double) 0, (double)((float)(u + w) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double) 0, (double)((float)(u + w) * f), (double)((float)(v + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double) 0, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
        tessellator.draw();
    }
}