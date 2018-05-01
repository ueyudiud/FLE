/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks.flora;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.CT;
import farcore.lib.tree.Tree;
import nebula.base.collection.A;
import nebula.client.util.Client;
import nebula.common.LanguageManager;
import nebula.common.block.BlockBase;
import nebula.common.block.IToolableBlock;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.OreDict;
import nebula.common.util.W;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLeaves extends BlockBase implements IShearable, IToolableBlock
{
	public static BlockLeaves create(Tree $tree)
	{
		return new BlockLeaves($tree)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return $tree.createLeavesStateContainer(this);
			}
			
			@Override
			public int getMetaFromState(IBlockState state)
			{
				return $tree.getLeavesMeta(state);
			}
			
			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return $tree.getLeavesState(this, meta);
			}
			
			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return $tree.initLeavesState(super.initDefaultState(state));
			}
		};
	}
	
	public Tree tree;
	
	BlockLeaves(Tree tree)
	{
		this("leaves." + tree.material.name, tree, tree.material.localName + " Leaves");
	}
	
	protected BlockLeaves(String name, Tree tree, String localName)
	{
		super(name, Material.LEAVES);
		this.tree = tree;
		setTickRandomly(true);
		setHardness(0.5F);
		setResistance(0.02F);
		setCreativeTab(CT.TREE);
		setLightOpacity(1);
		setSoundType(SoundType.PLANT);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("leaves", this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		// tree.registerRender();
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube(IBlockState state)
	{
		return !Client.shouldRenderBetterLeaves();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return Client.shouldRenderBetterLeaves() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}
	
	@Override
	public boolean causesSuffocation()
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if (!worldIn.isRemote)
		{
			worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		if (world instanceof World && !((World) world).isRemote)
		{
			((World) world).scheduleUpdate(pos, this, tickRate((World) world));
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		this.tree.updateLeaves(worldIn, pos, random, false);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.tree.updateLeaves(worldIn, pos, rand, true);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		this.tree.breakLeaves(worldIn, pos, state);
	}
	
	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
	{
		this.tree.beginLeavesDecay(world, pos);
		world.scheduleUpdate(pos, this, tickRate(world));
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 9;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		entityIn.motionX *= 0.8;
		entityIn.motionZ *= 0.8;
		if (entityIn instanceof EntityPlayer)
		{
			((EntityPlayer) entityIn).setSprinting(false);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (W.isCatchingRain(worldIn, pos) && !worldIn.isSideSolid(pos.down(), EnumFacing.UP) && rand.nextInt(15) == 1)
		{
			double d0 = pos.getX() + rand.nextFloat();
			double d1 = pos.getY() - 0.05D;
			double d2 = pos.getZ() + rand.nextFloat();
			worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		try
		{
			return this.tree.onLeavesRightClick(playerIn, worldIn, pos, state, Direction.of(side), hitX, hitY, hitZ, null);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return this.tree.getLeavesDrops(world, pos, state, fortune, silkTouch, A.nonnull());
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		List<ItemStack> stacks = new ArrayList<>();
		stacks.add(getSilkTouchDrop(world.getBlockState(pos)));
		return stacks;
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return this.tree.onToolClickLeaves(player, tool, level, stack, world, pos, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 50;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 80;
	}
}
