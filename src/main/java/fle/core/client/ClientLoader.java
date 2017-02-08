package fle.core.client;

import farcore.FarCoreRegistry;
import farcore.data.ColorMultiplier;
import farcore.data.EnumItem;
import fle.core.FLE;
import fle.core.client.render.TESRDitch;
import fle.core.common.CommonLoader;
import fle.core.handler.FleClientHandler;
import fle.loader.BlocksItems;
import fle.loader.Entities;
import nebula.client.ClientProxy;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.data.Misc;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientLoader extends CommonLoader
{
	@Override
	public void init(FMLPreInitializationEvent event, Configuration config)
	{
		super.init(event, config);
		
		MinecraftForge.EVENT_BUS.register(new FleClientHandler());
		
		ClientProxy.registerRenderObject();
		
		Renders.registerColorMultiplier(ColorMultiplier.TOOL_ITEM_MATERIAL_COLOR, EnumItem.tool.item);
		
		ModelLoader.setCustomStateMapper(BlocksItems.ditch, new StateMapperExt(FLE.MODID, "ditch", null, Misc.PROPS_SIDE_HORIZONTALS));
		
		Entities.clientInit();
		FarCoreRegistry.registerTESR(TESRDitch.class);
	}
}