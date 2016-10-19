package farcore.lib.block.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Cartesian;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStateContainerM extends ExtendedBlockState
{
	private static final IProperty<?>[] EMPTY1 = new IProperty[0];
	private static final Predicate<Optional> EMPTY = Predicates.equalTo(Optional.absent());
	private static final Joiner COMMA_JOINER = Joiner.on(',');
	private static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function < Entry < IProperty<?>, Comparable<? >> , String > ()
	{
		@Override
		@Nullable
		public String apply(@Nullable Entry < IProperty<?>, Comparable<? >> entry)
		{
			if (entry == null)
				return "<NULL>";
			else
			{
				IProperty<?> property = entry.getKey();
				return property.getName() + "=" + this.getPropertyName(property, (Comparable)entry.getValue());
			}
		}
		private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry)
		{
			return property.getName((T)entry);
		}
	};

	public static <T extends Comparable<T>> T cycleValue(IProperty<T> property, T currentValue)
	{
		Iterator<T> iterator = property.getAllowedValues().iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().equals(currentValue))
				return iterator.hasNext() ? iterator.next() : property.getAllowedValues().iterator().next();
		}
		return iterator.next();
	}
	
	protected final ImmutableMap<String, IProperty<?>> propertyMap;
	protected final ImmutableMap<String, IUnlistedProperty<?>> unlistedPropertyMap;
	protected final Block[] subblocks;
	protected final java.util.function.Function<IBlockState, Byte> subblockSelector;
	protected final ImmutableList<IBlockState> validStates;
	protected final BlockState instance;

	public BlockStateContainerM(Block blockIn, IFarProperty<?>...properties)
	{
		this(blockIn, properties, new IUnlistedProperty[0]);
	}
	public BlockStateContainerM(Block blockIn, IFarProperty<?>[] properties,
			IUnlistedProperty<?>[] unlistedProperties)
	{
		this(blockIn, properties, unlistedProperties, null, null);
	}
	public BlockStateContainerM(Block blockIn, IFarProperty<?>[] properties,
			IUnlistedProperty<?>[] unlistedProperties, Block[] blocklist, java.util.function.Function<IBlockState, Byte> subblockSelector)
	{
		super(blockIn, EMPTY1, unlistedProperties);
		Map<String, IProperty<?>> map = new HashMap();
		for (IFarProperty<?> iproperty : properties)
		{
			validateProperty(blockIn, iproperty);
			map.put(iproperty.getName(), iproperty);
		}
		ImmutableSortedMap.Builder<String, IUnlistedProperty<?>> builder = ImmutableSortedMap.naturalOrder();
		for (IUnlistedProperty<?> property : unlistedProperties)
		{
			builder.put(property.getName(), property);
		}
		
		propertyMap = ImmutableSortedMap.<String, IProperty<?>>copyOf(map);
		unlistedPropertyMap = builder.build();
		subblocks = subblockSelector == null ? new Block[]{blockIn} : blocklist;
		this.subblockSelector = subblockSelector;
		Map<Map<IProperty<?>, Comparable<?>>, BlockState> map2 = new LinkedHashMap();
		List<BlockState> list1 = new ArrayList();
		
		for (List<Comparable<?>> list : Cartesian.cartesianProduct(getAllowedValues()))
		{
			Map<IProperty<?>, Comparable<?>> map1 = MapPopulator.<IProperty<?>, Comparable<?>>createMap(propertyMap.values(), list);
			BlockState state = createState(ImmutableMap.copyOf(map1), unlistedProperties);
			map2.put(map1, state);
			list1.add(state);
		}
		
		for (BlockState state : list1)
		{
			state.buildPropertyValueTable(map2);
		}

		BlockState instance = list1.get(0);
		for (IFarProperty<?> property : properties)
		{
			instance = withValue(instance, (IFarProperty) property);
		}
		this.instance = instance;
		
		validStates = ImmutableList.<IBlockState>copyOf(list1);
	}

	private <T extends Comparable<T>> BlockState withValue(BlockState state, IFarProperty<T> property)
	{
		return state.withProperty(property, property.instance());
	}
	
	private BlockState createState(ImmutableMap<IProperty<?>, Comparable<?>> properties,
			IUnlistedProperty<?>[] unlistedProperties)
	{
		ImmutableMap.Builder<IUnlistedProperty<?>, Optional<?>> builder = ImmutableMap.builder();
		for(IUnlistedProperty<?> p : unlistedProperties)
		{
			builder.put(p, Optional.absent());
		}
		return createState(properties, builder.build());
	}

	public BlockState createState(ImmutableMap<IProperty<?>, Comparable<?>> properties,
			ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
	{
		return new BlockState(properties, unlistedProperties);
	}

	private List<Iterable<Comparable<?>>> getAllowedValues()
	{
		List<Iterable<Comparable<?>>> list = new ArrayList();
		
		for (IProperty<?> iproperty : propertyMap.values())
		{
			list.add(((IProperty) iproperty).getAllowedValues());
		}
		
		return list;
	}
	
	@Override
	public IBlockState getBaseState()
	{
		return instance;
	}
	
	@Override
	public Collection<IProperty<?>> getProperties()
	{
		return propertyMap.values();
	}
	
	@Override
	public IProperty<?> getProperty(String propertyName)
	{
		return super.getProperty(propertyName);
	}
	
	@Override
	public ImmutableList<IBlockState> getValidStates()
	{
		return validStates;
	}
	
	public class BlockState implements IFarBlockState, IExtendedBlockState
	{
		protected final ImmutableMap<IProperty<?>, Comparable<?>> properties;
		protected final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;
		protected ImmutableTable<IProperty<?>, Comparable<?>, BlockState> stateTable;
		protected final IBlockState cleanState;

		public BlockState(ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
		{
			this.properties = properties;
			this.unlistedProperties = unlistedProperties;
			cleanState = this;
		}
		BlockState(BlockState state, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
		{
			properties = state.properties;
			stateTable = state.stateTable;
			this.unlistedProperties = unlistedProperties;
			cleanState = state.cleanState;
		}

		public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockState> map)
		{
			if (stateTable != null)
				throw new IllegalStateException();
			else
			{
				Table<IProperty<?>, Comparable<?>, BlockState> table = HashBasedTable.<IProperty<?>, Comparable<?>, BlockState>create();

				for (Entry < IProperty<?>, Comparable<? >> entry : properties.entrySet())
				{
					IProperty<?> iproperty = entry.getKey();

					for (Comparable<?> comparable : iproperty.getAllowedValues())
					{
						if (comparable != entry.getValue())
						{
							table.put(iproperty, comparable, map.get(getPropertiesWithValue(iproperty, comparable)));
						}
					}
				}

				stateTable = ImmutableTable.<IProperty<?>, Comparable<?>, BlockState>copyOf(table);
			}
		}

		private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> property, Comparable<?> value)
		{
			Map<IProperty<?>, Comparable<?>> map = new HashMap(properties);
			map.put(property, value);
			return map;
		}
		
		@Override
		public Collection<IProperty<?>> getPropertyNames()
		{
			return propertyMap.values();
		}

		@Override
		public <T extends Comparable<T>> T getValue(IProperty<T> property)
		{
			return (T) (properties.get(property));
		}

		@Override
		public <T extends Comparable<T>, V extends T> BlockState withProperty(IProperty<T> property, V value)
		{
			return Iterables.all(unlistedProperties.values(), EMPTY) ? stateTable.get(property, value) : new BlockState(this, unlistedProperties);
		}
		
		@Override
		public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, V value)
		{
			if(!unlistedProperties.containsKey(property))
				throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
			if(!property.isValid(value))
				throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(getBlock()) + ", it is not an allowed value");
			Map<IUnlistedProperty<?>, Optional<?>> map = new HashMap(unlistedProperties);
			map.put(property, Optional.of(value));
			if (Iterables.all(map.values(), EMPTY))
			{
				IProperty<?> property1 = propertyMap.values().iterator().next();
				return stateTable.get(property1, getValue(property1));
			}
			return new BlockState(this, ImmutableMap.copyOf(map));
		}

		@Override
		public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property)
		{
			return withProperty(property, cycleValue(property, getValue(property)));
		}

		@Override
		public ImmutableMap<IProperty<?>, Comparable<?>> getProperties()
		{
			return properties;
		}

		@Override
		public Block getBlock()
		{
			return subblockSelector == null ? subblocks[0] : subblocks[subblockSelector.apply(this)];
		}
		
		@Override
		public Collection<IUnlistedProperty<?>> getUnlistedNames()
		{
			return unlistedPropertyMap.values();
		}

		@Override
		public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties()
		{
			return unlistedProperties;
		}
		
		@Override
		public <V> V getValue(IUnlistedProperty<V> property)
		{
			Optional<V> optional = (Optional<V>) unlistedProperties.get(property);
			return optional.isPresent() ? optional.get() : null;
		}

		@Override
		public IBlockState getClean()
		{
			return cleanState;
		}
		
		@Override
		public IBlockState withMirror(Mirror mirrorIn)
		{
			return this;
		}
		
		@Override
		public IBlockState withRotation(Rotation rot)
		{
			return this;
		}
		
		@Override
		public String toString()
		{
			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append(Block.REGISTRY.getNameForObject(subblocks[subblockSelector.apply(this)]));
			
			if (!getProperties().isEmpty())
			{
				stringbuilder.append("[");
				COMMA_JOINER.appendTo(stringbuilder, Iterables.<Entry<IProperty<?>, Comparable<?>>, String>transform(properties.entrySet(), MAP_ENTRY_TO_STRING));
				stringbuilder.append("]");
			}

			return stringbuilder.toString();
		}
	}
}