/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import nebula.Log;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.environment.EnviornmentEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A item collection with different function items contain.
 * <p>
 * For each sub item, use number id-string id pair to distinguish and searching
 * item.
 * <p>
 * 
 * @author ueyudiud
 * @see #addSubItem(int, String, String, IItemCapabilityProvider, IBehavior...)
 */
public class ItemSubBehavior extends ItemBase
{
	/** The number ID to name map. */
	protected final BiMap<Integer, String>		nameMap		= HashBiMap.create();
	/** The name to number ID map. */
	protected final BiMap<String, Integer>		idMap		= this.nameMap.inverse();
	/**
	 * The map contain behaviors of sub items. Use
	 * {@link #getBehavior(ItemStack)} to get behaviors.
	 * <p>
	 * Do not get this field for it may remove in the feature, because it is
	 * number ID mapping.
	 */
	private final Map<Integer, List<IBehavior>>	behaviors	= new HashMap<>();
	
	protected ItemSubBehavior(String name)
	{
		super(name);
		this.hasSubtypes = true;
	}
	
	protected ItemSubBehavior(String modid, String name)
	{
		super(modid, name);
		this.hasSubtypes = true;
	}
	
	/**
	 * Add a sub item to item collection.
	 * 
	 * @param id the number id of item, it should be range in [0, 32768).
	 * @param name the string id of item.
	 * @param localName the localize name for item in English(US). If input is
	 *            <tt>null</tt>, the initializer will not register local name.
	 * @param provider the item capability provider. See
	 *            {@link #initCapabilities(ItemStack, NBTTagCompound)}
	 * @param behaviors the behaviors list of sub item.
	 */
	public void addSubItem(int id, @Nonnull String name, @Nullable String localName, IBehavior...behaviors)
	{
		if (this.idMap.containsKey(name) || this.idMap.containsValue(id))
			throw new RuntimeException("The id " + id + " or name '" + name + "' are already registered!");
		this.nameMap.put(id, name);
		if (behaviors.length > 0)
		{
			this.behaviors.put(id, ImmutableList.copyOf(behaviors));
		}
		if (localName != null)
		{
			LanguageManager.registerLocal(getTranslateName(new ItemStack(this, 1, id)), localName);
		}
	}
	
	/**
	 * Get sub item by name with 1 stack size.
	 * 
	 * @param name the name of sub item
	 * @see #getSubItem(String, int)
	 */
	public final ItemStack getSubItem(String name)
	{
		return getSubItem(name, 1);
	}
	
	/**
	 * Get sub item by name.
	 * 
	 * @param name the name of sub item.
	 * @param size the stack size.
	 * @return the stack created, or <tt>null</tt> if no sub item with this
	 *         name.
	 */
	public ItemStack getSubItem(String name, int size)
	{
		Integer value = this.idMap.get(name);
		return value == null ? null : new ItemStack(this, size, value.intValue());
	}
	
	protected List<IBehavior> getBehavior(ItemStack stack)
	{
		return this.behaviors.getOrDefault(getBaseDamage(stack), IBehavior.NONE);
	}
	
