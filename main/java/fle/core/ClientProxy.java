package fle.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.cover.Cover;
import flapi.cover.CoverRegistry;
import flapi.event.FluidIconRegisterEvent;
import flapi.fluid.FluidBase;
import flapi.gui.GuiCondition;
import flapi.solid.Solid;
import flapi.solid.SolidRegistry;
import flapi.util.ColorMap;
import flapi.util.FleValue;
import flapi.util.IColorMapHandler;
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
		FleTextureMap map = new FleTextureMap("textures/condition");
		for(GuiCondition c : GuiCondition.register)
		{
			c.registerIcon(map);
		}
		Minecraft.getMinecraft().renderEngine.loadTexture(FleValue.conditionLocate, map);
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
		for(Cover tCover : CoverRegistry.getCoverRegister())
		{
			tCover.registerIcon(evt.register);
		}
	}
}