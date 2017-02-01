/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.behavior;

import static farcore.lib.block.instance.BlockRock.HEATED;
import static farcore.lib.block.instance.BlockRock.TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.EnumRockType;
import farcore.data.EnumToolTypes;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.tile.instance.TECustomCarvedStone;
import nebula.common.block.IBlockBehavior;
import nebula.common.data.EnumToolType;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.tile.IToolableTile;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class RockBehavior<B extends BlockRock> extends PropertyBlockable<B> implements IBlockBehavior<B>
{
	public RockBehavior(Mat material)
	{
		this.material = material;
	}
	
	@Override
	public float getBlockHardness(B block, IBlockState state, World world, BlockPos pos)
	{
		switch (state.getValue(TYPE))
		{
		case cobble :
			return this.hardness * .5F;
		case cobble_art :
			return this.hardness * .1F;
		case brick :
		case brick_compacted :
		case brick_crushed :
		case brick_mossy :
			return this.hardness * 1.2F;
		default:
			return this.hardness;
		}
	}
	
	public void onBlockPlacedBy(B block, IBlockState state, World world, BlockPos pos, EntityLivingBase placer,
			ItemStack stack)
	{
		if(!canBlockStay(world, pos, state))
		{
			Worlds.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(B block, IBlockState state, BlockPos pos, IBlockAccess world, TileEntity tile,
			int fortune, boolean silkTouch)
	{
		List<ItemStack> ret = new ArrayList();
		if(silkTouch)
		{
			ret.add(new ItemStack(block, 1, state.getValue(TYPE).ordinal()));
		}
		else
		{
			Random rand = world instanceof World ? ((World) world).rand : L.random();
			EnumRockType type = state.getValue(TYPE);
			switch (type)
			{
			case resource:
			case cobble:
				ret.add(new ItemStack(EnumItem.stone_chip.item, rand.nextInt(4) + 3, block.material.id));
				break;
			case cobble_art:
				ret.add(new ItemStack(EnumItem.stone_chip.item, 9, this.material.id));
				break;
			default:
				ret.add(new ItemStack(block, 1, type.noSilkTouchDropMeta));
				break;
			}
		}
		return ret;
	}
	
	@Override
	public void notifyAfterTicking(B block, IBlockState state, World world, BlockPos pos, IBlockState changed)
	{
		if(!canBlockStayTotally(world, pos, state, world.rand))
		{
			Worlds.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public void updateTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if(!canBlockStay(world, pos, state))
		{
			Worlds.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public void randomTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		EnumRockType type = state.getValue(TYPE);
		switch (type)
		{
		case resource :
			//			if(ThermalNet.getTemperature(worldIn, pos, false) > minTemperatureForExplosion)
			//			{
			//				if(!state.getValue(HEATED) && random.nextInt(3) == 0)
			//				{
			//					worldIn.setBlockState(pos, state.withProperty(HEATED, true), 6);
			//				}
			//				else if(Worlds.isBlockNearby(worldIn, pos, EnumBlock.water.block, true))
			//				{
			//					worldIn.setBlockState(pos, state.withProperty(TYPE, EnumRockType.cobble), 3);
			//					worldIn.playSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
			//							.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F, true);
			//					for (int k = 0; k < 8; ++k)
			//					{
			//						worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
			//					}
			//					return;
			//				}
			//			}
			//			else if(state.getValue(HEATED) && random.nextInt(4) == 0)
			//			{
			//				worldIn.setBlockState(pos, state.withProperty(HEATED, false), 6);
			//			}
		default:
			break;
		}
		updateTick(block, state, world, pos, random);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if(state.getValue(HEATED))
		{
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			double u1, v1, t1;
			for(int i = 0; i < 2; ++i)
			{
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = -random.nextDouble() * .05;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + v1, z - 0.1, 0D, 0D, t1);
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = -random.nextDouble() * .1;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y, z + v1, 0D, t1, 0D);
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = -random.nextDouble() * .05;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.1, y + u1, z + v1, t1, 0D, 0D);
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = random.nextDouble() * .05;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + v1, z + 1.1, 0D, 0D, t1);
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = random.nextDouble() * .05;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + u1, y + 1D, z + v1, 0D, 0D, 0D);
				u1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				v1 = (1D + random.nextDouble() - random.nextDouble()) * .5;
				t1 = random.nextDouble() * .05;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 1.1, y + u1, z + v1, t1, 0D, 0D);
			}
		}
	}
	
	public boolean isFlammable(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return state.getValue(TYPE).burnable;
	}
	
	public double getThermalConduct(B block, IBlockState state, World world, BlockPos pos)
	{
		return this.material.thermalConductivity;
	}
	
	public int getFireSpreadSpeed(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(block, state, world, pos, face) ? 40 : 0;
	}
	
	public boolean onBurn(B block, IBlockState state, World world, BlockPos pos, float burnHardness, Direction direction)
	{
		EnumRockType type = state.getValue(TYPE);
		if(type.burnable)
		{
			world.setBlockState(pos, state.withProperty(TYPE, type.burned()), 3);
		}
		return false;
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		EnumRockType type = state.getValue(TYPE);
		switch (type)
		{
		case cobble :
		case mossy :
		case cobble_art :
			return world.isSideSolid(pos.down(), EnumFacing.UP, false);
		default:
			return !world.isAirBlock(pos.down()) ? true :
				world.isSideSolid(pos.north(), EnumFacing.SOUTH, false) ||
				world.isSideSolid(pos.south(), EnumFacing.NORTH, false) ||
				world.isSideSolid(pos.east() , EnumFacing.WEST , false) ||
				world.isSideSolid(pos.west() , EnumFacing.EAST , false);
		}
	}
	
	public boolean canBlockStayTotally(IBlockAccess world, BlockPos pos, IBlockState state, Random rand)
	{
		EnumRockType type = state.getValue(TYPE);
		switch (type)
		{
		case cobble :
		case mossy :
		case cobble_art :
			if(!world.isSideSolid(pos.down(), EnumFacing.UP, false))
				return false;
			BlockPos pos1 = pos.down();
			for(Direction direction : Direction.DIRECTIONS_2D)
			{
				if(world.isAirBlock(direction.offset(pos)) &&
						world.isAirBlock(direction.offset(pos1)) &&
						rand.nextInt(7) == 0)
					return false;
			}
			return true;
		case resource :
			if(!world.isAirBlock(pos.down())) return true;
			int c = 0;
			for(Direction direction : Direction.DIRECTIONS_3D)
			{
				if(world.isAirBlock(direction.offset(pos)))
				{
					c++;
				}
			}
			return c > 3 ? true : c == 3 ? rand.nextInt(9) != 0 : rand.nextInt(3) != 0;
		default :
			return !world.isAirBlock(pos.down());
		}
	}
	
	
	public boolean canFallingBlockStay(B block, IBlockState state, World world, BlockPos pos)
	{
		return canBlockStay(world, pos, state);
	}
	
	public float onFallOnEntity(B block, IBlockState state, World world, EntityFallingBlockExtended entity, Entity target)
	{
		return (float) ((1.0F + this.material.toolDamageToEntity) * entity.motionY * entity.motionY * 0.25F);
	}
	
	public boolean onFallOnGround(B block, IBlockState state, World world, BlockPos pos, int height,
			NBTTagCompound tileNBT)
	{
		EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
		boolean broken = height < 2 ? false : height < 5 ? world.rand.nextInt(5 - height) == 0 : true;
		if(broken)
		{
			state = state.withProperty(TYPE, EnumRockType.values()[state.getValue(TYPE).fallBreakMeta]);
		}
		state = state.withProperty(HEATED, false);
		world.setBlockState(pos, state, 3);
		return true;
	}
	
	public ActionResult<Float> onToolClick(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player,
			EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if(tool == EnumToolTypes.CHISEL)
		{
			if(player.canPlayerEdit(pos, side.of(), stack))
			{
				EnumRockType type = state.getValue(TYPE);
				if(world.setBlockState(pos, EnumBlock.carved_rock.block.getDefaultState(), 2))
				{
					TileEntity tile = world.getTileEntity(pos);
					if(tile instanceof TECustomCarvedStone)
					{
						((TECustomCarvedStone) tile).setRock(this.material, type);
						return ((TECustomCarvedStone) tile).carveRock(player, hitX, hitY, hitZ);
					}
				}
			}
		}
		return IToolableTile.DEFAULT_RESULT;
	}
}