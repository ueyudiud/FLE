/*
 * copyright© 2016-2017 ueyudiud
 */
package ed.chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import ed.EDConfig;
import nebula.Log;
import nebula.base.A;
import nebula.base.HashIntMap;
import nebula.base.IntegerEntry;
import nebula.common.block.IBlockStateRegister;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * @author ueyudiud
 */
abstract class StateDelegate
{
	static final StateDelegate AIR = new StateDegegateSingle(Blocks.AIR);
	
	static StateDelegate create(Block block)
	{
		return block == Blocks.AIR ? AIR : block.getBlockState().getProperties().isEmpty() ? new StateDegegateSingle(block) : (EDConfig.buildStateIn || (block instanceof IExtendedDataBlock)) ? new StateDelegateExt(block) : new StateDegegateNormal(block);
	}
	
	final Block block;
	int id = -1;
	
	StateDelegate(Block block)
	{
		this.block = block;
	}
	
	void setId(int id)
	{
		this.id = id;
	}
	
	int getId()
	{
		return this.id;
	}
	
	abstract IBlockState get(int meta);
	
	abstract int getMeta(IBlockState state);
	
	abstract int capacity();
	
	abstract void logInformation();
	
	static class StateDegegateSingle extends StateDelegate
	{
		StateDegegateSingle(Block block)
		{
			super(block);
		}
		
		@Override
		IBlockState get(int meta)
		{
			return this.block.getDefaultState();
		}
		
		@Override
		int getMeta(IBlockState state)
		{
			return 0;
		}
		
		@Override
		int capacity()
		{
			return 1;
		}
		
		@Override
		void logInformation()
		{
			Log.trace("{}=>{}", this.id, this.block.getDefaultState());
			Log.trace("{}<=[{}]", this.id, this.block.getDefaultState());
		}
	}
	
	static class StateDegegateNormal extends StateDelegate
	{
		StateDegegateNormal(Block block)
		{
			super(block);
		}
		
		@Override
		IBlockState get(int meta)
		{
			try
			{
				return this.block.getStateFromMeta(meta);
			}
			catch (Exception exception)
			{
				return this.block.getDefaultState();
			}
		}
		
		@Override
		int getMeta(IBlockState state)
		{
			try
			{
				return this.block.getMetaFromState(state);
			}
			catch (Exception exception)
			{
				return 0;
			}
		}
		
		@Override
		int capacity()
		{
			return 16;
		}
		
		@Override
		void logInformation()
		{
			for (int i = 0; i < 16; ++i)
			{
				try
				{
					Log.trace("{}=>{}", this.id + i, this.block.getStateFromMeta(i));
				}
				catch (RuntimeException exception)
				{
				}
			}
			Map<Integer, List<IBlockState>> map = new HashMap<>();
			for (IBlockState state : this.block.getBlockState().getValidStates())
			{
				L.put(map, this.block.getMetaFromState(state), state);
			}
			for (Entry<Integer, List<IBlockState>> entry : map.entrySet())
			{
				Log.trace("{}<={}", this.id + entry.getKey(), entry.getValue());
			}
		}
	}
	
	static class StateDelegateExt extends StateDelegate
	{
		private static void build(final StateDelegateExt delegate, final Block block)
		{
			delegate.id_to_state = new ArrayList<>();
			delegate.state_to_id = new HashIntMap<>();
			IBlockStateRegister register = new IBlockStateRegister()
			{
				@Override
				public void registerStates(Block block, IProperty<?>...properties)
				{
					assert delegate.block == block;
					registerStates(properties);
				}
				
				@Override
				public void registerStates(IProperty<?>...properties)
				{
					if (properties.length == 0)
					{
						registerStateMap(delegate.block.getDefaultState(), delegate.block.getBlockState().getValidStates());
					}
					else
					{
						IBlockState state;
						Map<IBlockState, List<IBlockState>> map;
						
						List<IProperty> list = new ArrayList<>(delegate.block.getBlockState().getProperties());
						list.removeAll(A.argument(properties));
						
						forEach1(0, properties, L.cast(list, IProperty.class), delegate.block.getDefaultState(), map = new HashMap<>());
						
						List<IBlockState> states = new ArrayList<>(map.keySet());
						states.sort((s1, s2) -> A.compare(A.transform(properties, s1::getValue), A.transform(properties, s2::getValue)));
						
						for (int i = 0; i < states.size(); registerStateMap(state = states.get(i++), map.get(state)));
					}
				}
				
				void forEach1(int id, IProperty[] properties1, IProperty<?>[] properties2, final IBlockState state, Map<IBlockState, List<IBlockState>> map)
				{
					if (id == properties1.length)
					{
						if (properties2.length == 0)
						{
							List<IBlockState> list = new ArrayList<>();
							forEach2(0, properties2, state, list);
							map.put(state, ImmutableList.copyOf(list));
						}
						else
						{
							map.put(state, ImmutableList.of(state));
						}
					}
					else
					{
						IProperty property = properties1[id++];
						IBlockState state2 = state;
						do forEach1(id, properties1, properties2, state2, map);
						while ((state2 = state2.cycleProperty(property)) != state);
					}
				}
				
				void forEach2(int id, IProperty<?>[] properties, final IBlockState state, List<IBlockState> list)
				{
					if (id == properties.length)
					{
						list.add(state);
					}
					else
					{
						IProperty property = properties[id++];
						IBlockState state2 = state;
						do forEach2(id, properties, state2, list);
						while ((state2 = state2.cycleProperty(property)) != state);
					}
				}
				
				public void registerState(IBlockState state)
				{
					registerStateMap(state, ImmutableList.of(state));
				}
				
				public void registerStateMap(IBlockState source, IBlockState...castable)
				{
					registerStateMap(source, A.argument(castable));
				}
				
				public void registerStateMap(IBlockState source, Collection<IBlockState> castable)
				{
					int id = delegate.id_to_state.size();
					delegate.id_to_state.add(id, source);
					for (IBlockState state : castable)
						delegate.state_to_id.put(state, id);
				}
			};
			if (block instanceof IExtendedDataBlock)
				((IExtendedDataBlock) block).registerStateToRegister(register);
			else
				register.registerStates(L.cast(block.getBlockState().getProperties(), IProperty.class));
			delegate.id_to_state = ImmutableList.copyOf(delegate.id_to_state);
		}
		
		List<IBlockState>		id_to_state;
		HashIntMap<IBlockState>	state_to_id;
		
		StateDelegateExt(Block block)
		{
			super(block);
			build(this, block);
		}
		
		@Override
		IBlockState get(int meta)
		{
			return this.id_to_state.get(meta);
		}
		
		@Override
		int getMeta(IBlockState state)
		{
			return this.state_to_id.getOrDefault(state, 0);
		}
		
		@Override
		int capacity()
		{
			return this.id_to_state.size();
		}
		
		@Override
		void logInformation()
		{
			for (int i = 0; i < this.id_to_state.size(); ++i)
			{
				Log.trace("{}=>{}", this.id + i, this.id_to_state.get(i));
			}
			Map<Integer, List<IBlockState>> map = new HashMap<>();
			for (IntegerEntry<IBlockState> entry : this.state_to_id)
			{
				L.put(map, entry.getValue(), entry.getKey());
			}
			for (Entry<Integer, List<IBlockState>> entry : map.entrySet())
			{
				Log.trace("{}<={}", this.id + entry.getKey(), entry.getValue());
			}
		}
	}
}
