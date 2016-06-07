package farcore.item;

import farcore.enums.EnumDamageResource;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemProperty;
import farcore.lib.recipe.ICraftingInventory;
import farcore.util.U;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemSubDamagable extends ItemSubBehaviorII implements ICustomDamageItem
{
	private int craftingDamage;

	protected ItemSubDamagable(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSubDamagable(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemSubDamagable(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public ItemSubDamagable setCraftingDamage(int craftingDamage)
	{
		this.craftingDamage = craftingDamage;
		return this;
	}
	
	@Override
	public void addSubItem(int id, String name, String local, IBehavior behavior, IItemProperty property, IItemIconInfo iconInfo)
	{
		super.addSubItem(id, name, local, behavior, property, iconInfo);
	}
	
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, IItemIconInfo iconInfo)
	{
		super.addSubItem(id, name, local, itemInfo, iconInfo);
	}
	
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		return 1;
	}
	
	public static void setCustomDamage(ItemStack stack, float damage)
	{
		U.Inventorys.setupNBT(stack, true).setFloat("damage", damage);
	}
	
	public static float getCustomDamage(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getFloat("damage");
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getCustomDamage(stack) != 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) getCustomDamage(stack) / (double) getMaxCustomDamgae(stack);
	}

	@Override
	public void damangeItem(ItemStack stack, float amount, EntityLivingBase user, EnumDamageResource resource) 
	{
		float damage = getCustomDamage(stack);
		int max = getMaxCustomDamgae(stack);
		float ret = applyDamage(stack, amount, damage, max, user, resource);
		if(ret >= max)
		{
			stack.stackSize--;
			setCustomDamage(stack, 0);
		}
		else
		{
			setCustomDamage(stack, ret);
		}
	}
	
	protected float applyDamage(ItemStack target, float amount, float damage, int max, EntityLivingBase user, EnumDamageResource resource)
	{
		return amount + damage;
	}

	@Override
	public ItemStack getCraftedItem(ItemStack stack, ICraftingInventory crafting)
	{
		damangeItem(stack, craftingDamage, null, EnumDamageResource.CRAFT);
		return U.Inventorys.valid(stack);
	}
}