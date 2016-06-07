package farcore.lib.bio;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import farcore.util.U;

import java.util.Random;

public class DNA
{
	private static String makeSpeciesDescripition(String string)
	{
		int hash = string.length();
		int i = 0;
		String ret = "";
		while(i < string.length())
		{
			int value = string.charAt(i);
			ret += Character.toString((char) (value / 0x2F + hash));
			hash += value * 31;
			hash &= 0xFF;
			++i;
		}
		while(ret.length() < (hash & 0xF))
		{
			int hash1 = string.hashCode() + hash;
			ret += (char) (hash1 >> 24);
			ret += (char) ((hash1 >> 16) & 0xFF);
			ret += (char) ((hash1 >> 8) & 0xFF);
			ret += (char) (hash1 & 0xFF);
		}
		hash &= 0xF;
		return ret.substring(hash);
	}
	
	public String species;
	public Map<Character, DNAPart[]> map = new HashMap();

	public String encode()
	{
		String ret = makeSpeciesDescripition(species);
		for(Entry<Character, DNAPart[]> entry : map.entrySet())
		{
			DNAPart[] part = entry.getValue();
			ret += entry.getKey().toString() + Character.toString(part[0].key) + Character.toString(part[1].key);
		}
		return ret;
	}
	public String encodeWithMutation(Random random, float chance)
	{
		String ret = makeSpeciesDescripition(species);
		for(Entry<Character, DNAPart[]> entry : map.entrySet())
		{
			ret += entry.getKey().toString();
			if(random.nextFloat() > chance)
			{
				DNAPart[] part = entry.getValue();
				ret += Character.toString(part[0].key) + Character.toString(part[1].key);
			}
			else
			{
				DNAPart[] part = entry.getValue();
				int i = random.nextBoolean() ? 0 : 1;
				ret += Character.toString(U.Lang.randomSelect(part[i].col.parts, random).key) +
						Character.toString(part[i ^ 0x1].key);
			}
		}
		return ret;
	}
	public String encodeSingle(Random random)
	{
		String ret = makeSpeciesDescripition(species);
		for(Entry<Character, DNAPart[]> entry : map.entrySet())
		{
			DNAPart[] part = entry.getValue();
			ret += entry.getKey().toString() + Character.toString(part[random.nextBoolean() ? 1 : 0].key);
		}
		return ret;
	}
}