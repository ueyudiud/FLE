package farcore.energy.electric;

/**
 * The element in electrical net.
 * @author ueyudiud
 *
 */
public interface IElectricalElement
{
	void onUpdate(double voltage, double current);
	
	double eResistance(EnumCurrentType type);
	
	/**
	 * Might no element in game need these prop.
	 */
	//	double eCapacity(EnumCurrentType type);
	//	double eInductance(EnumCurrentType type);
}