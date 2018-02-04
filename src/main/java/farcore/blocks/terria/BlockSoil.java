/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks.terria;

import java.util.List;
import java.util.Random;

import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.base.ObjArrayParseHelper;
import nebula.common.block.IHitByFallenBehaviorBlock;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Properties;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This provide the ground related block, which is fallable.
 * 
 * @author ueyudiud
 */
public class BlockSoil extends BlockSoilLike implements ISmartFallableBlock
{
	protected static boolean canFallBelow(World world, BlockPos pos, IBlockState state)
	{
		BlockPos pos1 = pos.down();
		IBlockState state1 = world.getBlockState(pos1);
		if (state1.getBlock().isAir(state1, world, pos1))
		{
			return true;
		}
		else if (state1.getBlock() instanceof IHitByFallenBehaviorBlock)
		{
			IHitByFallenBehaviorBlock block = (IHitByFallenBehaviorBlock) state1.getBlock();
			return block.isPermeatableBy(world, pos1, state1, state);
		}
		else
		{
			return state1.getMaterial().isReplaceable();
		}
	}
	
	protected static byte canFallNearby(World world, BlockPos pos, IBlockState state)
	{
		byte result = 0;
		BlockPos pos2;
		pos2 = pos.north();
		if (Worlds.isAirOrReplacable(world, pos2) && canFallBelow(world, pos2, state))
		{
			result |= Direction.N.horizontalOrdinal;
		}
		pos2 = pos.south();
		if (Worlds.isAirOrReplacable(world, pos2) && canFallBelow(world, pos2, state))
		{
			result |= Direction.S.horizontalOrdinal;
		}
		pos2 = pos.west();
		if (Worlds.isAirOrReplacable(world, pos2) && canFallBelow(world, pos2, state))
		{
			result |= Direction.W.horizontalOrdinal;
		}
		pos2 = pos.east();
		if (Worlds.isAirOrReplacable(world, pos2) && canFallBelow(world, pos2, state))
		{
			result |= Direction.E.horizontalOrdinal;
		}
		return result;
	}
	
	public static final PropertyEnum<EnumCoverType> COVER_TYPE = Properties.get(EnumCoverType.class);
	
	public BlockSoil(String modid, String name, Material materialIn, Mat mat, PropertyBlockable soil)
	{
		super(modid, name, materialIn, mat, soil);
		this.uneffectiveSpeedMultiplier = 1F / 150F;
		this.effectiveSpeedMultiplier = 1F / 20F;
	}
	
	public BlockSoil(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn, Mat mat, PropertyBlockable soil)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn, mat, soil);
		this.uneffectiveSpeedMultiplier = 1F / 150F;
		this.effectiveSpeedMultiplier = 1F / 20F;
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		// The meta higher than 5 can not be harvested, so no name display.
		MC.soil.registerOre(this.material, this);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, EnumFacing facing, ItemStack stack)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		super.neighborChanged(state, worldIn, pos, blockIn);
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	protected boolean checkAndFall(World world, BlockPos pos, IBlockState state, Random rand, boolean checkFallToNearby)
	{
		if (canFallBelow(world, pos, state))
		{
			return Worlds.fallBlock(world, pos, state);
		}
		if (checkFallToNearby)
		{
			byte sides = canFallNearby(world, pos, state);
			switch (state.getValue(COVER_TYPE).noCover)
			{
			case NONE:
				if (L.bitCounts(sides) >= 3) return Worlds.fallBlock(world, pos,
						Direction.DIRECTIONS_2D[L.randomBit(sides, rand)].offset(pos.down()), state);
				break;
			case FROZEN:
				if (sides != 0) return Worlds.fallBlock(world, pos,
						Direction.DIRECTIONS_2D[L.randomBit(sides, rand)].offset(pos.down()), state);
				break;
			case GRASS:
			case MYCELIUM:
			case TUNDRA:
			case TUNDRA_FROZEN:
				int c = L.bitCounts(sides);
				if (c == 4 || (c == 3 && rand.nextInt(5) == 0)) return Worlds.fallBlock(world, pos,
						Direction.DIRECTIONS_2D[L.randomBit(sides, rand)].offset(pos.down()), state);
				break;
			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if (!worldIn.isRemote)
		{
			IBlockState state2 = updateBase(worldIn, pos, state, random, false);
			if (checkAndFall(worldIn, pos, state2, random, true)) return;
			if (state != state2)
			{
				worldIn.setBlockState(pos, state2, 2);
			}
			spreadCoverPlant(worldIn, pos, state2, random);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if (!worldIn.isRemote)
		{
			IBlockState state2 = updateBase(worldIn, pos, state, random, true);
			if (checkAndFall(worldIn, pos, state2, random, false)) return;
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return ObjArrayParseHelper.newArrayList(ItemMulti.createStack(this.material, MC.pile, 6 + RANDOM.nextInt(3)));
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return !canFallBelow(world, pos, state);
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
		EnumCoverType type = block.getBlock().getValue(COVER_TYPE);
		float amt = this.material.getProperty(MP.property_tool).damageToEntity;
		amt *= block.motionY * block.motionY;
		amt /= 25F;
		return type.isFrozen ? amt * 1.2F : amt;
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		float amt = 1F - this.material.getProperty(MP.fallen_damage_deduction) / 10000F;
		EnumCoverType type = worldIn.getBlockState(pos).getValue(COVER_TYPE);
		if (type.isSnow)
		{
			amt *= 1.2F;
		}
		entityIn.fall(fallDistance, amt);
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		super.fillWithRain(worldIn, pos);
		if (worldIn.canSnowAt(pos, false))
		{
			IBlockState state = worldIn.getBlockState(pos);
			EnumCoverType type = state.getValue(COVER_TYPE);
			if (worldIn.rand.nextInt(7) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(COVER_TYPE, type.snowCover), 3);
			}
		}
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(COVER_TYPE).isFrozen ? EnumCoverType.FROZEN.ordinal() : EnumCoverType.NONE.ordinal();
	}
}
