package farcore.lib.prop;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.ISubTagContainer;
import farcore.util.U;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyMaterial extends PropertyHelper<Mat>
{
	private List<Mat> allowedValues;
	
	public PropertyMaterial(String name, IDataChecker<ISubTagContainer> filter)
	{
		this(name, Mat.filt(filter));
	}
	public PropertyMaterial(String name, Mat...materials)
	{
		this(name, ImmutableList.copyOf(materials));
	}
	public PropertyMaterial(String name, List<Mat> materials)
	{
		super(name, Mat.class);
		allowedValues = materials;
	}
	
	@Override
	public Collection<Mat> getAllowedValues()
	{
		return allowedValues;
	}
	
	@Override
	public Optional<Mat> parseValue(String value)
	{
		return !Mat.register.contain(value) ? Optional.absent() : Optional.of(Mat.register.get(value));
	}
	
	@Override
	public String getName(Mat value)
	{
		return U.Strings.validateProperty(value.name);
	}
}