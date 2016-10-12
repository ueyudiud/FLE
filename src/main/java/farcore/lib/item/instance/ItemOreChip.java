package farcore.lib.item.instance;

import java.util.List;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOreChip extends ItemMulti
{
	public ItemOreChip()
	{
		super(FarCore.ID, MC.chip_ore);
		EnumItem.ore_chip.set(this);
	}

	@Override
	public void initalizeItems()
	{
		for(Mat material : Mat.filt(condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			LanguageManager.registerLocal(getTranslateName(templete), condition.getLocal(material));
			condition.registerOre(material, templete);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		FarCoreItemModelLoader.registerModel(this, new ResourceLocation(FarCore.ID, "ore_chip"));
		FarCoreItemModelLoader.registerSubmetaProvider(new ResourceLocation(FarCore.ID, "group/ore_chip/rock"), (ItemStack stack) -> "material:" + U.ItemStacks.setupNBT(stack, false).getString("rock"));
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
		for(Mat material : Mat.filt(condition))
		{
			subItems.add(createOreChip(1, material, M.stone));
		}
	}
}