package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.util.UnlocalizedList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeed extends ItemMulti
{
	public ItemSeed()
	{
		super(FarCore.ID, MC.seed);
		EnumItem.seed.set(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.register)
		{
			if(condition.isBelongTo(material))
			{
				ItemStack stack = applySeed(1, material, 0, material.crop.makeNativeDNA());
				subItems.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		unlocalizedList.add("info.crop.type", getMaterialFromItem(stack).crop.getLocalName(getDNAFromStack(stack)));
		unlocalizedList.add("info.crop.generation", getGenerationFromStack(stack));
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
	}
	
	public static ItemStack applySeed(int size, Mat material, int generation, String dna)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("generation", (short) generation);
		nbt.setString("dna", dna);
		return new ItemStack(EnumItem.seed.item, size, material.id, nbt);
	}
	
	public static String getDNAFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? "" : stack.getTagCompound().getString("dna");
	}
	
	public static int getGenerationFromStack(ItemStack stack)
	{
		return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getShort("generation");
	}
}