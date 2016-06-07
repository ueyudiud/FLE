package fle.cwg.world;

import java.util.Arrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.util.U;
import fle.cwg.gui.GuiFCWGCustomWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.layer.GenLayer;

public class FCWGWorldType extends WorldType
{
	public static FCWGWorldType FCWG;
	
	public FCWGWorldType(String name)
	{
		super(name);
	}
	
	@SideOnly(Side.CLIENT)
	public String getTranslateName()
	{
		return FarCore.translateToLocal(super.getTranslateName());
	}
		
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return new FCWGSurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), FCWGWorldInfo.createNewInfo(generatorOptions));
		case -1 : return new ChunkProviderHell(world, world.getSeed());
		case 1 : return new ChunkProviderEnd(world, world.getSeed());
		}
		return null;
	}
	
	@Override
	public WorldChunkManager getChunkManager(World world)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return new FCWGSurfaceManager(world, FCWGWorldInfo.createNewInfo(world.getWorldInfo().getGeneratorOptions()));
		case -1 : return new WorldChunkManager(world);
		case 1 : return new WorldChunkManager(world);
		}
		return null;
	}
	
	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return world.provider.isHellWorld ? 64 : 128;
	}
	
	@Override
	public double getHorizon(World world)
	{
		return 127.0D;
	}
	
	@Override
	public boolean isCustomizable()
	{
		return true;
	}
	
	@Override
	public void onCustomizeButton(Minecraft instance, GuiCreateWorld guiCreateWorld)
	{
		instance.displayGuiScreen(new GuiFCWGCustomWorld(guiCreateWorld));
	}
	
	@Override
	public void onGUICreateWorldPress()
	{
		super.onGUICreateWorldPress();
	}
}