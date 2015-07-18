package fla.core.block.rock;

public class BlockLimestone extends BlockRocks
{
	public BlockLimestone() 
	{
		super();
		setHarvestLevel("pickaxe", 1);
		setHardness(1.75F);
		setResistance(8.0F);
	}
}