package fle.core.client;

import farcore.FarCoreRegistry;
import farcore.FarCoreSetup;
import farcore.data.ColorMultiplier;
import farcore.data.EnumItem;
import farcore.util.U;
import fle.core.client.render.TESRDitch;
import fle.core.common.CommonLoader;
import fle.core.handler.FleClientHandler;
import fle.loader.BlocksItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientLoader extends CommonLoader
{
	@Override
	public void init(FMLPreInitializationEvent event)
	{
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new FleClientHandler());
		
		FarCoreSetup.ClientProxy.registerRenderObject();
		
		U.Mod.registerColorMultiplier(ColorMultiplier.TOOL_ITEM_MATERIAL_COLOR, EnumItem.tool.item);
		
		FarCoreRegistry.registerTESR(TESRDitch.class);
		
		FarCoreRegistry.setBuildinModel(BlocksItems.ditch);
	}
}
