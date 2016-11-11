/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.block.instance;

import farcore.FarCoreSetup.ClientProxy;
import farcore.data.CT;
import farcore.lib.block.BlockSlab;
import farcore.lib.block.instance.BlockPlank.EnumPlankState;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.model.block.StateMapperExt;
import farcore.util.U.OreDict;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockPlankSlab extends BlockSlab
{
	private final Mat material;
	private final BlockPlank parent;
	private final String localName;
	/**
	 * The meta of main block.
	 */
	private int meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockPlankSlab[] group;
	private final PropertyWood property;
	
	public BlockPlankSlab(int id, BlockPlank parent, BlockPlankSlab[] group, String localName)
	{
		super(parent.material.modid, "plank." + parent.material.name + ".slab." + id, Material.WOOD);
		meta = id;
		this.group = group;
		this.parent = parent;
		this.localName = localName;
		material = parent.material;
		property = parent.property;
		if(id == EnumPlankState.BROKE.ordinal())
		{
			lightOpacity = 100;
		}
		setHardness(id == EnumPlankState.BROKE.ordinal() ? property.hardness * 0.06F : property.hardness * 0.6F);
		setResistance(id == EnumPlankState.BROKE.ordinal() ? 0F : property.explosionResistance * 0.4F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(CT.tabBuilding);
	}

	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("slabWood", this);
		OreDict.registerValid("slab" + material.oreDictName, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(material.modid, "plank/slab/" + material.name, null);
		mapper.setVariants("state", EnumPlankState.values()[meta].getName());
		ClientProxy.registerCompactModel(mapper, this, EnumPlankState.values().length);
	}
	
	@Override
	protected String getLocalName()
	{
		return localName;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return meta != EnumPlankState.BROKE.ordinal();
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return meta == EnumPlankState.BROKE.ordinal() ? EnumPushReaction.DESTROY : blockMaterial.getMobilityFlag();
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return meta == EnumPlankState.FIRE_RESISTANCE.ordinal() ? 0 :
			super.getFireSpreadSpeed(world, pos, face);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return meta == EnumPlankState.FIRE_RESISTANCE.ordinal() ? 0 :
			super.getFlammability(world, pos, face);
	}
}