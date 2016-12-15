/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.crop.dna;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import farcore.lib.collection.IntegerEntry;
import farcore.lib.collection.Selector;
import farcore.util.L;

/**
 * @author ueyudiud
 */
public class DNAHCropSimpleBuilder
{
	final int id;
	final String name;
	ArrayList<Selector<Character>> nsbuilder = new ArrayList(2);
	ImmutableMap.Builder<Character, IntegerEntry<Selector<Character>>> msbuilder = ImmutableMap.builder();
	ImmutableMap.Builder<Character, int[]> ebuilder = ImmutableMap.builder();
	
	DNAHCropSimpleBuilder(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public DNAHCropSimpleBuilder putNativeDNA(char dna)
	{
		return putNativeDNA(Selector.single(dna));
	}
	public DNAHCropSimpleBuilder putNativeDNA(Selector<Character> selector)
	{
		this.nsbuilder.add(selector);
		return this;
	}
	
	public DNAHCropSimpleBuilder putMutateDNA(char parent, int chance, char mutation)
	{
		return putMutateDNA(parent, chance, Selector.single(mutation));
	}
	public DNAHCropSimpleBuilder putMutateDNA(char parent, int chance, Selector<Character> selector)
	{
		this.msbuilder.put(parent, new IntegerEntry(selector, chance));
		return this;
	}
	
	public DNAHCropSimpleBuilder putEffect(Character key, int[] effect)
	{
		this.ebuilder.put(key, effect);
		return this;
	}
	
	public DNAHCropSimple build()
	{
		ImmutableMap<Character, int[]> ebuilded = this.ebuilder.build();
		if(this.nsbuilder.size() == 1)
		{
			this.nsbuilder.add(this.nsbuilder.get(0));
		}
		return this.id == -1 ?
				new DNAHCropSimple(this.name, this.nsbuilder.toArray(new Selector[this.nsbuilder.size()]), this.msbuilder.build(), L.toFunction(ebuilded)) :
					new DNAHCropSimple(this.id, this.name, this.nsbuilder.toArray(new Selector[this.nsbuilder.size()]), this.msbuilder.build(), L.toFunction(ebuilded));
	}
}