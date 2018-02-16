/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks.flora;

import farcore.data.CT;
import farcore.data.MC;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.prop.PropertyWood;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.util.OreDict;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlank extends BlockMaterial<PropertyWood>
{
	public static enum EnumPlankState implements IStringSerializable
	{
		DEFAULT("default"), FIRE_RESISTANCE("fire_resistance"), ANTICORROSIVE("anticorrosive"), BROKE("broke");
		
		final String name;
		
		EnumPlankState(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
	}
	
	public static final PropertyEnum<EnumPlankState> STATE = PropertyEnum.create("state", EnumPlankState.class);
	
	public final BlockPlankSlab[] slabGroup;
	
	public BlockPlank(PropertyWood tree)
	{
		super(tree.material.modid, "plank." + tree.material.name, Material.WOOD, tree.material, tree);
		this.slabGroup = makeSlabs(this.blockName, this.material.localName + " Plank");
		setSoundType(SoundType.WOOD);
		setCreativeTab(CT.TREE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("plankWood", this);
		MC.plankBlock.registerOre(this.material, this);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "%s Plank", this.material.localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Fire Resistance %s Plank", this.material.localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), "Anticorrosive %s Plank", this.material.localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), "Broke %s Plank", this.material.localName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "plank/" + this.material.name, null);
		Renders.registerCompactModel(mapper, this, EnumPlankState.values().length);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, STATE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	protected BlockPlankSlab[] makeSlabs(String name, String localName)
	{
		BlockPlankSlab[] ret = new BlockPlankSlab[4];
		ret[0] = new BlockPlankSlab(0, this, ret, localName);
		ret[1] = new BlockPlankSlab(1, this, ret, "Fire Resistance " + localName);
		ret[2] = new BlockPlankSlab(2, this, ret, "Anticorrosive " + localName);
		ret[3] = new BlockPlankSlab(3, this, ret, "Broke " + localName);
		return ret;
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(STATE, EnumPlankState.DEFAULT);
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return state.getValue(STATE) == EnumPlankState.BROKE ? 100 : 255;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(STATE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(STATE, EnumPlankState.values()[meta]);
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		return (state.getValue(STATE) == EnumPlankState.BROKE ? 0.1F : 1.0F) * super.getBlockHardness(state, worldIn, pos);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(STATE) == EnumPlankState.BROKE ? 0F : super.getExplosionResistance(world, pos, exploder, explosion);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return state.getValue(STATE) != EnumPlankState.BROKE;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return state.getValue(STATE) == EnumPlankState.BROKE ? EnumPushReaction.DESTROY : state.getMaterial().getMobilityFlag();
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getValue(STATE) == EnumPlankState.FIRE_RESISTANCE ? 0 : super.getFireSpreadSpeed(world, pos, face);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(STATE) == EnumPlankState.FIRE_RESISTANCE ? 0 : super.getFlammability(world, pos, face);
	}
}
