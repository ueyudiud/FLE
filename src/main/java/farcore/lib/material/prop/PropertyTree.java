package farcore.lib.material.prop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IFamily;
import farcore.lib.block.wood.BlockLeaves;
import farcore.lib.block.wood.BlockLeavesCore;
import farcore.lib.block.wood.BlockLogArtificial;
import farcore.lib.block.wood.BlockLogNatural;
import farcore.lib.block.wood.BlockPlank;
import farcore.lib.material.Mat;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.ITree;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.IRegisteredNameable;
import nebula.common.world.chunk.IBlockStateRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class PropertyTree extends PropertyWood implements ITree, IRegisteredNameable
{
	private Mat material;
	public BlockLogNatural logNatural;
	public BlockLogArtificial logArtificial;
	public BlockLeaves leaves;
	public BlockLeavesCore leavesCore;
	public BlockPlank plank;
	
	public PropertyTree(Mat material, int harvestLevel, float hardness, float explosionResistance, float ashcontent,
			float burnHeat)
	{
		super(material, harvestLevel, hardness, explosionResistance, ashcontent, burnHeat);
	}
	
	public final void setMaterial(Mat material)
	{
		if(this.material != null)
			throw new RuntimeException("The tree property " + toString() + " has already has a kind of material ('" + this.material.name + "') belong.");
		this.material = material;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.material.getRegisteredName();
	}
	
	public final Mat material()
	{
		return this.material;
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
	
	public static class PropertyTreeWrapper extends Tree
	{
		private ITree tree;
		
		public PropertyTreeWrapper(PropertyWood property, ITree tree)
		{
			super(property.material, property.harvestLevel, property.hardness, property.explosionResistance, property.ashcontent, property.burnHeat);
			this.tree = tree;
		}
		
		@Override
		public void expressTrait(ISaplingAccess biology, GeneticMaterial gm)
		{
			this.tree.expressTrait(biology, gm);
		}
		
		@Override
		public IFamily<ISaplingAccess> getFamily()
		{
			return this.tree.getFamily();
		}
		
		@Override
		public GeneticMaterial createGameteGeneticMaterial(ISaplingAccess biology, GeneticMaterial gm)
		{
			return this.tree.createGameteGeneticMaterial(biology, gm);
		}
		
		@Override
		public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves,
				BlockLeavesCore leavesCore)
		{
			super.initInfo(logNatural, logArtificial, leaves, leavesCore);
			this.tree.initInfo(logNatural, logArtificial, leaves, leavesCore);
		}
		
		@Override
		public boolean tickLogUpdate()
		{
			return this.tree.tickLogUpdate();
		}
		
		@Override
		public void updateLog(World world, BlockPos pos, Random rand, boolean isArt)
		{
			this.tree.updateLog(world, pos, rand, isArt);
		}
		
		@Override
		public void updateLeaves(World world, BlockPos pos, Random rand, boolean check)
		{
			this.tree.updateLeaves(world, pos, rand, check);
		}
		
		@Override
		public void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt)
		{
			this.tree.breakLog(world, pos, state, isArt);
		}
		
		@Override
		public void breakLeaves(World world, BlockPos pos, IBlockState state)
		{
			this.tree.breakLeaves(world, pos, state);
		}
		
		@Override
		public void beginLeavesDecay(World world, BlockPos pos)
		{
			this.tree.beginLeavesDecay(world, pos);
		}
		
		@Override
		public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos,
				float yPos, float zPos, boolean isArt)
		{
			return this.tree.onLogRightClick(player, world, pos, side, xPos, yPos, zPos, isArt);
		}
		
		@Override
		public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
				BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
		{
			return this.tree.onToolClickLog(player, tool, stack, world, pos, side, hitX, hitY, hitZ, isArt);
		}
		
		@Override
		public ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack,
				World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
		{
			return this.tree.onToolClickLeaves(player, tool, stack, world, pos, side, hitX, hitY, hitZ);
		}
		
		@Override
		public List<ItemStack> getLogOtherDrop(World world, BlockPos pos, ArrayList list)
		{
			return this.tree.getLogOtherDrop(world, pos, list);
		}
		
		@Override
		public ArrayList<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune,
				boolean silkTouching, ArrayList list)
		{
			return this.tree.getLeavesDrops(world, pos, state, fortune, silkTouching, list);
		}
		
		@Override
		public int onSaplingUpdate(ISaplingAccess access)
		{
			return this.tree.onSaplingUpdate(access);
		}
		
		@Override
		public int getGrowAge(ISaplingAccess access)
		{
			return this.tree.getGrowAge(access);
		}
		
		@Override
		public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
		{
			return this.tree.generateTreeAt(world, x, y, z, random, info);
		}
		
		@Override
		public GeneticMaterial createNativeGeneticMaterial()
		{
			return this.tree.createNativeGeneticMaterial();
		}
		
		@Override
		public <T extends Block> T getBlock(BlockType type)
		{
			return this.tree.getBlock(type);
		}
		
		@Override
		public boolean generateTreeAt(World world, BlockPos pos, Random random, TreeInfo info)
		{
			return this.tree.generateTreeAt(world, pos, random, info);
		}
		
		@Override
		public int getLeavesMeta(IBlockState state)
		{
			return this.tree.getLeavesMeta(state);
		}
		
		@Override
		public IBlockState getLeavesState(Block block, int meta)
		{
			return this.tree.getLeavesState(block, meta);
		}
		
		@Override
		public int getLogMeta(IBlockState state, boolean isArt)
		{
			return this.tree.getLogMeta(state, isArt);
		}
		
		@Override
		public IBlockState getLogState(Block block, int meta, boolean isArt)
		{
			return this.tree.getLogState(block, meta, isArt);
		}
		
		@Override
		public BlockStateContainer createLeavesStateContainer(Block block)
		{
			return this.tree.createLeavesStateContainer(block);
		}
		
		@Override
		public BlockStateContainer createLogStateContainer(Block block, boolean isArt)
		{
			return this.tree.createLogStateContainer(block, isArt);
		}
		
		@Override
		public void registerLogExtData(Block block, boolean isArt, IBlockStateRegister register)
		{
			this.tree.registerLogExtData(block, isArt, register);
		}
	}
}