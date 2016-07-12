package farcore.lib.block.instance;

import java.util.ArrayList;

import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tree.ITree;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCoreLeaves extends BlockLeaves implements ITileEntityProvider
{
	private BlockLeaves leaves;
	
	public BlockCoreLeaves(Mat material, BlockLeaves leaves)
	{
		super("leaves.core." + material.name, material.tree, material.localName + " Leaves");
		this.leaves = leaves;
	}
		
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		super.breakBlock(world, x, y, z, block, meta);
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TECoreLeaves)
		{
			((TECoreLeaves) tile).onBlockBreak(block, meta);
		}
		world.removeTileEntity(x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TECoreLeaves();
	}
	
	@Override
	protected ItemStack createStackedBlock(int meta)
	{
		return new ItemStack(leaves, 1);
	}
}