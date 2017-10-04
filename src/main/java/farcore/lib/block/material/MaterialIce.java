package farcore.lib.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialIce extends Material
{
	public MaterialIce()
	{
		super(MapColor.ICE);
		setRequiresTool();
	}
	
	@Override
	public boolean isOpaque()
	{
		return false;
	}
}
