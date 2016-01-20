package flapi;

import farcore.collection.Register;
import farcore.substance.Substance;
import farcore.util.SubTag;

public class FleResource
{
	public static final Register<Substance> rock = new Register();
	public static final Register<Substance> mineral = new Register();
	public static final Register<Substance> sand = new Register();
	public static final Register<Substance> dirt = new Register();
	
	/**
	 * The all kinds of substance will auto register by this method,
	 * you can add SubTag.NOT_REGISTER to prevent register substance 
	 * to these list.<br>
	 * <code>
	 * for(Substance substance : Substance.getRegister())<br>
	 * {<br>
	 * if(!substance.contain(SubTag.NOT_REGISTER))<br>
	 * FleResource.registerSubstance(substance);<br>
	 * }<br>
	 * </code>
	 * These substance will use to get rocks, mineral, resource, etc.
	 * Use to get property and names.
	 * @param substance The register substance.
	 * @see farcore.substance.Substance
	 * @see farcore.util.SubTag
	 */
	public static void registerSubstance(Substance substance)
	{
		if(substance.contain(SubTag.MATERIAL_ROCK))
		{
			rock.register(substance, substance.getName());
		}
		if(substance.contain(SubTag.MATERIAL_MINERAL))
		{
			mineral.register(substance, substance.getName());
		}
		if(substance.contain(SubTag.MATERIAL_SAND))
		{
			sand.register(substance, substance.getName());
		}
		if(substance.contain(SubTag.MATERIAL_DIRT))
		{
			dirt.register(substance, substance.getName());
		}
	}
}