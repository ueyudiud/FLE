package farcore.item;

import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.IItemInfo;
import farcore.util.FleLog;
import fle.api.util.V;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSimpleBehavior extends ItemBase
{
	private IItemInfo behavior;
	
	protected ItemSimpleBehavior(String unlocalized, IItemInfo behavior)
	{
		super(unlocalized);
		this.behavior = behavior;
	}
	protected ItemSimpleBehavior(String unlocalized, String unlocalizedTooltip, IItemInfo behavior)
	{
		super(unlocalized, unlocalizedTooltip);
		this.behavior = behavior;
	}
	protected ItemSimpleBehavior(String modid, String unlocalized, String unlocalizedTooltip, IItemInfo behavior)
	{
		super(modid, unlocalized, unlocalizedTooltip);
		this.behavior = behavior;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
	{
		try
		{
			return behavior.onBlockDestroyed(stack, world, block, x, y, z, entity);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
	{
		try
		{
			return behavior.onBlockStartBreak(stack, player.getEntityWorld(), x, y, z, player);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		try
		{
			return behavior.onEaten(stack, world, player);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return stack;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		try
		{
			return behavior.onEntityItemUpdate(entityItem.getEntityItem(), entityItem);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		try
		{
			return behavior.onItemRightClick(stack, world, player);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return stack;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		try
		{
			return behavior.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		try
		{
			return behavior.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		try
		{
			return behavior.onLeftClickEntity(stack, player, entity);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		try
		{
			behavior.onUsingTick(stack, player, count);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		try
		{
			behavior.onPlayerStoppedUsing(stack, world, player, tick);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag)
	{
		try
		{
			behavior.onUpdate(stack, world, entity, tick, flag);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		try
		{
			return behavior.canHarvestBlock(block, stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return false;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int metadata)
	{
		try
		{
			return behavior.getDigSpeed(stack, block, metadata);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 1.0F;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		try
		{
			return behavior.getItemUseAction(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return EnumAction.none;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		try
		{
			return behavior.getItemStackLimit(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return super.getItemStackLimit(stack);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		try
		{
			return behavior.getMaxItemUseDuration(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 0;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		try
		{
			return behavior.hasContainerItem(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return hasContainerItem();
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		try
		{
			return behavior.getContainerItem(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return null;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		try
		{
			return behavior.getRarity(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return EnumRarity.common;
	}
}