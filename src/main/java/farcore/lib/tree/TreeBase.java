package farcore.lib.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumToolType;
import farcore.data.V;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TreeBase implements ITree
{
	public Block logNat;
	public Block logArt;
	public Block leaves;
	public Block leavesCore;
	protected Mat material;
	protected TreeDNAHelper helper;
	protected int leavesCheckRange = 4;
	
	public TreeBase(Mat material)
	{
		this.material = material;
	}
	
	@Override
	public String getRegisteredName()
	{
		return material.name;
	}
	
	@Override
	public void decodeDNA(TreeInfo biology, String dna)
	{
		helper.decodeDNA(biology, dna);
	}
	
	@Override
	public String makeNativeDNA()
	{
		return helper.nativeDNA;
	}
	
	@Override
	public String makeChildDNA(int generation, String par)
	{
		return helper.borderDNA(par, harmonic(generation, 2.5E-2, 1.0));
	}
	
	protected float harmonic(int x, double chance, double mul)
	{
		if(x <= 0) return 0F;
		x += 1;
		return 1F / (float) (1D / (Math.log(x) * mul) + 1D / chance);
	}
	
	@Override
	public String makeOffspringDNA(String par1, String par2)
	{
		return helper.mixedDNA(par1, par2);
	}
	
	@Override
	public Mat material()
	{
		return material;
	}
	
	@Override
	public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves,
			BlockLeavesCore leavesCore)
	{
		logNat = logNatural;
		logArt = logArtificial;
		this.leaves = leaves;
		this.leavesCore = leavesCore;
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
	public void updateLeaves(World world, BlockPos pos, Random rand)
	{
		if(!canLeaveGrowNearby(world, pos))
		{
			beginLeavesDency(world, pos);
		}
		if(shouldLeavesDency(world, pos))
		{
			checkDencyLeaves(world, pos, leavesCheckRange);
		}
	}
	
	protected boolean canLeaveGrowNearby(World world, BlockPos pos)
	{
		return U.Worlds.isBlockNearby(world, pos, logNat, false);
	}
	
	protected boolean shouldLeavesDency(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(net.minecraft.block.BlockLeaves.CHECK_DECAY);
	}
	
	@Override
	public void beginLeavesDency(World world, BlockPos pos)
	{
		U.Worlds.switchProp(world, pos, net.minecraft.block.BlockLeaves.CHECK_DECAY, true, 2);
	}
	
	public void stopLeavesDency(World world, BlockPos pos)
	{
		U.Worlds.switchProp(world, pos, net.minecraft.block.BlockLeaves.CHECK_DECAY, false, 2);
	}
	
	protected void checkDencyLeaves(World world, BlockPos pos, int maxL)
	{
		int range = 2 * maxL + 1;
		int[][][] checkBuffer = new int[range][range][range];
		for(int i = -maxL; i <= maxL; ++i)
		{
			for(int j = -maxL; j <= maxL; ++j)
			{
				for(int k = -maxL; k <= maxL; ++k)
				{
					if(!isLeaves(world, pos.add(i, j, k)))
					{
						checkBuffer[i + maxL][j + maxL][k + maxL] = -1;
						continue;
					}
					else if(checkBuffer[i + maxL][j + maxL][k + maxL] > 0)
					{
						continue;
					}
					checkLeaves(maxL, world, pos, i, j, k, checkBuffer);
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
		return block == leaves || block == leavesCore;
	}
	
	private void checkLeaves(int length, World world, BlockPos pos, int ofX, int ofY, int ofZ, int[][][] flags)
	{
		if(flags[length + ofX][length + ofY][length + ofZ] != 0)
			return;
		int v = 0;
		if(canLeaveGrowNearby(world, pos.add(ofX, ofY, ofZ)))
		{
			v = length;
		}
		else
		{
			int v1;
			if(ofX + length >= 1 && (v1 = flags[ofX + length - 1][ofY + length    ][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if(ofX < length      && (v1 = flags[ofX + length + 1][ofY + length    ][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if(ofY + length >= 1 && (v1 = flags[ofX + length    ][ofY + length - 1][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if(ofY < length      && (v1 = flags[ofX + length    ][ofY + length + 1][ofZ + length    ]) > v + 1)
			{
				v = v1 - 1;
			}
			if(ofZ + length >= 1 && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length - 1]) > v + 1)
			{
				v = v1 - 1;
			}
			if(ofZ < length      && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length + 1]) > v + 1)
			{
				v = v1 - 1;
			}
		}
		if((flags[ofX + length][ofY + length][ofZ + length] = v) > 1)
		{
			if(ofX + length >= 1)
			{
				checkLeaves(length, world, pos, ofX - 1, ofY, ofZ, flags);
			}
			if(ofX < length)
			{
				checkLeaves(length, world, pos, ofX + 1, ofY, ofZ, flags);
			}
			if(ofY + length >= 1)
			{
				checkLeaves(length, world, pos, ofX, ofY - 1, ofZ, flags);
			}
			if(ofY < length)
			{
				checkLeaves(length, world, pos, ofX, ofY + 1, ofZ, flags);
			}
			if(ofZ + length >= 1)
			{
				checkLeaves(length, world, pos, ofX, ofY, ofZ - 1, flags);
			}
			if(ofZ < length)
			{
				checkLeaves(length, world, pos, ofX, ofY, ofZ + 1, flags);
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
					if(v > 0)
					{
						stopLeavesDency(world, pos.add(i, j, k));
					}
					else if(v == 0)
					{
						onLeavesDead(world, pos.add(i, j, k));
					}
				}
			}
		}
	}
	
	protected void onLeavesDead(World world, BlockPos pos)
	{
		U.Worlds.spawnDropsInWorld(world, pos, getLeavesDrops(world, pos, world.getBlockState(pos), 0, false, new ArrayList()));
		world.setBlockToAir(pos);
	}
	
	@Override
	public void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt)
	{
		
	}
	
	@Override
	public void breakLeaves(World world, BlockPos pos, IBlockState state)
	{
		beginLeavesDency(leavesCheckRange, world, pos);
	}
	
	@Override
	public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos,
			float yPos, float zPos, boolean isArt)
	{
		return false;
	}
	
	@Override
	public float onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return 0;
	}
	
	@Override
	public float onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction direction, float hitX, float hitY, float hitZ)
	{
		return 0;
	}
	
	@Override
	public float onToolUseLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, BlockPos pos,
			Direction direction, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return 0;
	}
	
	@Override
	public float onToolUseLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction direction, float hitX, float hitY, float hitZ)
	{
		return 0;
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
		return list;
	}
	
	@Override
	public abstract boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info);
	
	@Override
	public int onSaplingUpdate(ISaplingAccess access)
	{
		return 1;
	}
	
	@Override
	public int getGrowAge()
	{
		return 80;
	}
	
	protected void generateTreeLeaves(World world, BlockPos pos, int meta, float generateCoreLeavesChance, TreeInfo info)
	{
		meta &= 0x7;
		int state = V.generateState ? 2 : 3;
		if(world.rand.nextDouble() <= generateCoreLeavesChance)
		{
			U.Worlds.setBlock(world, pos, leavesCore, meta, state);
			U.Worlds.setTileEntity(world, pos, new TECoreLeaves(this, info), !V.generateState);
		}
		else
		{
			U.Worlds.setBlock(world, pos, leaves, meta, state);
		}
	}
}