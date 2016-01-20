package farcore.block;

import farcore.block.item.ItemBlockBase;
import farcore.tileentity.TEBase;
import farcore.util.Direction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockWithTile extends BlockBase
{
	private static Direction getDefaultFacing(EntityLivingBase entity)
	{
		int l = MathHelper
				.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		switch (l) {
		case 0:
			return Direction.SOUTH;
		case 1:
			return Direction.WEST;
		case 2:
			return Direction.NORTH;
		case 3:
			return Direction.EAST;
		default:
			return Direction.UNKNOWN;
		}
	}

	public BlockWithTile(String unlocalized,
			Class<? extends ItemBlockBase> clazz, Material materialIn)
	{
		super(unlocalized, clazz, materialIn);
	}

	public BlockWithTile(String unlocalized, Material materialIn)
	{
		super(unlocalized, materialIn);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
		{
			((TEBase) world.getTileEntity(pos)).onBlockBreak();
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
			return ((TEBase) world.getTileEntity(pos)).canConnectRedstone(side);
		else
			return super.canConnectRedstone(world, pos, side);
	}

	@Override
	public abstract TEBase createTileEntity(World world, IBlockState state);

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos,
			IBlockState state, float chance, int fortune)
	{
		if (tileThread.get() instanceof TEBase)
		{
			((TEBase) tileThread.get()).onHarvest(harvesters.get(), pos,
					fortune, false);
		}
		else
		{
			super.dropBlockAsItemWithChance(worldIn, pos, state, chance,
					fortune);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
			return ((TEBase) world.getTileEntity(pos)).getLightValue();
		else
			return super.getLightValue(world, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, BlockPos pos,
			IBlockState state, EnumFacing side)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
			return ((TEBase) world.getTileEntity(pos))
					.getRedstonePower(Direction.toDirection(side));
		else
			return super.isProvidingStrongPower(world, pos, state, side);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos,
			IBlockState state, EntityPlayer player, Direction side, Vec3 vec)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
			return ((TEBase) world.getTileEntity(pos)).onBlockActivated(player,
					side, vec);
		return super.onBlockActivated(world, pos, state, player, side, vec);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
		{
			((TEBase) world.getTileEntity(pos)).onBlockExploded(explosion);
		}
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		if (world.getTileEntity(pos) instanceof TEBase)
		{
			((TEBase) world.getTileEntity(pos)).onBlockPlaced(stack, placer,
					getDefaultFacing(placer));
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	protected void onSilkHarvest(World world, EntityPlayer player, BlockPos pos,
			IBlockState state, TileEntity te)
	{
		if (te instanceof TEBase)
		{
			((TEBase) te).onHarvest(player, pos, 0, true);
		}
		else
		{
			super.onSilkHarvest(world, player, pos, state, te);
		}
	}
}