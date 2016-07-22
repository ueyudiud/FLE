package farcore.lib.tile.instance;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.material.Mat;
import farcore.lib.tile.TEStatic;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import farcore.util.U.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class TECoreLeaves extends TEStatic
{
	public TreeInfo info;

	public TECoreLeaves()
	{

	}
	public TECoreLeaves(ITree tree, TreeInfo info)
	{
		if(info == null)
		{
			info = new TreeInfo();
			tree.decodeDNA(info, tree.makeNativeDNA());
		}
		this.info = info;
	}

	public void setTree(TreeInfo info, boolean causeUpdate)
	{
		this.info = info;
		if(causeUpdate)
			syncToNearby();
	}

	public ItemStack provideSapling(Mat material)
	{
		return new ItemStack(EnumBlock.sapling.block, 1, material.id);
	}

	@Override
	public void onBlockBreak(IBlockState state)
	{
		Worlds.spawnDropsInWorld(this, ImmutableList.of(provideSapling(((BlockLeavesCore) state.getBlock()).tree.material())));
	}
}