package fle.tool.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.FleAPI;
import flapi.block.old.ItemFleBlock;
import flapi.util.FleValue;
import fle.core.init.IB;
import fle.core.te.TileEntityDitch;
import fle.tool.DitchInfo;

public class ItemDitch extends ItemFleBlock
{
	public ItemDitch(Block aBlock)
	{
		super(aBlock);
		setTextureName(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack aStack)
	{
		return super.getUnlocalizedName();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack aStack)
	{
		return FleAPI.langManager.translateToLocal(aStack.getUnlocalizedName() + ".name", getMaterial(aStack).getBlock().getDisplayName());
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, block, metadata, 3))
		{
			return true;
		}
		
		if (world.getTileEntity(x, y, z) instanceof TileEntityDitch)
		{
			((TileEntityDitch) world.getTileEntity(x, y, z)).setup(getMaterial(stack), getSize(stack));
		}
		
		if (world.getBlock(x, y, z) == block)
		{
			block.onBlockPlacedBy(world, x, y, z, player, stack);
			block.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}
	
	public static DitchInfo getMaterial(ItemStack aStack)
	{
		return DitchInfo.register.get(aStack.getItemDamage());
	}
	
	public static int getSize(ItemStack aStack)
	{
		return setupNBT(aStack).getInteger("Size");
	}
	
	public static void setMaterial(ItemStack aStack, DitchInfo aInfo)
	{
		aStack.setItemDamage(DitchInfo.register.serial(aInfo));
	}
	
	public static void setSize(ItemStack aStack, int size)
	{
		setupNBT(aStack).setInteger("Size", size);
	}

	public static ItemStack a(DitchInfo info, int capacity)
	{
		return a(1, info, capacity);
	}

	public static ItemStack a(int size, DitchInfo info, int capacity)
	{
		ItemStack ret = new ItemStack(IB.ditch, size);
		setMaterial(ret, info);
		setSize(ret, capacity);
		return ret;
	}
}