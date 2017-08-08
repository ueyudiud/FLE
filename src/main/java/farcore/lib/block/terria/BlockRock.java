/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.terria;

import java.util.List;

import farcore.data.CT;
import farcore.data.EnumRockType;
import farcore.data.MC;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.material.Mat;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSubBehavior;
import nebula.common.block.IBlockBehavior;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.block.IToolableBlock;
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
public class BlockRock extends BlockSubBehavior
implements ISmartFallableBlock, IThermalCustomBehaviorBlock, IToolableBlock
{
	public static final PropertyBool				HEATED = PropertyBool.create("heated");
	public static final PropertyEnum<EnumRockType>	TYPE = Properties.get(EnumRockType.class);
	
	//	protected final BlockRockSlab slab;
	protected final IBlockBehavior<BlockRock> behavior;
	public final Mat material;
	
	public BlockRock(Mat material, IBlockBehavior<BlockRock> behavior)
	{
		super("rock." + material.name, Material.ROCK);
		this.behavior = behavior;
		this.material = material;
		setSoundType(SoundType.STONE);
		setTickRandomly(true);
		//		this.slab = createRockSlab();
	}
	
	//	protected BlockRockSlab createRockSlab()
	//	{
	//		return new BlockRockSlab(this);
	//	}
	
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
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Renders.registerCompactModel(new StateMapperExt(this.material.modid, "rock/" + this.material.name, null, HEATED),
				this, TYPE);
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
		return new CreativeTabs[]{CT.tabBuilding, CT.tabTerria};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		if(tab == CT.tabTerria)
		{
			list.add(new ItemStack(item));
		}
		else
		{
			for(EnumRockType type : EnumRockType.values())
			{
				if(type.displayInTab)
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
}