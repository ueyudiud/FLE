/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.util.ItemStacks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTreeLog extends ItemMulti
{
	public ItemTreeLog()
	{
		super(FarCore.ID, MC.log_cutted);
		setDisableRegisterToOreDict();
		this.enableChemicalFormula = false;
		EnumItem.log.set(this);
	}
	
	@Override
	public void postInitalizedItems()
	{
		for(Mat material : Mat.filt(this.condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			LanguageManager.registerLocal(getTranslateName(templete), this.condition.getLocal(material));
			this.condition.registerOre(material, templete);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.filt(this.condition))
		{
			ItemStack stack = new ItemStack(itemIn, 1, material.id);
			setLogSize(stack, 16);
			subItems.add(stack);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		unlocalizedList.add("info.tree.log.length", getLogSize(stack));
	}
	
	public static void setLogSize(ItemStack stack, int size)
	{
		ItemStacks.getOrSetupNBT(stack, true).setShort("length", (short) size);
	}
	
	public static int getLogSize(ItemStack stack)
	{
		return ItemStacks.getOrSetupNBT(stack, false).getShort("length");
	}
}