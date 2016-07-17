package farcore.lib.bio;

import farcore.util.U;

public abstract class DNAHelper<T, E extends DNAProp>
{
	public String nativeDNA;
	public E[] list;
	
	public DNAHelper(E...props)
	{
		this.list = props;
		nativeDNA = "";
		for(E prop : props)
		{
			nativeDNA += prop.startType;
		}
	}
	
	public String borderDNA(String dna, float chance)
	{
		if(dna.length() != list.length)
			return "";
		String ret = "";
		for(int i = 0; i < dna.length(); ++i)
		{
			ret += list[i].borderDNA(dna.charAt(i), chance);
		}
		return ret;
	}
	
	public String mixedDNA(String dna1, String dna2)
	{
		if(dna1.length() != dna2.length())
			return "";
		String ret = "";
		for(int i = 0; i < dna1.length(); ++i)
		{
			ret += U.L.random(dna1.charAt(i), dna2.charAt(i));
		}
		return ret;
	}
	
	public void decodeDNA(T info, String dna)
	{
		if(dna.length() != list.length)
		{
			errorOn(info, 2);
			return;
		}
		for(int i = 0; i < list.length; ++i)
		{
			E prop = list[i];
			char chr = dna.charAt(i);
			label:
			{
				for(DNACharacter character : prop.allowedChars)
				{
					if(character.chr == chr)
					{
						character.affectOn(info);
						break label;
					}
				}
				errorOn(info, 1);
			}
		}
	}
	
	protected abstract void errorOn(T target, int type);
}
