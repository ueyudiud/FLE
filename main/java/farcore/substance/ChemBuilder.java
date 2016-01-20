package farcore.substance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import farcore.collection.abs.Stack;

public class ChemBuilder implements Iterator<Stack<IParticle>>
{
	String name;
	int idx;
	private int cachedIndex;
	
	public ChemBuilder(String name)
	{
		this.name = name;
	}
	
	public Stack<IParticle>[] loadAll()
	{
		List<Stack<IParticle>> list = new ArrayList<Stack<IParticle>>();
		while(hasNext())
		{
			list.add(next());
		}
		return list.toArray(new Stack[list.size()]);
	}
	
	public void remove()
	{
		throw new RuntimeException();
	}
	
	public Stack<IParticle> next()
	{
		IParticle partcile = readParticle();
		if(partcile == null) throw new RuntimeException();
		long size = readSize();
		if(size <= 0) return null;	
		return new Stack<IParticle>(partcile, size);
	}
	
	void pushIndex()
	{
		cachedIndex = idx;
	}
	
	void popIndex()
	{
		idx = cachedIndex;
	}
	
	IParticle readParticle()
	{
		try
		{
			pushIndex();
			char chr = at();
			switch(chr)
			{
			case '(' :
			{
				if(n()) throw new IllegalArgumentException("The particle chemical name " + name + " must contain a ')' each '('!");
				pushIndex();
				if((chr = at()) == ')') throw new IllegalArgumentException("The particle chemical name " + name + " can not include noting between '(' and ')'.");
				int l = 0;
				do
				{
					if(n()) throw new IllegalArgumentException("The particle chemical name " + name + " must contain a ')' each '('!");
					chr = at();
					if(chr == '(') ++l;
					else if(chr == ')') --l;
				}
				while(l != -1);
				Ion ret = Ion.ion(sub());
				n();
				return ret;
			}
			case '[' :
			{
				if(n()) throw new IllegalArgumentException("The particle chemical name " + name + " must contain a ']' each '['!");
				pushIndex();
				if((chr = at()) == ']') throw new IllegalArgumentException("The particle chemical name " + name + " can not include noting between '[' and ']'.");
				do
				{
					if(n()) throw new IllegalArgumentException("The particle chemical name " + name + " must contain a ']' each '['!");
				}
				while(at() != ']');
				AtomRadical ret = AtomRadical.radical(sub());
				n();
				return ret;
			}
			default  : break;
			}
			if(Character.isLetter(chr) && Character.isUpperCase(chr))
			{
				if(n())
				{
					return Atom.valueOf(sub());
				}
			}
			else
			{
				throw new IllegalArgumentException();
			}
			chr = at();
			while(Character.isLetter(chr) && Character.isLowerCase(chr))
			{
				if(n()) break;
				chr = at();
			}
			return Atom.valueOf(sub());
		}
		catch(Exception e)
		{
			popIndex();
			throw new RuntimeException(e);
		}
	}
	
	long readSize()
	{
		if(!hasNext()) return 1;
		try
		{
			pushIndex();
			char chr = at();
			if(!Character.isDigit(chr))
			{
				return 1;
			}
			long value = 0;
			do
			{
				value = 10 * value + Character.getNumericValue(chr);
				if(n()) break;
				chr = at();
			}
			while(Character.isDigit(chr));
			return value;
		}
		catch(Exception e)
		{
			popIndex();
			throw new RuntimeException(e);
		}
	}
	
	char at()
	{
		return name.charAt(idx);
	}
	
	boolean n()
	{
		return (name.length() <= ++idx);
	}
	
	String sub()
	{
		return name.substring(cachedIndex, idx);
	}
	
	public boolean hasNext()
	{
		return name.length() > idx;
	}
}