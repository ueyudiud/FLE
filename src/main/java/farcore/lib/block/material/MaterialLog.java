package farcore.lib.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialLog extends Material
{
	public MaterialLog()
	{
		super(MapColor.WOOD);
		setBurning();
		setRequiresTool();
	}
}
