package farcore.energy.electric;

/**
 * The element in electrical net.
 * @author ueyudiud
 *
 */
public interface IElectricalElement
{
	void onUpdate(double voltage, double current);
	
	double resistance();
}