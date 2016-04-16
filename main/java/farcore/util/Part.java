package farcore.util;

import farcore.lib.collection.Register;

public class Part
{
	private static final Register<Part> partRegister = new Register();
	
	public static Part get(String name)
	{
		return partRegister.get(name);
	}
	
	public static Part part(String name, int resolution)
	{
		return partRegister.contain(name) ? partRegister.get(name) : new Part(name, resolution);
	}
	
	public final String name;
	public final int resolution;
	
	Part(String name, int resolution)
	{
		this.name = name;
		this.resolution = resolution;
		partRegister.register(name, this);
	}
}