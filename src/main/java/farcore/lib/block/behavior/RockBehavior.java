/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block.behavior;

import static farcore.blocks.terria.BlockRock.HEATED;
import static farcore.blocks.terria.BlockRock.TYPE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.blocks.terria.BlockRock;
import farcore.data.EnumBlock;
import farcore.data.EnumRockType;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.IFallingStaySupport;
import farcore.lib.block.ISmartFallableBlockRockLike;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.world.TaskFalling;
import nebula.Log;
import nebula.common.NebulaWorldHandler;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Maths;
import nebula.common.util.W;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class RockBehavior<B extends BlockRock> extends PropertyBlockable<B>
implements IRockLikeBehavior<B>
{
	public Block stonechip;
	
	public RockBehavior(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		super(material, harvestLevel, hardness, explosionResistance);
	}
	
	@Override
	public float getBlockHardness(B block, IBlockState state, World world, BlockPos pos)
	{
		switch (state.getValue(TYPE))
		{
		case cobble:
			return this.hardness * .3F;
		case cobble_art:
			return this.hardness * .1F;
		case brick:
		case brick_compacted:
		case brick_crushed:
		case brick_mossy:
			return this.hardness * 1.2F;
		default:
			return this.hardness;
		}
	}
	
	public float getExplosionResistance(B block, IBlockState state, World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return this.explosionResistance;
	}
	
	public int getHarvestLevel(B block, IBlockState state)
	{
		switch (state.getValue(TYPE))
		{
		case cobble :
		case cobble_art :
			return 1;
		default :
			return this.harvestLevel;
		}
	}
	
	public String getHarvestTool(B block, IBlockState state)
	{
		return EnumToolTypes.PICKAXE.name;
	}
	
	public void onBlockPlacedBy(B block, IBlockState state, World world, BlockPos pos, EntityLivingBase placer, ItemStack stack)
	{
		if (!canBlockStay(world, pos, state))
		{
			W.fallBlock(world, pos, state);
		}
	}
	
	@Optional.Method(modid = FarCore.JEI)
	public void addDropRecipe()
	{
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.resource)),
				new AbstractStack[] { EnumToolTypes.HAMMER_DIGABLE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock)),
						new BaseStack(ItemMulti.createStack(this.material, MC.fragment))},
				new int[][] {{10000, 7500, 5000, 5000, 5000, 2500}, {5000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.resource)),
				new AbstractStack[] { EnumToolTypes.PICKAXE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock)),
						new BaseStack(ItemMulti.createStack(this.material, MC.fragment))},
				new int[][] {{10000, 7500, 5000, 5000, 5000, 2500}, {5000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.cobble)),
				new AbstractStack[] { EnumToolTypes.HAMMER_DIGABLE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock)),
						new BaseStack(ItemMulti.createStack(this.material, MC.fragment))},
				new int[][] {{10000, 7500, 5000, 5000, 5000, 2500}, {5000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.cobble)),
				new AbstractStack[] { EnumToolTypes.PICKAXE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock)),
						new BaseStack(ItemMulti.createStack(this.material, MC.fragment))},
				new int[][] {{10000, 7500, 5000, 5000, 5000, 2500}, {5000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.cobble_art)),
				new AbstractStack[] { EnumToolTypes.HAMMER_DIGABLE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock, 9))},
				null);
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.cobble_art)),
				new AbstractStack[] { EnumToolTypes.PICKAXE.stack() },
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.chip_rock, 9))},
				null);
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.resource)),
				new AbstractStack[] { EnumToolTypes.CHISEL_POLISH.stack() },
				new AbstractStack[] { new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.smoothed))},
				null);
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.resource)),
				new AbstractStack[] { new BaseStack(EnumBlock.fire.block), new BaseStack(EnumBlock.water.block) },
				new AbstractStack[] { new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.cobble))},
				null);
		for (EnumRockType type : EnumRockType.values())
		{
			if (type == EnumRockType.cobble || type == EnumRockType.cobble_art || type == EnumRockType.resource)
				continue;
			ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, type)),
					new AbstractStack[] { EnumToolTypes.HAMMER_DIGABLE.stack() },
					new AbstractStack[] { new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.values()[type.noSilkTouchDropMeta]))}, null);
			ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.block.getDefaultState().withProperty(TYPE, type)),
					new AbstractStack[] { EnumToolTypes.PICKAXE.stack() },
					new AbstractStack[] { new BaseStack(this.block.getDefaultState().withProperty(TYPE, EnumRockType.values()[type.noSilkTouchDropMeta]))}, null);
		}
	}
	
	@Override
	public boolean canSilkHarvest(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(B block, IBlockState state, BlockPos pos, IBlockAccess world, TileEntity tile, int fortune, boolean silkTouch)
	{
		List<ItemStack> ret = new ArrayList();
		if (silkTouch)
		{
			ret.add(new ItemStack(block, 1, state.getValue(TYPE).ordinal()));
		}
		else
		{
			Random rand = W.random(world);
			EnumRockType type = state.getValue(TYPE);
			switch (type)
			{
			case resource:
			case cobble:
				int count = rand.nextInt(4) + 3;
				if (rand.nextBoolean())
				{
					ret.add(ItemMulti.createStack(this.material, MC.fragment));
					count -= 2;
				}
				ret.add(ItemMulti.createStack(this.material, MC.chip_rock, count));
				break;
			case cobble_art:
				ret.add(ItemMulti.createStack(this.material, MC.chip_rock, 9));
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
		if (!canBlockStay(world, pos, state))
		{
			W.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public void updateTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if (!canBlockStay(world, pos, state))
		{
			W.fallBlock(world, pos, state);
		}
	}
	
	@Override
	public void randomTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		EnumRockType type = state.getValue(TYPE);
		switch (type)
		{
		case resource:
			boolean flag = W.isBlockNearby(world, pos, EnumBlock.water.block, true);
			boolean flag2 = state.getValue(HEATED);
			if (flag2)
			{
				world.setBlockState(pos, state.withProperty(TYPE, EnumRockType.cobble), 3);
				world.playSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, .5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
				for (int k = 0; k < 8; ++k)
				{
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
				}
				return;
			}
			if (ThermalNet.getTemperature(world, pos, false) > 400)
			{
				if (!flag && !flag2)
				{
					if (random.nextInt(3) == 0)
					{
						world.setBlockState(pos, state.withProperty(HEATED, true), 6);
					}
				}
			}
			else if (flag2 && random.nextInt(4) == 0)
			{
				world.setBlockState(pos, state.withProperty(HEATED, false), 6);
			}
		}
		updateTick(block, state, world, pos, random);
	}
	
	public void onBlockDestroyedByPlayer(B block, IBlockState state, World world, BlockPos pos)
	{
		if (world.isRemote) return;
		final int chance = 10;
		if (world.rand.nextInt(chance) == 0)
		{
			MutableBlockPos pos1 = new MutableBlockPos();
			for (int counter = 0; counter < 100; ++counter)
			{
				pos1.setPos(pos.getX() - 4 + world.rand.nextInt(9), pos.getY() - 2 + world.rand.nextInt(5), pos.getZ() - 4 + world.rand.nextInt(9));
				IBlockState state1 = world.getBlockState(pos1);
				if (state1.getBlock() instanceof ISmartFallableBlockRockLike &&
						((ISmartFallableBlockRockLike) state1.getBlock()).onCaveInCheck(world, pos1, state1))
				{
					return;
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if (state.getValue(HEATED))
		{
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			double u1, v1, t1;
			for (int i = 0; i < 2; ++i)
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
	
	public void onBlockExploded(B block, IBlockState state, World world, BlockPos pos, Explosion explosion)
	{
		if (state.getValue(TYPE) == EnumRockType.resource)
		{
			world.setBlockState(pos, state.withProperty(TYPE, EnumRockType.cobble));
		}
		if (W.isAirOrReplacable(world, pos.down()) && checkCollapse(world, pos, state, 200, true))
		{
			NebulaWorldHandler.schedueTask(new TaskFalling(world, pos, pos, state, 4));
		}
	}
	
	public boolean onBurn(B block, IBlockState state, IModifiableCoord coord, float burnHardness, Direction direction)
	{
		EnumRockType type = state.getValue(TYPE);
		if (type.burnable)
		{
			coord.setBlockState(state.withProperty(TYPE, type.burned()), 3);
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
			return W.isSideSolid(world, pos.down(), EnumFacing.UP, true);
		default:
			return !W.isAir(world, pos.down()) ||
					world.isSideSolid(pos.north(), EnumFacing.SOUTH, true) ||
					world.isSideSolid(pos.south(), EnumFacing.NORTH, true) ||
					world.isSideSolid(pos.east(), EnumFacing.WEST, true) ||
					world.isSideSolid(pos.west(), EnumFacing.EAST, true);
		}
	}
	
	public boolean canFallingBlockStay(B block, IBlockState state, World world, BlockPos pos)
	{
		return !EntityFallingBlockExtended.canFallAt(world, pos, state);
	}
	
	public float onFallOnEntity(B block, IBlockState state, World world, EntityFallingBlockExtended entity, Entity target)
	{
		return 9.0F + this.material.toolDamageToEntity;
	}
	
	public boolean onFallOnGround(B block, IBlockState state, World world, BlockPos pos, int height, NBTTagCompound tileNBT)
	{
		EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
		if (height < 2 ? false : height < 5 ? world.rand.nextInt(5 - height) == 0 : true)
		{
			state = state.withProperty(TYPE, EnumRockType.values()[state.getValue(TYPE).fallBreakMeta]);
		}
		state = state.withProperty(HEATED, false);
		world.setBlockState(pos, state, 3);
		return true;
	}
	
	public ActionResult<Float> onToolClick(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumToolType tool, int level, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.CHISEL_CARVE && this.harvestLevel <= level)
		{
			if (player.canPlayerEdit(pos, side.of(), stack))
			{
				EnumRockType type = state.getValue(TYPE);
				if (world.setBlockState(pos, EnumBlock.carved_rock.block.getDefaultState(), 2))
				{
					TileEntity tile = world.getTileEntity(pos);
					if (tile instanceof TECustomCarvedStone)
					{
						((TECustomCarvedStone) tile).setRock(this.material, type);
						return ((TECustomCarvedStone) tile).carveRock(player, hitX, hitY, hitZ);
					}
				}
			}
		}
		else if (tool == EnumToolTypes.CHISEL_POLISH && this.harvestLevel <= level)
		{
			if (player.canPlayerEdit(pos, side.of(), stack))
			{
				EnumRockType type = state.getValue(TYPE);
				if (type == EnumRockType.resource)
				{
					world.setBlockState(pos, state.withProperty(TYPE, EnumRockType.smoothed));
					return new ActionResult<>(EnumActionResult.SUCCESS, this.hardness / 3.0F);
				}
			}
		}
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public void scheduleFalling(B block, World world, BlockPos pos, IBlockState state, int delayMultiplier)
	{
		if (!W.isSideSolid(world, pos.down(), EnumFacing.UP, true) &&
				checkCollapse(world, pos, state, 500, true))
		{
			pos = pos.toImmutable();
			NebulaWorldHandler.schedueTask(new TaskFalling(world, pos, pos, state, 2 * delayMultiplier));
			Log.logger().info(pos);
		}
	}
	
	@Override
	public boolean onCaveInCheck(B block, World world, BlockPos pos, IBlockState state)
	{
		if (!W.isSideSolid(world, pos.down(), EnumFacing.UP, true) && checkCollapse(world, pos, state, 100, false))
		{
			W.fallBlock(world, pos, state);
			
			int range = 3 + world.rand.nextInt(16);
			MutableBlockPos pos2 = new MutableBlockPos();
			int scanX, scanY, scanZ;
			for (scanY = -4; scanY <= 1; ++scanY)
			{
				for (scanX = -range; scanX <= range; ++scanX)
				{
					for (scanZ = -range; scanZ <= range; ++scanZ)
					{
						if (world.rand.nextInt(100) < V.caveInSpreadChance)
						{
							IBlockState state1 = world.getBlockState(pos2.setPos(pos.getX() + scanX, pos.getY() + scanY, pos.getZ() + scanZ));
							if (state1.getBlock() instanceof ISmartFallableBlockRockLike)
							{
								((ISmartFallableBlockRockLike) state1.getBlock()).scheduleFalling(world, pos2, state1, scanY + 5);
							}
						}
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	protected boolean checkCollapse(IBlockAccess world, BlockPos pos, IBlockState state, int chance, boolean stableOnly)
	{
		switch (state.getValue(TYPE))
		{
		case cobble :
		case mossy :
			return true;
		case cobble_art :
			int weight = 0;
			for (Direction facing : Direction.DIRECTIONS_2D)
			{
				IBlockState state2 = world.getBlockState(facing.offset(pos));
				if (state2.getBlock() instanceof IFallingStaySupport)
				{
					weight += ((IFallingStaySupport) state2.getBlock()).getFallWeight(world, pos, state2, stableOnly);
				}
			}
			return weight < 8 * chance;
		default :
			int
			map[] = generateCollapseMap(world, pos, 5, new int[5], stableOnly);
			weight = map[0] + map[1] / 2 + map[2] / 3 + map[3] / 5 + map[4] / 8;
			return L.nextInt(weight * 3 / 4, world instanceof World ? ((World) world).rand : L.random()) + weight / 4 < 20 * chance;
		case resource:
			map = generateCollapseMap(world, pos, 4, new int[4], stableOnly);
			weight = map[0] + map[1] / 2 + map[2] / 4 + map[3] / 8;
			return L.nextInt(weight * 3 / 4, world instanceof World ? ((World) world).rand : L.random()) + weight / 4 < 20 * chance;
		}
	}
	
	private int[] generateCollapseMap(IBlockAccess world, BlockPos pos, int range, int[] cache, boolean stableOnly)
	{
		final int scale = (range << 1) | 1;
		LinkedList<byte[]> pooled = new LinkedList<>();
		boolean[][][] flags = new boolean[scale][scale][scale];
		flags[range][range][range] = true;
		pooled.addLast(new byte[] {-1, 0, 0});
		pooled.addLast(new byte[] { 1, 0, 0});
		pooled.addLast(new byte[] {0, -1, 0});
		pooled.addLast(new byte[] {0,  1, 0});
		pooled.addLast(new byte[] {0, 0, -1});
		pooled.addLast(new byte[] {0, 0,  1});
		final int X = pos.getX(), Y = pos.getY(), Z = pos.getZ();
		MutableBlockPos pos2 = new MutableBlockPos();
		int x, y, z;
		IBlockState state2;
		while (!pooled.isEmpty())
		{
			byte[] offset = pooled.poll();
			x = X + offset[0]; y = Y + offset[1]; z = Z + offset[2];
			pos2.setPos(x, y, z);
			int d = Maths.lp1Distance(offset[0], offset[1], offset[2]);
			flags[range + offset[0]][range + offset[1]][range + offset[2]] = true;
			
			if (d <= range && (state2 = world.getBlockState(pos2)).getBlock() instanceof IFallingStaySupport)
			{
				int weight = ((IFallingStaySupport) state2.getBlock()).getFallWeight(world, pos2, state2, stableOnly);
				cache[d - 1] += weight;
				
				if (weight > 0 && d < range)
				{
					if (!flags[range + offset[0] + 1][range + offset[1]][range + offset[2]])
					{
						offset[0] ++; pooled.addLast(offset);
					}
					if (!flags[range + offset[0] - 1][range + offset[1]][range + offset[2]])
					{
						pooled.addLast(new byte[] {(byte) (offset[0] - 1), offset[1], offset[2]});
					}
					if (!flags[range + offset[0]][range + offset[1] - 1][range + offset[2]])
					{
						pooled.addLast(new byte[] {offset[0], (byte) (offset[1] - 1), offset[2]});
					}
					if (!flags[range + offset[0]][range + offset[1] + 1][range + offset[2]])
					{
						pooled.addLast(new byte[] {offset[0], (byte) (offset[1] + 1), offset[2]});
					}
					if (!flags[range + offset[0]][range + offset[1]][range + offset[2] - 1])
					{
						pooled.addLast(new byte[] {offset[0], offset[1], (byte) (offset[2] - 1)});
					}
					if (!flags[range + offset[0]][range + offset[1]][range + offset[2] + 1])
					{
						pooled.addLast(new byte[] {offset[0], offset[1], (byte) (offset[2] + 1)});
					}
				}
			}
		}
		return cache;
	}
}
