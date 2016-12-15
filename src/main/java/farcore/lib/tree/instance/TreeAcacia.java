package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.bio.DNAHandler;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeAcacia extends TreeBase
{
	private final TreeGenAcacia generator1 = new TreeGenAcacia(this, 0.022F);
	
	public TreeAcacia()
	{
		super("acacia");
		this.helper = new DNAHandler[0];
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setHeight(5 + info.height, 6 + info.height);
		}
		else
		{
			this.generator1.setHeight(5, 6);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}