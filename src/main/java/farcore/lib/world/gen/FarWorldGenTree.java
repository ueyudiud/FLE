/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world.gen;

import farcore.lib.tree.ITreeGenerator;
import farcore.lib.tree.TreeInfo;
import nebula.base.function.Selector;

public class FarWorldGenTree extends FarWorldGenerator
{
	private final ITreeGenerator		generator;
	private final Selector<TreeInfo>	selector;
	
	public FarWorldGenTree(ITreeGenerator generator, Selector<TreeInfo> selector)
	{
		this.generator = generator;
		this.selector = selector;
	}
	
	@Override
	protected boolean generate()
	{
		return this.generator.generateTreeAt(this.world, this.pos, this.rand, this.selector.next(this.rand));
	}
}
