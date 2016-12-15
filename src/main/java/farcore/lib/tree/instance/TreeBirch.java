package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.bio.DNAHandler;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeBirch extends TreeBase
{
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.04F);
	
	public TreeBirch()
	{
		super("birch");
		this.helper = new DNAHandler[0];
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setHeight(info.height / 5 + 5, info.height / 4 + 2);
		}
		else
		{
			this.generator1.setHeight(5, 2);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}