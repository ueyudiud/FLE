package fle.resource.block.sand;

import flapi.block.InformationBlock;
import flapi.util.Values;

public class BlockSand1 extends BlockSand
{
	private void init()
	{
		addInfomation(new InformationBlock("sand"       , 0).setHardness(0.6F).setResistance(1.0F).setTextureName(Values.TEXTURE_FILE + ":sand/sand"));
		addInfomation(new InformationBlock("dune"       , 1).setHardness(0.5F).setResistance(0.8F).setTextureName(Values.TEXTURE_FILE + ":sand/dune"));
		addInfomation(new InformationBlock("coarse sand", 2).setHardness(1.1F).setResistance(1.1F).setTextureName(Values.TEXTURE_FILE + ":sand/coarse_sand"));
	}

	public BlockSand1()
	{
		super("i");
		init();
	}
}