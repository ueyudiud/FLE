package farcore.lib.block.instance;

import java.util.Random;

import farcore.data.CT;
import farcore.data.EnumSlabState;
import farcore.lib.block.BlockSlab;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
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

public class BlockRockSlab extends BlockSlab implements IThermalCustomBehaviorBlock
{
	private final Mat material;
	private final BlockRock parent;
	/**
	 * The meta of main block.
	 */
	private int meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockRockSlab[] group;
	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	public final int harvestLevel;

	public BlockRockSlab(int id, BlockRock parent, BlockRockSlab[] group, String name, Mat material,
			String localName)
	{
		super(name + ".slab", Material.ROCK);
		meta = id;
		this.group = group;
		this.parent = parent;
		this.material = parent.material;
		harvestLevel = parent.harvestLevel;
		setHardness((hardnessMultiplier = parent.hardnessMultiplier) * 0.6F);
		setResistance((resistanceMultiplier = parent.resistanceMultiplier) * 0.4F);
		if(RockType.values()[id].displayInTab)
		{
			setCreativeTab(CT.tabBuilding);
		}
		setTickRandomly(true);
		setDefaultState(getDefaultState().withProperty(BlockRock.HEATED, false));
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(5), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(6), "Double " + localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(7), "Double " + localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(8), "Double " + localName + " Slab");
		U.Mod.registerItemBlockModel(this, 0, material.modid, "rock/" + material.name + "/" + RockType.values()[meta].name() + "_slab");
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumSlabState.PROPERTY, BlockRock.HEATED);
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
		RockType type = RockType.values()[meta];
		switch (type)
		{
		case cobble_art:
			return 1;
		case cobble :
		case mossy :
			return harvestLevel / 2;
		default:
			return harvestLevel;
		}
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return RockType.values()[meta].burnable;
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
			U.Worlds.setBlock(world, pos, group[RockType.values()[meta].noMossy],
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
	public float getThermalConduct(World world, BlockPos pos)
	{
		return material.thermalConduct;
	}

	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
}