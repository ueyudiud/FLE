package farcore.biology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTTagCompound;

import farcore.biology.DNA.DNAPair;
import farcore.collection.Register;

public class DNAPart implements Comparable<DNAPart>
{
	public static class DNACharactor
	{
		public DNAPart parent;
		public String tag;
		public int level;
		
		public DNACharactor(String name, int l)
		{
			this.tag = name;
			this.level = l;
		}
		
		/**
		 * 0 for recessive, 2 for dominance.
		 * 
		 * @return
		 */
		public int dominanceLevel()
		{
			return level;
		}
		
		@Override
		public int hashCode()
		{
			return parent.hashCode() * 31 + tag.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof DNACharactor
					? parent.equals(((DNACharactor) obj).parent)
							&& tag.equals(((DNACharactor) obj).tag)
					: false;
		}
	}
	
	private static Register<DNAPart> register = new Register<DNAPart>();
	
	public static DNAPart part(String name)
	{
		return register.get(name);
	}
	
	public static DNAPart part(String name, Object... charactors)
	{
		if (register.contain(name))
			return part(name);
		List<DNACharactor> array = new ArrayList();
		for (int i = 0; i < charactors.length; i++)
		{
			String n = (String) charactors[i];
			int lvl = 2;
			if (i + 1 < charactors.length
					&& charactors[i + 1] instanceof Number)
			{
				lvl = ((Number) charactors[i + 1]).intValue();
				++i;
			}
			else if (i + 1 < charactors.length
					&& charactors[i + 1] instanceof Boolean)
			{
				lvl = ((Boolean) charactors[i + 1]).booleanValue() ? 2 : 0;
				++i;
			}
			array.add(new DNACharactor(n, lvl));
		}
		DNAPart part;
		register.register(part = new DNAPart(name,
				array.toArray(new DNACharactor[array.size()])), name);
		return part;
	}
	
	private final String name;
	private ImmutableMap<String, DNACharactor> charMap;
	private ImmutableList<DNACharactor> chars;
	
	private DNAPart(String name, DNACharactor... charactors)
	{
		this.name = name;
		chars = ImmutableList.copyOf(charactors);
		Map<String, DNACharactor> map = new HashMap();
		for (DNACharactor chr : chars)
		{
			if (chr.parent != null)
				throw new IllegalArgumentException(
						"This DNA is already has parent!");
			if (map.containsKey(chr))
				throw new IllegalArgumentException(
						"Can not put charactor with same tag in to map!");
			map.put(chr.tag, chr);
			chr.parent = this;
		}
		charMap = ImmutableMap.copyOf(map);
	}
	
	public DNACharactor getAffectCharactor(DNACharactor chr1, DNACharactor chr2)
	{
		return chr1.dominanceLevel() > chr2.dominanceLevel() ? chr1 : chr2;
	}
	
	public ImmutableList<DNACharactor> getAllowedCharactors()
	{
		return chars;
	}
	
	public String getName()
	{
		return name;
	}
	
	public DNAPair readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("C"))
			return new DNAPair(this, charMap.get(nbt.getString("A")),
					charMap.get(nbt.getString("B")),
					charMap.get(nbt.getString("C")));
		if (nbt.hasKey("B"))
			return new DNAPair(this, charMap.get(nbt.getString("A")),
					charMap.get(nbt.getString("B")));
		return new DNAPair(this, charMap.get(nbt.getString("A")), null);
	}
	
	public NBTTagCompound writeToNBT(DNAPair DNA, NBTTagCompound nbt)
	{
		nbt.setString("A", DNA.A.tag);
		if (DNA.B != null)
			nbt.setString("B", DNA.B.tag);
		if (DNA.C != null)
			nbt.setString("C", DNA.C.tag);
		return nbt;
	}
	
	public DNACharactor defaultPart()
	{
		return chars.get(0);
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DNAPart ? name.equals(((DNAPart) obj).name)
				: false;
	}
	
	@Override
	public int compareTo(DNAPart o)
	{
		return name.compareTo(o.name);
	}
}