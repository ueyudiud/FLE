package farcore.block.item;

import java.util.List;

import farcore.FarCore;
import farcore.block.BlockBase;
import farcore.render.block.ItemBlockRender;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemBlockBase<T extends BlockBase> extends ItemBlock
{	
	protected static NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(!aStack.hasTagCompound())
		{
			aStack.setTagCompound(new NBTTagCompound());
		}
		return aStack.stackTagCompound;
	}
	
	public T block;

	public ItemBlockBase(Block block)
	{
		super(block);
		this.block = (T) block;
		hasSubtypes = true;
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
	
	public String translateToLocal(String name, Object...objects)
	{
		return FarCore.translateToLocal(name, objects);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		return block.onPlacedAt(stack, player, world, x, y, z, 
				side, hitX, hitY, hitZ, metadata);
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
		return block.getMaxStackSize(stack);
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	public boolean useDefaultRender(ItemStack item)
	{
		return true;
	}
}