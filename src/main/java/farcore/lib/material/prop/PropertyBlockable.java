package farcore.lib.material.prop;

import net.minecraft.block.Block;

public class PropertyBlockable<B extends Block>
{
	public int harvestLevel;
	public float hardness;
	public float explosionResistance;

	public B block;
}
