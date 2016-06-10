package fle.core.block.machine.alpha;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import fle.api.FleAPI;
import fle.core.tile.TileEntityTerrine;
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

public class BlockCeramics extends BlockHasTile
{
	public BlockCeramics()
	{
		super("machine.ceramics", ItemBlockBase.class, Material.piston, Boolean.TRUE);
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
	public void registerBlockIcons(IIconRegister register)
	{
		Icons.block_ceramics = register.registerIcon(getTextureName() + "/argil");
	}
	
	@Override
	public IIcon getIcon(int meta, int side)
	{
		return Icons.block_ceramics;
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
		case 0:
			return new TileEntityTerrine();
		default:
			break;
		}
		return null;
	}
}