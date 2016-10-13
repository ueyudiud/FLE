package farcore.lib.block;

import java.util.Random;

import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.util.U;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFallable extends BlockBase implements ISmartFallableBlock
{
	protected static final PropertyBool PROPERTY_FALLABLE = PropertyBool.create("fallable");
	
	public BlockFallable(String name, Material materialIn)
	{
		super(name, materialIn);
		setTickRandomly(true);
	}
	public BlockFallable(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		setTickRandomly(true);
	}
	public BlockFallable(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		setTickRandomly(true);
	}
	public BlockFallable(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		setTickRandomly(true);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_FALLABLE);
	}

	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return super.initDefaultState(state).withProperty(PROPERTY_FALLABLE, true);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canFallingBlockStay(worldIn, pos, worldIn.getBlockState(pos).getActualState(worldIn, pos));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (!worldIn.isRemote && !canFallingBlockStay(worldIn, pos, state))
		{
			U.Worlds.fallBlock(worldIn, pos, state);
		}
	}

	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
		;
	}

	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return !state.getValue(PROPERTY_FALLABLE);
	}

	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return 0;
	}
}