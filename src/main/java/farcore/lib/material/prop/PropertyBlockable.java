package farcore.lib.material.prop;

import farcore.lib.material.Mat;
import net.minecraft.block.Block;

public class PropertyBlockable<B extends Block>
{
	public Mat material;
	
	public int harvestLevel;
	public float hardness;
	public float explosionResistance;
	
	public B block;
}