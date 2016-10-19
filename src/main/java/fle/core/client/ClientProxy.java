package fle.core.client;

import farcore.FarCoreSetup;
import farcore.data.ColorMultiplier;
import farcore.data.EnumItem;
import farcore.util.U;
import fle.core.common.CommonProxy;
import fle.core.handler.FleClientHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void init(FMLPreInitializationEvent event)
	{
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new FleClientHandler());
		
		FarCoreSetup.ClientProxy.registerRenderObject();

		U.Mod.registerColorMultiplier(ColorMultiplier.TOOL_ITEM_MATERIAL_COLOR, EnumItem.tool.item);
	}
}
