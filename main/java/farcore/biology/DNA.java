package farcore.biology;

import java.util.Arrays;
import java.util.Comparator;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import farcore.biology.DNAPart.DNACharactor;
import farcore.util.FleRandom;

public class DNA
{
	public static class DNAPair implements Comparable<DNAPair>
	{
		DNAPart part;
		DNACharactor A;
		DNACharactor B;
		DNACharactor C;
		
		public DNAPair(DNAPart part, DNACharactor A, DNACharactor B,
				DNACharactor C)
		{
			this.part = part;
			this.A = A;
			this.B = B;
			this.C = C;
		}
		
		public DNAPair(DNAPart part, DNACharactor A, DNACharactor B)
		{
			this.part = part;
			this.A = A;
			this.B = B;
		}
		
		/**
		 * A quickly way to create a default pari of dna.
		 * 
		 * @param part
		 */
		public DNAPair(DNAPart part)
		{
			this(part, part.defaultPart(), part.defaultPart());
		}
		
		public void affectBiology(IBiology biology)
		{
		
		}
		
		public boolean same(DNAPair pair)
		{
			return part.equals(pair.part) && A.equals(pair.A)
					&& (B == null ? pair.B == null : B.equals(pair.B))
					&& (C == null ? pair.C == null : C.equals(pair.C));
		}
		
		@Override
		public boolean equals(Object object)
		{
			return object instanceof DNAPair ? same((DNAPair) object) : false;
		}
		
		public DNACharactor getAffectCharactor()
		{
			return part.getAffectCharactor(A, B);
		}
		
		@Override
		public int compareTo(DNAPair arg0)
		{
			int compare = part.compareTo(arg0.part);
			if (compare == 0)
			{
				compare = A.tag.compareTo(arg0.A.tag);
				if (compare == 0)
					return B == null ? (arg0.B == null ? 0 : -1)
							: B.tag.compareTo(arg0.B.tag);
				return compare;
			}
			return compare;
		}
		
		public boolean hasVariation()
		{
			return B == null || C != null;
		}
	}
	
	public static DNA loadFromNBT(NBTTagCompound nbt)
	{
		DNA dna = new DNA();
		dna.readFromNBT(nbt);
		return dna;
	}
	
	private static final FleRandom RANDOM = new FleRandom();
	
	public static DNA mix(DNA dna1, DNA dna2)
	{
		return mix(0, dna1, dna2);
	}
	
	/**
	 * 
	 * @param hardness
	 *            100 is max, 0 is min.
	 * @param dna1
	 * @param dna2
	 * @return
	 */
	public static DNA mix(int hardness, DNA dna1, DNA dna2)
	{
		if (dna1.parts.length != dna2.parts.length)
			return null;
		DNA dna = new DNA();
		dna.parts = new DNAPair[dna1.parts.length];
		for (int i = 0; i < dna.parts.length; ++i)
		{
			if (!dna1.parts[i].equals(dna2.parts[i]))
			{
				if (hardness > 99 || RANDOM.nextInt(100 - hardness) == 0)
				{
					return null;
				}
				if (RANDOM.nextBoolean())
					dna.parts[i] = new DNAPair(dna1.parts[i].part,
							RANDOM.nextBoolean() ? dna1.parts[i].A
									: dna1.parts[i].B,
							null);
				else
					dna.parts[i] = new DNAPair(dna2.parts[i].part,
							RANDOM.nextBoolean() ? dna2.parts[i].A
									: dna2.parts[i].B,
							null);
			}
			else
			{
				if ((dna1.parts[i].hasVariation()
						|| dna2.parts[i].hasVariation())
						&& RANDOM.nextInt(105 - hardness) == 0)
				{
					return null;
				}
				if (RANDOM.nextInt(1000 - hardness) == 0)
				{
					dna.parts[i] = new DNAPair(dna1.parts[i].part,
							RANDOM.nextBoolean() ? dna1.parts[i].A
									: dna2.parts[i].A,
							RANDOM.nextBoolean() ? dna1.parts[i].B
									: dna2.parts[i].B,
							RANDOM.nextBoolean()
									? (RANDOM.nextBoolean() ? dna1.parts[i].A
											: dna1.parts[i].B)
									: (RANDOM.nextBoolean() ? dna2.parts[i].A
											: dna2.parts[i].B));
				}
				else
				{
					dna.parts[i] = new DNAPair(dna1.parts[i].part,
							RANDOM.nextBoolean() ? dna1.parts[i].A
									: dna2.parts[i].A,
							RANDOM.nextBoolean() ? dna1.parts[i].B
									: dna2.parts[i].B);
				}
			}
		}
		return dna;
	}
	
	DNAPair[] parts;
	
	DNA()
	{
		;
	}
	
	private static final Comparator<DNAPair> COMPARATOR = new Comparator<DNA.DNAPair>()
	{
		public int compare(DNAPair pair1, DNAPair pair2)
		{
			return pair1.compareTo(pair2);
		}
	};
	
	public DNA(DNAPair... parts)
	{
		this.parts = parts;
		Arrays.sort(this.parts, COMPARATOR);
	}
	
	public void getAllPossiableDNA()
	{
	
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("DNAs", 10);
		parts = new DNAPair[list.tagCount()];
		for (int i = 0; i < parts.length; ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			DNAPart part = DNAPart.part(nbt1.getString("name"));
			parts[i] = part.readFromNBT(nbt1);
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < parts.length; ++i)
		{
			if (parts[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("name", parts[i].part.getName());
			list.appendTag(parts[i].part.writeToNBT(parts[i], nbt1));
		}
		nbt.setTag("DNAs", list);
	}
	
	public DNA variation(float deformity)
	{
		int change = RANDOM
				.nextInt(100 + RANDOM.nextInt((int) (deformity * 100)));
		change /= 100;
		if (change == 0)
			return copy();
		DNA dna = copy();
		while (change > 0)
		{
			int l = RANDOM.nextInt(parts.length);
			float f = RANDOM.nextFloat();
			ImmutableList<DNACharactor> chars = parts[l].part
					.getAllowedCharactors();
			int size = chars.size();
			if (f < 0.4F)
			{
				dna.parts[l].A = chars.get(RANDOM.nextInt(size));
			}
			else if ((dna.parts[l].C == null && f < 0.95F) || f < 0.8F)
			{
				dna.parts[l].B = chars.get(RANDOM.nextInt(size));
			}
			else if (f < 0.9F)
			{
				dna.parts[l].C = chars.get(RANDOM.nextInt(size));
			}
			else
			{
				if (dna.parts[l].C != null)
					dna.parts[l].C = null;
				else
					dna.parts[l].B = null;
			}
		}
		return dna;
	}
	
	public DNA copy()
	{
		return new DNA(parts.clone());
	}
}