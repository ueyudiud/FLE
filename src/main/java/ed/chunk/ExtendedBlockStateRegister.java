/*
 * copyright© 2016-2017 ueyudiud
 */
package ed.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.Log;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.data.Misc;
import nebula.common.world.IBlockDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * The block state register.
 * 
 * @author ueyudiud
 */
public enum ExtendedBlockStateRegister implements IBlockDataProvider
{
	INSTANCE;
	
	static final Map<Block, StateDelegate>			DELEGATES		= new HashMap<>(4096);
	/**
	 * The tree map can use to search floor id from object. the delegates used
	 * <code>blockNetworkId + stateMeta</code> to allocate each
	 * networkid-blockstate mapping. Use <code>
	 * INT_TO_DELEGATE.floorEntry(id)<code> to get delegate by state id.
	 */
	static final TreeMap<Integer, StateDelegate>	INT_TO_DELEGATE	= new TreeMap<>();
	
	/**
	 * Get block data (meta) by block state.
	 * 
	 * @param state the block state.
	 * @return the data of block state. If <tt>state</tt> is <code>null</code>,
	 *         air block id will return.
	 */
	public int getStateData(@Nullable IBlockState state)
	{
		if (state == null) state = Misc.AIR;
		Block block = state.getBlock();
		int meta;
		try
		{
			meta = ((block instanceof IExtendedDataBlock) ? ((IExtendedDataBlock) block).getDataFromState(state) : block.getMetaFromState(state));
		}
		catch (Exception exception)
		{
			return 0;
		}
		return Block.REGISTRY.getIDForObject(block) << 20 | meta;
	}
	
	/**
	 * Get block state from data (meta), if the data is invalid
	 * <code>null</code> will be returned.
	 * 
	 * @param data the block data.
	 * @return the state of data.
	 */
	public @Nonnull IBlockState getStateFromData(int data)
	{
		Block block = Block.REGISTRY.getObjectById((data >>> 20) & 0xFFF);
		if (block == null) return Misc.AIR;
		try
		{
			IBlockState state = ((block instanceof IExtendedDataBlock) ? ((IExtendedDataBlock) block).getStateFromData(data & 0xFFFFF) : block.getStateFromMeta(data & 0xF));
			return state == null ? Misc.AIR : state;
		}
		catch (Exception exception)
		{
			return Misc.AIR;
		}
	}
	
	/**
	 * Get cached block state network id.
	 * 
	 * @param state the state.
	 * @return the cached network id.
	 */
	public int getNetworkID(@Nonnull IBlockState state)
	{
		StateDelegate delegate = DELEGATES.get(state.getBlock());
		return delegate.id + delegate.getMeta(state);
	}
	
	/**
	 * Get cached block state by network id.
	 * 
	 * @param id the network id.
	 * @return the block state, if no state found, the air block state will
	 *         return.
	 */
	public @Nonnull IBlockState getStateFromNetworkID(int id)
	{
		Entry<Integer, StateDelegate> entry = INT_TO_DELEGATE.floorEntry(id);
		if (entry == null) return Misc.AIR;
		StateDelegate delegate = entry.getValue();
		return delegate.get(id - delegate.id);
	}
	
	/**
	 * Get cached network id count.
	 * 
	 * @return the cached id size.
	 */
	public static int idCapacity()
	{
		return INSTANCE.capacity;
	}
	
	// Internal method and fields, do not use.
	private int capacity;
	
	/**
	 * INTERNAL METHOD, DO NOT USE!
	 * <p>
	 * Build state map by setting up block-delegate mapping.
	 */
	public void buildStateMap()
	{
		Log.info("Mapping state map.");
		DELEGATES.clear();
		this.capacity = 0;
		for (Block block : Block.REGISTRY)
		{
			StateDelegate delegate = StateDelegate.create(block);
			DELEGATES.put(block, delegate);
			delegate.id = this.capacity;
			this.capacity += delegate.capacity();
			//			Log.trace("#{}#", block.getRegistryName());
			//			delegate.logInformation();
		}
		Log.info("Remapping finished.");
	}
	
	/**
	 * INTERNAL METHOD, DO NOT USE!
	 * <p>
	 * Allocate blockNetworkID to each block state, the id used tessellation
	 * method.
	 */
	public void remapping()
	{
		Log.info("Remapping state map.");
		INT_TO_DELEGATE.clear();
		this.capacity = 0;
		for (Block block : Block.REGISTRY)
		{
			StateDelegate delegate = DELEGATES.get(block);
			if (delegate == null)// For block id missing.
				continue;
			delegate.id = this.capacity;
			INT_TO_DELEGATE.put(delegate.id, delegate);
			this.capacity += delegate.capacity();
			//			Log.trace("#{}#", block.getRegistryName());
			//			delegate.logInformation();
		}
		Log.info("Remapping finished.");
	}
	// Internal method end.
}
