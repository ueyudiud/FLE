package farcore.lib.tree.instance;

import static farcore.data.TDNA.COLD_I;
import static farcore.data.TDNA.COLD_II;
import static farcore.data.TDNA.EMPTY;
import static farcore.data.TDNA.GROWTH_I;
import static farcore.data.TDNA.GROWTH_III;
import static farcore.data.TDNA.GROWTH_V;
import static farcore.data.TDNA.HEIGHT_I;
import static farcore.data.TDNA.HEIGHT_II;
import static farcore.data.TDNA.HEIGHT_III;

import java.util.Random;

import farcore.lib.collection.Stack;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.lib.tree.dna.TreeCharacter;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.tree.dna.TreeDNAProp;
import net.minecraft.world.World;

public class TreeOak extends TreeBase
{
	private static final TreeCharacter HUGE = new TreeCharacter((char) 0x41);
	private static final TreeDNAHelper HELPER = new TreeDNAHelper(
			new TreeDNAProp(new Stack(EMPTY, 31), new Stack(HUGE)),
			new TreeDNAProp(new Stack(EMPTY, 102), new Stack(HEIGHT_I, 23), new Stack(HEIGHT_II, 7), new Stack(HEIGHT_III, 2)),
			new TreeDNAProp(new Stack(GROWTH_I, 281), new Stack(EMPTY, 38), new Stack(GROWTH_III, 9), new Stack(GROWTH_V, 2)),
			new TreeDNAProp(new Stack(COLD_I, 10), new Stack(COLD_II, 2)));
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.04F);
	private final TreeGenBig generator2 = new TreeGenBig(this, 0.02F);

	static
	{
		HUGE.prop = "height";
		HUGE.level = 1;
	}

	public TreeOak()
	{
		helper = HELPER;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null && info.map.get("huge") > 0)
		{
			generator2.setScale(1.0 + info.height * 0.03125, 1.0, 1.0);
			return generator2.generateTreeAt(world, x, y, z, random, info);
		}
		else
		{
			if(info != null)
			{
				generator1.setHeight(info.height / 4 + 4, info.height / 3 + 3);
			}
			else
			{
				generator1.setHeight(4, 3);
			}
			return generator1.generateTreeAt(world, x, y, z, random, info);
		}
	}
}