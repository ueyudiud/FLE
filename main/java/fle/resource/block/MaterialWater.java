package fle.resource.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

public class MaterialWater extends MaterialLiquid
{
	public MaterialWater(MapColor color)
	{
		super(color);
		setNoPushMobility();
	}
}