/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.world.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;

import nebula.Log;
import nebula.base.Cache;
import nebula.base.IntegerMap;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.data.Misc;
import nebula.common.util.L;
import nebula.common.util.Sides;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

/**
 * The block state register.
 * @author ueyudiud
 */
public enum ExtendedBlockStateRegister implements Runnable
{
	INSTANCE;
	
	/*
	 * Block Data information:
	 * The block data is number id to find each block state.<p>
	 * 
	 * It contains block id and meta id combine to a <code>int</code> id.<p>
	 * 
	 * The first 12 bits (0 to 4095) is block id, which is determined by Forge.
	 * The last 20 bits (0 to 1048575) is meta id, which is expanded by Nebula,
	 * but for most block, it only has 4 bits (0 to 15) to store meta, to get
	 * more meta slots, you should let block implements IExtendedDataBlock.<p>
	 */
	
	static final Map<Integer, IBlockState> TO_STATE_LIST = new HashMap<>(1024);
	static final IntegerMap<IBlockState> TO_ID_MAP = new IntegerMap<IBlockState>(4096, 1.0F)
	{
		@Override
		protected int hashcode(Object object)
		{
			return Objects.toString(object).hashCode();
		}
	};
	
	/**
	 * Get block data (meta) by block state.
	 * @param state the block state.
	 * @return the data of block state.
	 */
	public static int getStateData(IBlockState state)
	{
		if (state == null) state = Misc.AIR;
		Block block = state.getBlock();
		int meta;
		try
		{
			meta = ((block instanceof IExtendedDataBlock) ?
					((IExtendedDataBlock) block).getDataFromState(state) :
						block.getMetaFromState(state));
		}
		catch (Exception exception)
		{
			return 0;
		}
		return Block.REGISTRY.getIDForObject(block) << 20 | meta;
	}
	
	/**
	 * Get block state from data (meta), if
	 * the data is invalid <code>null</code> will
	 * be returned.
	 * @param data the block data.
	 * @return the state of data.
	 */
	public static @Nonnull IBlockState getStateFromData(int data)
	{
		Block block = Block.REGISTRY.getObjectById((data >> 20) & 0xFFF);
		if (block == null) return Misc.AIR;
		try
		{
			IBlockState state = ((block instanceof IExtendedDataBlock) ?
					((IExtendedDataBlock) block).getStateFromData(data & 0xFFFFF) :
						block.getStateFromMeta(data & 0xF));
			return state == null ? Misc.AIR : state;
		}
		catch (Exception exception)
		{
			return Misc.AIR;
		}
	}
	
	/**
	 * Get cached block state network id.
	 * @param state the state.
	 * @return the cached network id.
	 */
	public static int getCachedID(IBlockState state)
	{
		return TO_ID_MAP.getOrDefault(state, -1);
	}
	
	/**
	 * Get cached block state by network id.
	 * @param id the network id.
	 * @return the block state.
	 */
	public static IBlockState getCachedState(int id)
	{
		return TO_STATE_LIST.getOrDefault(id, Misc.AIR);
	}
	
	/**
	 * Get cached network id count.
	 * @return the id size.
	 */
	public static int idCapacity()
	{
		return TO_STATE_LIST.size();
	}
	
	//Internal method, do not use.
	public static Cache<Boolean> built = new Cache<>();
	private static volatile Thread thread = null;
	
	private static void waitingForBuild()
	{
		if (thread != null && thread.isAlive())
		{
			try
			{
				Log.info("Wating last building state map end.");
				thread.join();
			}
			catch (InterruptedException exception)
			{
				exception.printStackTrace();
			}
		}
	}
	
	public static void buildAndSyncStateMap()
	{
		if ((Sides.isServer() && Sides.isSimulating()) || Sides.isClient())
		{
			waitingForBuild();
			(thread = new Thread(INSTANCE, "State Map Thread")).start();
		}
	}
	
	@Override
	public void run()
	{
		synchronized (built)
		{
			buildStateMap();
			built.set(true);
		}
	}
	
	private int blockid;
	private int metaid;
	
