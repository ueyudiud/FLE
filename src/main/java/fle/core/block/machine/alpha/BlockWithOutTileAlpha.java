package fle.core.block.machine.alpha;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.block.ItemBlockBase;
import farcore.interfaces.gui.IHasGui;
import farcore.util.LanguageManager;
import farcore.util.V;
import fle.api.FleAPI;
import fle.api.gui.ContainerCraftingFle;
import fle.api.gui.GuiCraftingFle;
import fle.core.container.alpha.ContainerPolish;
import fle.core.gui.alpha.GuiPolish;
import fle.load.Icons;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWithOutTileAlpha extends BlockBase implements IHasGui
{
	public BlockWithOutTileAlpha()
	{
		super("machine.i.alpha", ItemBlockBase.class, Material.wood, Boolean.TRUE);
		blockHardness = 1.5F;
		blockResistance = 1.0F;
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		ItemStack stack = new ItemStack(this);
		stack.setItemDamage(0);
		manager.registerLocal(stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name", "Polishing Table");
		stack.setItemDamage(1);
		manager.registerLocal(stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name", "Manual Operating Platform");
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		Icons.block_polish_top = register.registerIcon(getTextureName() + "/polish_top");
		Icons.block_polish_side = register.registerIcon(getTextureName() + "/polish_side");
		Icons.block_polish_bottom = register.registerIcon(getTextureName() + "/polish_bottom");
		Icons.block_manual_operating_platform_top = register.registerIcon(getTextureName() + "/manual_operating_platform_top");
		Icons.block_manual_operating_platform_side = register.registerIcon(getTextureName() + "/manual_operating_platform_side");
		Icons.block_manual_operating_platform_bottom = register.registerIcon(getTextureName() + "/manual_operating_platform_bottom");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		switch (meta)
		{
		case 0 : return side == 0 ? Icons.block_polish_bottom : side == 1 ?
				Icons.block_polish_top : Icons.block_polish_side;
		case 1 : return side == 0 ? Icons.block_manual_operating_platform_bottom : side == 1 ?
				Icons.block_manual_operating_platform_top : Icons.block_manual_operating_platform_side;
		default:
			break;
		}
		return V.voidBlockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
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
			case 1:
				FleAPI.openGui(1, player, world, x, y, z);
				return true;
			default:
				break;
			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public Gui openGUI(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0:
			return new GuiPolish(world, x, y, z, player);
		case 1:
			return new GuiCraftingFle(world, x, y, z, player);
		default:
			break;
		}
		return null;
	}

	@Override
	public Container openContainer(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0:
			return new ContainerPolish(world, x, y, z, player);
		case 1:
			return new ContainerCraftingFle(world, x, y, z, player);
		default:
			break;
		}
		return null;
	}
}