package fla.core.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import fla.core.Fla;
import fla.core.FlaCreativeTab;
import fla.core.block.BlockArgilBase;

public class ItemArgil extends ItemBase
{
	private IIcon[] icons;
	BlockArgilBase block;
	
	public ItemArgil(Block aBlock) 
	{
		maxStackSize = 1;
		setHasSubtypes(true);
		setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab);
		if(!(aBlock instanceof BlockArgilBase)) throw new NullPointerException();
		block = (BlockArgilBase) aBlock;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
		icons = new IIcon[16];
		for(int i = 0; i < block.getMaxDamage(); ++i)
		{
			icons[i] = register.registerIcon(block.getItemIconName(i));
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player,
			World world, int x, int y, int z, int side, float xPos, float yPos,
			float zPos)
	{
		if(Item.getItemFromBlock(block).onItemUse(item, player, world, x, y, z, side, xPos, yPos, zPos))
		{			
			block.addNBTToTileByItemStack(item);
			return true;
		}
		return false;
	}
	
	@Override
	public IIcon getIconFromDamage(int damage) 
	{
		return icons[damage % icons.length];
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < block.getMaxDamage(); ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public void addInformation(ItemStack i, EntityPlayer e, List l, boolean u) 
	{
		block.getBlockInfomation(i, e.isSneaking(), Fla.fla.km.get().isPlaceKeyDown(e), l);
		super.addInformation(i, e, l, u);
	}
}