package fla.core.block.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.core.Fla;
import fla.core.block.BlockBase;

public class ItemSubBlock extends ItemBlock
{
	protected BlockBase block;
	
	public ItemSubBlock(Block block)
	{
		super(block);
		this.block = (BlockBase) block;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return block.getUnlocalizedName() + ":" + itemstack.getItemDamage();
	}

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return this.block.getIcon(2, damage);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player,	List list, boolean b) 
    {
    	this.block.getBlockInfomation(item, player.isSneaking(), Fla.fla.km.get().isSneakKeyDown(player), list);
    }
}
