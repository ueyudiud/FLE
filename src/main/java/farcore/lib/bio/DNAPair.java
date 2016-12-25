/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.bio;

import java.util.Comparator;
import java.util.Random;

import farcore.lib.nbt.INBTWriter;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class DNAPair<T extends IBiology> implements INBTWriter<DNAPair>
{
	static final String SORT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	static final Comparator<Character> COMPARATOR = (Character chr1, Character chr2) ->
	{
		int i1 = SORT.indexOf(chr1);
		int i2 = SORT.indexOf(chr2);
		return i1 == -1 ? i2 == -1 ? Character.compare(chr1, chr2) : 1 :
			i2 == -1 ? -1 : i1 - i2;
	};
	
	public static DNAPair loadFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("handler"))
		{
			DNAHandler handler = DNAHandler.REGISTER.get(nbt.getString("handler"));
			return handler == null ? null : new DNAPair(handler, (char) nbt.getByte("a"), (char) nbt.getByte("b"));
		}
		return null;
	}
	
	public static DNAPair[] mixPairs(Random random, DNAPair[] pairs1, DNAPair[] pairs2)
	{
		assert(pairs1.length == pairs2.length);
		DNAPair[] mix = new DNAPair[pairs1.length];
		for(int i = 0; i < mix.length; ++i)
		{
			mix[i] = new DNAPair(pairs1[i].handler, pairs1[i].randSelect(random), pairs2[i].randSelect(random));
		}
		return mix;
	}
	
	public final DNAHandler<T> handler;
	public final char DNA1;
	public final char DNA2;
	
	public DNAPair(DNAHandler handler, char DNA)
	{
		this(handler, DNA, ' ');
	}
	public DNAPair(DNAHandler handler, char DNA1, char DNA2)
	{
		this.handler = handler;
		if(COMPARATOR.compare(DNA1, DNA2) > 0)
		{
			this.DNA1 = DNA2;
			this.DNA2 = DNA1;
		}
		else
		{
			this.DNA1 = DNA1;
			this.DNA2 = DNA2;
		}
	}
	
	public char randSelect(Random random)
	{
		return this.DNA2 == ' ' ? this.DNA1 : random.nextBoolean() ? this.DNA2 : this.DNA1;
	}
	
	public void express(T target)
	{
		this.handler.expressTrait(target, this);
	}
	
	@Override
	public void writeToNBT(DNAPair target, NBTTagCompound nbt)
	{
		nbt.setString("handler", DNAHandler.REGISTER.name(this.handler));
		nbt.setByte("a", (byte) this.DNA1);
		nbt.setByte("b", (byte) this.DNA2);
	}
}