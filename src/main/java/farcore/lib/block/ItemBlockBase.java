package farcore.lib.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlockBase extends ItemBlock
{
	public BlockBase block;

	public ItemBlockBase(Block block)
	{
		super(block);
		this.block = (BlockBase) block;
		this.hasSubtypes = true;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(block.getTranslateNameForItemStack(stack));
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return block.damageDropped(meta);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return block.getIcon(0, meta);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, block, metadata, 3))
		{
			return false;
		}
		if (world.getBlock(x, y, z) == block)
		{
			block.onBlockPlacedBy(world, x, y, z, player, stack);
			block.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean F3H)
	{
		block.addInfomation(stack, player, list, F3H);
	}
}