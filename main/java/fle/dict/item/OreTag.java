package fle.dict.item;

import farcore.substance.Substance;
import farcore.util.Part;
import farcore.util.Util;

public class OreTag
{
	public final String name;
	public final Part part;
	public final Substance substance;
	
	public OreTag(Part part, Substance substance)
	{
		this.part = part;
		this.substance = substance;
		this.name = Util.oreDictFormat(part.name, "\\.", false) + 
				Util.oreDictFormat(substance.getName(), " ");
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof OreTag ? name.equals(((OreTag) obj).name) : false;
	}
}