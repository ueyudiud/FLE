package farcore.lib.material.prop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumToolType;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import farcore.lib.util.Direction;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class PropertyTree extends PropertyWood implements ITree, IRegisteredNameable
{
	public static final PropertyTree VOID = new PropertyTree.PropertyTreeWrapper(ITree.VOID);
	
	private Mat material;
	public BlockLogNatural logNatural;
	public BlockLogArtificial logArtificial;
	public BlockLeaves leaves;
	public BlockLeavesCore leavesCore;
	
	public final void setMaterial(Mat material)
	{
		if(this.material != null)
			throw new RuntimeException("The tree property " + toString() + " has already has a kind of material ('" + this.material.name + "') belong.");
		this.material = material;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return material.getRegisteredName();
	}
	
	public final Mat material()
	{
		return material;
	}
	
	@Override
	public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves,
			BlockLeavesCore leavesCore)
	{
		this.logArtificial = logArtificial;
		this.logNatural = logNatural;
		this.leaves = leaves;
		this.leavesCore = leavesCore;
	}

	public static class PropertyTreeWrapper extends PropertyTree
	{
		private ITree tree;

		public PropertyTreeWrapper(ITree tree)
		{
			this.tree = tree;
		}
		
		@Override
		public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves,
				BlockLeavesCore leavesCore)
		{
			super.initInfo(logNatural, logArtificial, leaves, leavesCore);
			tree.initInfo(logNatural, logArtificial, leaves, leavesCore);
		}

		@Override
		public boolean tickLogUpdate()
		{
			return tree.tickLogUpdate();
		}

		@Override
		public void updateLog(World world, BlockPos pos, Random rand, boolean isArt)
		{
			tree.updateLog(world, pos, rand, isArt);
		}

		@Override
		public void updateLeaves(World world, BlockPos pos, Random rand)
		{
			tree.updateLeaves(world, pos, rand);
		}

		@Override
		public void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt)
		{
			tree.breakLog(world, pos, state, isArt);
		}

		@Override
		public void breakLeaves(World world, BlockPos pos, IBlockState state)
		{
			tree.breakLeaves(world, pos, state);
		}

		@Override
		public void beginLeavesDency(World world, BlockPos pos)
		{
			tree.beginLeavesDency(world, pos);
		}

		@Override
		public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos,
				float yPos, float zPos, boolean isArt)
		{
			return tree.onLogRightClick(player, world, pos, side, xPos, yPos, zPos, isArt);
		}

		@Override
		public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
				BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
		{
			return tree.onToolClickLog(player, tool, stack, world, pos, side, hitX, hitY, hitZ, isArt);
		}

		@Override
		public ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack,
				World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
		{
			return tree.onToolClickLeaves(player, tool, stack, world, pos, side, hitX, hitY, hitZ);
		}

		@Override
		public ActionResult<Float> onToolUseLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
				long useTick, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
		{
			return tree.onToolUseLog(player, tool, stack, world, useTick, pos, side, hitX, hitY, hitZ, isArt);
		}

		@Override
		public ActionResult<Float> onToolUseLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
				long useTick, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
		{
			return tree.onToolUseLeaves(player, tool, stack, world, useTick, pos, side, hitX, hitY, hitZ);
		}

		@Override
		public List<ItemStack> getLogOtherDrop(World world, BlockPos pos, ArrayList list)
		{
			return tree.getLogOtherDrop(world, pos, list);
		}

		@Override
		public ArrayList<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune,
				boolean silkTouching, ArrayList list)
		{
			return tree.getLeavesDrops(world, pos, state, fortune, silkTouching, list);
		}

		@Override
		public int onSaplingUpdate(ISaplingAccess access)
		{
			return tree.onSaplingUpdate(access);
		}

		@Override
		public int getGrowAge(ISaplingAccess access)
		{
			return tree.getGrowAge(access);
		}

		@Override
		public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
		{
			return tree.generateTreeAt(world, x, y, z, random, info);
		}

		@Override
		public void decodeDNA(TreeInfo biology, String dna)
		{
			tree.decodeDNA(biology, dna);
		}

		@Override
		public String makeNativeDNA()
		{
			return tree.makeNativeDNA();
		}

		@Override
		public String makeChildDNA(int generation, String par)
		{
			return tree.makeChildDNA(generation, par);
		}

		@Override
		public String makeOffspringDNA(String par1, String par2)
		{
			return tree.makeOffspringDNA(par1, par2);
		}
	}
}