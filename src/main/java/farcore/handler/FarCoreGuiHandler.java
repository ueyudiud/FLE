package farcore.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.lib.world.CalendarHandler;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@SideOnly(Side.CLIENT)
public class FarCoreGuiHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderHUD(RenderGameOverlayEvent.Text event)
	{
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
        	event.left.add(FarCore.translateToLocal("info.debug.date") + CalendarHandler.getDateInfo(U.Players.player().worldObj));
        }
        else
        {
        	
        }
	}
}
