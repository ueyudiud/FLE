package farcore.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFleBlock extends ItemBlock
{
	protected BlockFle block;

	public ItemFleBlock(Block aBlock)
	{
		super(aBlock);
		if(aBlock instanceof BlockFle)
		{
			block = (BlockFle) aBlock;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return block.getUnlocalizedName();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return block.getLocalizedName(stack);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		return block.onPlacedAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
	}
	
	@Override
	public void addInformation(ItemStack aStack, EntityPlayer aPlayer,
			List aList, boolean aFlag)
	{
		if(block != null)
		{
			block.addInformation(aStack, aList, aPlayer);
		}
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) 
	{
		if(block == null) return super.getItemStackLimit(stack);
		return block.getMaxStackSize();
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
	
	protected static NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(!aStack.hasTagCompound())
		{
			aStack.setTagCompound(new NBTTagCompound());
		}
		return aStack.stackTagCompound;
	}
}