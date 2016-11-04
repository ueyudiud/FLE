package farcore.lib.world.gen;

import farcore.lib.collection.Selector;
import farcore.lib.tree.ITreeGenerator;
import farcore.lib.tree.TreeInfo;

public class FarWorldGenTree extends FarWorldGenerator
{
	private final ITreeGenerator generator;
	private final Selector<TreeInfo> selector;
	
	public FarWorldGenTree(ITreeGenerator generator, Selector<TreeInfo> selector)
	{
		this.generator = generator;
		this.selector = selector;
	}

	@Override
	protected boolean generate()
	{
		return generator.generateTreeAt(world, pos, rand, selector.next(rand));
	}
}