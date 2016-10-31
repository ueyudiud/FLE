package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCore;
import farcore.FarCoreSetup.ClientProxy;
import farcore.data.CT;
import farcore.data.MC;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.model.block.StateMapperExt;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.util.U.OreDict;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlank extends BlockMaterial<PropertyWood>
{
	public static final IProperty<Boolean> FIRE_RESISTANCE = PropertyBool.create("fire_resistance");
	public static final IProperty<Boolean> ANTICORROSIVE = PropertyBool.create("anticorrosive");
	public static final IProperty<Boolean> WET = PropertyBool.create("wet");
	/**
	 * Default broken plank only can be get at such as sinking ship.
	 */
	public static final IProperty<Boolean> BROKE = PropertyBool.create("broke");

	public BlockPlank(Mat material, PropertyWood property)
	{
		super(FarCore.ID, "plank." + material.name, Material.WOOD, material, property);
		setCreativeTab(CT.tabBuilding);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("plankWood", this);
		MC.plankBlock.registerOre(material, this);
		for(int i = 0; i < 16; ++i)
		{
			IBlockState state = getStateFromMeta(i);
			String name = material.localName + " Plank";
			if(state.getValue(FIRE_RESISTANCE))
			{
				name = "Fire Resistanced " + name;
			}
			if(state.getValue(WET))
			{
				name = "Wet " + name;
			}
			LanguageManager.registerLocal(getTranslateNameForItemStack(i), name);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(material.modid, "plank", null);
		mapper.setVariants("type", material.name);
		ClientProxy.registerCompactModel(mapper, this, 16);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FIRE_RESISTANCE, ANTICORROSIVE, WET, BROKE);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(WET, false).withProperty(FIRE_RESISTANCE, false).withProperty(ANTICORROSIVE, false).withProperty(BROKE, false);
	}

	@Override
	public int getLightOpacity(IBlockState state)
	{
		return state.getValue(BROKE) ? 100 : 255;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int v = 0;
		if(state.getValue(FIRE_RESISTANCE))
		{
			v |= 1;
		}
		if(state.getValue(ANTICORROSIVE))
		{
			v |= 2;
		}
		if(state.getValue(WET))
		{
			v |= 4;
		}
		if(state.getValue(BROKE))
		{
			v |= 8;
		}
		return v;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		if((meta & 0x1) != 0)
		{
			state = state.withProperty(FIRE_RESISTANCE, true);
		}
		if((meta & 0x2) != 0)
		{
			state = state.withProperty(ANTICORROSIVE, true);
		}
		if((meta & 0x4) != 0)
		{
			state = state.withProperty(WET, true);
		}
		if((meta & 0x8) != 0)
		{
			state = state.withProperty(BROKE, true);
		}
		return state;
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		return (state.getValue(BROKE) ? 0.1F : state.getValue(WET) ? 0.8F : 1.0F) * super.getBlockHardness(state, worldIn, pos);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(BROKE) ? 0F : super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return !state.getValue(BROKE);
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return !state.getValue(BROKE);
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return state.getValue(BROKE) ? EnumPushReaction.DESTROY : state.getMaterial().getMobilityFlag();
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getValue(FIRE_RESISTANCE) ? 0 :
			super.getFireSpreadSpeed(world, pos, face) / (state.getValue(WET) ? 3 : 1);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(FIRE_RESISTANCE) ? 0 :
			super.getFlammability(world, pos, face);
	}
	
	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getValue(WET))
		{
			if (!world.isRemote && rand.nextInt(5) == 0)
			{
				world.setBlockState(pos, state.withProperty(WET, false), 7);
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.7F + 2.0F);
			}
			return true;
		}
		return super.onBurningTick(world, pos, rand, fireSourceDir, fireState);
	}
}