/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks.terria;

import java.util.List;

import farcore.data.CT;
import farcore.data.EnumRockType;
import farcore.data.MC;
import farcore.lib.block.ISmartFallableBlockRockLike;
import farcore.lib.block.behavior.IRockLikeBehavior;
import farcore.lib.material.Mat;
import nebula.NebulaLog;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSubBehavior;
import nebula.common.block.IBlockBehavior;
import nebula.common.stack.BaseStack;
import nebula.common.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRock extends BlockSubBehavior implements ISmartFallableBlockRockLike
{
	public static final PropertyBool				HEATED	= PropertyBool.create("heated");
	public static final PropertyEnum<EnumRockType>	TYPE	= Properties.get(EnumRockType.class);
	
	// protected final BlockRockSlab slab;
	public final IRockLikeBehavior<BlockRock>	behavior;
	public final Mat							material;
	
	public BlockRock(Mat material, IRockLikeBehavior<BlockRock> behavior)
	{
		super("rock." + material.name, Material.ROCK);
		this.behavior = behavior;
		this.material = material;
		setSoundType(SoundType.STONE);
		setTickRandomly(true);
		// this.slab = createRockSlab();
	}
	
	// protected BlockRockSlab createRockSlab()
	// {
	// return new BlockRockSlab(this);
	// }
	
	@Override
	protected IBlockBehavior<?> getBehavior(IBlockState state)
	{
		return this.behavior;
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		for (EnumRockType type : EnumRockType.values())
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(type.ordinal()), type.local, MC.stone.getLocal(this.material));
		}
		MC.stone.registerOre(this.material, new BaseStack(getDefaultState().withProperty(TYPE, EnumRockType.resource)).instance());
		MC.cobble.registerOre(this.material, new BaseStack(getDefaultState().withProperty(TYPE, EnumRockType.cobble)).instance());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Renders.registerCompactModel(new StateMapperExt(this.material.modid, "rock/" + this.material.name, null, HEATED), this, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE, HEATED);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(TYPE, EnumRockType.resource).withProperty(HEATED, false);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, EnumRockType.values()[meta]);
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] { CT.BUILDING, CT.TERRIA };
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		if (tab == CT.TERRIA)
		{
			list.add(new ItemStack(item));
		}
		else
		{
			for (EnumRockType type : EnumRockType.values())
			{
				if (type.displayInTab)
				{
					list.add(new ItemStack(item, 1, type.ordinal()));
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		worldIn.scheduleUpdate(pos, blockIn, tickRate(worldIn));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public void scheduleFalling(World world, BlockPos pos, IBlockState state, int delayMultiplier)
	{
		try
		{
			this.behavior.scheduleFalling(this, world, pos, state, delayMultiplier);
		}
		catch (Throwable exception)
		{
			NebulaLog.catching(exception);
		}
	}
	
	@Override
	public boolean onCaveInCheck(World world, BlockPos pos, IBlockState state)
	{
		try
		{
			return this.behavior.onCaveInCheck(this, world, pos, state);
		}
		catch (Throwable exception)
		{
			NebulaLog.catching(exception);
			return false;
		}
	}
}
