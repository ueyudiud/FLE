package farcore.lib.tree;

import java.util.Random;

import farcore.data.M;
import farcore.lib.material.Mat;
import net.minecraft.world.World;

public class TreeVoid extends TreeBase
{
	public TreeVoid()
	{
		super(M.VOID);
	}

	@Override
	public void decodeDNA(TreeInfo biology, String dna)
	{
		
	}

	@Override
	public String makeNativeDNA()
	{
		return "";
	}

	@Override
	public String makeChildDNA(int generation, String par)
	{
		return "";
	}

	@Override
	public String makeOffspringDNA(String par1, String par2)
	{
		return "";
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random rand, TreeInfo info)
	{
		return false;
	}
}