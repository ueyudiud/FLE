/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.items;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import nebula.common.LanguageManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOreChip extends ItemMulti
{
	public ItemOreChip()
	{
		super(FarCore.ID, MC.chip_ore);
		setDisableRegisterToOreDict();
		EnumItem.ore_chip.set(this);
	}
	
	@Override
	public void postInitalizedItems()
	{
		for (Mat material : Mat.filt(this.condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			LanguageManager.registerLocal(getTranslateName(templete), this.condition.getLocal(material));
			this.condition.registerOre(material, templete);
		}
	}
	
	public static ItemStack createOreChip(int size, Mat ore, Mat rock)
	{
		ItemStack stack = new ItemStack(EnumItem.ore_chip.item, size, ore.id);
		NBTTagCompound nbt;
		stack.setTagCompound(nbt = new NBTTagCompound());
		nbt.setString("rock", rock.name);
		return stack;
	}
	
	public static String getRockString(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getString("rock") : "";
	}
	
	public static Mat getRock(ItemStack stack)
	{
		return stack.hasTagCompound() ? Mat.material(stack.getTagCompound().getString("rock"), M.stone) : M.stone;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (Mat material : Mat.filt(this.condition))
		{
			subItems.add(createOreChip(1, material, M.stone));
		}
	}
}
