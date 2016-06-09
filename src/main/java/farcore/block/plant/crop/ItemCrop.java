package farcore.block.plant.crop;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.ItemBlockBase;
import farcore.lib.crop.CropCard;
import farcore.lib.crop.CropManager;
import farcore.util.LanguageManager;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemCrop extends ItemBlockBase
{
	public ItemCrop(Block block)
	{
		super(block, Boolean.TRUE);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerLocalizedName(LanguageManager manager)
	{
		super.registerLocalizedName(manager);
		for(CropCard card : CropManager.getRegister())
		{
			card.registerLocalizedName(manager);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
//		getCrop(stack).addInformation(list);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		try
		{
			return getCrop(stack).getIcon(stack);
		}
		catch(Exception exception)
		{
			return V.voidBlockIcon;
		}
	}
	
	public static String getDNA(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getString("dna");
	}
	
	public static CropCard getCrop(ItemStack stack)
	{
		return CropManager.getCrop(U.Inventorys.setupNBT(stack, false).getString("crop"));
	}
	
	public static int getGeneration(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getInteger("generation");
	}
	
	public static ItemStack setCrop(ItemStack stack, String dna, int generation, CropCard card)
	{
		NBTTagCompound nbt = U.Inventorys.setupNBT(stack, true);
		nbt.setString("crop", card.name());
		nbt.setString("dna", dna);
		nbt.setInteger("generation", generation);
		return stack;
	}
	
	public static ItemStack setCrop(ItemStack stack, String dna, int generation)
	{
		NBTTagCompound nbt = U.Inventorys.setupNBT(stack, true);
		nbt.setString("dna", dna);
		nbt.setInteger("generation", generation);
		return stack;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		if(!CropManager.getRegister().contain(damage))
		{
			super.setDamage(stack, damage);
			return;
		}
		U.Inventorys.setupNBT(stack, true).setString("crop", CropManager.getRegister().name(damage));
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("crop"))
		{
			return super.getDamage(stack);
		}
		return CropManager.getRegister().id(U.Inventorys.setupNBT(stack, false).getString("crop"));
	}
}