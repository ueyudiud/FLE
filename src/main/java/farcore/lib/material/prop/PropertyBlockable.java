/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material.prop;

import farcore.lib.material.Mat;
import net.minecraft.block.Block;

public class PropertyBlockable<B extends Block>
{
	public Mat material;
	
	public int		harvestLevel;
	/**
	 * The block hardness, for each block unit, this is extensive value.
	 */
	public float	hardness;
	/**
	 * The block explosion resistance, for every block part, this is intensity
	 * value.
	 */
	public float	explosionResistance;
	
	public B block;
	
	public PropertyBlockable(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		this.material = material;
		this.hardness = hardness;
		this.explosionResistance = explosionResistance;
	}
}
