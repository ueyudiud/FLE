package farcore.substance;

public interface IPhaseCondition
{
	/**
	 * Unit : Pa
	 * @return
	 */
	long getPressure();
	
	/**
	 * Unit : K
	 * @return
	 */
	long getTemperature();
}