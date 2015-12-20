package flapi.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import flapi.FleAPI;
import flapi.block.old.BlockFle;

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
	public String getUnlocalizedName(ItemStack aStack)
	{
		return block.hasSubs() ? block.getUnlocalizedName() + ":" + aStack.getItemDamage() :
			block.getUnlocalizedName();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack aStack)
	{
		return FleAPI.langManager.translateToLocal(getUnlocalizedName(aStack) + ".name", new Object[0]);
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
	public ItemFleBlock setMaxStackSize(int aSize)
	{
		if(block == null) return (ItemFleBlock) super.setMaxStackSize(aSize);
		block.setMaxStackSize(maxStackSize = aSize);
		return this;
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