	/**
	 * Use to checking whether ItemStack has valid data. It is not suggested to
	 * using argument if <tt>false</tt> is returned.
	 * 
	 * @param stack the stack to check.
	 * @return <tt>true</tt> for item is usable, and it is not suggested to
	 *         using input stack if <tt>false</tt> is returned.
	 */
	protected boolean isItemUsable(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!isItemUsable(stack)) return false;
		try
		{
			boolean flag = false;
			for (IBehavior behavior : getBehavior(stack))
				if (behavior.onBlockDestroyed(stack, worldIn, state, pos, entityLiving))
				{
					flag = true;
				}
			return flag;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
	{
		if (!isItemUsable(item)) return true;
		try
		{
			boolean flag = true;
			for (IBehavior behavior : getBehavior(item))
				if (!behavior.onDroppedByPlayer(item, player))
				{
					flag = false;
				}
			return flag;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return true;
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if (!isItemUsable(entityItem.getEntityItem())) return false;
		if (this instanceof IUpdatableItem && !entityItem.world.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityItem), entityItem.getEntityItem());
			if (stack == null)
			{
				entityItem.setDead();
			}
			else if (stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if (stack.getItem() != this) return true;
		}
		try
		{
			boolean flag = false;
			for (IBehavior behavior : getBehavior(entityItem.getEntityItem()))
			{
				if (behavior.onEntityItemUpdate(entityItem))
				{
					flag = true;
				}
				if (entityItem.isDead) return false;
			}
			return flag;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!isItemUsable(itemStackIn)) return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
		try
		{
			ActionResult<ItemStack> result = new ActionResult<>(EnumActionResult.PASS, itemStackIn);
			for (IBehavior behavior : getBehavior(itemStackIn))
			{
				ActionResult<ItemStack> result2;
				if ((result2 = behavior.onItemRightClick(result.getResult(), worldIn, playerIn, hand)) != null && result2.getType() != EnumActionResult.PASS)
				{
					EnumActionResult result3;
					if (result2.getType() == EnumActionResult.SUCCESS || result.getType() == EnumActionResult.SUCCESS)
					{
						result3 = EnumActionResult.SUCCESS;
					}
					else
					{
						result3 = EnumActionResult.FAIL;
					}
					result = new ActionResult<>(result3, result2.getResult());
				}
			}
			return result;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!isItemUsable(stack)) return EnumActionResult.PASS;
		try
		{
			EnumActionResult result = EnumActionResult.PASS;
			for (IBehavior behavior : getBehavior(stack))
			{
				EnumActionResult result2;
				if ((result2 = behavior.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)) != EnumActionResult.PASS)
				{
					if (result == EnumActionResult.SUCCESS)
					{
						continue;
					}
					result = result2;
				}
			}
			return result;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!isItemUsable(stack)) return EnumActionResult.PASS;
		try
		{
			EnumActionResult result = EnumActionResult.PASS;
			for (IBehavior behavior : getBehavior(stack))
			{
				EnumActionResult result2;
				if ((result2 = behavior.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand)) != EnumActionResult.PASS)
				{
					if (result == EnumActionResult.SUCCESS)
					{
						continue;
					}
					result = result2;
				}
			}
			return result;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (!isItemUsable(stack)) return false;
		try
		{
			boolean flag = false;
			for (IBehavior behavior : getBehavior(stack))
			{
				if (behavior.onLeftClickEntity(stack, player, entity))
				{
					flag = true;
				}
			}
			return flag;
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (!isItemUsable(stack)) return;
		for (IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!isItemUsable(stack)) return;
		ItemStack stack2 = stack;
		if (this instanceof IUpdatableItem && !entityIn.world.isRemote)
		{
			try
			{
				stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityIn), stack);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
			if (entityIn instanceof EntityPlayer)
			{
				if (stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
			}
			if (stack.getItem() != this) return;
		}
		for (IBehavior behavior : getBehavior(stack))
		{
			try
			{
				stack = behavior.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
			if (entityIn instanceof EntityPlayer)
			{
				if (stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
				else if (stack != stack2)
				{
					((EntityPlayer) entityIn).inventory.setInventorySlotContents(itemSlot, stack);
				}
				if (stack.getItem() != this) return;
			}
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (!isItemUsable(stack)) return;
		for (IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.onUsingTick(stack, player, count);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if (!isItemUsable(stack)) return false;
		boolean flag = false;
		for (IBehavior behavior : getBehavior(stack))
		{
			try
			{
				if (behavior.onRightClickEntity(stack, playerIn, target, hand))
				{
					flag = true;
				}
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
				return false;
			}
		}
		return flag;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		if (!isItemUsable(stack))
		{
			// To show this item may has broken data, it is suggested player do
			// not use this item in game.
			unlocalizedList.add("info.invalid");
			return;
		}
		for (IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.addInformation(stack, playerIn, unlocalizedList, advanced);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (int id : ImmutableSortedSet.copyOf(this.idMap.values()))
		{
			try
			{
				createSubItem(id, subItems);
			}
			catch (Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	/**
	 * For each sub item should display on creative tab, for iterating helper in
	 * {@link #getSubItems(Item, CreativeTabs, List)} and called this method
	 * with each sub item id.
	 * 
	 * @param meta the adding meta.
	 * @param subItems the sub items list, added sub item here.
	 */
	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this, 1, meta));
	}
}
