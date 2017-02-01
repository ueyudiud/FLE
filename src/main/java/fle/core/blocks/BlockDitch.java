/*
 * copyright© 2016-2017 ueyudiud
 */

/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import java.util.List;

import farcore.FarCoreRegistry;
import farcore.data.M;
import farcore.lib.material.Mat;
import fle.api.tile.IDitchTile.DitchBlockHandler;
import fle.core.client.model.ModelDitch;
import fle.core.tile.ditchs.TEDitch;
import nebula.client.CreativeTabBase;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.OrderModelLoader;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSingleTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockDitch extends BlockSingleTE
{
	public BlockDitch()
	{
		super("ditch", Material.ROCK);
		setCreativeTab(new CreativeTabBase("fle.ditch", "Ditches", () -> new ItemStack(BlockDitch.this, 1, M.oak.id)));
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		FarCoreRegistry.registerTileEntity("Ditch", TEDitch.class);
		for(Mat material : Mat.filt(DitchBlockHandler.HANDLER, true))
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(material.id), material.localName + " Ditch Block");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		OrderModelLoader.putModel(this, new ModelDitch());
		ModelResourceLocation location = new ModelResourceLocation(getRegistryName(), "normal");
		ModelLoader.setCustomMeshDefinition(this.item, stack -> location);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEDitch)
		{
			return BlockStateTileEntityWapper.wrap(tile, state);
		}
		return super.getExtendedState(state, world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(Mat material : Mat.filt(DitchBlockHandler.HANDLER, true))
		{
			list.add(new ItemStack(itemIn, 1, material.id));
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEDitch();
	}
}