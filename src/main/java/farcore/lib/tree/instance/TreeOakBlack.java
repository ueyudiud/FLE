package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.bio.DNAHandler;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeOakBlack extends TreeBase
{
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.02F);
	
	public TreeOakBlack()
	{
		super("oak_black");
		this.helper = new DNAHandler[0];
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if(info != null)
		{
			this.generator1.setHeight(info.height / 2 + 4, info.height + 3);
		}
		else
		{
			this.generator1.setHeight(4, 3);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 90;
	}
}