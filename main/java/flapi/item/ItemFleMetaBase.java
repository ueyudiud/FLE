package flapi.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.collection.Register;
import farcore.util.FleLog;
import flapi.FleAPI;
import flapi.item.interfaces.IItemBehavior;
import flapi.util.Values;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemFleMetaBase extends ItemFle
{
	protected final Register<IItemBehavior<ItemFleMetaBase>> itemBehaviors = new Register();
	
	protected ItemFleMetaBase(String aUnlocalized) 
	{
		super(aUnlocalized);
		setHasSubtypes(true);
		setMaxDamage(0);
		setTextureName(Values.TEXTURE_FILE + ":void");
	}

	public final ItemFleMetaBase addSubItem(int metaValue, String tag, IItemBehavior<ItemFleMetaBase> behaviour)
	{
		if ((metaValue < 0) || (metaValue >= 32766) || (behaviour == null))
		{
			return this;
	    }
	    itemBehaviors.register(metaValue, behaviour, tag);
		ItemStack aStack = new ItemStack(this, 1);
		setDamage(aStack, metaValue);
		FarCore.lang.registerLocal(getUnlocalizedName(aStack) + ".tooltip", "No tool tip.");
	    return this;
	}
	public final ItemFleMetaBase addSubItem(int metaValue, String tag, String localized, IItemBehavior<ItemFleMetaBase> behaviour)
	{
		if ((metaValue < 0) || (metaValue >= 32766) || (behaviour == null))
		{
			return this;
	    }
	    itemBehaviors.register(metaValue, behaviour, tag);
//	    textureHandlers.put(tag, new ItemTextureHandler(handler));
		ItemStack aStack = new ItemStack(this, 1);
		setDamage(aStack, metaValue);
		FarCore.lang.registerLocal(getUnlocalizedName(aStack) + ".name", localized);
		FarCore.lang.registerLocal(getUnlocalizedName(aStack) + ".tooltip", "No tool tip.");
	    return this;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack aStack)
	{
		return FarCore.lang.translateToLocal(getUnlocalizedName(aStack) + ".name", new Object[0]);
	}
	  
	public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity)
	{
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior.onLeftClickEntity(this, aStack, aPlayer, aEntity))
	    	{
	    		if (aStack.stackSize <= 0) 
	    		{
	    			aPlayer.destroyCurrentEquippedItem();
	            }
	            return true;
	        }
	    	if (aStack.stackSize <= 0)
	    	{
	    		aPlayer.destroyCurrentEquippedItem();
	            return false;
	        }
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	    return false;
	}
	  
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ)
	{
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior.onItemUse(this, aStack, aPlayer, aWorld, aX, aY, aZ, ForgeDirection.VALID_DIRECTIONS[aSide], hitX, hitY, hitZ))
	    	{
	    		if (aStack.stackSize <= 0) 
	    		{
	    			aPlayer.destroyCurrentEquippedItem();
	            }
	            return true;
	        }
	    	if (aStack.stackSize <= 0)
	    	{
	    		aPlayer.destroyCurrentEquippedItem();
	            return false;
	        }
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	    return false;
	  }
	
	public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ)
	{
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior.onItemUseFirst(this, aStack, aPlayer, aWorld, aX, aY, aZ, ForgeDirection.VALID_DIRECTIONS[aSide], hitX, hitY, hitZ))
	    	{
	    		if (aStack.stackSize <= 0)
	    		{
	    			aPlayer.destroyCurrentEquippedItem();
	            }
	            return true;
	        }
	    	if (aStack.stackSize <= 0)
	    	{
	            aPlayer.destroyCurrentEquippedItem();
	            return false;
	        }
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	    return false;
	}
	
	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) 
	{
		IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(itemstack)));
		try
	    {
	    	return tBehavior.getDigSpeed(this, itemstack, block, metadata);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 1.0F;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack aStack, World aWorld,
			Block aBlock, int aX, int aY,
			int aZ, EntityLivingBase aEntity)
	{
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior.onItemDamageBlock(this, aStack, aBlock, aEntity, aWorld, aX, aY, aZ))
	    	{
	    		if (aEntity instanceof EntityPlayer && aStack.stackSize <= 0) 
	    		{
	    			((EntityPlayer) aEntity).destroyCurrentEquippedItem();
	            }
	            return true;
	        }
    		if (aEntity instanceof EntityPlayer && aStack.stackSize <= 0) 
    		{
    			((EntityPlayer) aEntity).destroyCurrentEquippedItem();
            }
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem item) 
	{
		IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(item.getEntityItem())));
		try
	    {
	    	boolean flag = tBehavior.onEntityItemUpdate(this, item);
	    	if(item.getEntityItem().stackSize <= 0)
	    	{
	    		item.setDead();
	    	}
	    	return flag;
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	public boolean canHarvestBlock(EntityPlayer player, Block aBlock, ItemStack aStack) 
	{
		IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			MovingObjectPosition aPos = getMovingObjectPositionFromPlayer(player.worldObj, player, false);
			int tMeta = 0;
			if(player != null) tMeta = player.worldObj.getBlockMetadata(aPos.blockX, aPos.blockY, aPos.blockZ);
	    	return tBehavior.canHarvestBlock(this, aBlock, tMeta, aStack);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}

	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer)
	{
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	aStack = tBehavior.onItemRightClick(this, aStack, aWorld, aPlayer);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	    return aStack;
	}

	public final void addInformation(ItemStack stack, EntityPlayer player, List list, boolean aF3_H)
	{
		String tKey = getUnlocalizedName(stack) + ".tooltip";
		String tString = FarCore.translateToLocal(tKey);
	    list.add(tString);
	    IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get((short) getDamage(stack));
	    if(tBehavior != null)
	    {
	    	tBehavior.getAdditionalToolTips(this, list, stack, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
	    }
	    try
	    {
	    	addAdditionalToolTips(player, list, stack);
	    }
	    catch(Throwable e)
	    {
	    	list.add("This item require an bug lead the tool info can't load!");
	    	FleLog.getLogger().debug("Catching during get tool info.", e);
	    }
	}
	  
	protected void addAdditionalToolTips(EntityPlayer aPlayer, List aList, ItemStack aStack) 
	{
		addAdditionalToolTips(aList, aStack);
	}
	protected void addAdditionalToolTips(List aList, ItemStack aStack) 
	{
		
	}

	public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand)
	{
		IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	tBehavior.onUpdate(this, aStack, aWorld, aPlayer, aTimer, aIsInHand);
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack aStack, World aWorld,
			EntityPlayer aPlayer, int aUseTick)
	{
		IItemBehavior<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
			tBehavior.onPlayerStoppedUsing(this, aWorld, aPlayer, aUseTick, aStack);
	    	if (aStack.stackSize <= 0)
	    	{
	    		aPlayer.destroyCurrentEquippedItem();
	    	}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	public int getItemStackLimit(ItemStack aStack)
	{
	    return maxStackSize;
	}
	  
	public int getItemEnchantability()
	{
		return 0;
	}
	  
	public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook)
	{
		return false;
	}
	  
	public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial)
	{
		return false;
	}

