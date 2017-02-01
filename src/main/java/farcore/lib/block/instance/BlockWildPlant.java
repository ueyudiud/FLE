package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import farcore.data.MP;
import farcore.lib.material.Mat;
import farcore.lib.plant.IPlant;
import nebula.common.block.BlockBase;
import nebula.common.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockWildPlant extends BlockBase implements IPlantable
{
	public static BlockWildPlant create(Mat material)
	{
		IPlant plant = material.getProperty(MP.property_plant);
		return new BlockWildPlant(material.name)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return plant.createBlockState(this);
			}
			
			@Override
			public int getMetaFromState(IBlockState state)
			{
				return plant.getMeta(this, state);
			}

			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return plant.getState(this, meta);
			}

			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return plant.initDefaultState(super.initDefaultState(state));
			}
		};
	}

	private IPlant plant;

	protected BlockWildPlant(String name)
	{
		super("wild.plant" + name, Material.PLANTS);
		setHardness(0.2F);
		setTickRandomly(true);
		setLightOpacity(1);
	}
	
	private Set<String> toolSet = ImmutableSet.of("knife");
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return plant.onBlockActive(this, worldIn, pos, state, Direction.of(side), playerIn, heldItem, hand, hitX, hitY, hitZ);
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		plant.onEntityWalk(this, worldIn, pos, entityIn);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return toolSet.contains(type);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState state;
		return (state = worldIn.getBlockState(pos.down())).getBlock()
				.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if(!canPlaceBlockAt(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		plant.tickUpdate(this, worldIn, pos, state, random);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		return new ArrayList(plant.getDrops(this, world, pos, state, world instanceof World ? ((World) world).rand : RANDOM));
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return plant.type();
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return getDefaultState();
	}
}