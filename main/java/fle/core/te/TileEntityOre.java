package fle.core.te;

import net.minecraft.block.Block;
import fle.api.material.MaterialOre;
import fle.api.te.TEStatic;

public class TileEntityOre extends TEStatic
{
	private Block base;
	private int metadata;
	private MaterialOre ore;
	
	public TileEntityOre()
	{
		
	}
	
	public void init(MaterialOre ore, Block baseBlock, int metadata)
	{
		this.ore = ore;
		base = baseBlock;
		this.metadata = metadata;
	}	
}