package farcore.handler;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.DEBUG;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class FarCoreGuiHandler
{
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
}