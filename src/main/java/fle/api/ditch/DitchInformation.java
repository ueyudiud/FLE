/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.ditch;

import nebula.base.IPropertyMap;

/**
 * @author ueyudiud
 */
public class DitchInformation
{
	public static final IPropertyMap.IProperty<DitchInformation> DITCH_INFORMATION_PROPERTY = () -> null;
	
	public int tankCapacity;
	public int destroyTemperature;
	public int speedMultiple;
	public int maxTransferLimit;
}