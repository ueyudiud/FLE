package farcore.lib.tree.instance;

import static farcore.data.TDNA.COLD_I;
import static farcore.data.TDNA.COLD_II;
import static farcore.data.TDNA.COLD_III;
import static farcore.data.TDNA.EMPTY;
import static farcore.data.TDNA.GROWTH_I;
import static farcore.data.TDNA.GROWTH_III;
import static farcore.data.TDNA.GROWTH_IV;
import static farcore.data.TDNA.HEIGHT_I;
import static farcore.data.TDNA.HEIGHT_II;

import java.util.Random;

import farcore.lib.collection.Stack;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.tree.dna.TreeDNAProp;
import net.minecraft.world.World;

public class TreeBirch extends TreeBase
{
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 93), new Stack(HEIGHT_I, 17), new Stack(HEIGHT_II, 2)),
			new TreeDNAProp(new Stack(GROWTH_I, 281), new Stack(EMPTY, 38), new Stack(GROWTH_III, 9), new Stack(GROWTH_IV, 2)),
			new TreeDNAProp(new Stack(COLD_I, 18), new Stack(COLD_II, 4), new Stack(COLD_III)));
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.04F);
	
	public TreeBirch()
	{
		helper = HELPER;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			generator1.setHeight(info.height / 5 + 5, info.height / 4 + 2);
		}
		else
		{
			generator1.setHeight(5, 2);
		}
		return generator1.generateTreeAt(world, x, y, z, random, info);
	}
}