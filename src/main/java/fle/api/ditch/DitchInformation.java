/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.ditch;

import nebula.base.IPropertyMap.IProperty;

/**
 * @author ueyudiud
 */
public class DitchInformation
{
	public static final IProperty<DitchInformation> DITCH_INFORMATION_PROPERTY = IProperty.to();
	
	public int	tankCapacity;
	public int	destroyTemperature;
	public int	speedMultiple;
	public int	maxTransferLimit;
}
