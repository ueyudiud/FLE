package farcore.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.IBreakSpeedItem;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemInfo.ItemInfoMix;
import farcore.interfaces.item.IItemProperty;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.util.FleLog;
import farcore.util.LanguageManager;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemSubBehavior extends ItemBase implements IBreakSpeedItem
{
	protected final IRegister<IItemInfo> register;
	private Map<String, IItemIconInfo> infos = new HashMap();
	private Map<String, String> localizes = new HashMap();
	
	protected ItemSubBehavior(String unlocalized)
	{
		super(unlocalized);
		register = provideRegister();
		hasSubtypes = true;
	}
	protected ItemSubBehavior(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
		register = provideRegister();
		hasSubtypes = true;
	}
	protected ItemSubBehavior(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
		register = provideRegister();
		hasSubtypes = true;
	}
	
	protected Register<IItemInfo> provideRegister()
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
	
	public void addSubItem(int id, String name, String local, IBehavior behavior, IItemProperty property, IItemIconInfo iconInfo)
	{
		addSubItem(id, name, local, new ItemInfoMix(behavior, property), iconInfo);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, IItemIconInfo iconInfo)
	{
		register.register(id, name, itemInfo);
		infos.put(name, iconInfo);
		localizes.put(name, local);
	}

	@SideOnly(Side.CLIENT)
	public void registerLocalizedName(LanguageManager manager)
	{
		String unlocalized = getUnlocalizedName();
		for(String tag : register.names())
		{
			manager.registerLocal(unlocalized + ":" + tag, localizes.get(tag));
		}
	}
	
	@Override
	public String getMetaUnlocalizedName(int metadata)
	{
		return register.name(metadata);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		for(IItemIconInfo info : infos.values())
		{
			try 
			{
				info.registerIcons(register);
			}
			catch (Exception e)
			{
				
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata)
	{
		try
		{
			return infos.get(register.name(metadata)).getPasses();
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		try
		{
			return infos.get(register.name(getDamage(stack))).getColor(stack, pass);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		try
		{
			return infos.get(register.name(getDamage(stack))).getIcon(stack, pass);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return V.voidItemIcon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		return getIcon(stack, 0);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
	{
		try
		{
			return register.get(getDamage(stack)).onBlockDestroyed(stack, world, block, x, y, z, entity);
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
			return register.get(getDamage(stack)).onBlockStartBreak(stack, player.getEntityWorld(), x, y, z, player);
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
			return register.get(getDamage(stack)).onEaten(stack, world, player);
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
			return register.get(getDamage(entityItem.getEntityItem())).onEntityItemUpdate(entityItem.getEntityItem(), entityItem);
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
			return register.get(getDamage(stack)).onItemRightClick(stack, world, player);
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
			return register.get(getDamage(stack)).onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
			return register.get(getDamage(stack)).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
			return register.get(getDamage(stack)).onLeftClickEntity(stack, player, entity);
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
			register.get(getDamage(stack)).onUsingTick(stack, player, count);
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
			register.get(getDamage(stack)).onPlayerStoppedUsing(stack, world, player, tick);
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
			register.get(getDamage(stack)).onUpdate(stack, world, entity, tick, flag);
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
			return register.get(getDamage(stack)).canHarvestBlock(block, stack);
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
		return 1.0F;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		try
		{
			return register.get(getDamage(stack)).getItemUseAction(stack);
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
			return register.get(getDamage(stack)).getItemStackLimit(stack);
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
			return register.get(getDamage(stack)).getMaxItemUseDuration(stack);
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
			return register.get(getDamage(stack)).hasContainerItem(stack);
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
			return register.get(getDamage(stack)).getContainerItem(stack);
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
			return register.get(getDamage(stack)).getRarity(stack);
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
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		try
		{
			return register.get(getDamage(stack)).getToolClasses(stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return ImmutableSet.of();
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		try
		{
			return register.get(getDamage(stack)).getHarvestLevel(stack, toolClass);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return -1;
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = HashMultimap.create();
		try
		{
			register.get(getDamage(stack)).addAttributeModifiers(multimap, stack);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return multimap;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
		try
		{
			register.get(getDamage(stack)).addInformation(stack, player, list, flag);
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
	public float getSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int meta)
	{
		try
		{
			return register.get(getDamage(stack)).getDigSpeed(stack, world, x, y, z, block, meta);
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
	public boolean isRepairable()
	{
		return false;
	}
}