package farcore.energy.kinetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.enums.Direction;
import farcore.interfaces.energy.IEnergyNet;
import farcore.interfaces.energy.kinetic.IKineticAccess;
import farcore.interfaces.energy.kinetic.IKineticTile;
import farcore.lib.collection.IntArray;
import farcore.util.FleLog;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class KineticNet implements IEnergyNet
{
	private static Map<Integer, Local> netMap = new HashMap();
	
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
	
	private static class Local implements IKineticAccess
	{
		private static final Local instance = new Local(null);
		
		private volatile boolean isUpdating = false;
		
		private World world;
		
		private final Map<ChunkCoordinates, IKineticTile> map = new HashMap();
		private final Map<IntArray, KineticPkg> cachedSend = new HashMap();
		private final List<IntArray> cachedSended = new ArrayList();
		private IKineticTile cachedTile;
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
			else if(tile instanceof IKineticTile)
			{
				map.put(U.Worlds.makeCoordinate(tile), (IKineticTile) tile);
			}
		}
		
		public void remove(TileEntity tile)
		{
			if(world == null) return;
			if(isUpdating)
			{
				cachedRemovedTile.add(U.Worlds.makeCoordinate(tile));
			}
			else if(tile instanceof IKineticTile)
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
			//Switch update flag to true, to prevent remove element in map when iterating.
			isUpdating = true;
			boolean updated = true;
			try
			{
				//Check if last tick is already updated, or just update lasting tile.
				if(cachedSend.isEmpty())
				{
					//Pre-update all tile(Suggested update emitter in this method).
					for(IKineticTile tile : map.values())
					{
						cachedTile = tile;
						tile.kineticPreUpdate(this);
					}
				}
				cachedTile = null;
				//Send kinetic energy to other tile.
				Map<IntArray, KineticPkg> sends = new HashMap();
				Direction direction;
				do
				{
					sends.clear();
					sends.putAll(cachedSend);
					cachedSend.clear();
					for(Entry<IntArray, KineticPkg> entry : sends.entrySet())
					{
						int[] info = entry.getKey().array;
						cachedCoordinates.posX = info[0];
						cachedCoordinates.posY = info[1];
						cachedCoordinates.posZ = info[2];
						IKineticTile source = map.get(cachedCoordinates);
						direction = Direction.directions[info[3]];
						cachedCoordinates.posX += direction.x;
						cachedCoordinates.posY += direction.y;
						cachedCoordinates.posZ += direction.z;
						KineticPkg pkg = entry.getValue();
						if(map.containsKey(cachedCoordinates))
						{
							IKineticTile tile = map.get(cachedCoordinates);
							if(!tile.canAccessKineticEnergyFromDirection(direction.getOpposite()))
							{
								cachedTile = source;
								source.emitKineticEnergy(this, direction, pkg);
							}
							else if(!tile.isRotatable(direction.getOpposite(), pkg))
							{
								cachedTile = source;
								source.onStuck(direction, pkg.speed, pkg.torque);
								source.emitKineticEnergy(this, direction, pkg);
							}
							else
							{
								float speed = pkg.speed;
								cachedTile = tile;
								tile.receiveKineticEnergy(this, direction.getOpposite(), pkg);
								cachedTile = source;
								if(speed != pkg.speed)
								{
									source.onStuck(direction, Math.abs(pkg.speed - speed), pkg.torque);
								}
								source.emitKineticEnergy(this, direction, pkg);
							}
						}
						else
						{
							cachedTile = source;
							source.emitKineticEnergy(this, direction, pkg);
						}
						cachedSended.add(entry.getKey());
					}
				} 
				//Some tile maybe is a kinetic conductor, looped check.
				while (!cachedSend.isEmpty());
			}
			catch(StackOverflowError error)
			{
				updated = false;
				if(V.debug)
				{
					FleLog.getCoreLogger().debug("Stack overflow when update kinetic net.", error);
				}
			}
			catch (OutOfMemoryError error)
			{
				updated = false;
				if(V.debug)
				{
					FleLog.getCoreLogger().debug("Out of memory when update kinetic net.", error);
				}
			}
			cachedCoordinates.posX = 0;
			cachedCoordinates.posY = 0;
			cachedCoordinates.posZ = 0;
			cachedTile = null;
			if(updated)
			{
				isUpdating = false;
				cachedSended.clear();
				for(TileEntity tile : cachedAddedTile)
				{
					if(tile instanceof IKineticTile)
					{
						map.put(U.Worlds.makeCoordinate(tile), (IKineticTile) tile);
					}
				}
				for(ChunkCoordinates coordinates : cachedRemovedTile)
				{
					map.remove(coordinates);
				}
				cachedAddedTile.clear();
				cachedRemovedTile.clear();
			}
		}

		@Override
		public void sendEnergyTo(Direction direction, KineticPkg pkg)
		{
			if(world == null || cachedTile == null || !isUpdating || pkg == null) return;
			IntArray array = new IntArray(new int[]{
					((TileEntity) cachedTile).xCoord,
					((TileEntity) cachedTile).yCoord,
					((TileEntity) cachedTile).zCoord,
					direction.ordinal()}); 
			if(cachedSended.contains(array)) return;
			cachedSend.put(array, pkg);
		}
	}
}