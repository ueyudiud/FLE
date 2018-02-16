/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.plant;

import java.util.List;
import java.util.Random;

import farcore.blocks.flora.BlockPlant;
import farcore.lib.material.Mat;
import nebula.common.block.IBlockBehavior;
import nebula.common.block.IMetaExtHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPlant<B extends BlockPlant> extends IMetaExtHandler, IBlockBehavior<B>, IPlantable
{
	AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.75, 0.875);
	
	Mat material();
	
	Block block();
	
	@Override
	default AxisAlignedBB getBoundingBox(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BUSH_AABB;
	}
	
	@Override
	default AxisAlignedBB getCollisionBoundingBox(B block, IBlockState state, World world, BlockPos pos)
	{
		return Block.NULL_AABB;
	}
	
	@Override
	default void notifyAfterTicking(B block, IBlockState state, World world, BlockPos pos, IBlockState changed)
	{
		if (!canBlockStay(block, state, world, pos))
		{
			world.setBlockToAir(pos);
		}
	}
	
	@Override
	default void updateTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if (!canBlockStay(block, state, world, pos))
		{
			world.setBlockToAir(pos);
		}
	}
	
	@Override
	default boolean canPlaceTorchOnTop(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	default boolean isSideSolid(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	default boolean canSustainPlant(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return false;
	}
	
	default boolean canBlockStay(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IBlockState state1 = world.getBlockState(pos.down());
		return state1.getBlock().canSustainPlant(state1, world, pos, EnumFacing.UP, this);
	}
	
	EnumPlantType getPlantType(IBlockAccess world, BlockPos pos);
	
	@Override
	default IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos);
	}
	
	@Override
	default boolean canEntityDestroy(B block, IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return true;
	}
	
	@Override
	default boolean canConnectRedstone(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	default boolean isFlammable(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	default boolean isNormalCube(B block, IBlockState state)
	{
		return false;
	}
	
	@Override
	default boolean isOpaqueCube(B block, IBlockState state)
	{
		return false;
	}
	
	default boolean isTranslucent(B block, IBlockState state)
	{
		return false;
	}
	
	@Override
	default int getLightOpacity(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	default void onEntityCollidedWithBlock(B block, IBlockState state, World world, BlockPos pos, Entity entity)
	{
		entity.motionX *= 0.9375F;
		entity.motionZ *= 0.9375F;
	}
	
	@Override
	default boolean canBreakEffective(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	default float getBlockHardness(B block, IBlockState state, World world, BlockPos pos)
	{
		return 0.4F;
	}
	
	@Override
	default float getExplosionResistance(B block, IBlockState state, World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return 0.0F;
	}
	
	@SideOnly(Side.CLIENT)
	default void addSubBlocks(Item item, List<ItemStack> list)
	{
		list.add(new ItemStack(item));
	}
}
