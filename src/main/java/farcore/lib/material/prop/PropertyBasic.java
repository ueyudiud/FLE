package farcore.lib.material.prop;

public class PropertyBasic
{
	public static final PropertyBasic INSTANCE = new PropertyBasic();
	
	public double heatCap;
	public double thermalConduct;
	public double maxSpeed;
	public double maxTorque;
	public double dielectricConstant;
	public double electrialResistance;
	public float redstoneResistance;
	
	static
	{
		INSTANCE.heatCap = Float.MAX_VALUE;
		INSTANCE.thermalConduct = 0;
		INSTANCE.maxSpeed = 0;
		INSTANCE.maxTorque = 0;
		INSTANCE.dielectricConstant = 1F;
		INSTANCE.electrialResistance = Float.MAX_VALUE;
		INSTANCE.redstoneResistance = 1F;
	}
}