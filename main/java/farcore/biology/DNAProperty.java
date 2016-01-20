package farcore.biology;

import com.google.common.collect.ImmutableList;

import farcore.biology.DNA.DNAPair;

public class DNAProperty
{
	ImmutableList<DNAPart> parts;
	DNA defaultDNA;
	
	public DNAProperty(DNAPart... parts)
	{
		this.parts = ImmutableList.copyOf(parts);
		DNAPair[] pairs = new DNAPair[parts.length];
		for (int i = 0; i < parts.length; ++i)
		{
			pairs[i] = new DNAPair(parts[i]);
		}
		defaultDNA = new DNA(pairs);
	}
	
	public ImmutableList<DNAPart> getAllowedParts()
	{
		return parts;
	}
	
	public DNA getDefaultDNA()
	{
		return defaultDNA.copy();
	}
}