package farcore.lib.matter;

import java.util.List;

import com.google.common.collect.ImmutableSet;

import farcore.enums.EnumChemCondition;
import farcore.enums.EnumSize;
import farcore.lib.collection.IPropertyMap;
import farcore.lib.collection.ISaveableProperty;
import farcore.lib.collection.PropertyEnum;
import farcore.lib.collection.PropertyMap;

public interface IMatterContainer
{
	public static final PropertyEnum<EnumChemCondition> propertyChemCondition = new PropertyEnum("chemCondition", EnumChemCondition.Open);
	public static final PropertyEnum<EnumSize> propertyMinSize = new PropertyEnum("minSize", EnumSize.Middle);
	
	/**
	 * A property map instance, this is a example 
	 */
	static final ImmutableSet<ISaveableProperty> standardMatterContainerSet =
			ImmutableSet.of(propertyChemCondition, propertyMinSize);
	
	public static PropertyMap newStandardMap()
	{
		return new PropertyMap(standardMatterContainerSet);
	}
	
	float temperature();
	
	float press();
	
	float ph();
	
	List<MatterStack> stacks();
	
	IPropertyMap properties();
}