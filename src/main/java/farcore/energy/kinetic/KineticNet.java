package farcore.energy.kinetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.FarCore;
import farcore.energy.IEnergyNet;
import farcore.lib.collection.IntArray;
import farcore.lib.util.Direction;
import farcore.lib.util.EnumModifyFlag;
import farcore.lib.util.Log;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KineticNet implements IEnergyNet
{
	public static final KineticNet instance = new KineticNet();

	private static Map<Integer, Local> netMap = new HashMap();

	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}
	
	@Override
	public void add(Object tile)
	{
		if(tile instanceof IKineticHandler)
		{
			getNet(((IKineticHandler) tile).world(), false).add((IKineticHandler) tile);
		}
	}
	
	@Override
	public void remove(Object tile)
	{
		if(tile instanceof IKineticHandler)
		{
			getNet(((IKineticHandler) tile).world(), false).remove((IKineticHandler) tile);
		}
	}

	@Override
	public void mark(Object tile)
	{
		//Do nothing.
	}
	
	@Override
	public void reload(Object tile)
	{
		if(tile instanceof IKineticHandler)
		{
			getNet(((IKineticHandler) tile).world(), false).reload((IKineticHandler) tile);
		}
	}
	
	@Override
	public void unload(World world)
	{
		netMap.remove(world.provider.getDimension());
	}
	
	@Override
	public void load(World world)
	{
		netMap.put(world.provider.getDimension(), new Local(world));
	}

	private Local getNet(World world, boolean create)
	{
		if(!create)
			return netMap.getOrDefault(world.provider.getDimension(), Local.instance);
		if(!netMap.containsKey(world.provider.getDimension()))
		{
			netMap.put(world.provider.getDimension(), new Local(world));
		}
		return netMap.get(world.provider.getDimension());
	}

	private static class Local implements IKineticAccess
	{
		private static final Local instance = new Local(null);

		private volatile boolean isUpdating = false;

		private World world;

		private final Map<BlockPos, IKineticHandler> map = new HashMap();
		private final Map<IntArray, double[]> cachedSend = new HashMap();
		private final List<IntArray> cachedSended = new ArrayList();
		private IKineticHandler cachedTile;
		private BlockPos cachedPos = BlockPos.ORIGIN;
		private final Map<IKineticHandler, EnumModifyFlag> cachedChangedTile = new HashMap();

		public Local(World world)
		{
			this.world = world;
		}

		public void add(IKineticHandler tile)
		{
			if(world == null) return;
			if(isUpdating)
			{
				cachedChangedTile.put(tile, EnumModifyFlag.add);
			}
			else
			{
				map.put(tile.pos(), tile);
			}
		}

		public void remove(IKineticHandler tile)
		{
			if(world == null) return;
			if(isUpdating)
			{
				cachedChangedTile.put(tile, EnumModifyFlag.remove);
			}
			else
			{
				map.remove(tile.pos());
			}
		}

		public void reload(IKineticHandler tile)
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
					for(IKineticHandler tile : map.values())
					{
						cachedTile = tile;
						tile.kineticPreUpdate(this);
					}
				}
				cachedTile = null;
				//Send kinetic energy to other tile.
				Map<IntArray, double[]> sends = new HashMap();
				Direction direction;
				do
				{
					sends.clear();
					sends.putAll(cachedSend);
					cachedSend.clear();
					for(Entry<IntArray, double[]> entry : sends.entrySet())
					{
						int[] info = entry.getKey().array;
						cachedPos = new BlockPos(info[0], info[1], info[2]);
						IKineticHandler source = map.get(cachedPos);
						direction = Direction.directions[info[3]];
						cachedPos = direction.offset(cachedPos);
						double[] pkg = entry.getValue();
						if(map.containsKey(cachedPos))
						{
							IKineticHandler tile = map.get(cachedPos);
							if(!tile.canAccessKineticEnergyFromDirection(direction.getOpposite()))
							{
								cachedTile = source;
								source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
							}
							else if(!tile.isRotatable(direction.getOpposite(), pkg[0], pkg[1]))
							{
								cachedTile = source;
								source.onStuck(direction, pkg[0], pkg[1]);
								source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
							}
							else
							{
								double speed = pkg[0];
								cachedTile = tile;
								double send = tile.receiveKineticEnergy(this, direction.getOpposite(), pkg[0], pkg[1]);
								cachedTile = source;
								if(speed != send)
								{
									source.onStuck(direction, Math.abs(send - speed), pkg[1]);
								}
								source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
							}
						}
						else
						{
							cachedTile = source;
							source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
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
				if(FarCore.debug)
				{
					Log.warn("Stack overflow when update kinetic net.", error);
				}
			}
			catch (OutOfMemoryError error)
			{
				updated = false;
				if(FarCore.debug)
				{
					Log.warn("Out of memory when update kinetic net.", error);
				}
			}
			cachedPos = BlockPos.ORIGIN;
			cachedTile = null;
			if(updated)
			{
				isUpdating = false;
				cachedSended.clear();
				for(Entry<IKineticHandler, EnumModifyFlag> entry : cachedChangedTile.entrySet())
				{
					switch (entry.getValue())
					{
					case add :
						map.put(entry.getKey().pos(), entry.getKey());
						break;
					case remove :
						map.remove(entry.getKey().pos());
						break;
					case reload :
						map.remove(entry.getKey().pos());
						map.put(entry.getKey().pos(), entry.getKey());
						break;
					case mark:
						break;
					}
				}
				cachedChangedTile.clear();
			}
		}
		
		@Override
		public void sendEnergyTo(Direction direction, double speed, double torque)
		{
			if(world == null || cachedTile == null || !isUpdating || torque <= 0) return;
			BlockPos pos = cachedTile.pos();
			IntArray array = new IntArray(new int[]{
					pos.getX(),
					pos.getY(),
					pos.getZ(),
					direction.ordinal()});
			if(cachedSended.contains(array)) return;
			cachedSend.put(array, new double[]{speed, torque});
		}
	}
}