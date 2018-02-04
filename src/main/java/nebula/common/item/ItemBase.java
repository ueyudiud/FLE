/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.Log;
import nebula.client.util.Client;
import nebula.client.util.IRenderRegister;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.capability.CapabilityProviderItem;
import nebula.common.nbt.INBTCompoundReader;
import nebula.common.nbt.INBTCompoundWriter;
import nebula.common.util.Game;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base item type provider by Nebula.
 * <p>
 * Contains some useful method for application.
 * 
 * @author ueyudiud
 */
public class ItemBase extends Item implements IRegisteredNameable, IRenderRegister
{
	private static List<ItemBase> list = new ArrayList<>();
	
	/**
	 * Called when all others object(fluids, blocks, configurations, materials,
	 * etc) are already initialized.
	 */
	public static void post()
	{
		Log.info("Reloading items...");
		for (ItemBase item : list)
		{
			item.postInitalizedItems();
		}
		Log.info("Reloaded {} items.", list.size());
		list = null;
	}
	
	protected final String	modid;
	protected String		localized;
	protected String		unlocalized;
	protected String		unlocalizedTooltip;
	
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
		if (list == null) throw new RuntimeException("The item has already post registered, please create new item before pre-init.");
		this.modid = modid;
		this.unlocalized = modid + "." + name;
		if (unlocalizedTooltip != null)
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
	
	/**
	 * Called when item are all already initialized.
	 * <p>
	 * For get some extra data which requires lazy loading.
	 */
	public void postInitalizedItems()
	{
		
	}
	
	/**
	 * Already set in constructor, this method is useless.
	 */
	@Override
	@Deprecated
	public final Item setUnlocalizedName(String unlocalizedName)
	{
		return this;
	}
	
	/**
	 * Get base unlocalized name for item stack.
	 */
	@Override
	public final String getUnlocalizedName()
	{
		return this.unlocalized;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.hasSubtypes ? getUnlocalizedName() + "@" + getDamage(stack) : getUnlocalizedName();
	}
	
	protected String getTranslateName(ItemStack stack)
	{
		return getUnlocalizedName(stack) + ".name";
	}
	
	/**
	 * Get display name for item stack.
	 */
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
		list.list();// Build list.
	}
	
	/**
	 * Get stack information when ItemStack tip rendering on HUD.
	 * 
	 * @param stack the item stack.
	 * @param unlocalizedList added information to here, this list provide some
	 *            helper method to localize tips.
	 * @param advanced <tt>true</tt> means information will be displayed in more
	 *            information mode. (F3+H to switch mode).
	 */
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		if (this.unlocalizedTooltip != null)
		{
			unlocalizedList.addToolTip(this.unlocalizedTooltip);
		}
	}
	
	/**
	 * The offset meta given by item NBT. Use to divide the sub item of each
	 * material.
	 * 
	 * @param stack the stack.
	 * @return the offset meta of stack.
	 */
	public int getStackMetaOffset(ItemStack stack)
	{
		return 0;
	}
	
	/**
	 * Get base meta given by meta from item stack.
	 * 
	 * @param stack
	 * @return
	 * @see net.minecraft.item.Item#getDamage(ItemStack)
	 */
	public int getBaseDamage(ItemStack stack)
	{
		return super.getDamage(stack);
	}
	
	/**
	 * Get real damage of item stack, which is combined offset meta and base
	 * meta.
	 */
	@Override
	public int getDamage(ItemStack stack)
	{
		return getStackMetaOffset(stack) << 15 | super.getDamage(stack);
	}
	
	/**
	 * Get nebula overridden font render to render item name and tooltips, which
	 * contains custom letter rendering.
	 * 
	 * @return
	 * @see nebula.client.util.Client#getFontRender()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return Client.getFontRender();
	}
	
	class CapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>
	{
		ItemStack			stack;
		ICapabilityProvider	provider;
		NBTTagCompound		nbt;
		
		CapabilityProvider(ItemStack stack)
		{
			this.stack = stack;
		}
		
		private void initalize()
		{
			if (this.provider == null)
			{
				if ((this.provider = createProvider(this.stack)) != null)
				{
					if (this.nbt != null)
					{
						deserializeNBT(this.nbt);
						this.nbt = null;
					}
				}
				else
				{
					this.provider = this;
				}
				
			}
		}
		
		@Override
		public NBTTagCompound serializeNBT()
		{
			if (this.provider instanceof INBTCompoundWriter<?>)
				return ((INBTCompoundWriter) this.provider).writeToNBT(new NBTTagCompound());
			else
				return this.nbt != null ? this.nbt : new NBTTagCompound();
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			if (this.provider instanceof INBTCompoundReader<?>)
				((INBTCompoundReader) this.provider).readFromNBT(nbt);
			else
				this.nbt = nbt;
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			initalize();
			return this.provider == this ? false : this.provider.hasCapability(capability, facing);
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			initalize();
			return this.provider == this ? null : this.provider.getCapability(capability, facing);
		}
	}
	
	/**
	 * The ** forge, the meta data is initialized after capabilities
	 * initialized! These cause I can only make provider with lazy loading.
	 * <p>
	 * The item capability to provide a holder to contain extra data during the
	 * life of ItemStack, and it needed to be initialize BEFORE item stack being
	 * used.
	 * <p>
	 * 
	 * @see net.minecraftforge.common.capabilities.ICapabilityProvider
	 * @see net.minecraft.item.Item#initCapabilities(ItemStack, NBTTagCompound)
	 */
	@Override
	public final ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return !hasCapability() ? null : new CapabilityProvider(stack);
	}
	
	protected boolean hasCapability()
	{
		return false;
	}
	
	protected CapabilityProviderItem createProvider(ItemStack stack)
	{
		return null;
	}
}
