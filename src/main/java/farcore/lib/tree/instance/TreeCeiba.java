package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeCeiba extends Tree
{
	private final TreeGenJungle generator1 = new TreeGenJungle(this, 1.2E-2F);
	
	public TreeCeiba()
	{
		this.leavesCheckRange = 6;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setHeight(36 + info.height * 2, 4 + info.height);
		}
		else
		{
			this.generator1.setHeight(38, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 120;
	}
}