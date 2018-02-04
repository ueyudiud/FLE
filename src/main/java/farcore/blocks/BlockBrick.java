/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks;

import java.util.List;

import farcore.data.CT;
import farcore.data.MC;
import farcore.data.Materials;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.util.OreDict;
import nebula.common.util.Properties;
import nebula.common.util.Properties.EnumStateName;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockBrick extends BlockMaterial<PropertyBlockable>
{
	@EnumStateName("type")
	public static enum EnumType implements IStringSerializable
	{
		NORMAL, EIGEN;
		
		static final EnumType[] VALUES = values();
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
	
	public static final IProperty<EnumType> TYPE = Properties.get(EnumType.class);
	
	public BlockBrick(String modid, String name, Mat mat, PropertyBlockable property)
	{
		super(modid, name, Materials.POTTERY, mat, property);
		setCreativeTab(CT.BUILDING);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		OreDict.registerValid(MC.brickBlock.getOreName(this.material), this);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), MC.brickBlock.getLocal(this.material));
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Eigen " + MC.brickBlock.getLocal(this.material));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Renders.registerCompactModel(new StateMapperExt(this.material.modid, "brick/" + this.material.name, null), this, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, EnumType.VALUES[meta % EnumType.VALUES.length]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumType type : EnumType.VALUES)
			list.add(new ItemStack(item, 1, type.ordinal()));
	}
}
