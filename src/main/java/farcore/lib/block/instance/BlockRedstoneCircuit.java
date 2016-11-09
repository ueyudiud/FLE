package farcore.lib.block.instance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.Others;
import farcore.lib.block.BlockTE;
import farcore.lib.collection.IRegister;
import farcore.lib.material.Mat;
import farcore.lib.model.block.BlockStateTileEntityWapper;
import farcore.lib.model.block.ModelRedstoneCircuit;
import farcore.lib.model.block.StateMapperCircuit;
import farcore.lib.prop.PropertyString;
import farcore.lib.prop.PropertyTE.TETag;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_CollisionBoundingBox;
import farcore.lib.tile.instance.circuit.TECircuitAnd;
import farcore.lib.tile.instance.circuit.TECircuitBase;
import farcore.lib.tile.instance.circuit.TECircuitCross;
import farcore.lib.tile.instance.circuit.TECircuitImples;
import farcore.lib.tile.instance.circuit.TECircuitIntegration;
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
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneCircuit extends BlockTE
{
	public static final Map<String, List<String>> ALLOWED_STATES;
	
	public static final PropertyString CUSTOM_VALUE;
	
	static
	{
		ImmutableMap.Builder<String, List<String>> builder1 = ImmutableMap.builder();
		builder1.put("repeater", Arrays.asList("t0_on", "t0_off", "t1_on", "t1_off", "t2_on", "t2_off", "t3_on", "t3_off", "t4_on", "t4_off"));
		builder1.put("ticking", Arrays.asList("on_a", "on_b", "off"));
		builder1.put("rslatch", Arrays.asList("on", "off"));
		builder1.put("synchronizer", Arrays.asList("_"));
		builder1.put("not", Arrays.asList("on", "off"));
		builder1.put("or", Arrays.asList("ddd", "dde", "ded", "edd", "dee", "ede", "eed", "eee"));
		builder1.put("and", Arrays.asList("ddd", "dde", "ded", "edd", "dee", "ede", "eed", "eee"));
		builder1.put("xor", Arrays.asList("dd", "ed", "de", "ee"));
		builder1.put("nand", Arrays.asList("ddd", "dde", "ded", "edd", "dee", "ede", "eed", "eee"));
		builder1.put("nor", Arrays.asList("ddd", "dde", "ded", "edd", "dee", "ede", "eed", "eee"));
		builder1.put("imples", Arrays.asList("ldd", "lde", "led", "lee", "rdd", "rde", "red", "ree"));
		builder1.put("integration", Arrays.asList("_"));
		builder1.put("cross", Arrays.asList("_"));
		builder1.put("invert", Arrays.asList("_"));
		builder1.put("sensor_light", Arrays.asList("_"));
		ALLOWED_STATES = builder1.build();
		HashSet<String> set = new HashSet();
		for(List<String> list : ALLOWED_STATES.values())
		{
			set.addAll(list);
		}
		CUSTOM_VALUE = new PropertyString("value", ImmutableList.copyOf(set));
	}
	
	public BlockRedstoneCircuit()
	{
		super(FarCore.ID, "red.circuit", Material.CIRCUITS);
		EnumBlock.circuit.set(this);
	}

	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Redstone Repeater");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), "Redstone Ticker");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), "Redstone RS Latch");
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), "Redstone Synchronizer");
		LanguageManager.registerLocal(getTranslateNameForItemStack(16), "Redstone Not Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(17), "Redstone Or Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(18), "Redstone And Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(19), "Redstone Xor Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(20), "Redstone Nand Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(21), "Redstone Nor Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(22), "Redstone Imples Door");
		LanguageManager.registerLocal(getTranslateNameForItemStack(32), "Redstone Crosser");
		LanguageManager.registerLocal(getTranslateNameForItemStack(33), "Redstone Invert");
		LanguageManager.registerLocal(getTranslateNameForItemStack(64), "Redstone Light Sensor");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ModelLoaderRegistry.registerLoader(ModelRedstoneCircuit.RedstoneCircuitModelLoader.INSTANCE);
		ModelLoader.setCustomStateMapper(this, new StateMapperCircuit());
		for(TETag tag : property_TE.getAllowedValues())
		{
			ModelLoader.setCustomModelResourceLocation(item, tag.id(), new ModelResourceLocation(FarCore.ID + ":circuit/" + tag.name(), "inventory"));
		}
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
		return new BlockStateContainer(this, createTEProperty(), Others.PROP_DIRECTION_HORIZONTALS, CUSTOM_VALUE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		state = property_TE.withProperty(state, tile);
		if(tile instanceof TECircuitBase)
		{
			try
			{
				state = state
						.withProperty(Others.PROP_DIRECTION_HORIZONTALS, ((TECircuitBase) tile).getRotation())
						.withProperty(CUSTOM_VALUE, ((TECircuitBase) tile).getState());
			}
			catch(Exception exception)
			{
				if(FarCore.debug)
					throw exception;
				return state;
			}
		}
		return state;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECircuitBase)
			return BlockStateTileEntityWapper.wrap(tile, state);
		return super.getExtendedState(state, world, pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		TileEntity tile = source.getTileEntity(pos);
		if(tile instanceof ITP_CollisionBoundingBox)
			return ((ITP_CollisionBoundingBox) tile).getCollisionBoundingBox(state);
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(TETag tag : property_TE.getAllowedValues())
		{
			if(tag.isValidTag())
			{
				list.add(createItemStack(tag.id(), M.stone));
			}
		}
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
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
		register.register(22, "imples", TECircuitImples.class);
		register.register(24, "integration", TECircuitIntegration.class);
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