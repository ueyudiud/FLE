package farcore.lib.tile.instance;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.block.instance.BlockCoreLeaves;
import farcore.lib.material.Mat;
import farcore.lib.tile.TEStatic;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import farcore.util.U.Worlds;
import net.minecraft.block.Block;
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
		{
			syncToNearby();
		}
	}
	
	@Override
	public void onBlockBreak(Block block, int meta)
	{
		super.onBlockBreak(block, meta);
		Worlds.spawnDropsInWorld(this, ImmutableList.of(provideSapling(((BlockCoreLeaves) block).info.material())));
	}
	
	public ItemStack provideSapling(Mat material)
	{
		return new ItemStack(EnumBlock.sapling.block, 1, material.id);
	}
}