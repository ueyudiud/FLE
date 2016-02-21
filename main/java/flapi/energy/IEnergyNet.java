package flapi.energy;

/**
 * To mark this class is an energy net.
 * @author ueyudiud
 */
public abstract class IEnergyNet
{
	public abstract String getEnergyNetName();
	
	public abstract EnumEnergyType getEnergyType();
}
