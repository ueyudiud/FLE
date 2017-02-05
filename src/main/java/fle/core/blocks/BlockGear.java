/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import java.util.List;

import farcore.FarCoreRegistry;
import farcore.data.M;
import farcore.lib.material.Mat;
import fle.api.FLEAPI;
import fle.core.tile.kinetic.TEGear;
import fle.core.tile.kinetic.TEGear.GearSize;
import nebula.common.block.BlockSingleTE;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockGear extends BlockSingleTE
{
	public BlockGear()
	{
		super("gear.box", Material.IRON);
		setCreativeTab(FLEAPI.tabSimpleMachinery);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		FarCoreRegistry.registerTileEntity("Gear", TEGear.class);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		final Mat[] materials = {M.oak, M.stone};
		for (GearSize size : GearSize.values())
		{
			ItemStack stack = new ItemStack(itemIn, 1, size.ordinal());
			for (Mat material : materials)
			{
				Mat.setMaterialToStack(stack, "material", material);
				list.add(stack.copy());
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEGear();
	}
}