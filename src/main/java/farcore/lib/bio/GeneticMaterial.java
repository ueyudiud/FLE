/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.bio;

import java.util.Random;
import java.util.function.Function;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class GeneticMaterial
{
	public static <T extends IBiology> GeneticMaterial newGeneticMaterial(String specie, T target, int generation, DNAHandler<T>[] handlers)
	{
		DNAPair<T>[] pairs = new DNAPair[handlers.length];
		for(int i = 0; i < pairs.length; ++i)
		{
			pairs[i] = handlers[i].createNative(target);
		}
		return newGeneticMaterial(specie, generation, pairs);
	}
	public static GeneticMaterial newGeneticMaterial(String specie, int generation, DNAPair[] pairs)
	{
		return new GeneticMaterial(specie, generation, pairs);
	}
	public static GeneticMaterial newGeneticMaterial(IBiology biology, Random random, GeneticMaterial parent, GeneticMaterial gamate)
	{
		return new GeneticMaterial(parent.specie, parent.generation + 1, DNAPair.mixPairs(random, parent.coders, gamate.coders));
	}
	
	public String specie;
	public int generation;
	public DNAPair[] coders;
	
	GeneticMaterial(String specie, int generation, DNAPair[] coders)
	{
		this.specie = specie;
		this.generation = generation;
		this.coders = coders;
	}
	
	public void expressTrait(IBiology biology)
	{
		for(DNAPair pair : this.coders)
		{
			pair.express(biology);
		}
	}
	
	public String getDNAString()
	{
		StringBuilder builder = new StringBuilder();
		for(DNAPair pair : this.coders)
		{
			builder.append(pair.DNA1).append(pair.DNA2);
		}
		return builder.toString();
	}
	
	public GeneticMaterial generateGameteDNA(IBiology target, Random random, boolean mutate)
	{
		DNAPair[] DNAS = new DNAPair[this.coders.length];
		for(int i = 0; i < DNAS.length; ++i)
		{
			DNAS[i] = this.coders[i].handler.createGamete(target, this.coders[i], mutate);
		}
		return new GeneticMaterial(this.specie, this.generation, DNAS);
	}
	
	public GeneticMaterial copy()
	{
		return new GeneticMaterial(this.specie, this.generation, this.coders);
	}
	
	public static enum GenticMaterialFactory implements INBTCompoundReaderAndWritter<GeneticMaterial>
	{
		INSTANCE;
		
		private static final Function<NBTBase, DNAPair> FUNCTION1 = tag -> DNAPair.loadFromNBT((NBTTagCompound) tag);
		private static final Function<DNAPair, NBTBase> FUNCTION2 = pair -> pair.writeToNBT(pair);
		
		@Override
		public GeneticMaterial readFromNBT(NBTTagCompound nbt)
		{
			if(nbt.hasKey("specie"))
			{
				String specie = nbt.getString("specie");
				int generation = nbt.getInteger("generation");
				DNAPair[] coders = NBTs.getListOrDefault(nbt, "coders", DNAPair.class, null, FUNCTION1, false);
				return new GeneticMaterial(specie, generation, coders);
			}
			return null;
		}
		
		@Override
		public void writeToNBT(GeneticMaterial target, NBTTagCompound nbt)
		{
			nbt.setString("specie", target.specie);
			nbt.setInteger("generation", target.generation);
			NBTs.setList(nbt, "coders", target.coders, FUNCTION2, false);
		}
	}
}