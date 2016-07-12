package farcore.lib.crop.dna;

import java.util.LinkedList;
import java.util.List;

import farcore.lib.crop.CropInfo;
import farcore.util.U;

public class CropDNAHelper
{
	public String nativeDNA;
	public DNAProp[] list;
	
	public CropDNAHelper(DNAProp...props)
	{
		this.list = props;
		nativeDNA = "";
		for(DNAProp prop : props)
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
	
	public void decodeDNA(CropInfo info, String dna)
	{
		if(dna.length() != list.length)
		{
			info.map.put("error", 2);
			return;
		}
		for(int i = 0; i < list.length; ++i)
		{
			DNAProp prop = list[i];
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
				info.map.put("error", 1);
			}
		}
	}
}