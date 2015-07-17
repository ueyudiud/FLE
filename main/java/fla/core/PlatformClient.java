package fla.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class PlatformClient extends Platform
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
	public World getWorld(int dimId)
	{
		if (isSimulating())
		{
			return super.getWorld(dimId);
		} 
		else
		{
			World world = Minecraft.getMinecraft().theWorld;
			return world.provider.dimensionId != dimId ? null : world;
		}
	}

	@Override
	public void profilerStartSection(String section)
	{
		if (isRendering())
			Minecraft.getMinecraft().mcProfiler.startSection(section);
		else
			super.profilerStartSection(section);
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

	@Override
	public int addArmor(String name)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(name);
	}
}
