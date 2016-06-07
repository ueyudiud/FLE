package farcore.energy.thermal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import farcore.enums.Direction;
import farcore.interfaces.energy.IEnergyNet;
import farcore.interfaces.energy.thermal.IThermalProviderBlock;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.interfaces.energy.thermal.IWorldThermalConductivityHandler;
import farcore.util.FleLog;
import farcore.util.U;
import farcore.util.Values;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

public class ThermalNet implements IEnergyNet
{
	private static List<IWorldThermalConductivityHandler> worldCHandlers = new ArrayList();
	private static ThreadLocal<Integer> thread = new ThreadLocal();
	
	public static float getEnviormentTemp(World world, int x, int y, int z)
	{
		if(world == null) return Values.C_0_Point;
		float bioTemp = 0;
		int count = 0;
		for(int i = -2; i <= 2; ++i)
			for(int j = -2; j <= 2; ++j)
			{
				if(world.blockExists(x + i, y, z + j))
				{
					count++;
					bioTemp += U.Worlds.getBiomeBaseTemperature(world, x + i, y, z + j);
				}
			}
		if(count == 0) return Values.C_0_Point;
		bioTemp /= count;
		float hor = (float) world.provider.getHorizon();
		return Math.max(((bioTemp * 0.8F - 12F) * bioTemp + 44F) * (bioTemp - 0.15F) + 
				Values.C_0_Point - (hor < y ? ((float) y - hor) * .15F : 0), 0F);
	}
	
