package fle.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class PlatformClient extends PlatformCommon
{
	private final Minecraft mc = Minecraft.getMinecraft();
	private final Map renders = new HashMap();

	public PlatformClient()
	{
		
	}

	@Override
	public void registerRenderers()
	{
		
	}

	@Override
	public EntityPlayer getPlayerInstance()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getWorldInstance(int aDimId)
	{
		if (isSimulating())
		{
			return super.getWorldInstance(aDimId);
		} 
		else
		{
			World world = Minecraft.getMinecraft().theWorld;
			return world == null ? null : world.provider.dimensionId != aDimId ? null : world;
		}
	}

	@Override
	public void profilerStartSection(String aSection)
	{
		if (isRendering())
			Minecraft.getMinecraft().mcProfiler.startSection(aSection);
		else
			super.profilerStartSection(aSection);
	}

	@Override
	public void profilerEndSection()
	{
		if (isRendering())
			Minecraft.getMinecraft().mcProfiler.endSection();
		else
			super.profilerEndSection();
	}

	@Override
	public void profilerEndStartSection(String section)
	{
		if (isRendering())
			Minecraft.getMinecraft().mcProfiler.endStartSection(section);
		else
			super.profilerEndStartSection(section);
	}

	@Override
	public File getMinecraftDir()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void playSoundSp(String sound, float f, float g)
	{
		//Or not...
	}
}