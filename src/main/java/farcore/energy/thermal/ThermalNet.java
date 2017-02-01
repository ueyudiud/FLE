package farcore.energy.thermal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.data.V;
import farcore.energy.IEnergyNet;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.util.EnumModifyFlag;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import nebula.Log;
import nebula.Nebula;
import nebula.common.NebulaWorldHandler;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import nebula.common.world.ICoord;
import nebula.common.world.IObjectInWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

public class ThermalNet implements IEnergyNet
{
	private static final List<IWorldThermalHandler> worldCHandlers = new ArrayList();
	
	public static void registerWorldThermalHandler(IWorldThermalHandler handler)
	{
		worldCHandlers.add(handler);
	}
	
	public static float getWorldTemperature(World world, BlockPos pos)
	{
		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(world);
		return 270F + properties.getTemperature(world, pos) * 6.0F;
	}
	
	public static float getEnviormentTemperature(World world, BlockPos pos)
	{
		ChunkPos pos2 = new ChunkPos(pos);
		float base = getWorldTemperature(world, pos);
		IThermalHandlerBox box = instance.getNet(world, false).getBoxAtPos(pos);
		if(box != null)
		{
			base = box.getTemperature(pos);
		}
		float affected = 0;
		for(IObjectInWorld obj : NebulaWorldHandler.getObjectInRange(world, pos, 24))
		{
			if(obj instanceof IThermalObjectInWorld)
			{
				affected += ((IThermalObjectInWorld) obj).getDetTemp(Worlds.distanceSqTo(obj, pos), base);
			}
		}
		return base + affected;
	}
	
	public static float getRealHandlerTemperature(IThermalHandler handler, Direction direction)
	{
		float temp1 = getEnviormentTemperature(handler.world(), handler.pos());
		float temp2 = handler.getTemperatureDifference(direction);
		if(temp2 == Float.POSITIVE_INFINITY)
			return Float.MAX_VALUE;
		else if(temp2 == Float.NEGATIVE_INFINITY)
			return 0;
		return Float.isNaN(temp2) ? temp1 : temp1 + temp2;
	}
	
	public static float getTemperature(ICoord coord)
	{
		return getTemperature(coord.world(), coord.pos(), true);
	}
	
	public static float getTemperature(World world, BlockPos pos, boolean withNearby)
	{
		if(pos.getY() < 0 || pos.getY() >= 256)
			return getWorldTemperature(world, pos);
		float temp;
		TileEntity tile;
		if((tile = world.getTileEntity(pos)) instanceof IThermalHandler)
		{
			temp = getRealHandlerTemperature((IThermalHandler) tile, Direction.Q);
		}
		else
		{
			temp = getEnviormentTemperature(world, pos);
			for(IWorldThermalHandler handler : worldCHandlers)
			{
				float v;
				if((v = handler.getTemperature(world, pos, temp)) >= 0)
				{
					temp = v;
				}
			}
		}
		if(withNearby)
		{
			double tempD = temp;
			tempD *= 6;
			int c = 6;
			for(Direction direction : Direction.DIRECTIONS_3D)
			{
				BlockPos pos2 = direction.offset(pos);
				tempD += getTemperature(world, pos2, false);
				++c;
			}
			tempD /= c;
			return (float) tempD;
		}
		return temp;
	}
	
	public static float getTempDifference(World world, BlockPos pos)
	{
		double temp = getTemperature(world, pos, false);
		double delta = 0;
		int c = 0;
		for(Direction direction : Direction.DIRECTIONS_3D)
		{
			delta += Math.abs(temp - getTemperature(world, direction.offset(pos), false));
			c++;
		}
		delta /= c;
		return (float) delta;
	}
	
	/**
	 * Called when can not found an block or tile to get thermal conductivity.
	 * @param world
	 * @param pos
	 * @param state
	 * @return
	 */
	public static float getBaseThermalConductivity(World world, BlockPos pos, IBlockState state)
	{
		float a;
		for(IWorldThermalHandler handler : worldCHandlers)
		{
			if((a = handler.getThermalConductivity(world, pos, state)) >= 0)
				return a;
		}
		return V.k0;
	}
	
	public static double getThermalConductivity(World world, BlockPos pos, Direction direction)
	{
		IBlockState state;
		TileEntity tile;
		if((tile = world.getTileEntity(pos)) instanceof IThermalHandler)
			return ((IThermalHandler) tile).getThermalConductivity(direction);
		if((state = world.getBlockState(pos)).getBlock() instanceof IThermalCustomBehaviorBlock)
			return ((IThermalCustomBehaviorBlock) state.getBlock()).getThermalConduct(world, pos);
		state = state.getActualState(world, pos);
		return getBaseThermalConductivity(world, pos, state);
	}
	
