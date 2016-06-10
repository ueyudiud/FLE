package fle.core.block.machine.alpha;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import fle.api.FleAPI;
import fle.core.tile.TileEntityArgilUnsmelted;
import fle.load.Icons;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCeramicsUnsmelted extends BlockHasTile
{
	public BlockCeramicsUnsmelted()
	{
		super("machine.ceramics.unsmelted", ItemBlockBase.class, Material.piston, Boolean.TRUE);
		setStepSound(soundTypeStone);
		blockHardness = 2.0F;
		blockResistance = 1.2F;
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerA.getRenderId();
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
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(item, 1, 0));
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		Icons.block_ceramics_unsmelted = register.registerIcon(getTextureName() + "/argil_unsmelted");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int meta, int side)
	{
		return Icons.block_ceramics_unsmelted;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityArgilUnsmelted();
	}
}