/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.energy.thermal;

import static java.lang.Math.expm1;
import static nebula.common.util.Direction.DIRECTIONS_3D;
import static nebula.common.util.Direction.OPPISITE;
import static nebula.common.util.Maths.logarithmicMean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import farcore.data.V;
import farcore.energy.EnergyHandler;
import farcore.energy.EnergyHandler.Energy;
import farcore.energy.IEnergyNet;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.util.EnumModifyFlag;
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
	private static final List<IWorldThermalHandler> worldCHandlers = new ArrayList<>();
	
	private static float cacheTemp;
	private static int cacheDim;
	private static BlockPos cachePos;
	
	public static void registerWorldThermalHandler(IWorldThermalHandler handler)
	{
		worldCHandlers.add(handler);
	}
	
	/**
	 * Get base world temperature affected by biomes, terrain.
	 * 
	 * @param world the world.
	 * @param pos the position.
	 * @return the temperature
	 */
	public static float getWorldTemperature(World world, BlockPos pos)
	{
		return WorldPropHandler.getWorldProperty(world).getTemperature(world, pos);
	}
	
	/**
	 * Get environment temperature affected by world handler and thermal object
	 * in world.
	 * 
	 * @param world the world.
	 * @param pos the position.
	 * @return the temperature.
	 * @see #getWorldTemperature(World, BlockPos)
	 * @see farcore.energy.thermal.IThermalObjectInWorld
	 */
	public static float getEnvironmentTemperature(World world, BlockPos pos)
	{
		if (cachePos != null && world.provider.getDimension() == cacheDim && cachePos.equals(pos))
			return cacheTemp;
		float base = getWorldTemperature(world, pos);
		IThermalHandlerBox box = INSTANCE.getNet(world, false).getBoxAtPos(pos);
		if (box != null)
		{
			base = box.getTemperature(pos);
		}
		float affected = 0;
		for (IObjectInWorld obj : NebulaWorldHandler.getObjectInRange(world, pos, 24))
		{
			if (obj instanceof IThermalObjectInWorld)
			{
				affected += ((IThermalObjectInWorld) obj).getDetTemp(Worlds.distanceSqTo(obj, pos), base);
			}
		}
		return base + affected;
	}
	
	/**
	 * Get temperature from thermal handler directly.
	 * 
	 * @param handler the thermal handler.
	 * @param direction the temperature of facing offset to check.
	 * @return the temperature.
	 */
	public static float getRealHandlerTemperature(IThermalHandler handler, Direction direction)
	{
		float temp1 = cacheTemp = getEnvironmentTemperature(handler.world(), handler.pos());
		cacheDim = handler.world().provider.getDimension();
		cachePos = handler.pos();
		float temp2 = handler.getTemperatureDifference(direction);
		cachePos = null;
		return temp2 == Float.POSITIVE_INFINITY ? Float.MAX_VALUE : temp2 == Float.NEGATIVE_INFINITY ? 0 : Float.isNaN(temp2) ? temp1 : temp1 + temp2;
	}
	
	public static float getTemperature(ICoord coord)
	{
		return getTemperature(coord.world(), coord.pos(), true);
	}
	
	public static float getTemperature(World world, BlockPos pos, boolean withNearby)
	{
		if (pos.getY() < 0 || pos.getY() >= 256) return getWorldTemperature(world, pos);
		float temp;
		TileEntity tile;
		if ((tile = world.getTileEntity(pos)) instanceof IThermalHandler)
		{
			temp = getRealHandlerTemperature((IThermalHandler) tile, Direction.Q);
		}
		else if (tile instanceof IThermalProvider)
		{
			temp = getRealHandlerTemperature(((IThermalProvider) tile).getThermalHandler(), Direction.Q);
		}
		else
		{
			temp = getEnvironmentTemperature(world, pos);
			float v;
			for (IWorldThermalHandler handler : worldCHandlers)
			{
				if ((v = handler.getTemperature(world, pos, temp)) >= 0)
				{
					temp = v;
				}
			}
		}
		if (!withNearby) return temp;
		temp *= 6;
		int c = 6;
		for (Direction direction : DIRECTIONS_3D)
		{
			BlockPos pos2 = direction.offset(pos);
			temp += getTemperature(world, pos2, false);
			++c;
		}
		temp /= c;
		return temp;
	}
	
	public static float getTempDifference(World world, BlockPos pos)
	{
		float temp = getTemperature(world, pos, false);
		float delta = 0;
		for (Direction direction : DIRECTIONS_3D)
		{
			delta += Math.abs(temp - getTemperature(world, direction.offset(pos), false));
		}
		return delta / DIRECTIONS_3D.length;
	}
	
	/**
	 * Called when can not found an block or tile to get thermal conductivity.
	 * 
	 * @param world
	 * @param pos
	 * @param state
	 * @return
	 */
	public static float getBaseThermalConductivity(World world, BlockPos pos, IBlockState state)
	{
		float a;
		for (IWorldThermalHandler handler : worldCHandlers)
		{
			if ((a = handler.getThermalConductivity(world, pos, state)) >= 0) return a;
		}
		return V.k0;
	}
	
	/**
	 * Called when can not found an block or tile to get heat capacity.
	 * 
	 * @param world
	 * @param pos
	 * @param state
	 * @return
	 */
	public static float getBaseHeatCapacity(World world, BlockPos pos, IBlockState state)
	{
		float a;
		for (IWorldThermalHandler handler : worldCHandlers)
		{
			if ((a = handler.getHeatCapacity(world, pos, state)) >= 0) return a;
		}
		return Float.MAX_VALUE;
	}
	
	public static double getThermalConductivity(World world, BlockPos pos, Direction direction)
	{
		IBlockState state;
		TileEntity tile;
		double value = -1;
		
		if ((tile = world.getTileEntity(pos)) instanceof IThermalHandler) value = ((IThermalHandler) tile).getThermalConductivity(direction);
		else if (tile instanceof IThermalProvider) value = ((IThermalProvider) tile).getThermalHandler().getThermalConductivity(direction);
		state = world.getBlockState(pos);
		if (value < 0 && state.getBlock() instanceof IThermalCustomBehaviorBlock) value = ((IThermalCustomBehaviorBlock) state.getBlock()).getThermalConduct(world, pos, state);
		
		return value > 0 ? value : getBaseThermalConductivity(world, pos, state.getActualState(world, pos));
	}
	
	public static double getHeatCapacity(World world, BlockPos pos, Direction direction)
	{
		TileEntity tile;
		double value = -1;
		
		if ((tile = world.getTileEntity(pos)) instanceof IThermalHandler)
			value = ((IThermalHandler) tile).getHeatCapacity(direction);
		else if (tile instanceof IThermalProvider)
			value = ((IThermalProvider) tile).getThermalHandler().getHeatCapacity(direction);
		//		if (value < 0 && state.getBlock() instanceof IThermalCustomBehaviorBlock)
		//			value = ((IThermalCustomBehaviorBlock)
		//					state.getBlock()).getHeatCapacity(world, pos, state);
		
		return value > 0 ? value :
			getBaseHeatCapacity(world, pos, world.getBlockState(pos).getActualState(world, pos));
	}
	
	public static void sendHeatToBlock(World world, BlockPos pos, Direction direction, double amount)
	{
		IBlockState state;
		if ((state = world.getBlockState(pos)).getBlock() instanceof IThermalCustomBehaviorBlock)
		{
			((IThermalCustomBehaviorBlock) state.getBlock()).onHeatChanged(world, pos, direction, amount);
		}
	}
	
	public static final ThermalNet INSTANCE = new ThermalNet();
	
	private Map<Integer, Local> netMap = new HashMap<>();
	
	/**
	 * The thermal energy net use <code>STANDARD_LANG</code> energy formatter,
	 * for using in heat exchange, internal energy store. Although temperature,
	 * heat capacity, some others data use <code>float</code> or
	 * <code>double</code> as format.
	 * <p>
	 * This is because the heat capacity is more larger than energy unit, it
	 * needn't use a float value to calculate when energy transfer.
	 */
	@Override
	public final Energy getEnergyFormat()
	{
		return EnergyHandler.STANDARD_LONG;
	}
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}
	
	@Override
	public void add(Object tile)
	{
		if (tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).add((IThermalHandler) tile);
		}
		else if (tile instanceof IThermalProvider)
		{
			getNet(((IThermalProvider) tile).world(), false).add(((IThermalProvider) tile).getThermalHandler());
		}
		if (tile instanceof IThermalHandlerBox)
		{
			getNet(((IThermalHandlerBox) tile).world(), false).add((IThermalHandlerBox) tile);
		}
	}
	
	@Override
	public void remove(Object tile)
	{
		if (tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).remove((IThermalHandler) tile);
		}
		else if (tile instanceof IThermalProvider)
		{
			getNet(((IThermalProvider) tile).world(), false).remove(((IThermalProvider) tile).getThermalHandler());
		}
		if (tile instanceof IThermalHandlerBox)
		{
			getNet(((IThermalHandlerBox) tile).world(), false).remove((IThermalHandlerBox) tile);
		}
	}
	
	@Override
	public void mark(Object tile)
	{
		// Do nothing.
	}
	
	@Override
	public void reload(Object tile)
	{
		if (tile instanceof IThermalHandler)
		{
			getNet(((IThermalHandler) tile).world(), false).reload((IThermalHandler) tile);
		}
		else if (tile instanceof IThermalProvider)
		{
			getNet(((IThermalProvider) tile).world(), false).reload(((IThermalProvider) tile).getThermalHandler());
		}
		if (tile instanceof IThermalHandlerBox)
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
		// Do nothing.
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
		if (!create) return this.netMap.getOrDefault(world.provider.getDimension(), Local.instance);
		if (!this.netMap.containsKey(world.provider.getDimension()))
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
		
		private final Map<BlockPos, IThermalHandler>			map					= new HashMap<>();
		private final Map<ChunkPos, List<IThermalHandlerBox>>	map2				= new HashMap<>();
		private final List<IThermalHandler>						cachedList			= new ArrayList<>();
		private BlockPos										cachedPos			= BlockPos.ORIGIN;
		private final Map<BlockPos, EnumModifyFlag>				cachedChangedTile	= new HashMap<>();
		private final Map<IThermalHandlerBox, EnumModifyFlag>	cacheChangedBoxList	= new HashMap<>();
		
		public Local(World world)
		{
			this.world = world;
		}
		
		public void add(IThermalHandlerBox box)
		{
			if (this.world == null) return;
			if (this.isUpdating)
			{
				this.cacheChangedBoxList.put(box, EnumModifyFlag.add);
			}
			else
			{
				List<ChunkPos> chunks = box.chunks();
				if (chunks.isEmpty()) return;
				for (ChunkPos chunk : chunks)
				{
					L.put(this.map2, chunk, box);
				}
			}
		}
		
		public void remove(IThermalHandlerBox box)
		{
			if (this.world == null) return;
			if (this.isUpdating)
			{
				this.cacheChangedBoxList.put(box, EnumModifyFlag.remove);
			}
			else
			{
				List<ChunkPos> chunks = box.chunks();
				if (chunks.isEmpty()) return;
				for (ChunkPos chunk : chunks)
				{
					L.remove(this.map2, chunk, box);
				}
			}
		}
		
		public void reload(IThermalHandlerBox box)
		{
			if (this.world == null) return;
			if (this.isUpdating)
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
			if (this.world == null) return;
			if (this.isUpdating)
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
			if (this.world == null) return;
			if (this.isUpdating)
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
			if (this.world == null || this.world.isRemote || this.world.getWorldType() == WorldType.DEBUG_WORLD) return;
			// Initialize cache.
			this.cachedList.clear();
			// Switch update flag to true, to prevent remove element in map when iterating.
			this.isUpdating = true;
			boolean updated = true;
			try
			{
				// Cached current.
				long[] current = new long[DIRECTIONS_3D.length];
				// Update tile.
				for (IThermalHandler tile : this.map.values())
				{
					Arrays.fill(current, 0);
					updateTileInNet(tile, current);
					// Send cached energy.
					tile.onHeatChange(current);
					this.cachedList.add(tile);
				}
			}
			catch (OutOfMemoryError error)
			{
				if (Nebula.debug)
				{
					Log.warn("The net update is out of memory, " + "this error will prevent update.", error);
				}
				updated = false;
			}
			if (updated)
			{
				updateMarkedTile();
				this.isUpdating = false;
				this.cachedList.clear();
			}
		}
		
		private void updateMarkedTile()
		{
			for (Entry<BlockPos, EnumModifyFlag> entry : this.cachedChangedTile.entrySet())
			{
				BlockPos pos = entry.getKey();
				switch (entry.getValue())
				{
				case add:
					TileEntity tile = this.world.getTileEntity(pos);
					if (tile instanceof IThermalHandler)
					{
						this.map.put(pos, (IThermalHandler) tile);
					}
					else if (tile instanceof IThermalProvider)
					{
						this.map.put(pos, ((IThermalProvider) tile).getThermalHandler());
					}
					break;
				case remove:
					this.map.remove(pos);
					break;
				case reload:
					this.map.remove(pos);
					tile = this.world.getTileEntity(entry.getKey());
					if (tile instanceof IThermalHandler)
					{
						this.map.put(pos, (IThermalHandler) tile);
					}
					else if (tile instanceof IThermalProvider)
					{
						this.map.put(pos, ((IThermalProvider) tile).getThermalHandler());
					}
					break;
				case mark :
					break;
				}
			}
			for (Entry<IThermalHandlerBox, EnumModifyFlag> entry : this.cacheChangedBoxList.entrySet())
			{
				switch (entry.getValue())
				{
				case add:
					for (ChunkPos pos : entry.getKey().chunks())
					{
						L.put(this.map2, pos, entry.getKey());
					}
					break;
				case remove:
					for (ChunkPos pos : entry.getKey().chunks())
					{
						L.remove(this.map2, pos, entry.getKey());
					}
					break;
				case reload:
					for (ChunkPos pos : entry.getKey().chunks())
					{
						L.put(this.map2, pos, entry.getKey());
					}
					for (ChunkPos pos : entry.getKey().chunks())
					{
						L.remove(this.map2, pos, entry.getKey());
					}
					break;
				default:// case mark :
					break;
				}
			}
			this.cachedChangedTile.clear();
			this.cacheChangedBoxList.clear();
		}
		
		private void updateTileInNet(IThermalHandler Hs, long[] W)
		{
			// Get cache current from each direction.
			for (int i = 0; i < DIRECTIONS_3D.length; ++i)
			{
				Direction d1 = DIRECTIONS_3D[i], d2 = DIRECTIONS_3D[OPPISITE[i]];
				if (Hs.canConnectTo(d1))
				{
					this.cachedPos = d1.offset(Hs.pos());
					float T1 = getRealHandlerTemperature(Hs, d1), T2;
					double
					k1 = Hs.getThermalConductivity(d1), k2,
					c1 = Hs.getHeatCapacity(d1), c2,
					sigma;
					try
					{
						if (this.map.containsKey(this.cachedPos))
						{
							IThermalHandler Ht = this.map.get(this.cachedPos);
							if (!Ht.canConnectTo(d2) || this.cachedList.contains(Ht))
								//To check if this current is already calculated.
								continue;
							T2 = getRealHandlerTemperature(Ht, d2);
							if (L.similar(T1, T2)) continue;
							k2 = Ht.getThermalConductivity(d2);
							c2 = Ht.getHeatCapacity(d2);
							sigma = logarithmicMean(k1, k2);
							if ((W[i] = (long) (sigma * (T2 - T1) * expm1(sigma * (1 / c1 + 1 / c2)))) != 0)
							{
								Ht.onHeatChange(d2, -W[i]);
							}
						}
						else
						{
							T2 = getTemperature(this.world, this.cachedPos, false);
							if (L.similar(T1, T2)) continue;
							k2 = getThermalConductivity(this.world, this.cachedPos, d2);
							c2 = getHeatCapacity(this.world, this.cachedPos, d2);
							sigma = logarithmicMean(k1, k2);
							if ((W[i] = (long) (sigma * (T2 - T1) * expm1(sigma * (1 / c1 + 1 / c2)))) != 0)
							{
								IThermalHandlerBox box = getBoxAtPos(this.cachedPos);
								if (box == null || !box.onHeatChange(Hs.pos(), this.cachedPos, d2, -W[i]))
								{
									sendHeatToBlock(this.world, this.cachedPos, d2, -W[i]);
								}
							}
						}
					}
					catch (OutOfMemoryError | RuntimeException e)
					{
						if (Nebula.debug)
						{
							Log.logger().error("Catching an exception during emmit thermal energy.", e);
						}
					}
				}
			}
		}
		
		public IThermalHandlerBox getBoxAtPos(BlockPos pos)
		{
			return L.get(this.map2.getOrDefault(new ChunkPos(pos), ImmutableList.of()), box -> box.containPosition(pos));
		}
	}
}