	public static void sendHeatToBlock(World world, BlockPos pos, Direction direction, double amount)
	{
		IBlockState state;
		if((state = world.getBlockState(pos)).getBlock() instanceof IThermalCustomBehaviorBlock)
		{
			((IThermalCustomBehaviorBlock) state.getBlock()).onHeatChanged(world, pos, direction, amount);
		}
	}
	
	public static final ThermalNet instance = new ThermalNet();
	
	private Map<Integer, Local> netMap = new HashMap();
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}
	
	@Override
	public void add(Object tile)
	{
		if(tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).add((IThermalHandler) tile);
		}
		if(tile instanceof IThermalHandlerBox)
		{
			getNet(((IThermalHandlerBox) tile).world(), false).add((IThermalHandlerBox) tile);
		}
	}
	
	@Override
	public void remove(Object tile)
	{
		if(tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).remove((IThermalHandler) tile);
		}
		if(tile instanceof IThermalHandlerBox)
		{
			getNet(((IThermalHandlerBox) tile).world(), false).remove((IThermalHandlerBox) tile);
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
		if(tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).reload((IThermalHandler) tile);
		}
		if(tile instanceof IThermalHandlerBox)
		{
			getNet(((IThermalHandlerBox) tile).world(), false).reload((IThermalHandlerBox) tile);
		}
	}
	
	public void remove(IThermalHandlerBox box)
	{
		getNet(box.world(), false).remove(box);
	}
	
	public void mark(IThermalHandlerBox tile)
	{
		//Do nothing.
	}
	
	public void reload(IThermalHandlerBox box)
	{
		getNet(box.world(), false).reload(box);
	}
	
	@Override
	public void unload(World world)
	{
		this.netMap.remove(world.provider.getDimension());
	}
	
	@Override
	public void load(World world)
	{
		this.netMap.put(world.provider.getDimension(), new Local(world));
	}
	
	private Local getNet(World world, boolean create)
	{
		if(!create)
			return this.netMap.getOrDefault(world.provider.getDimension(), Local.instance);
		if(!this.netMap.containsKey(world.provider.getDimension()))
		{
			this.netMap.put(world.provider.getDimension(), new Local(world));
		}
		return this.netMap.get(world.provider.getDimension());
	}
	
	private static class Local
	{
		private static final Local instance = new Local(null);
		
		private volatile boolean isUpdating = false;
		
		private World world;
		
		private final Map<BlockPos, IThermalHandler> map = new HashMap();
		private final Map<ChunkPos, List<IThermalHandlerBox>> map2 = new HashMap();
		private final List<IThermalHandler> cachedList = new ArrayList();
		private final List<IThermalHandlerBox> cacheBoxList = new ArrayList();
		private BlockPos cachedPos = BlockPos.ORIGIN;
		private final Map<BlockPos, EnumModifyFlag> cachedChangedTile = new HashMap();
		private final Map<IThermalHandlerBox, EnumModifyFlag> cacheChangedBoxList = new HashMap();
		
		public Local(World world)
		{
			this.world = world;
		}
		
		public void add(IThermalHandlerBox box)
		{
			if(this.world == null) return;
			if(this.isUpdating)
			{
				this.cacheChangedBoxList.put(box, EnumModifyFlag.add);
			}
			else
			{
				List<ChunkPos> chunks = box.chunks();
				if(chunks.isEmpty()) return;
				for(ChunkPos chunk : chunks)
				{
					L.put(this.map2, chunk, box);
				}
			}
		}
		
		public void remove(IThermalHandlerBox box)
		{
			if(this.world == null) return;
			if(this.isUpdating)
			{
				this.cacheChangedBoxList.put(box, EnumModifyFlag.remove);
			}
			else
			{
				List<ChunkPos> chunks = box.chunks();
				if(chunks.isEmpty()) return;
				for(ChunkPos chunk : chunks)
				{
					L.remove(this.map2, chunk, box);
				}
			}
		}
		
		public void reload(IThermalHandlerBox box)
		{
			if(this.world == null) return;
			if(this.isUpdating)
			{
				this.cacheChangedBoxList.put(box, EnumModifyFlag.reload);
			}
			else
			{
				remove(box);
				add(box);
			}
		}
		
		public void add(IThermalHandler tile)
		{
			if(this.world == null) return;
			if(this.isUpdating)
			{
				this.cachedChangedTile.put(tile.pos(), EnumModifyFlag.add);
			}
			else
			{
				this.map.put(tile.pos(), tile);
			}
		}
		
		public void remove(IThermalHandler tile)
		{
			if(this.world == null) return;
			if(this.isUpdating)
			{
				this.cachedChangedTile.put(tile.pos(), EnumModifyFlag.remove);
			}
			else
			{
				this.map.remove(tile.pos());
			}
		}
		
		public void reload(IThermalHandler tile)
		{
		}
		
		public void updateNet()
		{
			if(this.world == null || this.world.isRemote || this.world.getWorldType() == WorldType.DEBUG_WORLD) return;
			//Initialize cache.
			this.cachedList.clear();
			//Switch update flag to true, to prevent remove element in map when iterating.
			this.isUpdating = true;
			boolean updated = true;
			try
			{
				//Cached current.
				double[] current = new double[Direction.DIRECTIONS_3D.length];
				//Update tile.
				for(IThermalHandler tile : this.map.values())
				{
					Arrays.fill(current, 0);
					//Get cache current from each direction.
					for(int i = 0; i < Direction.DIRECTIONS_3D.length; ++i)
					{
						Direction direction = Direction.DIRECTIONS_3D[i];
						if(tile.canConnectTo(direction))
						{
							this.cachedPos = direction.offset(tile.pos());
							double tc1 = tile.getThermalConductivity(direction);
							float temp = getEnviormentTemperature(this.world, tile.pos()) + tile.getTemperatureDifference(direction);
							double tc2;
							float temp2;
							try
							{
								if(this.map.containsKey(this.cachedPos))
								{
									IThermalHandler tile1 = this.map.get(this.cachedPos);
									if(!tile1.canConnectTo(direction.getOpposite()) || this.cachedList.contains(tile1))
									{
										continue;
									}
									tc2 = tile1.getThermalConductivity(direction.getOpposite());
									temp2 = getEnviormentTemperature(this.world, tile1.pos()) + tile1.getTemperatureDifference(direction.getOpposite());
									if(!L.similar(temp, temp2))//Ignore small temperature difference.
									{
										tile1.onHeatChange(direction.getOpposite(), current[i] = (temp - temp2) * Math.sqrt(tc1 * tc2));
									}
								}
								else
								{
									tc2 = getThermalConductivity(this.world, this.cachedPos, direction.getOpposite());
									temp2 = getTemperature(this.world, this.cachedPos, false);
									if(!L.similar(temp, temp2))//Ignore small temperature difference.
									{
										double v = (temp - temp2) * Math.sqrt(tc1 * tc2);
										current[i] = v;
										if(v != 0)
										{
											IThermalHandlerBox box = getBoxAtPos(this.cachedPos);
											if(box != null && box.onHeatChange(tile.pos(), this.cachedPos, direction.getOpposite(), v))
											{
												;
											}
											else
											{
												sendHeatToBlock(this.world, this.cachedPos, direction.getOpposite(), v);
											}
										}
									}
								}
							}
							catch(Exception exception)
							{
								if(Nebula.debug)
								{
									Log.error("Catching an exception during emmit thermal energy.", exception);
								}
							}
						}
					}
					//Send cached energy.
					for(int i = 0; i < current.length; ++i)
					{
						tile.onHeatChange(Direction.DIRECTIONS_3D[i], -current[i]);
					}
					this.cachedList.add(tile);
				}
			}
			catch(OutOfMemoryError error)
			{
				if(Nebula.debug)
				{
					Log.warn("The net update is out of memory, "
							+ "this error will prevent update.", error);
				}
				updated = false;
			}
			if(updated)
			{
				for(Entry<BlockPos, EnumModifyFlag> entry : this.cachedChangedTile.entrySet())
				{
					switch (entry.getValue())
					{
					case add :
						TileEntity tile = this.world.getTileEntity(entry.getKey());
						if(tile instanceof IThermalHandler)
						{
							this.map.put(entry.getKey(), (IThermalHandler) tile);
						}
						break;
					case remove :
						this.map.remove(entry.getKey());
						break;
					case reload :
						IThermalHandler handler = this.map.remove(entry.getKey());
						if(handler != null)
						{
							this.map.put(handler.pos(), handler);
						}
						break;
					case mark :
						break;
					}
				}
				for(Entry<IThermalHandlerBox, EnumModifyFlag> entry : this.cacheChangedBoxList.entrySet())
				{
					switch (entry.getValue())
					{
					case add :
						for(ChunkPos pos : entry.getKey().chunks())
						{
							L.put(this.map2, pos, entry.getKey());
						}
						break;
					case remove :
						for(ChunkPos pos : entry.getKey().chunks())
						{
							L.remove(this.map2, pos, entry.getKey());
						}
						break;
					case reload :
						for(ChunkPos pos : entry.getKey().chunks())
						{
							L.put(this.map2, pos, entry.getKey());
						}
						for(ChunkPos pos : entry.getKey().chunks())
						{
							L.remove(this.map2, pos, entry.getKey());
						}
						break;
					case mark :
						break;
					}
				}
				this.isUpdating = false;
				this.cachedList.clear();
				this.cachedChangedTile.clear();
				this.cacheChangedBoxList.clear();
			}
		}
		
		public IThermalHandlerBox getBoxAtPos(BlockPos pos)
		{
			ChunkPos pos2 = new ChunkPos(pos);
			if(this.map2.containsKey(pos2))
			{
				for(IThermalHandlerBox box : this.map2.get(pos2))
				{
					if(box.containPosition(pos))
						return box;
				}
			}
			return null;
		}
	}
}