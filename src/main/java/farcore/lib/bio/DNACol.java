package farcore.lib.bio;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.util.U;

public class DNACol
{
	public char key;
	public Map<Character, DNAPart> partMap;
	public List<DNAPart> parts;
	
	public DNACol(char key, DNAPart...parts)
	{
		this.key = key;
		this.parts = ImmutableList.copyOf(parts);
		ImmutableMap.Builder<Character, DNAPart> builder = ImmutableMap.builder();
		for(DNAPart part : parts)
		{
			part.setCollection(this);
			builder.put(part.key, part);
		}
		partMap = builder.build();
	}

	public DNAPart randomGet(Random random)
	{
		return U.Lang.randomSelect(parts, random);
	}
}