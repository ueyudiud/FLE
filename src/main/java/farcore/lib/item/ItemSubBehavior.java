package farcore.lib.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import farcore.lib.item.behavior.IBehavior;
import farcore.lib.util.EnviornmentEntity;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
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
		hasSubtypes = true;
	}

	protected ItemSubBehavior(String modid, String name)
	{
		super(modid, name);
		hasSubtypes = true;
	}

	public void addSubItem(int id, String name, String localName, @Nullable IItemCapabilityProvider provider, IBehavior...behaviors)
	{
		if(idMap.containsKey(id) || idMap.containsValue(name))
			throw new RuntimeException("The id " + id + " or name '" + name + "' are already registered!");
		idMap.put(name, id);
		nameMap.put(id, name);
		if(behaviors.length > 0)
		{
			this.behaviors.put(id, ImmutableList.copyOf(behaviors));
		}
		if(provider != null)
		{
			providers.put(id, provider);
		}
		if(localName != null)
		{
			LanguageManager.registerLocal(getTranslateName(new ItemStack(this, 1, id)), localName);
		}
	}
	
	protected List<IBehavior> getBehavior(ItemStack stack)
	{
		return behaviors.getOrDefault(getDamage(stack), IBehavior.NONE);
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
				!entityItem.worldObj.isRemote)
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
			return false;
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if(!isItemUsable(itemStackIn))
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		try
		{
			ActionResult<ItemStack> result = new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
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
					result = new ActionResult<ItemStack>(result3, result2.getResult());
				}
			}
			return result;
		}
		catch(Exception exception)
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
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
			behavior.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!isItemUsable(stack))
			return;
		ItemStack stack2 = stack;
		if(this instanceof IUpdatableItem &&
				!entityIn.worldObj.isRemote)
		{
			stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityIn), stack);
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
			stack = behavior.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
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
		try
		{
			for(IBehavior behavior : getBehavior(stack))
			{
				behavior.onUsingTick(stack, player, count);
			}
		}
		catch(Exception exception)
		{
			;
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand)
	{
		if(!isItemUsable(stack))
			return false;
		try
		{
			boolean flag = false;
			for(IBehavior behavior : getBehavior(stack))
			{
				if(behavior.onRightClickEntity(stack, playerIn, target, hand))
				{
					flag = true;
				}
			}
			return flag;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return providers.getOrDefault(getDamage(stack), IItemCapabilityProvider.NONE).initCapabilities(stack, nbt);
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
			behavior.addInformation(stack, playerIn, unlocalizedList, advanced);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(int id : idMap.values())
		{
			try
			{
				createSubItem(id, subItems);
			}
			catch(Exception exception)
			{
				;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	protected void createSubItem(int meta, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this, 1, meta));
	}
}