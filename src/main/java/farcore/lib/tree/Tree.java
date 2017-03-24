/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.tree;

import static net.minecraft.block.BlockLeaves.CHECK_DECAY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumItem;
import farcore.data.V;
import farcore.lib.bio.FamilyTemplate;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import fle.loader.BlocksItems;
import nebula.common.base.Appliable;
import nebula.common.data.Misc;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.A;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public abstract class Tree implements ITree
{
	public static final Appliable.AppliableCached<ItemStack> LEAVES_APPLIER1 = Appliable.wrapCached(()-> BlocksItems.crop.getSubItem("broadleaf"));
	public static final Appliable.AppliableCached<ItemStack> LEAVES_APPLIER2 = Appliable.wrapCached(()-> BlocksItems.crop.getSubItem("coniferous"));
	
	protected Mat material;
	protected FamilyTemplate<Tree, ISaplingAccess> family;
	//logNative logArtifical leaves leavesCore
	public Block[] blocks;
	protected long[] nativeTreeValue = Misc.LONGS_EMPTY;
	protected int[] nativeTreeDatas = Misc.INTS_EMPTY;
	protected int leavesCheckRange = 4;
	protected boolean isBroadLeaf = true;
	
	public Tree setMaterial(Mat material)
	{
		this.material = material;
		return this;
	}
	
	public Tree setDefFamily()
	{
		this.family = new FamilyTemplate<>(this);
		return this;
	}
	
	public Tree setFamily(FamilyTemplate<Tree, ISaplingAccess> family)
	{
		this.family = family;
		family.addSpecies(this);
		return this;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.material.name;
	}
	
	@Override
	public FamilyTemplate<Tree, ISaplingAccess> getFamily()
	{
		return this.family;
	}
	
	@Override
	public GeneticMaterial createNativeGeneticMaterial()
	{
		return new GeneticMaterial(this.family.getRegisteredName(), 0, this.nativeTreeValue.clone(), this.nativeTreeDatas.clone());
	}
	
	@Override
	public GeneticMaterial createGameteGeneticMaterial(ISaplingAccess biology, GeneticMaterial gm)
	{
		if ((gm.coders.length & 0x1) != 0) return null;
		Random random = biology.rng();
		long[] coders = A.createLongArray(gm.coders.length >> 1, idx-> {
			long a = gm.coders[idx << 1];
			long b = gm.coders[idx << 1 | 1];
			long result = 0;
			for (int i = 0; i < Long.SIZE; ++i)
			{
				long x = 1L << i;
				if (getGameteResult(idx << 6 | i, random, (a & x) != 0, (b & x) != 0))
				{
					result |= x;
				}
			}
			return result;
		});
		int[] datas = gm.nativeValues.clone();
		return new GeneticMaterial(this.family.getRegisteredName(), gm.generation + 1, coders, datas);
	}
	
	protected boolean getGameteResult(int idx, Random random, boolean a, boolean b)
	{
		return random.nextBoolean() ? a : b;
	}
	
	@Override
	public void expressTrait(ISaplingAccess biology, GeneticMaterial gm)
	{
		TreeInfo info = biology.info();
		info.height		+= gm.nativeValues[0];
		info.growth		+= gm.nativeValues[1];
		info.resistance	+= gm.nativeValues[2];
		info.vitality	+= gm.nativeValues[3];
	}
	
	@Override
	public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial,
			BlockLeaves leaves, BlockLeavesCore leavesCore)
	{
		this.blocks = new Block[]{logNatural, logArtificial, leaves, leavesCore};
	}
	
	@Override
	public boolean tickLogUpdate()
	{
		return false;
	}
	
	@Override
	public void updateLog(World world, BlockPos pos, Random rand, boolean isArt)
	{
		
	}
	
	@Override
	public void updateLeaves(World world, BlockPos pos, Random rand, boolean checkDency)
	{
		if (checkDency)
		{
			if (world.isAreaLoaded(pos, this.leavesCheckRange) || !V.generateState)
			{
				if (!canLeaveGrowNearby(world, pos))
				{
					beginLeavesDency(world, pos);
				}
				if (shouldLeavesDency(world, pos))
				{
					checkDencyLeaves(world, pos, this.leavesCheckRange);
				}
			}
		}
	}
	
	protected boolean canLeaveGrowNearby(World world, BlockPos pos)
	{
		return Worlds.isBlockNearby(world, pos, this.blocks[0], false);
	}
	
	protected boolean shouldLeavesDency(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(CHECK_DECAY);
	}
	
	protected void checkDencyLeaves(World world, BlockPos pos, final int maxL)
	{
		final int range = 2 * maxL + 1;
		int[][][] checkBuffer = new int[range][range][range];
		for(int i = -maxL; i <= maxL; ++i)
		{
			for(int j = -maxL; j <= maxL; ++j)
			{
				for(int k = -maxL; k <= maxL; ++k)
				{
					checkLeaves(maxL, maxL, world, pos, i, j, k, checkBuffer);
				}
			}
		}
		dencyLeaves(maxL, world, pos, checkBuffer);
	}
	
	protected boolean isLeaves(World world, BlockPos pos)
	{
		return isLeaves(world.getBlockState(pos).getBlock());
	}
	
	protected boolean isLeaves(Block block)
	{
		return block == this.blocks[2] || block == this.blocks[3];
	}
	
	private void checkLeaves(int depth, int length, World world, BlockPos pos, int ofX, int ofY, int ofZ, int[][][] flags)
	{
		if (flags[length + ofX][length + ofY][length + ofZ] != 0)
			return;
		if (!isLeaves(world, pos.add(ofX, ofY, ofZ)))
		{
			flags[length + ofX][length + ofY][length + ofZ] = -1;
			return;
		}
		int v = 0;
		if (canLeaveGrowNearby(world, pos.add(ofX, ofY, ofZ)))
		{
			v = depth;
		}
		else
		{
			int v1;
			if (ofX + length >= 1 && (v1 = flags[ofX + length - 1][ofY + length    ][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if (ofX < length      && (v1 = flags[ofX + length + 1][ofY + length    ][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if (ofY + length >= 1 && (v1 = flags[ofX + length    ][ofY + length - 1][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if (ofY < length      && (v1 = flags[ofX + length    ][ofY + length + 1][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if (ofZ + length >= 1 && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length - 1]) > v + 1)
			{
				v = v1 - 1;
			}
			if (ofZ < length      && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length + 1]) > v + 1)
			{
				v = v1 - 1;
			}
		}
		if ((flags[ofX + length][ofY + length][ofZ + length] = v) > 1)
		{
			if (ofX + length >= 1)
			{
				checkLeaves(depth, length, world, pos, ofX - 1, ofY, ofZ, flags);
			}
			if (ofX < length)
			{
				checkLeaves(depth, length, world, pos, ofX + 1, ofY, ofZ, flags);
			}
			if (ofY + length >= 1)
			{
				checkLeaves(depth, length, world, pos, ofX, ofY - 1, ofZ, flags);
			}
			if (ofY < length)
			{
				checkLeaves(depth, length, world, pos, ofX, ofY + 1, ofZ, flags);
			}
			if (ofZ + length >= 1)
			{
				checkLeaves(depth, length, world, pos, ofX, ofY, ofZ - 1, flags);
			}
			if (ofZ < length)
			{
				checkLeaves(depth, length, world, pos, ofX, ofY, ofZ + 1, flags);
			}
		}
	}
	
	protected void beginLeavesDency(int length, World world, BlockPos pos)
	{
		BlockPos pos2;
		IBlockState state;
		for(int i = -length; i <= length; ++i)
		{
			for(int j = -length; j <= length; ++j)
			{
				for(int k = -length; k <= length; ++k)
				{
					(state = world.getBlockState(pos2 = pos.add(i, j, k))).getBlock().beginLeavesDecay(state, world, pos2);
				}
			}
		}
	}
	
	private void dencyLeaves(int length, World world, BlockPos pos, int[][][] flags)
	{
		for(int i = -length; i <= length; ++i)
		{
			for(int j = -length; j <= length; ++j)
			{
				for(int k = -length; k <= length; ++k)
				{
					int v = flags[i + length][j + length][k + length];
					if (i == 0 && j == 0 && k == 0)
					{
						if (v == 0)
						{
							onLeavesDead(world, pos);
							continue;
						}
						else
						{
							stopLeavesDency(world, pos);
						}
					}
					if(v > 0)
					{
						stopLeavesDency(world, pos.add(i, j, k));
					}
					else if(v == 0)
					{
						beginLeavesDency(world, pos.add(i, j, k));
					}
				}
			}
		}
	}
	
	protected void onLeavesDead(World world, BlockPos pos)
	{
		Worlds.spawnDropsInWorld(world, pos, getLeavesDrops(world, pos, world.getBlockState(pos), 0, false, new ArrayList()));
		world.setBlockToAir(pos);
	}
	
	@Override
	public void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt)
	{
		
	}
	
	@Override
	public void breakLeaves(World world, BlockPos pos, IBlockState state)
	{
		beginLeavesDency(this.leavesCheckRange, world, pos);
	}
	
	@Override
	public void beginLeavesDency(World world, BlockPos pos)
	{
		Worlds.switchProp(world, pos, CHECK_DECAY, true, 2);
	}
	
	public void stopLeavesDency(World world, BlockPos pos)
	{
		Worlds.switchProp(world, pos, CHECK_DECAY, false, 2);
	}
	
	@Override
	public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos,
			float yPos, float zPos, boolean isArt)
	{
		return false;
	}
	
	@Override
	public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public List<ItemStack> getLogOtherDrop(World world, BlockPos pos, ArrayList list)
	{
		return list;
	}
	
	@Override
	public ArrayList<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune,
			boolean silkTouching, ArrayList list)
	{
		if (L.nextInt(8) == 0)
		{
			list.add(new ItemStack(EnumItem.branch.item, 1, this.material.id));
		}
		if (L.nextInt(7) == 0)
		{
			list.add((this.isBroadLeaf ? LEAVES_APPLIER1 : LEAVES_APPLIER2).apply().copy());
		}
		return list;
	}
	
	@Override
	public int onSaplingUpdate(ISaplingAccess access)
	{
		return 1;
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 80;
	}
	
	protected void generateTreeLeaves(World world, BlockPos pos, int meta, float generateCoreLeavesChance, TreeInfo info)
	{
		meta &= 0x7;
		int state = V.generateState ? 2 : 3;
		if(world.rand.nextFloat() <= generateCoreLeavesChance)
		{
			Worlds.setBlock(world, pos, this.blocks[3], meta, state);
			Worlds.setTileEntity(world, pos, new TECoreLeaves(this, info), !V.generateState);
		}
		else
		{
			Worlds.setBlock(world, pos, this.blocks[2], meta, state);
		}
	}
	
	@Override
	public <T extends Block> T getBlock(BlockType type)
	{
		switch (type)
		{
		case LOG :
			return (T) this.blocks[0];
		case LOG_ART :
			return (T) this.blocks[1];
		case LEAVES :
			return (T) this.blocks[2];
		case LEAVES_CORE :
			return (T) this.blocks[3];
		default:
			return null;
		}
	}
}