package farcore.lib.item.instance;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.lib.item.ItemBase;
import farcore.lib.material.Mat;
import farcore.lib.util.INamedIconRegister;
import farcore.util.U;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemTreeLog extends ItemBase
{
	public ItemTreeLog()
	{
		super(FarCore.ID, "tree.log");
		hasSubtypes = true;
		EnumItem.log.set(this);
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcons(INamedIconRegister register)
	{
		for(Mat material : Mat.register)
		{
			if(material.hasTree)
			{
				register.push(material);
				register.registerIcon(null, material.modid + getIconString() + "/" + material.name);
				register.pop();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass)
	{
		register.push(Mat.register.get(getDamage(stack)));
		IIcon icon = register.getIconFromName(null);
		register.pop();
		return icon;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(Mat material : Mat.register)
		{
			if(material.hasTree)
			{
				list.add(new ItemStack(item, 1, material.id));
			}
		}
	}
	
	public static ItemStack setLogSize(ItemStack stack, int length)
	{
		U.ItemStacks.setupNBT(stack, true).setShort("length", (short) length);
		return stack;
	}
	
	public static int getLogSize(ItemStack stack)
	{
		return U.ItemStacks.setupNBT(stack, false).getShort("length");
	}
}