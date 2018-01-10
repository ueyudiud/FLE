/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks.wood;

import farcore.blocks.wood.BlockPlank.EnumPlankState;
import farcore.data.CT;
import farcore.lib.block.BlockSlab;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.util.OreDict;
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
	private final Mat				material;
	private final BlockPlank		parent;
	private final String			localName;
	/**
	 * The meta of main block.
	 */
	private int						meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockPlankSlab[]	group;
	private final PropertyWood		property;
	
	public BlockPlankSlab(int id, BlockPlank parent, BlockPlankSlab[] group, String localName)
	{
		super(parent.material.modid, "plank." + parent.material.name + ".slab." + id, Material.WOOD);
		this.meta = id;
		this.group = group;
		this.parent = parent;
		this.localName = localName;
		this.material = parent.material;
		this.property = parent.property;
		if (id == EnumPlankState.BROKE.ordinal())
		{
			this.lightOpacity = 100;
		}
		setHardness(id == EnumPlankState.BROKE.ordinal() ? this.property.hardness * 0.06F : this.property.hardness * 0.6F);
		setResistance(id == EnumPlankState.BROKE.ordinal() ? 0F : this.property.explosionResistance * 0.4F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(CT.TREE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("slabWood", this);
		OreDict.registerValid("slab" + this.material.oreDictName, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "plank/slab/" + this.material.name, null);
		mapper.setVariants("state", EnumPlankState.values()[this.meta].getName());
		Renders.registerCompactModel(mapper, this, EnumPlankState.values().length);
	}
	
	@Override
	protected String getLocalName()
	{
		return this.localName;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return this.meta != EnumPlankState.BROKE.ordinal();
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return this.meta == EnumPlankState.BROKE.ordinal() ? EnumPushReaction.DESTROY : this.blockMaterial.getMobilityFlag();
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.meta == EnumPlankState.FIRE_RESISTANCE.ordinal() ? 0 : 5;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.meta == EnumPlankState.FIRE_RESISTANCE.ordinal() ? 0 : 20;
	}
}
