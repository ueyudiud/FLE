/*
 * copyright 2016-2018 ueyudiud
 */

/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.blocks;

import java.util.List;

import farcore.FarCoreRegistry;
import farcore.data.M;
import farcore.lib.material.Mat;
import fle.api.ditch.DitchBlockHandler;
import fle.core.client.model.ModelDitch;
import fle.core.client.render.TESRDitch;
import fle.core.tile.ditchs.TEDitch;
import nebula.client.CreativeTabBase;
import nebula.client.model.OrderModelLoader;
import nebula.client.util.Client;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSingleTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
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
		for (Mat material : Mat.filt(DitchBlockHandler.HANDLER, true))
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
		Client.registerModel(this.item, new ModelResourceLocation(getRegistryName(), "normal"));
		FarCoreRegistry.registerTESR(TESRDitch.class);
		
		// ModelLoader.setCustomStateMapper(BlocksItems.ditch, new
		// StateMapperExt(FLE.MODID, "ditch", null,
		// Misc.PROPS_SIDE_HORIZONTALS));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (Mat material : Mat.filt(DitchBlockHandler.HANDLER, true))
		{
			list.add(new ItemStack(item, 1, material.id));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		if (tooltip.isSneakDown())
		{
			Mat material = Mat.material(stack.getItemDamage());
			try
			{
				DitchBlockHandler.getFactory(material).addTooltip(material, tooltip);
			}
			catch (Exception exception)
			{
				tooltip.addLocal("Error Ditch Inforamtion!");
			}
		}
		else
		{
			tooltip.addShiftClickInfo();
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
