package nebula.client.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

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
public class StateMapperExt extends StateMapperBase implements IStateMapperExt
{
	private static final Comparator<IProperty<?>> PROPERTY_COMPARATOR = (property1, property2) -> property1.getName().compareTo(property2.getName());
	
	private String path;
	private IProperty<?> fileProperty;
	private List<IProperty> ignore;
	private String variantsKey;
	private String variantsValue;
	
	IProperty<String> fakeProperty;
	
	/**
	 * Create a new extended block state map.
	 * @param modid The mod belong.
	 * @param path The state file path.
	 * @param property1 The file split property, use this property value name as file name, no property
	 * means state mapper don't split file.
	 * @param properties The ignore properties, those properties will not present in model state.
	 */
	public StateMapperExt(String modid, String path, @Nullable IProperty property1, IProperty...properties)
	{
		this(modid + ":" + path, property1, properties);
	}
	public StateMapperExt(String path, @Nullable IProperty property1, IProperty...properties)
	{
		this.path = path;
		this.fileProperty = property1;
		this.ignore = ImmutableList.copyOf(properties);
	}
	
	public void markVariantProperty()
	{
		markVariantProperty(createFakeProperty(this.variantsKey, this.variantsValue));
	}
	
	public void markVariantProperty(IProperty<String> property)
	{
		this.fakeProperty = property;
	}
	
	/**
	 * The block may is a sub block in a block group (But for different id), set
	 * the variant entry for each block to identify them.
	 * @param key The property name.
	 * @param value The state mapper variant name.
	 */
	public void setVariants(String key, String value)
	{
		this.variantsKey = key;
		this.variantsValue = value;
	}
	
	/**
	 * The map will create a instance ModelResourceLocation for selected block state.
	 * @param state The state for location.
	 * @return The mapping location.
	 */
	@Override
	public ModelResourceLocation getLocationFromState(IBlockState state)
	{
		Map<IProperty<?>, Comparable<?>> map = new HashMap(state.getProperties());
		
		String path = modifyMap(map);
		
		map = ImmutableSortedMap.copyOf(map, PROPERTY_COMPARATOR);
		
		ModelResourceLocation location = new ModelResourceLocation(path, getPropertyKey(map));
		return location;
	}
	
	/**
	 * For it was start as {@link #getLocationFromState} and this method is not exist.
	 * But it is reported that game crashed on obf environment without this method, so
	 * I split two method.
	 */
	@Override
	protected final ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return getLocationFromState(state);
	}
	
	/**
	 * Modify property map.
	 * @param map
	 * @return
	 */
	protected String modifyMap(Map<IProperty<?>, Comparable<?>> map)
	{
		if (this.variantsKey != null)
		{
			if (this.fakeProperty == null)
			{
				markVariantProperty();
			}
			map.put(this.fakeProperty, this.variantsValue);
		}
		
		String key = this.path;
		if(this.fileProperty != null)
		{
			key += "/" + removeAndGetName(this.fileProperty, map);
		}
		
		for(IProperty property : this.ignore)
		{
			map.remove(property);
		}
		return key;
	}
	
	/**
	 * Create a fake property, only contain a single value.
	 * @param key
	 * @param values
	 * @return
	 */
	public static IProperty<String> createFakeProperty(String key, String...values)
	{
		return new PropertyHelper<String>(key, String.class)
		{
			public Collection<String> getAllowedValues() { return ImmutableList.copyOf(values); }
			public Optional<String> parseValue(String value) { return Optional.of(value); }
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
	
	public static <T extends Comparable<T>> String removeAndGetName(IProperty<T> property, Map<IProperty<?>, Comparable<?>> map)
	{
		return property.getName(removeAndGetValue(property, map));
	}
	
	public static <T extends Comparable<T>> T removeAndGetValue(IProperty<T> property, Map<IProperty<?>, Comparable<?>> map)
	{
		return (T) map.remove(property);
	}
}