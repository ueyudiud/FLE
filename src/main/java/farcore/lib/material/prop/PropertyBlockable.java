package farcore.lib.material.prop;

import farcore.lib.material.Mat;
import net.minecraft.block.Block;

public class PropertyBlockable<B extends Block>
{
	public Mat material;
	
	public int harvestLevel;
	/**
	 * The block hardness, for each block unit, this is extensive value.
	 */
	public float hardness;
	/**
	 * The block explosion resistance, for every block part, this is intensity value.
	 */
	public float explosionResistance;
	
	public B block;
}