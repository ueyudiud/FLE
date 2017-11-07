/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.bio;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class GeneticMaterial
{
	public String	specie;
	public int		generation;
	public long[]	coders;
	public int[]	nativeValues;
	
	public GeneticMaterial(String specie, int generation, long[] coders, int[] nativeValues)
	{
		this.specie = specie;
		this.generation = generation;
		this.coders = coders;
		this.nativeValues = nativeValues;
	}
	
	public GeneticMaterial copy()
	{
		return new GeneticMaterial(this.specie, this.generation, this.coders.clone(), this.nativeValues.clone());
	}
	
	public static enum GenticMaterialFactory implements INBTCompoundReaderAndWritter<GeneticMaterial>
	{
		INSTANCE;
		
		@Override
		public GeneticMaterial readFromNBT(NBTTagCompound nbt)
		{
			if (nbt.hasKey("specie"))
			{
				String specie = nbt.getString("specie");
				int generation = nbt.getInteger("generation");
				long[] coders = NBTs.getLongArrayOrDefault(nbt, "coders", null);
				int[] nativeValues = NBTs.getIntArrayOrDefault(nbt, "values", null);
				return new GeneticMaterial(specie, generation, coders, nativeValues);
			}
			return null;
		}
		
		@Override
		public void writeToNBT(GeneticMaterial target, NBTTagCompound nbt)
		{
			nbt.setString("specie", target.specie);
			nbt.setInteger("generation", target.generation);
			NBTs.setLongArray(nbt, "coders", target.coders);
			nbt.setIntArray("values", target.nativeValues);
		}
	}
}
