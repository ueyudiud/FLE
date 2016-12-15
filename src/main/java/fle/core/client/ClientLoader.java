package fle.core.client;

import farcore.FarCoreRegistry;
import farcore.FarCoreSetup;
import farcore.data.ColorMultiplier;
import farcore.data.EnumItem;
import farcore.data.Others;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.util.U;
import fle.core.FLE;
import fle.core.client.render.TESRDitch;
import fle.core.common.CommonLoader;
import fle.core.handler.FleClientHandler;
import fle.loader.BlocksItems;
import net.minecraftforge.client.model.ModelLoader;
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
		
		ModelLoader.setCustomStateMapper(BlocksItems.ditch, new StateMapperExt(FLE.MODID, "ditch", null, Others.PROPS_SIDE_HORIZONTALS));
		FarCoreRegistry.registerTESR(TESRDitch.class);
	}
}
