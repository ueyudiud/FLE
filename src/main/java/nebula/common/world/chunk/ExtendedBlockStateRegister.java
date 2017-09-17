/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.world.chunk;

import java.io.IOException;
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
import nebula.base.IntegerMap;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.data.Misc;
import nebula.common.network.PacketBufferExt;
import nebula.common.util.A;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public enum ExtendedBlockStateRegister implements Runnable
{
	SERVER,
	CLIENT;
	
	static final List<IBlockState> TO_STATE_LIST = new ArrayList<>(1024);
	static final IntegerMap<IBlockState> TO_ID_MAP = new IntegerMap<IBlockState>(4096, 1.0F) {
		@Override
		protected int hashcode(Object object)
		{
			return Objects.toString(object).hashCode();
		}
	};
	
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
	
	public static int getCachedID(IBlockState state)
	{
		return TO_ID_MAP.containsKey(state) ? TO_ID_MAP.get(state) : -1;
	}
	
	public static IBlockState getCachedState(int id)
	{
		return id >= 0 && id < TO_STATE_LIST.size() ? TO_STATE_LIST.get(id) : Misc.AIR;
	}
	
	public static int idCapacity()
	{
		return TO_STATE_LIST.size();
	}
	
	public static void encode(PacketBufferExt output) throws IOException
	{
		waitingForBuild();
		int length = idCapacity();
		output.writeInt(length);
		for (int i = 0; i < length; ++i)
		{
			IBlockState state = TO_STATE_LIST.get(i);
			Block block = state.getBlock();
			String key = block.getRegistryName().toString();
			int meta;
			if(block instanceof IExtendedDataBlock)
			{
				meta = ((IExtendedDataBlock) block).getDataFromState(state);
			}
			else
			{
				meta = block.getMetaFromState(state);
			}
			output.writeString(key + ":" + meta);
		}
	}
	
	public static void decode(PacketBufferExt input) throws IOException
	{
		TO_STATE_LIST.clear();
		TO_ID_MAP.clear();
		int len = input.readInt();
		List<String> list = new ArrayList<>();
		Map<Integer, Integer> intMap = new HashMap<>();
		for (int i = 0; i < len; ++i)
		{
			String key = input.readString(999);
			String[] split = key.split(":");
			if(split.length != 3) throw new IOException("Wrong block state key.");
			IBlockState state;
			try
			{
				ResourceLocation location = new ResourceLocation(split[0], split[1]);
				Block block = Block.REGISTRY.getObject(location);
				if(block == Blocks.AIR && i != 0)
					throw new RuntimeException();
				int meta = Integer.parseInt(split[2]);
				if (block instanceof IExtendedDataBlock)
				{
					state = ((IExtendedDataBlock) block).getStateFromData(meta);
				}
				else
				{
					state = block.getStateFromMeta(meta);
				}
			}
			catch (Exception exception)
			{
				list.add(key);
				continue;
			}
			TO_STATE_LIST.add(state);
			intMap.put(getStateData(state), i);
		}
		for (Block block : Block.REGISTRY)
		{
			int id = Block.REGISTRY.getIDForObject(block) << 24;
			for(IBlockState state : block.getBlockState().getValidStates())
			{
				int meta;
				if(block instanceof IExtendedDataBlock)
				{
					meta = ((IExtendedDataBlock) block).getDataFromState(state);
				}
				else
				{
					meta = block.getMetaFromState(state);
				}
				Integer i = intMap.get(id | meta);
				if(i != null)
				{
					TO_ID_MAP.put(state, i);
				}
			}
		}
	}
	
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
		waitingForBuild();
		(thread = new Thread(SERVER, "State Map Thread")).start();
	}
	
	@Override
	public void run()
	{
		switch (this)
		{
		case SERVER :
		default:
			buildStateMap();
			break;
		case CLIENT :
			break;
		}
	}
	
	void buildStateMap()
	{
		TO_ID_MAP.clear();
		TO_STATE_LIST.clear();
		Log.reset();
		
		for (Block block : Block.REGISTRY)
		{
			try
			{
				if(block instanceof IExtendedDataBlock)
				{
					((IExtendedDataBlock) block).registerStateToRegister(this);
				}
				else
				{
					registerDefaultBlockStateMap(block);
				}
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
			IBlockState state;
			IBlockState[] states = new IBlockState[16];
			for (int i = 0; i < 16; ++i)//Default meta capacity is 16.
			{
				try
				{
					state = block.getStateFromMeta(i);
					if (!A.contain(states, state))
					{
						states[i] = state;
					}
				}
				catch (RuntimeException exception)
				{
					;
				}
			}
			Map<IBlockState, List<IBlockState>> stateMap = new HashMap<>();
			boolean flag = false;
			for (IBlockState state1 : container.getValidStates())
			{
				try
				{
					int meta = block.getMetaFromState(state1);
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
	
	public void registerState(IBlockState state)
	{
		registerStateMap(state, state);
	}
	
	public void registerStateMap(IBlockState source, Collection<IBlockState> castable)
	{
		int id = TO_STATE_LIST.size();
		TO_STATE_LIST.add(source);
		for (IBlockState state : castable) TO_ID_MAP.put(state, id);
	}
	
	public void registerStateMap(IBlockState source, IBlockState...castable)
	{
		int id = TO_STATE_LIST.size();
		TO_STATE_LIST.add(source);
		for (IBlockState state : castable) TO_ID_MAP.put(state, id);
	}
}