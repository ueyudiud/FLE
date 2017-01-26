/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.state;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import farcore.lib.util.IDataChecker;
import net.minecraft.block.properties.PropertyHelper;

/**
 * @author ueyudiud
 */
public class PropertyMaterial extends PropertyHelper<Mat>
{
	final Collection<Mat> collection;
	
	public static PropertyMaterial create(String name, IDataChecker<? super Mat> checker)
	{
		return new PropertyMaterial(name, Mat.filt(checker, false));
	}
	public static PropertyMaterial create(String name, Collection<Mat> collection)
	{
		return new PropertyMaterial(name, ImmutableList.copyOf(collection));
	}
	
	PropertyMaterial(String name, Collection<Mat> collection)
	{
		super(name, Mat.class);
		this.collection = collection;
	}
	
	@Override
	public Collection<Mat> getAllowedValues()
	{
		return this.collection;
	}
	
	@Override
	public Optional<Mat> parseValue(String value)
	{
		Mat material = Mat.material(value);
		
		return this.collection.contains(material) ? Optional.of(material) : Optional.absent();
	}
	
	@Override
	public String getName(Mat value)
	{
		return value.name;
	}
}