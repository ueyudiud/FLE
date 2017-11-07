/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.blocks.container;

import java.util.List;

import farcore.FarCoreRegistry;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import fle.core.client.model.ModelSmallRockyTank;
import fle.core.client.render.TESRFluidFase;
import fle.core.tile.tanks.TESmallRockyTank;
import nebula.base.IRegister;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.OrderModelLoader;
import nebula.client.util.Client;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRockyTank extends BlockTE
{
	public BlockRockyTank()
	{
		super("rocky.tank", Materials.ROCK);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Rocky Tank");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		OrderModelLoader.putModel(this, new ModelSmallRockyTank());
		Client.registerModel(this.item, new ModelResourceLocation(getRegistryName(), "normal"));
		FarCoreRegistry.registerTESR(TESmallRockyTank.class, new TESRFluidFase<TESmallRockyTank>(0.25F, 0.25F, 0.25F, 0.75F, 0.9375F, 0.75F, t -> t.getFluidTank().getInfo()));
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "tank_small", TESmallRockyTank.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (Mat material : Mat.filt(SubTags.ROCK))
		{
			ItemStack stack = new ItemStack(item, 1, 0);
			Mat.setMaterialToStack(stack, "material", material);
			list.add(stack);
		}
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		return BlockStateTileEntityWapper.wrap(tile, state);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
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
		return BlockRenderLayer.CUTOUT;
	}
}