//	@Override
//	public void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource, float aDamage) 
//	{
//		stack.damageItem((int) Math.ceil(aDamage), aUser);
//	}

	@Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
		return super.getAttributeModifiers(stack);
    }

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList)
	{
		for(IItemBehavior tBehavior : itemBehaviors)
		{
			aList.add(new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior)));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
//		for(IItemBehavior tBehavior : itemBehaviors)
//		{
//			ItemTextureHandler handler = textureHandlers.get(itemBehaviors.name(tBehavior));
//			if(handler != null)
//				handler.registerIcon(register);
//		}
	}
	
//	@Override
//	public IIcon getIconIndex(ItemStack itemstack)
//	{
//		return getIcon(itemstack, 0);
//	}
//	
//	@Override
//	public IIcon getIconFromDamage(int meta)
//	{
//		return getIconIndex(new ItemStack(this, 1, meta));
//	}
//	
//	@Override
//	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
//	{
//		return getIcon(new ItemStack(this, 1, meta), pass);
//	}
//	
//	@Override
//	public IIcon getIcon(ItemStack stack, int pass) 
//	{
//		try
//		{
//			return textureHandlers.get(itemBehaviors.name(getDamage(stack))).getIcon(stack, pass);
//		}
//		catch(Throwable e)
//		{
//			FleLog.getLogger().info("Fail to get icon of item " + getUnlocalizedName(stack) + " please check your mod pack.");
//			return itemIcon;
//		}
//	}
	
//	@Override
//	public boolean requiresMultipleRenderPasses() 
//	{
//		return true;
//	}
	
//	@Override
//	public int getRenderPasses(int meta)
//	{
//		try
//		{
//			return textureHandlers.get(itemBehaviors.name(meta)).getRenderPasses(meta);
//		}
//		catch(Throwable e)
//		{
//			return 1;
//		}
//	}
	
	@Override
	public MovingObjectPosition getMovingObjectPositionFromPlayer(
			World aWorld, EntityPlayer aPlayer, boolean aFlag)
	{
		return super.getMovingObjectPositionFromPlayer(aWorld, aPlayer,	aFlag);
	}
}