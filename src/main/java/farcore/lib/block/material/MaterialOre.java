package farcore.lib.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialOre extends Material
{
	public MaterialOre()
	{
		super(MapColor.STONE);
		setRequiresTool();
	}
}