package farcore.lib.block.instance;

import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tree.Tree;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BreakBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This leaves will drop tree sapling.
 * It has a tile entity to save DNA.
 * @author ueyudiud
 *
 */
public class BlockLeavesCore extends BlockLeaves implements ITileEntityProvider
{
	public static BlockLeavesCore create(BlockLeaves leaves, Tree $tree)
	{
		return new BlockLeavesCore($tree, leaves)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return $tree.createLeavesStateContainer(this);
			}
			
			@Override
			public int getMetaFromState(IBlockState state)
			{
				return $tree.getLeavesMeta(state);
			}
			
			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return $tree.getLeavesState(this, meta);
			}
		};
	}
	
	private BlockLeaves leaves;
	
	protected BlockLeavesCore(Tree tree, BlockLeaves leaves)
	{
		super("leaves.core." + tree.material.name, tree, tree.material.localName + " Leaves");
		setCreativeTab(null);
		this.leaves = leaves;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		TileEntity tile;
		if((tile = worldIn.getTileEntity(pos)) instanceof ITB_BreakBlock)
		{
			((ITB_BreakBlock) tile).onBlockBreak(state);
		}
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECoreLeaves();
	}
}