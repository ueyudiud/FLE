package farcore.handler;

import nebula.client.light.LightFix;
import nebula.common.NebulaConfig;
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
			if(NebulaConfig.multiThreadLight)
			{
				event.getLeft().add(LightFix.getOverlayInfo());
			}
		}
	}
}