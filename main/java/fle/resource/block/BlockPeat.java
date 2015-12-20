package fle.resource.block;

import net.minecraft.block.material.Material;
import flapi.block.old.BlockFle;

public class BlockPeat extends BlockFle
{
	public BlockPeat(String aName, String aLocalized)
	{
		super(aName, aLocalized, Material.ground);
		setHardness(0.6F);
		setResistance(0.2F);
	}
}