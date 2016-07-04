package farcore.alpha.item;

import java.util.List;
import java.util.Map;

import farcore.FarCoreSetup;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.item.behavior.IBehavior;
import farcore.enums.EnumDamageResource;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.recipe.ICraftingInventory;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSubBehavior<T extends ItemSubBehavior> extends ItemBase 
implements ICustomDamageItem
{
	protected final IRegister<IBehavior<T>> register;
	
	protected ItemSubBehavior(String unlocalized)
	{
		super(unlocalized);
		register = provideRegister();
		hasSubtypes = true;
	}
	protected ItemSubBehavior(String modid, String unlocalized)
	{
		super(modid, unlocalized);
		register = provideRegister();
		hasSubtypes = true;
	}
	
	protected Register<IBehavior<T>> provideRegister()
	{
		return new Register();
	}
	
	public boolean hasSubItem(String name)
	{
		return register.contain(name);
	}
	
	public boolean hasSubItem(int id)
	{
		return register.contain(id);
	}
	
	public boolean hasSubItem(ItemStack stack)
	{
		return hasSubItem(getDamage(stack));
	}
	
	void addSubItem(int id, String name, String local, IBehavior<T> itemInfo)
	{
		register.register(id, name, itemInfo);
		FarCoreSetup.lang.registerLocal(getUnlocalizedName(id) + ".name", local);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onLeftClickEntity((T) this, stack, player, entity);
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onRightClickEnity((T) this, stack, player, entity);
		}
		return super.itemInteractionForEntity(stack, player, entity);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
    		float hitX, float hitY, float hitZ)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onItemUseFirst((T) this, stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
    	return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x,
    		int y, int z, int side, float hitX, float hitY, float hitZ)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onItemUse((T) this, stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
    	return super.onItemUse(stack, player, world, x, y, z, side, hitX,
    			hitY, hitZ);
    }
	
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onItemRightClick((T) this, stack, player, world);
		}
		return super.onItemRightClick(stack, world, player);
	}
    
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int tick,
    		boolean flag)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			behavior.onInventoryItemUpdate((T) this, stack, world, entity, tick, flag);
		}
		super.onUpdate(stack, world, entity, tick, flag);
    }
    
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
    	ItemStack stack = entityItem.getEntityItem();
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			behavior.onEntityItemUpdate((T) this, stack, entityItem);
		}
    	return super.onEntityItemUpdate(entityItem);
    }
    
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			behavior.onPlayerUsingTick((T) this, stack, player, count);
		}
		super.onUsingTick(stack, player, count);
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			behavior.onPlayerStoppedUsing((T) this, stack, player, world, tick);
		}
		super.onPlayerStoppedUsing(stack, world, player, tick);
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onBlockStartBreak((T) this, stack, player, x, y, z);
		}
		return super.onBlockStartBreak(stack, x, y, z, player);
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x,
    		int y, int z, EntityLivingBase entity)
    {
		if(hasSubItem(stack))
		{
			IBehavior<T> behavior = register.get(getDamage(stack));
			return behavior.onBlockDestroyed((T) this, stack, entity, world, block, x, y, z); 
		}
    	return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
    }
	
    @Override
	public void damangeItem(ItemStack stack, float amount, EntityLivingBase user, EnumDamageResource resource)
	{
		stack.damageItem((int) Math.ceil(amount), user);
	}
    
	@Override
	public ItemStack getCraftedItem(ItemStack stack, ICraftingInventory crafting)
	{
		stack.stackSize--;
		return U.Inventorys.valid(stack);
	}
}