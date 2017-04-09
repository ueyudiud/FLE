/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.CT;
import farcore.data.EnumBlock;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.block.state.PropertyMaterial;
import farcore.lib.material.Mat;
import nebula.client.ClientProxy;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSubBehavior;
import nebula.common.block.IBlockBehavior;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.data.Misc;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockMetal extends BlockSubBehavior implements IExtendedDataBlock
{
	public static final PropertyInteger CUSTOM_DATA = Misc.PROP_CUSTOM_DATA;
	public static final PropertyMaterial METALS = PropertyMaterial.create("type", SubTags.METAL);
	
	public BlockMetal()
	{
		super(FarCore.ID, "metalblock", Materials.METALIC);
		setCreativeTab(CT.tabMaterial);
		EnumBlock.metalblock.set(this);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		for (int i = 0; i < METALS.getAllowedValues().size(); ++i)
		{
			Mat material = METALS.getMaterialFromID(i, null);
			LanguageManager.registerLocal(getTranslateNameForItemStack(i), MC.block.getLocal(material));
			MC.block.registerOre(material, new ItemStack(this.item, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ClientProxy.registerCompactModel(new StateMapperExt(FarCore.ID, "metalblock", METALS), this, METALS);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, METALS, CUSTOM_DATA);
	}
	
	@Override
	protected IBlockBehavior<?> getBehavior(IBlockState state)
	{
		return state.getValue(METALS).getProperty(MP.property_metal_block);
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		return state.getValue(CUSTOM_DATA) << 16 | METALS.indexOf(state);
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		return getDefaultState()
				.withProperty(METALS, METALS.getMaterialFromID(meta & 0xFFFF, null))
				.withProperty(CUSTOM_DATA, (meta >> 16) & 0xF);
	}
	
	@Override
	public void registerStateToRegister(ExtendedBlockStateRegister register)
	{
		register.registerStates(this, METALS, CUSTOM_DATA);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (int i = 0; i < METALS.getAllowedValues().size(); ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
}