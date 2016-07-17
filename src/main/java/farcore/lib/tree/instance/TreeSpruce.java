package farcore.lib.tree.instance;

import static farcore.data.TDNA.*;

import java.util.Random;

import farcore.lib.collection.Stack;
import farcore.lib.material.Mat;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.tree.dna.TreeDNAProp;
import net.minecraft.world.World;

public class TreeSpruce extends TreeBase
{
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 102), new Stack(HEIGHT_I, 63), new Stack(HEIGHT_II, 27), new Stack(HEIGHT_III, 3)),
			new TreeDNAProp(new Stack(EMPTY, 21), new Stack(GROWTH_I, 12), new Stack(GROWTH_III)),
			new TreeDNAProp(new Stack(COLD_I, 18), new Stack(COLD_II, 7), new Stack(COLD_III, 3)));
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.05F);
	
	public TreeSpruce(Mat material)
	{
		super(material);
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			generator1.setHeight(6 + info.height / 2, 4 + info.height / 4);
		}
		else
		{
			generator1.setHeight(6, 4);
		}
		return generator1.generateTreeAt(world, x, y, z, random, info);
	}
}