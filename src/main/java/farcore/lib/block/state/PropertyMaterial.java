/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.state;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import nebula.common.util.IDataChecker;
import nebula.common.util.L;
import net.minecraft.block.properties.PropertyHelper;

/**
 * @author ueyudiud
 */
public class PropertyMaterial extends PropertyHelper<Mat>
{
	final List<Mat> list;
	
	public static PropertyMaterial create(String name, IDataChecker<? super Mat> checker)
	{
		return new PropertyMaterial(name, Mat.filt(checker, false));
	}
	public static PropertyMaterial create(String name, Collection<Mat> collection)
	{
		Mat[] materials = L.cast(collection, Mat.class);
		Arrays.sort(materials, (m1, m2) -> m1.name.compareTo(m2.name));
		return new PropertyMaterial(name, ImmutableList.copyOf(materials));
	}
	
	PropertyMaterial(String name, List<Mat> collection)
	{
		super(name, Mat.class);
		this.list = collection;
	}
	
	public int indexOf(Mat material)
	{
		return this.list.indexOf(material);
	}
	
	public Mat getMaterialFromID(int id, Mat def)
	{
		return id >= 0 && id < this.list.size() ? this.list.get(id) : def;
	}
	
	@Override
	public Collection<Mat> getAllowedValues()
	{
		return this.list;
	}
	
	@Override
	public Optional<Mat> parseValue(String value)
	{
		Mat material = Mat.material(value);
		
		return this.list.contains(material) ? Optional.of(material) : Optional.absent();
	}
	
	@Override
	public String getName(Mat value)
	{
		return value.name;
	}
}