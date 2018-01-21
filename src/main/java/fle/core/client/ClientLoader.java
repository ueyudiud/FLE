package fle.core.client;

import farcore.FarCoreRegistry;
import farcore.data.ColorMultiplier;
import farcore.data.EnumItem;
import fle.api.client.PolishingStateIconLoader;
import fle.core.client.render.TESRDirtMixture;
import fle.core.common.CommonLoader;
import fle.core.handler.FleClientHandler;
import fle.loader.EntityRender;
import fle.loader.Recipes;
import fle.loader.SpecialRender;
import nebula.client.ClientProxy;
import nebula.client.NebulaTextureHandler;
import nebula.client.util.Renders;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
		
		MinecraftForge.EVENT_BUS.register(FleClientHandler.class);
		NebulaTextureHandler.addIconLoader(new PolishingStateIconLoader());
		
		ClientProxy.registerRenderObject();
		
		Renders.registerColorMultiplier(ColorMultiplier.TOOL_ITEM_MATERIAL_COLOR, EnumItem.tool.item);
		
		EntityRender.clientInit();
		Recipes.addRenderStates();
		FarCoreRegistry.registerTESR(TESRDirtMixture.class);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		SpecialRender.init();
	}
}
