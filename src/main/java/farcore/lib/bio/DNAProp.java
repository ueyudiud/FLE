package farcore.lib.bio;

import farcore.lib.collection.Stack;
import farcore.util.U;

public abstract class DNAProp<T extends DNACharacter>
{
	public char startType;
	public float borderChanceBase;
	public char fixedType;
	public long allWeight;
	public T[] allowedChars;
	public Stack<T>[] weight;
	
	public DNAProp(Stack<T>...characters)
	{
		startType = characters[0].element.chr;
		allowedChars = createCharacters(characters.length);
		weight = characters;
		for(int i = 0; i < characters.length; ++i)
		{
			allowedChars[i] = characters[i].element;
			allWeight += characters[i].size;
		}
	}
	
	protected abstract T[] createCharacters(int length);
	
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