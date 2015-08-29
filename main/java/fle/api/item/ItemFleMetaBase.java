package fle.api.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.enums.EnumDamageResource;
import fle.api.util.FleLog;
import fle.api.util.IBlockTextureManager;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;

public class ItemFleMetaBase extends ItemFle
{
	protected final Register<IItemBehaviour<ItemFleMetaBase>> itemBehaviors = new Register();
	private Map<String, ITextureLocation> textureLocations = new HashMap();
	private Map<String, IIcon[]> icons = new HashMap();
	
	protected ItemFleMetaBase(String aUnlocalized, String aUnlocalizedTooltip) 
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setHasSubtypes(true);
		setMaxDamage(0);
		setTextureName(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
	}
	
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, ITextureLocation aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		if ((aMetaValue < 0) || (aMetaValue >= 32766) || (aBehavior == null))
		{
			return this;
	    }
	    itemBehaviors.register(aMetaValue, aBehavior, aTagName);
	    textureLocations.put(aTagName, aLocate);
	    return this;
	}
	  
	public ItemStack onDispense(IBlockSource aSource, ItemStack aStack)
	{
		IItemBehaviour<ItemFleMetaBase> tBehaviour = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    if(tBehaviour.canDispense(this, aSource, aStack))
	    {
	    	return tBehaviour.onDispense(this, aSource, aStack);
	    }
	    return super.onDispense(aSource, aStack);
	}
	  
	public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity)
	{
	    use(aStack, 0.0D, aPlayer);
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
		use(aStack, 0.0D, aPlayer);
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
		use(aStack, 0.0D, aPlayer);
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(itemstack)));
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
		use(aStack, 0.0D, aEntity);
		isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior.onItemDamageBlock(this, aStack, aBlock, aEntity, aWorld, aX, aY, aZ))
	    	{
	            return true;
	        }
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem aItem) 
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aItem.getEntityItem())));
		try
	    {
	    	return tBehavior.onEntityItemUpdate(this, aItem);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	public boolean canHarvestBlock(EntityPlayer player, Block aBlock, ItemStack aStack) 
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			MovingObjectPosition aPos = getMovingObjectPositionFromPlayer(player.worldObj, player, false);
			int tMeta = player.worldObj.getBlockMetadata(aPos.blockX, aPos.blockY, aPos.blockZ);
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
		use(aStack, 0.0D, aPlayer);
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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

	public final void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H)
	{
		String tKey = getUnlocalizedName(aStack) + ".tooltip";
		String tString = StatCollector.translateToLocal(tKey);
	    aList.add(tString);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    if(tBehavior != null)
	    {
	    	tBehavior.getAdditionalToolTips(this, aList, aStack, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
	    }
	    addAdditionalToolTips(aList, aStack);
	}
	  
	protected void addAdditionalToolTips(List aList, ItemStack aStack) 
	{
		
	}

	public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
			if (tBehavior != null)
		    {
		    	tBehavior.onUpdate(this, aStack, aWorld, aPlayer, aTimer, aIsInHand);
		    }
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
			if (tBehavior != null)
		    {
		    	tBehavior.onPlayerStoppedUsing(this, aWorld, aPlayer, aUseTick, aStack);
		    }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	 
	public final boolean use(ItemStack aStack, double aAmount, EntityLivingBase aPlayer)
	{
		return true;
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

	@Override
	public void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource, float aDamage) 
	{
		stack.damageItem((int) Math.ceil(aDamage), aUser);
	}

	@Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
		return super.getAttributeModifiers(stack);
    }

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList)
	{
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			aList.add(new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior)));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister aRegister)
	{
		super.registerIcons(aRegister);
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			int meta = itemBehaviors.serial(tBehavior);
			int tSize = getRenderPasses(meta);
			IIcon[] tIcons = new IIcon[tSize];
			for(int i = 0; i < tSize; ++i)
			{
				String tFileName = getTextureFileName(meta, i);
				String tIconName = getTextureName(meta, i);
				
				tIcons[i] = aRegister.registerIcon(new StringBuilder().append(tFileName).append(":").append(tIconName).toString());
			}
			icons.put(itemBehaviors.name(tBehavior), tIcons);
		}
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) 
	{
		try
		{
			return icons.get(itemBehaviors.name(getDamage(stack)))[pass];
		}
		catch(Throwable e)
		{
			FleLog.getLogger().info("Fail to get icon of item " + getUnlocalizedName(stack) + " please check your mod pack.");
			return itemIcon;
		}
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() 
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int aMeta)
	{
		try
		{
			return textureLocations.get(itemBehaviors.name(aMeta)).getLocateSize();
		}
		catch(Throwable e)
		{
			return 1;
		}
	}
	
	public String getTextureFileName(int aMeta, int pass)
	{
		try
		{
			return textureLocations.get(itemBehaviors.name(aMeta)).getTextureFileName(pass);
		}
		catch(Throwable e)
		{
			return FleValue.TEXTURE_FILE;
		}
	}
	
	public String getTextureName(int aMeta, int pass)
	{
		try
		{
			return textureLocations.get(itemBehaviors.name(aMeta)).getTextureName(pass);
		}
		catch(Throwable e)
		{
			return FleValue.VOID_ICON_FILE;
		}
	}
	
	@Override
	public MovingObjectPosition getMovingObjectPositionFromPlayer(
			World aWorld, EntityPlayer aPlayer, boolean aFlag)
	{
		return super.getMovingObjectPositionFromPlayer(aWorld, aPlayer,	aFlag);
	}
}