	public static float getBlockTemp(World world, int x, int y, int z, boolean checkNearby)
	{
		if(world == null) return Values.C_0_Point;
		Block block = world.getBlock(x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		thread.set(0);
		if(block instanceof IThermalProviderBlock)
		{
			return ((IThermalProviderBlock) block).getBlockTemperature(world, x, y, z);
		}
		else if(block instanceof BlockFluidBase)
		{
			return BlockFluidBase.getTemperature(world, x, y, z);
		}
		if(tile instanceof IThermalTile)
		{
			return ((IThermalTile) tile).getTemperature(Direction.Q);
		}
		if(!checkNearby) return -1;
		float temp = 0;
		int count = 0;
		for(Direction direction : Direction.directions)
		{
			if(!world.blockExists(x + direction.x, y + direction.y, z + direction.z)) continue;
			block = world.getBlock(x + direction.x, y + direction.y, z + direction.z);
			if(block instanceof IThermalProviderBlock)
			{
				temp += ((IThermalProviderBlock) block).getBlockTemperature(world, x, y, z);
				++count;
				continue;
			}
			tile = world.getTileEntity(x + direction.x, y + direction.y, z + direction.z);
			if(tile instanceof IThermalTile)
			{
				temp += ((IThermalTile) tile).getTemperature(direction.getOpposite());
				++count;
				continue;
			}
		}
		if(count == 0) return -1;
		thread.set(count);
		return temp / (float) count;
	}

	public static float getTemp(Entity entity)
	{
		return getTemp(entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ, true);
	}
	
	public static float getTemp(World world, int x, int y, int z, boolean checkNearby)
	{
		thread.set(null);
		float envior = getEnviormentTemp(world, x, y, z);
		float block = getBlockTemp(world, x, y, z, checkNearby);
		if(block == -1)
		{
			return envior;
		}
		else
		{
			if(!checkNearby) return block;
			int count = thread.get().intValue();
			if(count == 0)
			{
				return (envior + block) * 0.5F;
			}
			else
			{
				return (envior * 6 + block * count) / (6F + (float) count);
			}
		}
	}

	public static float getTempDifference(World world, int x, int y, int z)
	{
		if(world == null) return 0;
		float tempLocal = getTemp(world, x, y, z, true);
		float tempDif = 0;
		for(Direction direction : Direction.directions)
		{
			if(!world.blockExists(x + direction.x, y + direction.y, z + direction.z))
				continue;
			float delta = Math.abs(tempLocal - getTemp(world, x + direction.x, y + direction.y, z + direction.z, false));
			if(tempDif < delta)
				tempDif = delta;
		}
		return tempDif;
	}

	public static float getThermalConductivity(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z) instanceof IThermalProviderBlock)
		{
			return ((IThermalProviderBlock) world.getBlock(x, y, z)).getThermalConductivity(world, x, y, z);
		}
		float a;
		for(IWorldThermalConductivityHandler handler : worldCHandlers)
		{
			if((a = handler.getThermalConductivity(world, x, y, z)) > 0)
				return a;
		}
		return Values.standardThermalConductivity;
	}
	
	public static void sendHeatToBlock(World world, int x, int y, int z, float amount)
	{
		if(world.getBlock(x, y, z) instanceof IThermalProviderBlock)
		{
			((IThermalProviderBlock) world.getBlock(x, y, z)).onHeatChanged(world, x, y, z, amount);
		}
	}
	
	private Map<Integer, Local> netMap = new HashMap();
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}

	@Override
	public void add(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).add(tile);
	}

	@Override
	public void remove(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).remove(tile);
	}

	@Override
	public void mark(TileEntity tile)
	{
		//Do nothing.
	}
	
	@Override
	public void reload(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).reload(tile);
	}

	@Override
	public void unload(World world)
	{
		netMap.remove(world.provider.dimensionId);
	}

	@Override
	public void load(World world)
	{
		netMap.put(world.provider.dimensionId, new Local(world));
	}
	
	private Local getNet(World world, boolean create)
	{
		if(!create)
		{
			return netMap.getOrDefault(world.provider.dimensionId, Local.instance);
		}
		if(!netMap.containsKey(world.provider.dimensionId))
		{
			netMap.put(world.provider.dimensionId, new Local(world));
		}
		return netMap.get(world.provider.dimensionId);
	}
	
	private static class Local
	{
		private static final Local instance = new Local(null);
		
		private volatile boolean isUpdating = false;
		
		private World world;
		
		private final Map<ChunkCoordinates, IThermalTile> map = new HashMap();
		private final List<IThermalTile> cachedList = new ArrayList();
		private final ChunkCoordinates cachedCoordinates = new ChunkCoordinates();
		private final List<ChunkCoordinates> cachedRemovedTile = new ArrayList();
		private final List<TileEntity> cachedAddedTile = new ArrayList();
		
		public Local(World world)
		{
			this.world = world;
		}
		
		public void add(TileEntity tile)
		{
			if(world == null) return;
			if(isUpdating)
			{
				cachedAddedTile.add(tile);
			}
			else if(tile instanceof IThermalTile)
			{
				map.put(U.Worlds.makeCoordinate(tile), (IThermalTile) tile);
			}
		}
		
		public void remove(TileEntity tile)
		{
			if(world == null) return;
			if(isUpdating)
			{
				cachedRemovedTile.add(U.Worlds.makeCoordinate(tile));
			}
			else if(tile instanceof IThermalTile)
			{
				map.remove(U.Worlds.makeCoordinate(tile));
			}
		}
		
		public void reload(TileEntity tile)
		{
			if(world == null) return;
			remove(tile);
			add(tile);
		}
		
		public void updateNet()
		{
			if(world == null) return;
			//Initialize cache.
			cachedList.clear();
			//Switch update flag to true, to prevent remove element in map when iterating.
			isUpdating = true;
			boolean updated = true;
			try
			{
				//Update tile.
				for(IThermalTile tile : map.values())
				{
					//Cached current.
					float[] current = new float[Direction.directions.length];
					//Get cache current from each direction.
					for(int i = 0; i < Direction.directions.length; ++i)
					{
						Direction direction = Direction.directions[i];
						if(tile.canConnectTo(direction))
						{
							cachedCoordinates.posX = ((TileEntity) tile).xCoord + direction.x;
							cachedCoordinates.posY = ((TileEntity) tile).yCoord + direction.y;
							cachedCoordinates.posZ = ((TileEntity) tile).zCoord + direction.z;
							float tc1 = tile.getThermalConductivity(direction);
							float temp = tile.getTemperature(direction);
							float tc2;
							float temp2;
							try
							{
								if(map.containsKey(cachedCoordinates))
								{
									IThermalTile tile1 = map.get(cachedCoordinates);
									if(cachedList.contains(tile1)) continue;
									tc2 = tile1.getThermalConductivity(direction.getOpposite());
									temp2 = tile1.getTemperature(direction.getOpposite());
									if(temp > temp2)
									{
										tile1.receiveThermalEnergy(direction.getOpposite(), current[i] = (temp - temp2) * (tc1 + tc2) * 0.5F);
									}
									else if(temp < temp2)
									{
										tile1.emitThermalEnergy(direction.getOpposite(), -(current[i] = (temp - temp2) * (tc1 + tc2) * 0.5F));
									}
								}
								else
								{
									tc2 = getThermalConductivity(world, cachedCoordinates.posX, cachedCoordinates.posY, cachedCoordinates.posZ);
									temp2 = getTemp(world, cachedCoordinates.posX, cachedCoordinates.posY, cachedCoordinates.posZ, false);
									sendHeatToBlock(world, cachedCoordinates.posX, cachedCoordinates.posY, cachedCoordinates.posZ, current[direction.ordinal()] = (temp - temp2) * (tc1 + tc2) * 0.5F);
								}
							}
							catch(Exception exception)
							{
								if(V.debug)
								{
									FleLog.getCoreLogger().error("Catching an exception during emmit thermal energy.", exception);
								}
							}
						}
					}
					//Send cached energy.
					for(int i = 0; i < current.length; ++i)
					{
						float value = current[i];
						if(value > 0)
						{
							tile.emitThermalEnergy(Direction.directions[i], value);
						}
						else if(value < 0)
						{
							tile.receiveThermalEnergy(Direction.directions[i], -value);
						}
					}
					cachedList.add(tile);
				}
			}
			catch(OutOfMemoryError error)
			{
				if(V.debug)
				{
					FleLog.getCoreLogger().warn("The net update is out of memory, "
							+ "this error prevent update.", error);
				}
				updated = false;
			}
			cachedCoordinates.posX = 0;
			cachedCoordinates.posY = 0;
			cachedCoordinates.posZ = 0;
			if(updated)
			{
				for(ChunkCoordinates coordinates : cachedRemovedTile)
				{
					map.remove(coordinates);
				}
				for(TileEntity tile : cachedAddedTile)
				{
					if(!(tile instanceof IThermalTile)) continue;
					map.put(new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord), (IThermalTile) tile);
				}
				isUpdating = false;
				cachedList.clear();
				cachedRemovedTile.clear();
				cachedAddedTile.clear();
			}
		}
	}
}