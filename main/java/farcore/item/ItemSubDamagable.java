package farcore.item;

import farcore.enums.EnumDamageResource;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemProperty;
import farcore.util.U;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class ItemSubDamagable extends ItemSubBehavior implements ICustomDamageItem
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
	public void addSubItem(int id, String name, IBehavior behavior, IItemProperty property, IItemIconInfo iconInfo)
	{
		super.addSubItem(id, name, behavior, property, iconInfo);
	}
	
	@Override
	public void addSubItem(int id, String name, IItemInfo itemInfo, IItemIconInfo iconInfo)
	{
		super.addSubItem(id, name, itemInfo, iconInfo);
	}
	
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		return 1;
	}
	
	protected void setCustomDamage(ItemStack stack, int damage)
	{
		U.Inventorys.setupNBT(stack, true).setInteger("damage", damage);
	}
	
	protected int getCustomDamage(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getInteger("damage");
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
	public void damangeItem(ItemStack stack, int amount, EntityLivingBase user, EnumDamageResource resource) 
	{
		int damage = getCustomDamage(stack);
		int max = getMaxCustomDamgae(stack);
		int ret = applyDamage(stack, amount, damage, max, user, resource);
		if(ret >= max)
		{
			stack.stackSize --;
			setCustomDamage(stack, 0);
		}
		else
		{
			setCustomDamage(stack, ret);
		}
	}
	
	protected int applyDamage(ItemStack target, int amount, int damage, int max, EntityLivingBase user, EnumDamageResource resource)
	{
		return amount + damage;
	}

	@Override
	public ItemStack getCraftedItem(ItemStack stack, InventoryCrafting crafting, int x, int y)
	{
		damangeItem(stack, craftingDamage, null, EnumDamageResource.CRAFT);
		return U.Inventorys.valid(stack);
	}
}