	void buildStateMap()
	{
		TO_ID_MAP.clear();
		TO_STATE_LIST.clear();
		Log.reset();
		for (Block block : Block.REGISTRY)
		{
			this.blockid = Block.REGISTRY.getIDForObject(block);
			this.metaid = 0;
			try
			{
				if (block instanceof IExtendedDataBlock)
					((IExtendedDataBlock) block).registerStateToRegister(this);
				else
					registerDefaultBlockStateMap(block);
			}
			catch (Exception exception)
			{
				throw new RuntimeException("Illegal block state registy action. Block : " + block, exception);
			}
		}
	}
	
	void registerDefaultBlockStateMap(Block block)
	{
		BlockStateContainer container = block.getBlockState();
		if (container.getProperties().isEmpty())
		{
			registerState(block.getDefaultState());
		}
		else
		{
			IBlockState[] states = new IBlockState[16];
			Map<IBlockState, List<IBlockState>> stateMap = new HashMap<>();
			boolean flag = false;
			for (IBlockState state1 : container.getValidStates())
			{
				try
				{
					int meta = block.getMetaFromState(state1) & 0xF;
					if (states[meta] == null)
						states[meta] = block.getStateFromMeta(meta);
					L.put(stateMap, states[meta], state1);
				}
				catch (RuntimeException exception)
				{
					if (!flag)
					{
						flag = true;
						Log.cache(new Object[]{block, -1});
					}
				}
			}
			for (Entry<IBlockState, List<IBlockState>> entry : stateMap.entrySet())
			{
				registerStateMap(entry.getKey(), entry.getValue());
			}
		}
	}
	//Internal method end.
	
	/**
	 * Register allowed state with all these properties cycled.
	 * @param block
	 * @param properties
	 */
	public void registerStates(Block block, IProperty<?>...properties)
	{
		if (properties.length == 0)
		{
			registerStateMap(block.getDefaultState(), block.getBlockState().getValidStates());
		}
		else
		{
			List<IProperty> list = new ArrayList<>(block.getBlockState().getProperties());
			list.removeAll(Arrays.asList(properties));
			IProperty[] properties2 = L.cast(list, IProperty.class);
			forEach1(0, properties, properties2, block.getDefaultState());
		}
	}
	
	void forEach1(int id, IProperty[] properties1, IProperty<?>[] properties2, IBlockState state)
	{
		if (id == properties1.length)
		{
			List<IBlockState> list = new ArrayList<>();
			forEach2(0, properties2, state, list);
			registerStateMap(state, list);
			return;
		}
		IProperty property = properties1[id];
		IBlockState state2 = state;
		do forEach1(id + 1, properties1, properties2, state2);
		while((state2 = state2.cycleProperty(property)) != state);
	}
	
	void forEach2(int id, IProperty<?>[] properties, IBlockState state, List<IBlockState> list)
	{
		if(id == properties.length)
		{
			list.add(state);
			return;
		}
		IProperty property = properties[id];
		IBlockState state2 = state;
		do forEach2(id + 1, properties, state2, list);
		while((state2 = state2.cycleProperty(property)) != state);
	}
	
	//The following methods only used when block state re-register.
	public void registerState(IBlockState state)
	{
		registerStateMap(state, state);
	}
	
	public void registerStateMap(IBlockState source, Collection<IBlockState> castable)
	{
		int id = ensureIDSize();
		TO_STATE_LIST.put(id, source);
		for (IBlockState state : castable) TO_ID_MAP.put(state, id);
	}
	
	public void registerStateMap(IBlockState source, IBlockState...castable)
	{
		int id = ensureIDSize();
		TO_STATE_LIST.put(id, source);
		for (IBlockState state : castable) TO_ID_MAP.put(state, id);
	}
	
	private int ensureIDSize()
	{
		if (((this.metaid + 1) & 0xFFF00000) != 0)
			throw new IndexOutOfBoundsException("Too many of meta! blockid: " + this.blockid);
		return this.blockid << 20 | this.metaid++;
	}
	//Ending
}