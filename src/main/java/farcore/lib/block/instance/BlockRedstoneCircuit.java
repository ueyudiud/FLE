package farcore.lib.block.instance;

import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.Others;
import farcore.lib.block.BlockTE;
import farcore.lib.collection.IRegister;
import farcore.lib.material.Mat;
import farcore.lib.prop.PropertyTE.TETag;
import farcore.lib.tile.instance.circuit.TECircuitAnd;
import farcore.lib.tile.instance.circuit.TECircuitBase;
import farcore.lib.tile.instance.circuit.TECircuitBelong;
import farcore.lib.tile.instance.circuit.TECircuitCross;
import farcore.lib.tile.instance.circuit.TECircuitInvert;
import farcore.lib.tile.instance.circuit.TECircuitNand;
import farcore.lib.tile.instance.circuit.TECircuitNor;
import farcore.lib.tile.instance.circuit.TECircuitNot;
import farcore.lib.tile.instance.circuit.TECircuitOr;
import farcore.lib.tile.instance.circuit.TECircuitRSLatch;
import farcore.lib.tile.instance.circuit.TECircuitRepeater;
import farcore.lib.tile.instance.circuit.TECircuitSynchronizer;
import farcore.lib.tile.instance.circuit.TECircuitTicking;
import farcore.lib.tile.instance.circuit.TECircuitXor;
import farcore.lib.tile.instance.circuit.TESensorLight;
import farcore.lib.util.Direction;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneCircuit extends BlockTE
{
	private static final PropertyInteger CUSTOM_VALUE = PropertyInteger.create("custom", 0, 16);
	
	public BlockRedstoneCircuit()
	{
		super(FarCore.ID, "red.circuit", Material.CIRCUITS);
		EnumBlock.circuit.set(this);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return property_TE.getMetaFromState(state);
	}

	public static ItemStack createItemStack(int meta, Mat material)
	{
		ItemStack stack = new ItemStack(EnumBlock.circuit.block, 1, meta);
		NBTTagCompound nbt;
		stack.setTagCompound(nbt = new NBTTagCompound());
		nbt.setString("material", material.name);
		return stack;
	}

	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return super.initDefaultState(state).withProperty(Others.PROP_DIRECTION_HORIZONTALS, Direction.N);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, createTEProperty(), Others.PROP_DIRECTION_HORIZONTALS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		state = property_TE.withProperty(state, tile);
		if(tile instanceof TECircuitBase)
		{
			state = state.withProperty(Others.PROP_DIRECTION_HORIZONTALS, ((TECircuitBase) tile).getRotation());
		}
		return state;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(TETag tag : property_TE.getAllowedValues())
		{
			list.add(createItemStack(tag.id(), M.stone));
		}
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canBlockStay(worldIn, pos);
	}

	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos.down(), EnumFacing.UP);
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
	}
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if(!canBlockStay(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return;
		}
		super.neighborChanged(state, worldIn, pos, blockIn);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!canBlockStay(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return;
		}
		super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(1, "repeater", TECircuitRepeater.class);
		register.register(2, "ticking", TECircuitTicking.class);
		register.register(3, "rslatch", TECircuitRSLatch.class);
		register.register(4, "synchronizer", TECircuitSynchronizer.class);
		register.register(16, "not", TECircuitNot.class);
		register.register(17, "or", TECircuitOr.class);
		register.register(18, "and", TECircuitAnd.class);
		register.register(19, "xor", TECircuitXor.class);
		register.register(20, "nand", TECircuitNand.class);
		register.register(21, "nor", TECircuitNor.class);
		register.register(22, "belong", TECircuitBelong.class);
		register.register(32, "cross", TECircuitCross.class);
		register.register(33, "invert", TECircuitInvert.class);
		register.register(64, "sensor_light", TESensorLight.class);
		return true;
	}

	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip,
			boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		Mat material = Mat.material(U.ItemStacks.setupNBT(stack, false).getString("material"), M.stone);
		tooltip.add("info.redstone.circuit.material", material.getLocalName());
	}
}