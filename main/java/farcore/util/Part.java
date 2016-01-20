package farcore.util;

import java.util.Locale;

import farcore.collection.Register;
import flapi.FleAPI;

public class Part
{
	private static final Register<Part> partRegister = new Register();

	public static final Part ore = part("ore", 144);
	
	public static Register<Part> values()
	{
		return partRegister;
	}
	
	public static Part get(String name)
	{
		return partRegister.get(name);
	}
	
	public static Part part(String name, int resolution)
	{
		return partRegister.contain(name) ? partRegister.get(name) : new Part(name, resolution, SubTag.getNewSubTag("PART_" + name.toUpperCase(Locale.ENGLISH)));
	}
	
	public final String name;
	public final int resolution;
	public SubTag partTag;
	
	private Part(String name, int resolution, SubTag tag)
	{
		if(name == null || name.length() == 0) throw new IllegalArgumentException("Part name can not be null or nothing!");
		this.name = name;
		this.resolution = resolution;
		this.partTag = tag;
		partRegister.register(this, name);
	}

	public Part registerTranslate(String display, String display1)
	{
		FleAPI.lang.registerLocal(getTranslateName(), display);
		FleAPI.lang.registerLocal(getTranslateName1(), display1);
		return this;
	}
	public Part registerTranslate(String locale, String display, String display1)
	{
		FleAPI.lang.registerLocal(locale, getTranslateName(), display);
		FleAPI.lang.registerLocal(locale, getTranslateName1(), display1);
		return this;
	}

	protected String getTranslateName()
	{
		return "part." + name + ".name";
	}
	protected String getTranslateName1()
	{
		return "part.related." + name + ".name";
	}
	
	public String getDisplayName()
	{
		return FleAPI.lang.translateToLocal(getTranslateName(), new Object[0]);
	}
	
	public String getDisplayName(String name)
	{
		return FleAPI.lang.translateToLocal(getTranslateName1(), name);
	}
}