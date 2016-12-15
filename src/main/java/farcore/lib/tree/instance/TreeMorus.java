package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.bio.DNAHandler;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeMorus extends TreeBase
{
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.08F, false);
	
	public TreeMorus()
	{
		super("morus");
		this.helper = new DNAHandler[0];
		this.generator1.setTreeLeavesShape(2, 5, 1, 3.2F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setTreeLogShape(4 + info.height / 4, 3 + info.height / 3);
		}
		else
		{
			this.generator1.setTreeLogShape(4, 3);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}