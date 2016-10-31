package farcore.lib.tree.instance;

import static farcore.data.TDNA.COLD_I;
import static farcore.data.TDNA.EMPTY;
import static farcore.data.TDNA.GROWTH_I;
import static farcore.data.TDNA.GROWTH_III;
import static farcore.data.TDNA.GROWTH_IV;
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

public class TreeAcacia extends TreeBase
{
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 38), new Stack(HEIGHT_I, 3)),
			new TreeDNAProp(new Stack(EMPTY, 38), new Stack(GROWTH_I, 3), new Stack(GROWTH_III, 9), new Stack(GROWTH_IV, 2)),
			new TreeDNAProp(new Stack(EMPTY, 37), new Stack(COLD_I, 3)),
			new TreeDNAProp(new Stack(HOT_I, 12), new Stack(HOT_II, 2)));
	private final TreeGenAcacia generator1 = new TreeGenAcacia(this, 0.022F);
	
	public TreeAcacia()
	{
		helper = HELPER;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			generator1.setHeight(5 + info.height, 6 + info.height);
		}
		else
		{
			generator1.setHeight(5, 6);
		}
		return generator1.generateTreeAt(world, x, y, z, random, info);
	}
}