package farcore.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FarCoreGuiHandler
{
	@SubscribeEvent
	public void onGameOverlay(RenderGameOverlayEvent.Text event)
	{
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
		}
	}
}
