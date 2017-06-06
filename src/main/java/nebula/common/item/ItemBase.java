package nebula.common.item;

import java.util.ArrayList;
import java.util.List;

import nebula.Log;
import nebula.client.util.Client;
import nebula.client.util.IRenderRegister;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.util.Game;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IRegisteredNameable, IRenderRegister
{
	private static List<ItemBase> list = new ArrayList<>();
	
	/**
	 * Called when all others object(fluids, blocks, configurations, materials, etc)
	 * are already initialized.
	 */
	public static void post()
	{
		Log.info("Nebula reloading items...");
		for(ItemBase item : list)
		{
			item.postInitalizedItems();
		}
		list = null;
	}
	
	protected final String modid;
	protected String localized;
	protected String unlocalized;
	protected String unlocalizedTooltip;
	
	protected ItemBase(String name)
	{
		this(Game.getActiveModID(), name);
	}
	protected ItemBase(String modid, String name)
	{
		this(modid, name, null, null);
	}
	protected ItemBase(String modid, String name, String unlocalizedTooltip, String localTooltip)
	{
		if(list == null)
			throw new RuntimeException("The item has already post registered, please create new item before pre-init.");
		this.modid = modid;
		this.unlocalized = modid + "." + name;
		if(unlocalizedTooltip != null)
		{
			this.unlocalizedTooltip = modid + "." + unlocalizedTooltip;
			LanguageManager.registerLocal(modid + "." + unlocalizedTooltip, localTooltip);
		}
		Game.registerItem(this, modid, name);
		/**
		 * Added item into post-initialized list.
		 */
		list.add(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		
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
		return this.unlocalized;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.hasSubtypes ?
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
		UnlocalizedList list = new UnlocalizedList(tooltip);
		addInformation(stack, playerIn, list, advanced);
		list.list();//Build list.
	}
	
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		if(this.unlocalizedTooltip != null)
		{
			unlocalizedList.addToolTip(this.unlocalizedTooltip);
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return Client.getFontRender();
	}
}