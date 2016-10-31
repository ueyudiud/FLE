package farcore.lib.tree.instance;

import static farcore.data.TDNA.COLD_I;
import static farcore.data.TDNA.EMPTY;
import static farcore.data.TDNA.GROWTH_I;
import static farcore.data.TDNA.GROWTH_II;
import static farcore.data.TDNA.GROWTH_III;
import static farcore.data.TDNA.HEIGHT_I;
import static farcore.data.TDNA.HOT_I;
import static farcore.data.TDNA.HOT_II;

import java.util.Random;

import farcore.lib.collection.Stack;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.tree.dna.TreeDNAProp;
import net.minecraft.world.World;

public class TreeWillow extends TreeBase
{
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 62), new Stack(HEIGHT_I, 3)),
			new TreeDNAProp(new Stack(EMPTY, 22), new Stack(GROWTH_I, 13), new Stack(GROWTH_II, 8), new Stack(GROWTH_III)),
			new TreeDNAProp(new Stack(EMPTY, 21), new Stack(COLD_I, 8)),
			new TreeDNAProp(new Stack(HOT_I, 12), new Stack(HOT_II, 3)));
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.08F, true);
	
	public TreeWillow()
	{
		helper = HELPER;
		generator1.setTreeLeavesShape(1, 6, 2, 3.6F);
		leavesCheckRange = 5;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			generator1.setTreeLogShape(4 + info.height / 3, 4 + info.height / 3);
		}
		else
		{
			generator1.setTreeLogShape(4, 4);
		}
		return generator1.generateTreeAt(world, x, y, z, random, info);
	}
}
