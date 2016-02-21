package fle.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.event.FluidIconRegisterEvent;
import flapi.fluid.FluidBase;
import flapi.gui.GuiCondition;
import flapi.solid.Solid;
import flapi.solid.SolidRegistry;
import flapi.util.ColorMap;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.util.IColorMapHandler;
import fle.core.init.Renders;
import fle.core.init.Rs;
import fle.core.util.FleColorMap;
import fle.core.util.FleTextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener, IColorMapHandler
{
	private List<FleColorMap> cachedColorMap = new ArrayList();
	
	public ClientProxy() 
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void load(FMLPreInitializationEvent event)
	{
		super.load(event);
    	((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}
	
	@Override
	public void load(FMLPostInitializationEvent event) 
	{
		super.load(event);
		Renders.init();
	}
	
	@Override
	public void load(FMLLoadCompleteEvent event) 
	{
		super.load(event);
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		FleTextureMap map = new FleTextureMap("textures/condition");
		for(GuiCondition c : GuiCondition.register)
		{
			c.registerIcon(map);
		}
		try
		{
			Minecraft.getMinecraft().getTextureManager().loadTexture(FleValue.conditionLocate, map);
		}
		catch(NullPointerException exception)
		{
			;
		}
		for(FleColorMap colorMap : cachedColorMap)
		{
			try
			{
				colorMap.setColors(TextureUtil.readImageData(manager, new ResourceLocation(colorMap.getLocate())));
			}
			catch (Exception e)
			{
				colorMap.setColors(new int[256 * 256]);
				FleLog.addExceptionToCache(e);
			}
		}
		FleLog.resetAndCatchException("Fail to load these location.");
	}

	@Override
	public ColorMap registerColorMap(String resourceName) 
	{
		if(resourceName == null) return new FleColorMap();
		for(FleColorMap map : cachedColorMap)
		{
			if(resourceName.equals(map.getLocate()))
			{
				return map;
			}
		}
		FleColorMap map = new FleColorMap(resourceName);
		cachedColorMap.add(map);
		return map;
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
}