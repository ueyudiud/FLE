package farcore.lib.model.block;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The extended block state map, included vanilla state map abilities.<br>
 * Also can make custom variant key for block for sub blocks.
 * @author ueyudiud
 *
 */
@SideOnly(Side.CLIENT)
public class StateMapperExt extends StateMapperBase
{
	private static final Comparator<IProperty<?>> PROPERTY_COMPARATOR = (IProperty<?> property1, IProperty<?> property2) -> property1.getName().compareTo(property2.getName());
	
	private String path;
	private IProperty<?> fileProperty;
	private List<IProperty> ignore;
	private String variantsKey;
	private String variantsValue;
	
	private IProperty<String> fakeProperty;
	
	public StateMapperExt(String modid, String path, IProperty property1, IProperty...properties)
	{
		this(modid + ":" + path, property1, properties);
	}
	public StateMapperExt(String path, IProperty property1, IProperty...properties)
	{
		this.path = path;
		fileProperty = property1;
		ignore = ImmutableList.copyOf(properties);
	}

	public void markVariantProperty()
	{
		markVariantProperty(createFakeProperty(variantsKey, variantsValue));
	}
	
	public void markVariantProperty(IProperty<String> property)
	{
		fakeProperty = property;
	}

	public void setVariants(String key, String value)
	{
		variantsKey = key;
		variantsValue = value;
	}

	@Override
	public ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		Map<IProperty<?>, Comparable<?>> map = new HashMap(state.getProperties());
		if (variantsKey != null)
		{
			if (fakeProperty == null)
			{
				markVariantProperty();
			}
			map.put(fakeProperty, variantsValue);
		}
		
		String key = path;
		if(fileProperty != null)
		{
			key += "/" + removeAndGet(fileProperty, map);
		}
		else
		{
			;
		}
		for(IProperty property : ignore)
		{
			map.remove(property);
		}

		map = ImmutableSortedMap.copyOf(map, PROPERTY_COMPARATOR);

		ModelResourceLocation location = new ModelResourceLocation(path, getPropertyKey(map));
		return location;
	}
	
	public static IProperty<String> createFakeProperty(String key, String value)
	{
		return new PropertyHelper<String>(key, String.class)
		{
			@Override
			public Collection<String> getAllowedValues() { return ImmutableList.of(value); }
			@Override
			public Optional<String> parseValue(String value) { return Optional.of(value); }
			@Override
			public String getName(String value) { return value; }
		};
	}
	
	public static String getPropertyKey(Map<IProperty<?>, Comparable<?>> values)
	{
		if(!(values instanceof ImmutableSortedMap))
		{
			values = ImmutableSortedMap.copyOf(values, PROPERTY_COMPARATOR);
		}
		StringBuilder builder = new StringBuilder();
		
		for (Entry<IProperty<?>, Comparable<?>> entry : values.entrySet())
		{
			if (builder.length() != 0)
			{
				builder.append(",");
			}
			IProperty property = entry.getKey();
			builder.append(property.getName()).append("=").append(property.getName(entry.getValue()));
		}
		if (builder.length() == 0)
		{
			builder.append("normal");
		}
		return builder.toString();
	}
	
	public static <T extends Comparable<T>> String removeAndGet(IProperty<T> property, Map<IProperty<?>, Comparable<?>> map)
	{
		return property.getName((T) map.remove(property));
	}
}