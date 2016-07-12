package farcore.lib.crop.dna;

import farcore.lib.collection.Stack;
import farcore.util.U;

public class DNAProp
{
	public char startType;
	public float borderChanceBase;
	public char fixedType;
	public long allWeight;
	public DNACharacter[] allowedChars;
	public Stack<DNACharacter>[] weight;
	
	public DNAProp(Stack<DNACharacter>...characters)
	{
		startType = characters[0].element.chr;
		allowedChars = new DNACharacter[characters.length];
		weight = characters;
		for(int i = 0; i < characters.length; ++i)
		{
			allowedChars[i] = characters[i].element;
			allWeight += characters[i].size;
		}
	}
	
	public DNAProp setBorderChance(float chance)
	{
		this.borderChanceBase = chance;
		return this;
	}
	
	public DNAProp setFixedType(char fixedType)
	{
		this.fixedType = fixedType;
		return this;
	}

	public char borderDNA(char dna0, float chance)
	{
		if(dna0 == fixedType)
		{
			return dna0;
		}
		if(Math.random() < chance * borderChanceBase)
		{
			return U.L.randomInStack(weight, allWeight).chr;
		}
		return dna0;
	}
}