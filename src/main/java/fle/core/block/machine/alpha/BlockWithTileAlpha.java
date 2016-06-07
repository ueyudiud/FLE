package fle.core.block.machine.alpha;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockBase;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import farcore.interfaces.gui.IHasGui;
import farcore.util.V;
import fle.api.FleAPI;
import fle.core.container.alpha.ContainerPolish;
import fle.core.gui.alpha.GuiPolish;
import fle.core.tile.TileEntityDryingTable;
import fle.load.Icons;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWithTileAlpha extends BlockHasTile
{
	public BlockWithTileAlpha()
	{
		super("machine.ii.alpha", ItemBlockBase.class, Material.wood, Boolean.TRUE);
		blockHardness = 1.5F;
		blockResistance = 1.0F;
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerB.getRenderId();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		Icons.block_drying_table = register.registerIcon(getTextureName() + "/drying_table");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		switch (meta)
		{
		case 0 : return Icons.block_drying_table;
		default:
			break;
		}
		return V.voidBlockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		if(!world.isRemote)
		{
			switch (world.getBlockMetadata(x, y, z))
			{
			case 0:
				FleAPI.openGui(0, player, world, x, y, z);
				return true;
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		switch (meta)
		{
		case 0 : return new TileEntityDryingTable();
		default:
			break;
		}
		return null;
	}
}