package fla.core.world;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fla.api.recipe.IItemChecker.OreChecker;
import fla.api.world.BlockPos;
import fla.api.world.BlockPos.ChunkPos;
import fla.api.world.IWorldManager;

public class FWM implements IWorldManager
{
	public static void init()
	{
		
		//DimensionManager.unregisterDimension(0);
		//DimensionManager.unregisterProviderType(0);
		//DimensionManager.registerProviderType(0, FlaWorldProvider.class, true);
		//DimensionManager.registerDimension(0, 0);
	}
	
	private World world;
	private HashMap<ChunkPos, NBTTagCompound> worldStateMap = new HashMap(); 
	
	public FWM()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt)
	{
		if(!evt.world.isRemote)
			this.world = evt.world;
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt)
	{
		this.world = null;
	}
	
	@SubscribeEvent
	public void onBlockPlease(PlaceEvent evt)
	{
		
	}

	@SubscribeEvent
	public void onBlockDestoryed(BreakEvent evt)
	{
		setBlockMeta(new BlockPos(evt.world, evt.x, evt.y, evt.z), 0);
	}

	@Override
	public int getBlockMeta(BlockPos pos) 
	{
		return pos.getBlockMeta();
	}

	@Override
	public void setBlockMeta(BlockPos pos, int meta) 
	{
		
	}
	
	/**
	 * 
	 * @param worldObj
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @return from 0D to 415D.
	 */
	public static double getWaterLevel(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		double ret = 0.0D;
		BiomeGenBase base = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		int i;
		int k;
		for(i = -4; i < 5; ++i)
			for(int j = 0; j < 5; ++j)
				for(k = -4; k < 5; ++k)
				{
					double distance = Math.pow((double) i, 2D) + Math.pow((double) j, 2D) + Math.pow((double) k, 2D) + 1D;
					Block block = worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k);
					if(block.isAir(worldObj, xCoord + i, yCoord + j, zCoord + k)) ret += 0.5D / distance;
					if(block.getMaterial() == Material.water) ret += 1D / distance;
					if(block.getMaterial() == Material.rock) ret -= 0.3D / distance;
				}
		ret -= 1024D / Math.pow((192D - (double) yCoord) + 64D, 2.0D);
		ret += base.getFloatRainfall() * 5;
		for(i = -2; i < 3; ++i)
			for(k = -2; k < 3; ++k)
				if(worldObj.getBlock(xCoord + i, yCoord - 1, zCoord + k).getMaterial() == Material.water) ret += 0.2D;
		return ret;
	}
	
	public static double getAirLevel(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		double ret = 0.0D;
		BiomeGenBase base = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		for(int i = -4; i < 5; ++i)
			for(int j = 0; j < 5; ++j)
				for(int k = -4; k < 5; ++k)
				{
					double distance = Math.pow((double) i, 2D) + Math.pow((double) j, 2D) + Math.pow((double) k, 2D) + 1D;
					Block block = worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k);
					if(block.isAir(worldObj, xCoord + i, yCoord + j, zCoord + k)) ret += 1D / distance;
					if(new OreChecker("treeLeaves").match(new ItemStack(block, 1, worldObj.getBlockMetadata(xCoord + i, yCoord+ j, zCoord + k)))) ret += 2D / distance;
				}
		ret += 4096D / Math.pow((192D - (double) yCoord) + 64D, 2.0D);
		return ret;
	}
	
	/**
	 * 
	 * @param worldObj
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @return from 0D to 1D.
	 */
	public static double getTempretureLevel(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		double ret = 0.0D;
		BiomeGenBase base = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		ret += base.getFloatTemperature(xCoord, yCoord, zCoord);
		return ret;
	}
}
