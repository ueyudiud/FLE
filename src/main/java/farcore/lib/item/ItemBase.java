package farcore.lib.item;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IRegisteredNameable
{
	private static List<ItemBase> list = new ArrayList();

	/**
	 * Called when all others object(fluids, blocks, configurations, materials, etc)
	 * are already initialized.
	 */
	public static void post()
	{
		Log.info("Far core reloading items...");
		for(ItemBase item : list)
		{
			item.postInitalizedItems();
		}
		list = null;
	}

	protected String localized;
	protected String unlocalized;
	protected String unlocalizedTooltip;
	
	protected ItemBase(String name)
	{
		this(name, null);
	}
	protected ItemBase(String modid, String name)
	{
		this(modid, name, null, null);
	}
	protected ItemBase(String modid, String name, String unlocalizedTooltip, String localTooltip)
	{
		unlocalized = modid + "." + name;
		if(unlocalizedTooltip != null)
		{
			this.unlocalizedTooltip = "fle." + unlocalizedTooltip;
			LanguageManager.registerLocal(unlocalizedTooltip, localTooltip);
		}
		U.Mod.registerItem(this, modid, name);
		/**
		 * Added item into post-initialized list.
		 */
		list.add(this);
	}

	public void postInitalizedItems()
	{

	}
	
	@Override
	public final Item setUnlocalizedName(String unlocalizedName)
	{
		return this;
	}

	@Override
	public final String getUnlocalizedName()
	{
		return unlocalized;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return hasSubtypes ?
				getUnlocalizedName() + "@" + getDamage(stack) :
					getUnlocalizedName();
	}
	
	protected String getTranslateName(ItemStack stack)
	{
		return getUnlocalizedName(stack) + ".name";
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return LanguageManager.translateToLocal(getTranslateName(stack), getTranslateObject(stack));
	}
	
	protected Object[] getTranslateObject(ItemStack stack)
	{
		return new Object[0];
	}
	
	@Override
	public String getRegisteredName()
	{
		return REGISTRY.getNameForObject(this).toString();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		addInformation(stack, playerIn, new UnlocalizedList(tooltip), advanced);
	}

	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		if(unlocalizedTooltip != null)
		{
			unlocalizedList.add(unlocalizedTooltip);
		}
	}

	/**
	 * The offset meta given by item nbt. Use to divide
	 * the sub item of each material.
	 * @param stack
	 * @return
	 */
	public int getStackMetaOffset(ItemStack stack)
	{
		return 0;
	}
	
	public int getBaseDamage(ItemStack stack)
	{
		return super.getDamage(stack);
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		return (getStackMetaOffset(stack) << 15) | super.getDamage(stack);
	}
}