package farcore.lib.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.data.EnumToolType;
import farcore.data.V;
import farcore.lib.block.instance.BlockCoreLeaves;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.tree.dna.TreeDNAHelper;
import farcore.lib.util.INamedIconRegister;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
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
	
	public void decodeDNA(TreeInfo biology, String dna)
	{
		helper.decodeDNA(biology, dna);
	}
	
	public String makeNativeDNA()
	{
		return helper.nativeDNA;
	}

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
			BlockCoreLeaves leavesCore)
	{
		this.logNat = logNatural;
		this.logArt = logArtificial;
		this.leaves = leaves;
		this.leavesCore = leavesCore;
	}

	@Override
	public boolean tickLogUpdate()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void registerLogIcon(INamedIconRegister register)
	{
		register.registerIcon("side", material.modid + ":logs/" + material.name + "_side");
		register.registerIcon("top", material.modid + ":logs/" + material.name + "_top");
	}

	@SideOnly(Side.CLIENT)
	public void registerLeavesIcon(INamedIconRegister register)
	{
		register.registerIcon("simple", material.modid + ":leaves/" + material.name +  "_opaque");
		register.registerIcon("adv", material.modid + ":leaves/" + material.name);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getLogIcon(INamedIconRegister register, int meta, int side)
	{
		return  register.getIconFromName((meta == 0 ? side == 0 || side == 1 : 
			meta == 1 ? side == 2 || side == 3 :
				meta == 2 ? side == 4 || side == 5 :
					false) ? "top" : "side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getLeavesIcon(INamedIconRegister register, int meta, int side)
	{
		return register.getIconFromName(U.Client.shouldRenderBetterLeaves() ?
				"adv" : "simple");
	}

	@Override
	public void updateLog(World world, int x, int y, int z, Random rand, boolean isArt)
	{
		
	}

	@Override
	public void updateLeaves(World world, int x, int y, int z, Random rand)
	{
		if(shouldLeavesDency(world, x, y, z))
		{
			checkDencyLeaves(world, x, y, z, leavesCheckRange);
		}
	}
	
	protected boolean canLeaveGrowNearby(World world, int x, int y, int z)
	{
		return U.Worlds.isBlockNearby(world, x, y, z, logNat, -1, false);
	}
	
	protected boolean shouldLeavesDency(World world, int x, int y, int z)
	{
		return (world.getBlockMetadata(x, y, z) & 0x8) != 0;
	}

	public void beginLeavesDency(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 0x8, 2);
	}

	public void stopLeavesDency(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & 0x7, 2);
	}
	
	protected void checkDencyLeaves(World world, int x, int y, int z, int maxL)
	{
		int range = 2 * maxL + 1;
		int[][][] checkBuffer = new int[range][range][range];
		for(int i = -maxL; i <= maxL; ++i)
			for(int j = -maxL; j <= maxL; ++j)
				for(int k = -maxL; k <= maxL; ++k)
				{
					if(!isLeaves(world, x + i, y + j, z + k))
					{
						checkBuffer[i + maxL][j + maxL][k + maxL] = -1;
						continue;
					}
					else if(checkBuffer[i + maxL][j + maxL][k + maxL] > 0)
					{
						continue;
					}
					checkLeaves(maxL, world, x, y, z, i, j, k, checkBuffer);
				}
		dencyLeaves(maxL, world, x, y, z, checkBuffer);
	}
	
	protected boolean isLeaves(World world, int x, int y, int z)
	{
		return isLeaves(world.getBlock(x, y, z));
	}
	
	protected boolean isLeaves(Block block)
	{
		return block == leaves || block == leavesCore;
	}
	
	private void checkLeaves(int length, World world, int x, int y, int z, int ofX, int ofY, int ofZ, int[][][] flags)
	{
		if(flags[length + ofX][length + ofY][length + ofZ] != 0)
		{
			return;
		}
		int v = 0;
		if(canLeaveGrowNearby(world, x + ofX, y + ofY, z + ofZ))
		{
			v = length;
		}
		else
		{
			int v1;
			if(ofX + length >= 1 && (v1 = flags[ofX + length - 1][ofY + length    ][ofZ + length    ]) > v + 1){v = v1 - 1;}
			if(ofX < length      && (v1 = flags[ofX + length + 1][ofY + length    ][ofZ + length    ]) > v + 1){v = v1 - 1;}
			if(ofY + length >= 1 && (v1 = flags[ofX + length    ][ofY + length - 1][ofZ + length    ]) > v + 1){v = v1 - 1;}
			if(ofY < length      && (v1 = flags[ofX + length    ][ofY + length + 1][ofZ + length    ]) > v + 1){v = v1 - 1;}
			if(ofZ + length >= 1 && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length - 1]) > v + 1){v = v1 - 1;}
			if(ofZ < length      && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length + 1]) > v + 1){v = v1 - 1;}
		}
		if((flags[ofX + length][ofY + length][ofZ + length] = v) > 1)
		{
			if(ofX + length >= 1)
				checkLeaves(length, world, x, y, z, ofX - 1, ofY, ofZ, flags);
			if(ofX < length)
				checkLeaves(length, world, x, y, z, ofX + 1, ofY, ofZ, flags);
			if(ofY + length >= 1)
				checkLeaves(length, world, x, y, z, ofX, ofY - 1, ofZ, flags);
			if(ofY < length)
				checkLeaves(length, world, x, y, z, ofX, ofY + 1, ofZ, flags);
			if(ofZ + length >= 1)
				checkLeaves(length, world, x, y, z, ofX, ofY, ofZ - 1, flags);
			if(ofZ < length)
				checkLeaves(length, world, x, y, z, ofX, ofY, ofZ + 1, flags);
		}
	}

	protected void beginLeavesDency(int length, World world, int x, int y, int z)
	{
		for(int i = -length; i <= length; ++i)
			for(int j = -length; j <= length; ++j)
				for(int k = -length; k <= length; ++k)
				{
					world.getBlock(x + i, y + j, z + k).beginLeavesDecay(world, x + i, y + j, z + k);
				}
	}
	
	private void dencyLeaves(int length, World world, int x, int y, int z, int[][][] flags)
	{
		for(int i = -length; i <= length; ++i)
			for(int j = -length; j <= length; ++j)
				for(int k = -length; k <= length; ++k)
				{
					int v = flags[i + length][j + length][k + length];
					if(v > 0)
					{
						stopLeavesDency(world, x, y, z);
					}
					else if(v == 0)
					{
						onLeavesDead(world, x + i, y + j, z + k);
					}
				}
	}
	
	protected void onLeavesDead(World world, int x, int y, int z)
	{
		U.Worlds.spawnDropsInWorld(world, x, y, z, getLeavesDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0, false, new ArrayList()));
		world.setBlockToAir(x, y, z);
	}

	@Override
	public void breakLog(World world, int x, int y, int z, int meta, boolean isArt)
	{
		
	}

	@Override
	public void breakLeaves(World world, int x, int y, int z, int meta)
	{
		beginLeavesDency(leavesCheckRange, world, x, y, z);
	}

	@Override
	public boolean onLogRightClick(EntityPlayer player, World world, int x, int y, int z, int side, float xPos,
			float yPos, float zPos, boolean isArt)
	{
		return false;
	}

	@Override
	public float onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y,
			int z, int side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return 0;
	}

	@Override
	public float onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y,
			int z, int side, float hitX, float hitY, float hitZ)
	{
		return 0;
	}

	@Override
	public float onToolUseLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return 0;
	}

	@Override
	public float onToolUseLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return 0;
	}

	@Override
	public List<ItemStack> getLogOtherDrop(World world, int x, int y, int z, ArrayList list)
	{
		return list;
	}

	@Override
	public ArrayList<ItemStack> getLeavesDrops(World world, int x, int y, int z, int metadata, int fortune,
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
	
	protected void generateTreeLeaves(World world, int x, int y, int z, int meta, float generateCoreLeavesChance, TreeInfo info)
	{
		meta &= 0x7;
		int state = V.generateState ? 2 : 3;
		if(world.rand.nextDouble() <= generateCoreLeavesChance)
		{
			world.setBlock(x, y, z, leavesCore, meta, state);
			U.Worlds.setTileEntity(world, x, y, z, new TECoreLeaves(this, info), !V.generateState);
		}
		else
		{
			world.setBlock(x, y, z, leaves, meta, state);
		}
	}
}