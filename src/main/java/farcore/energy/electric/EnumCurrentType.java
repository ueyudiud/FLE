package farcore.energy.electric;

/**
 * The current type of electrical net.
 * May removed in future version.
 * @author ueyudiud
 *
 */
public enum EnumCurrentType
{
	SIGNAL,
	DC,
	AC;//Default frequency is 50Hz.
	
	public static final double AC_frequency = 50D;
}