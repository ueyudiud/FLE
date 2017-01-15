package farcore.energy.electric;

/**
 * The electrical node.
 * @author ueyudiud
 *
 */
public interface IElectricalNode
{
	void onUpdate(double voltage, double current);
	
	int getIndex();
	
	/**
	 * Get handler inner link.
	 * Use in each electrical handler to get
	 * other element.
	 * @return
	 */
	int[] link();
	
	/**
	 * The element which has voltage is usually
	 * as battery (And also suggested with inner
	 * resistance after voltage source).<br>
	 * The far core use this voltage as approximation
	 * of real battery output voltage.<br>
	 * @return
	 */
	double voltage();
}