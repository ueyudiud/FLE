package nebula.common.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.Log;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.enviornment.EnviornmentEntity;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSubBehavior extends ItemBase
{
	protected final Map<Integer, String> nameMap = new HashMap();
	protected final Map<String, Integer> idMap = new HashMap();
	private final Map<Integer, IItemCapabilityProvider> providers = new HashMap();
	private final Map<Integer, List<IBehavior>> behaviors = new HashMap();
	
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
	
	public void addSubItem(int id, String name, String localName, @Nullable IItemCapabilityProvider provider, IBehavior...behaviors)
	{
		if(this.idMap.containsKey(id) || this.idMap.containsValue(name))
			throw new RuntimeException("The id " + id + " or name '" + name + "' are already registered!");
		this.idMap.put(name, id);
		this.nameMap.put(id, name);
		if(behaviors.length > 0)
		{
			this.behaviors.put(id, ImmutableList.copyOf(behaviors));
		}
		if(provider != null)
		{
			this.providers.put(id, provider);
		}
		if(localName != null)
		{
			LanguageManager.registerLocal(getTranslateName(new ItemStack(this, 1, id)), localName);
		}
	}
	
	protected List<IBehavior> getBehavior(ItemStack stack)
	{
		return this.behaviors.getOrDefault(getDamage(stack), IBehavior.NONE);
	}
	
	protected boolean isItemUsable(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving)
	{
		if(!isItemUsable(stack))
			return false;
		try
		{
			boolean flag = false;
			for(IBehavior behavior : getBehavior(stack))
				if(behavior.onBlockDestroyed(stack, worldIn, state, pos, entityLiving))
				{
					flag = true;
				}
			return flag;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
	{
		if(!isItemUsable(item))
			return true;
		try
		{
			boolean flag = true;
			for(IBehavior behavior : getBehavior(item))
				if(!behavior.onDroppedByPlayer(item, player))
				{
					flag = false;
				}
			return flag;
		}
		catch(Exception exception)
		{
			return true;
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if(!isItemUsable(entityItem.getEntityItem()))
			return false;
		if(this instanceof IUpdatableItem &&
				!entityItem.world.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityItem), entityItem.getEntityItem());
			if(stack == null)
			{
				entityItem.setDead();
			}
			else if(stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if(stack.getItem() != this)
				return true;
		}
		try
		{
			boolean flag = false;
			for(IBehavior behavior : getBehavior(entityItem.getEntityItem()))
			{
				if(behavior.onEntityItemUpdate(entityItem))
				{
					flag = true;
				}
				if(entityItem.isDead) return false;
			}
			return flag;
		}
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if(!isItemUsable(itemStackIn))
			return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
		try
		{
			ActionResult<ItemStack> result = new ActionResult<>(EnumActionResult.PASS, itemStackIn);
			for(IBehavior behavior : getBehavior(itemStackIn))
			{
				ActionResult<ItemStack> result2;
				if((result2 = behavior.onItemRightClick(result.getResult(), worldIn, playerIn, hand)) != null &&
						result2.getType() != EnumActionResult.PASS)
				{
					EnumActionResult result3;
					if(result2.getType() == EnumActionResult.SUCCESS || result.getType() == EnumActionResult.SUCCESS)
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
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!isItemUsable(stack))
			return EnumActionResult.PASS;
		try
		{
			EnumActionResult result = EnumActionResult.PASS;
			for(IBehavior behavior : getBehavior(stack))
			{
				EnumActionResult result2;
				if((result2 = behavior.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)) != EnumActionResult.PASS)
				{
					if(result == EnumActionResult.SUCCESS)
					{
						continue;
					}
					result = result2;
				}
			}
			return result;
		}
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(!isItemUsable(stack))
			return EnumActionResult.PASS;
		try
		{
			EnumActionResult result = EnumActionResult.PASS;
			for(IBehavior behavior : getBehavior(stack))
			{
				EnumActionResult result2;
				if((result2 = behavior.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand)) != EnumActionResult.PASS)
				{
					if(result == EnumActionResult.SUCCESS)
					{
						continue;
					}
					result = result2;
				}
			}
			return result;
		}
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(!isItemUsable(stack))
			return false;
		try
		{
			boolean flag = false;
			for(IBehavior behavior : getBehavior(stack))
			{
				if(behavior.onLeftClickEntity(stack, player, entity))
				{
					flag = true;
				}
			}
			return flag;
		}
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if(!isItemUsable(stack))
			return;
		for(IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!isItemUsable(stack))
			return;
		ItemStack stack2 = stack;
		if(this instanceof IUpdatableItem &&
				!entityIn.world.isRemote)
		{
			try
			{
				stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityIn), stack);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
			if(entityIn instanceof EntityPlayer)
			{
				if(stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
			}
			if(stack.getItem() != this)
				return;
		}
		for(IBehavior behavior : getBehavior(stack))
		{
			try
			{
				stack = behavior.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
			if(entityIn instanceof EntityPlayer)
			{
				if(stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
				else if(stack != stack2)
				{
					((EntityPlayer) entityIn).inventory.setInventorySlotContents(itemSlot, stack);
				}
				if(stack.getItem() != this)
					return;
			}
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if(!isItemUsable(stack))
			return;
		for(IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.onUsingTick(stack, player, count);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand)
	{
		if(!isItemUsable(stack))
			return false;
		boolean flag = false;
		for(IBehavior behavior : getBehavior(stack))
		{
			try
			{
				if(behavior.onRightClickEntity(stack, playerIn, target, hand))
				{
					flag = true;
				}
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
				return false;
			}
		}
		return flag;
	}
	
	/**
	 * The ** forge, the meta data is initialized after capabilities initialized! These
	 * cause I can only make provider with lazy loading.
	 */
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new ICapabilityProvider()
		{
			ICapabilityProvider provider;
			
			private ICapabilityProvider getProvider()
			{
				if (this.provider == null)
				{
					this.provider = ItemSubBehavior.this.providers.getOrDefault(getBaseDamage(stack), IItemCapabilityProvider.NONE).initCapabilities(stack, nbt);
				}
				return this.provider;
			}
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return getProvider().hasCapability(capability, facing);
			}
			
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return getProvider().getCapability(capability, facing);
			}
		};
	}
	
	/**
	 * @param advanced Is information being display in more information mode. (F3+H to switch mode).
	 */
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		if(!isItemUsable(stack))
		{
			unlocalizedList.add("info.invalid");
			return;
		}
		for(IBehavior behavior : getBehavior(stack))
		{
			try
			{
				behavior.addInformation(stack, playerIn, unlocalizedList, advanced);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(int id : this.idMap.values())
		{
			try
			{
				createSubItem(id, subItems);
			}
			catch(Exception exception)
			{
				Log.catchingIfDebug(exception);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this, 1, meta));
	}
}