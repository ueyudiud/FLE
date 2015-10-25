package fle.core.block;

import net.minecraft.block.material.Material;
import fle.api.block.BlockFle;

public class BlockPeat extends BlockFle
{
	public BlockPeat(String aName, String aLocalized)
	{
		super(aName, aLocalized, Material.ground);
		setHardness(0.6F);
		setResistance(0.2F);
	}
}