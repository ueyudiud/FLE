package fle.core.world.climate.surface;

import java.util.Arrays;
import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.world.gen.tree.TreeGenAcacia;
import fle.core.util.BlockInfo;
import fle.core.world.climate.Climate;
import net.minecraft.init.Blocks;

public class ClimateSavanna extends Climate
{
	public ClimateSavanna(int id, String name)
	{
		super(id, name);
		setDisableRain();
	}
	
	@Override
	public ITreeGenerator getTreeGenerator(int x, int z, Random random)
	{
		return new TreeGenAcacia();
	}
}