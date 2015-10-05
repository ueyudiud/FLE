package fle.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.event.FLEFontRegisterEvent;
import fle.api.fluid.FluidBase;
import fle.api.gui.GuiCondition;
import fle.api.soild.Solid;
import fle.api.soild.SolidRegistry;
import fle.api.util.ColorMap;
import fle.api.util.FluidIconRegisterEvent;
import fle.api.util.IColorMapHandler;
import fle.core.init.Renders;
import fle.core.init.Rs;
import fle.core.util.FleColorMap;
import fle.core.util.FleTextureMap;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener, IColorMapHandler
{
	@SideOnly(Side.CLIENT)
	private IResourceManager resourceManager;
	
	public ClientProxy() 
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onPreload() 
	{
		super.onPreload();
    	((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}
	
	@Override
	public void onPostload() 
	{
		super.onPostload();
		Renders.init();
	}
	
	@Override
	public void onCompleteLoad() 
	{
		super.onCompleteLoad();
		if(flag)
			onIconRegister();
		Rs.completeInit();
	}
	
	boolean flag = false;

	@Override
	public void onIconRegister()
	{
		FleAPI.conditionIconRegister = new FleTextureMap("textures/condition");
		FleAPI.fontRegister = new FleTextureMap("textures/font");
		for(GuiCondition c : GuiCondition.register)
		{
			c.registerIcon(FleAPI.conditionIconRegister);
		}
		MinecraftForge.EVENT_BUS.post(new FLEFontRegisterEvent(FleAPI.fontRegister));
		Minecraft.getMinecraft().renderEngine.loadTexture(FleAPI.conditionLocate, (FleTextureMap) FleAPI.conditionIconRegister);
		Minecraft.getMinecraft().renderEngine.loadTexture(FleAPI.fontLocate, (FleTextureMap) FleAPI.fontRegister);
	}

	@Override
	public void onResourceManagerReload(IResourceManager aManager)
	{
		resourceManager = aManager;
		flag = true;
	}

	@Override
	public ColorMap registerColorMap(String aResourceName) 
	{
		try
		{
			return new FleColorMap(TextureUtil.readImageData(resourceManager, new ResourceLocation(aResourceName)));
		}
		catch(Throwable e)
		{
			return new FleColorMap();
		}
	}
	
	@SubscribeEvent
	public void onFluidIconRegister(FluidIconRegisterEvent evt)
	{
		for(FluidBase tFluid : FluidBase.register)
		{
			tFluid.registerIcon(evt.register);
		}
		for(Solid tSoild : SolidRegistry.getSolidList())
		{
			tSoild.registerIcon(evt.register);
		}
	}
	
	@SubscribeEvent
	public void onFontIconRegister(FLEFontRegisterEvent evt)
	{
		
	}
}