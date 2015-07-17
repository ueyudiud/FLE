package fla.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import fla.api.item.ITreeLog;
import fla.api.util.FlaValue;
import fla.api.util.InfoBuilder;

public class ItemLog extends ItemBase implements ITreeLog
{
	public ItemLog()
	{
		this.ib = new TreeInfoBuilder();
		this.setHasSubtypes(true);
		this.maxStackSize = 1;
	}
	
	@Override
	public void registerIcons(IIconRegister register) 
	{
		this.icons = new IIcon[6];
		this.icons[0] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/1");
		this.icons[1] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/2");
		this.icons[2] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/3");
		this.icons[3] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/4");
		this.icons[4] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/5");
		this.icons[5] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":logs/6");
	}
	
	@Override
	public IIcon getIconFromDamage(int i) 
	{
		return icons[i % icons.length];
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab,
			List list)
	{
		list.add(setUpLogLength(new ItemStack(item, 1, 0), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 0), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 0), 15));
		list.add(setUpLogLength(new ItemStack(item, 1, 1), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 1), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 1), 15));
		list.add(setUpLogLength(new ItemStack(item, 1, 2), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 2), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 2), 15));
		list.add(setUpLogLength(new ItemStack(item, 1, 3), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 3), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 3), 15));
		list.add(setUpLogLength(new ItemStack(item, 1, 4), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 4), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 4), 15));
		list.add(setUpLogLength(new ItemStack(item, 1, 5), 4));
		list.add(setUpLogLength(new ItemStack(item, 1, 5), 9));
		list.add(setUpLogLength(new ItemStack(item, 1, 5), 15));
	}

	@Override
	public int getLogLength(ItemStack itemstack) 
	{
		try
		{
			return itemstack.getTagCompound().getShort("LogLength");
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public ItemStack getLogDrop(ItemStack itemstack) 
	{
		int size = getLogLength(itemstack);
		if(size > 64)
		{
			ItemStack ret = createStandardLog(itemstack.getItemDamage(), size / 2);
			ret.stackSize = 2;
			return ret;
		}
		return itemstack.getItemDamage() < 3 ? new ItemStack(Blocks.log, size, itemstack.getItemDamage()) : new ItemStack(Blocks.log2, size, itemstack.getItemDamage() - 4);
	}

	@Override
	public ItemStack getBarkDrop(ItemStack itemstack) 
	{
		return null;
	}

	@Override
	public ItemStack createStandardLog(int meta, int length) 
	{
		return setUpLogLength(new ItemStack(this, 1, meta), length);
	}

	@Override
	public ItemStack createStandardLog(Block block, int meta, int length) 
	{
		return block == Blocks.log ? createStandardLog(meta % 4, length) : block == Blocks.log2 ? createStandardLog((meta % 4) + 4, length) : null;
	}
	
	public static ItemStack setUpLogLength(ItemStack itemstack, int length)
	{
		ItemStack ret = itemstack.copy();
		if(!ret.hasTagCompound())
		{
			ret.stackTagCompound = new NBTTagCompound();
		}
		ret.stackTagCompound.setShort("LogLength", (short)length);
		return ret;
	}
	
	public class TreeInfoBuilder implements InfoBuilder<ItemStack>
	{
		@Override
		public List<String> getInfo(ItemStack t) 
		{
			List<String> ret = new ArrayList();
			if(t.hasTagCompound())
			{
				ret.add(StatCollector.translateToLocal("info.log.length") + ":" + t.getTagCompound().getShort("LogLength"));
			}
			else
			{
				ret.add("This log don't have NBT! Don't use it or your game will be CRUSH.");
			}
			return ret;
		}
		
	}
}
