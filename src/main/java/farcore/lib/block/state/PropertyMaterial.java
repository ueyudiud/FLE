/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block.state;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import nebula.base.Judgable;
import nebula.common.util.L;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;

/**
 * @author ueyudiud
 */
public class PropertyMaterial extends PropertyHelper<Mat>
{
	private static final Comparator<Mat> COMPARATOR = (m1, m2) -> m1.id - m2.id;
	
	final List<Mat> list;
	
	public static PropertyMaterial create(String name, Judgable<? super Mat> checker)
	{
		return new PropertyMaterial(name, Mat.filt(checker, false));
	}
	
	public static PropertyMaterial create(String name, Collection<Mat> collection)
	{
		Mat[] materials = L.cast(collection, Mat.class);
		Arrays.sort(materials, COMPARATOR);
		return new PropertyMaterial(name, ImmutableList.copyOf(materials));
	}
	
	PropertyMaterial(String name, List<Mat> collection)
	{
		super(name, Mat.class);
		this.list = collection;
	}
	
	public int indexOf(IBlockState state)
	{
		return indexOf(state.getValue(this));
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
