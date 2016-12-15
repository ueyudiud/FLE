package farcore.lib.tile.instance;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.material.Mat;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BreakBlock;
import farcore.lib.tile.abstracts.TEStatic;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import farcore.util.U.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TECoreLeaves extends TEStatic implements ITB_BreakBlock
{
	public TreeInfo info;
	
	public TECoreLeaves()
	{
		this.info = new TreeInfo();
	}
	public TECoreLeaves(ITree tree, TreeInfo info)
	{
		if(info == null)
		{
			info = new TreeInfo();
			info.DNA = tree.applyNativeDNA();
		}
		this.info = info;
		info.DNA.expressTrait(info);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		this.info.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.info.readFromNBT(compound);
		super.readFromNBT(compound);
	}
	
	public void setTree(TreeInfo info, boolean causeUpdate)
	{
		this.info = info;
		if(causeUpdate)
		{
			syncToNearby();
		}
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