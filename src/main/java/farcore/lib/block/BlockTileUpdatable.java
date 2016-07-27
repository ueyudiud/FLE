package farcore.lib.block;

import java.util.List;

import farcore.lib.tile.IBreakingDropableTile;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TEBase;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTileUpdatable extends BlockBase
{
	public BlockTileUpdatable(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	public BlockTileUpdatable(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
	}
	public BlockTileUpdatable(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}
	public BlockTileUpdatable(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		RayTraceResult result = U.Worlds.rayTrace(worldIn, playerIn, false);
		if(result == null) return;
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TEBase)
		{
			((TEBase) tile).onBlockClicked(playerIn, Direction.of(result.sideHit), (float) result.hitVec.xCoord, (float) result.hitVec.yCoord, (float) result.hitVec.zCoord);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TEBase)
		{
			EnumActionResult result = ((TEBase) tile).onBlockActivated(playerIn, hand, heldItem, Direction.of(side), hitX, hitY, hitZ);
			if(result != EnumActionResult.PASS) return result == EnumActionResult.SUCCESS ? true : false;
		}
		return U.TileEntities.onTileActivatedGeneral(playerIn, hand, heldItem, Direction.of(side), hitX, hitY, hitZ, tile);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IBreakingDropableTile)
		{
			List<ItemStack> stacks = ((IBreakingDropableTile) tile).getDropsOnTileRemoved(state);
			U.Worlds.spawnDropsInWorld(worldIn, pos, stacks);
		}
		if(tile instanceof TEBase)
		{
			((TEBase) tile).onBlockBreak(state);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(pos, world.getBlockState(pos), true);
		}
		super.onNeighborChange(world, pos, neighbor);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(pos, worldIn.getBlockState(pos), false);
		}
	}
}