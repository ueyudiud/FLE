/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCoreSetup.ClientProxy;
import farcore.data.CT;
import farcore.data.EnumRockType;
import farcore.lib.block.BlockStairV1;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.lib.model.block.statemap.StateMapperStiars;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRockStairs extends BlockStairV1 implements IThermalCustomBehaviorBlock
{
	private final Mat material;
	private final BlockRock parent;
	private String localName;
	/**
	 * The meta of main block.
	 */
	private int meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockRockStairs[] group;
	public final PropertyRock property;
	
	public BlockRockStairs(int id, BlockRock parent, BlockRockStairs[] group, String name, Mat material,
			String localName)
	{
		super(name + ".stairs", Material.ROCK);
		this.material = parent.material;
		this.parent = parent;
		this.localName = localName;
		this.meta = id;
		this.group = group;
		this.property = parent.property;
		setHardness(this.property.hardness * 0.8F);
		setResistance(this.property.explosionResistance * 0.75F);
		if(EnumRockType.values()[id].displayInTab)
		{
			setCreativeTab(CT.tabBuilding);
		}
		setTickRandomly(true);
		setDefaultState(getDefaultState().withProperty(BlockRock.HEATED, false));
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), this.localName + " Stairs");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperStiars(this.material.modid, "rock/stairs/" + this.material.name, null, BlockRock.HEATED);
		mapper.setVariants("type", EnumRockType.values()[this.meta].getName());
		ClientProxy.registerCompactModel(mapper, this, 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, HALF, SHAPE, BlockRock.HEATED);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		EnumRockType type = EnumRockType.values()[this.meta];
		switch (type)
		{
		case cobble_art:
			return 1;
		case cobble :
		case mossy :
			return this.property.harvestLevel / 2;
		default:
			return this.property.harvestLevel;
		}
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return EnumRockType.values()[this.meta].burnable;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(world, pos, face) ? 40 : 0;
	}
	
	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		if(isFlammable(world, pos, direction.of()))
		{
			U.Worlds.setBlock(world, pos, this.group[EnumRockType.values()[this.meta].noMossy],
					U.Worlds.getBlockMeta(world, pos), 3);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public double getThermalConduct(World world, BlockPos pos)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
}