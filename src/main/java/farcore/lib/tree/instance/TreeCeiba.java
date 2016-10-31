package farcore.lib.tree.instance;

import static farcore.data.TDNA.EMPTY;
import static farcore.data.TDNA.GROWTH_I;
import static farcore.data.TDNA.GROWTH_II;
import static farcore.data.TDNA.HEIGHT_I;
import static farcore.data.TDNA.HEIGHT_II;
import static farcore.data.TDNA.HEIGHT_III;
import static farcore.data.TDNA.HEIGHT_IV;
import static farcore.data.TDNA.HOT_I;
import static farcore.data.TDNA.HOT_II;
import static farcore.data.TDNA.HOT_III;

import java.util.Random;

import farcore.lib.collection.Stack;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.tree.dna.TreeDNAProp;
import net.minecraft.world.World;

public class TreeCeiba extends TreeBase
{
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 27), new Stack(HEIGHT_I, 21), new Stack(HEIGHT_II, 13), new Stack(HEIGHT_III, 7), new Stack(HEIGHT_IV, 2)),
			new TreeDNAProp(new Stack(EMPTY, 49), new Stack(GROWTH_I, 18), new Stack(GROWTH_II, 2)),
			new TreeDNAProp(new Stack(HOT_I, 18), new Stack(HOT_II, 4), new Stack(HOT_III)));
	private final TreeGenJungle generator1 = new TreeGenJungle(this, 1.2E-2F);
	
	public TreeCeiba()
	{
		helper = HELPER;
		leavesCheckRange = 6;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			generator1.setHeight(36 + info.height * 2, 4 + info.height);
		}
		else
		{
			generator1.setHeight(38, 4);
		}
		return generator1.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 120;
	}
}