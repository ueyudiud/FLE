/*
 * copyright© 2016-2017 ueyudiud
 */
package ed.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import nebula.Log;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.data.Misc;
import nebula.common.world.IBlockDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * The block state register.
 * @author ueyudiud
 */
public enum ExtendedBlockStateRegister implements IBlockDataProvider
{
	INSTANCE;
	
	static final Map<Block, StateDelegate> DELEGATES = new HashMap<>(4096);
	static final TreeMap<Integer, StateDelegate> INT_TO_DELEGATE = new TreeMap<>();
	
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
	
	/**
	 * Get block data (meta) by block state.
	 * @param state the block state.
	 * @return the data of block state.
	 */
	public int getStateData(IBlockState state)
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
	public @Nonnull IBlockState getStateFromData(int data)
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
	public int getNetworkID(IBlockState state)
	{
		StateDelegate delegate = DELEGATES.get(state.getBlock());
		return delegate.id + delegate.getMeta(state);
	}
	
	/**
	 * Get cached block state by network id.
	 * @param id the network id.
	 * @return the block state.
	 */
	public IBlockState getStateFromNetworkID(int id)
	{
		StateDelegate delegate = INT_TO_DELEGATE.floorEntry(id).getValue();
		return delegate.get(id - delegate.id);
	}
	
	/**
	 * Get cached network id count.
	 * @return the id size.
	 */
	public static int idCapacity()
	{
		return INSTANCE.capacity;
	}
	
	//Internal method and fields, do not use.
	private int capacity;
	
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
			Log.trace("#{}#", block.getRegistryName());
			delegate.logInformation();
		}
		Log.info("Remapping finished.");
	}
	
	public void remapping()
	{
		Log.info("Remapping state map.");
		INT_TO_DELEGATE.clear();
		this.capacity = 0;
		for (Block block : Block.REGISTRY)
		{
			StateDelegate delegate = DELEGATES.get(block);
			if (delegate == null)//For block id missing.
				continue;
			delegate.id = this.capacity;
			INT_TO_DELEGATE.put(delegate.id, delegate);
			this.capacity += delegate.capacity();
			Log.trace("#{}#", block.getRegistryName());
			delegate.logInformation();
		}
		Log.info("Remapping finished.");
	}
	//Internal method end.
}