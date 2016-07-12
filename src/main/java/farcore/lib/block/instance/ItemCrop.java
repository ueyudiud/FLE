package farcore.lib.block.instance;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.crop.ICrop;
import farcore.lib.material.Mat;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemCrop extends ItemBlockBase
{
	public ItemCrop(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean F3H)
	{
		super.addInformation(stack, player, list, F3H);
//		getCrop(stack).addInformation(list);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		return FarCore.voidBlockIcon;
	}
	
	public static String getDNA(ItemStack stack)
	{
		return U.ItemStacks.setupNBT(stack, false).getString("dna");
	}
	
	public static ICrop getCrop(ItemStack stack)
	{
		return Mat.register.get(U.ItemStacks.setupNBT(stack, false).getString("crop")).crop;
	}
	
	public static int getGeneration(ItemStack stack)
	{
		return U.ItemStacks.setupNBT(stack, false).getInteger("generation");
	}
	
	public static ItemStack setCrop(ItemStack stack, String dna, int generation, ICrop crop)
	{
		NBTTagCompound nbt = U.ItemStacks.setupNBT(stack, true);
		nbt.setString("crop", crop.getRegisteredName());
		nbt.setString("dna", dna);
		nbt.setInteger("generation", generation);
		return stack;
	}
	
	public static ItemStack setCrop(ItemStack stack, String dna, int generation)
	{
		NBTTagCompound nbt = U.ItemStacks.setupNBT(stack, true);
		nbt.setString("dna", dna);
		nbt.setInteger("generation", generation);
		return stack;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		if(!Mat.register.contain(damage))
		{
			super.setDamage(stack, damage);
			return;
		}
		U.ItemStacks.setupNBT(stack, true).setString("crop", Mat.register.name(damage));
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("crop"))
		{
			return super.getDamage(stack);
		}
		return Mat.register.id(stack.getTagCompound().getString("crop"));
	}
}