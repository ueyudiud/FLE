package farcore.energy.electric.instance;

public interface IElectricalHandlerHelper
{
	void providePowerByVoltage(double power);

	void heatByCurrent(double power);
}