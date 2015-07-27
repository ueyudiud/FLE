package fle.api.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleValue;
import fle.api.enums.EnumDamageResource;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;

public class ItemFleMetaBase extends ItemFle
{
	private final Register<IItemBehaviour<ItemFleMetaBase>> itemBehaviors = new Register();
	private Map<String, ITextureLocation> textureLocations = new HashMap();
	private Map<String, IIcon[]> icons = new HashMap();
	
	protected ItemFleMetaBase(String aUnlocalized, String aUnlocalizedTooltip) 
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setHasSubtypes(true);
		setMaxDamage(0);
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
	    	tBehavior.getAdditionalToolTips(this, aList, aStack);
	    }
	    addAdditionalToolTips(aList, aStack);
	}
	  
	protected void addAdditionalToolTips(List aList, ItemStack aStack) 
	{
		
	}

	public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    if (tBehavior != null)
	    {
	    	tBehavior.onUpdate(this, aStack, aWorld, aPlayer, aTimer, aIsInHand);
	    }
	}
	 
	public final boolean use(ItemStack aStack, double aAmount, EntityLivingBase aPlayer)
	{
		return true;
	}
	
	public int getItemStackLimit(ItemStack aStack)
	{
	    return 64;
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
	public void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource) 
	{
		stack.damageItem(1, aUser);
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
	public void registerIcons(IIconRegister aRegister)
	{
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
	public boolean requiresMultipleRenderPasses() 
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int aMeta)
	{
		return textureLocations.get(itemBehaviors.name(aMeta)).getLocateSize();
	}
	
	public String getTextureFileName(int aMeta, int pass)
	{
		return textureLocations.get(itemBehaviors.name(aMeta)).getTextureFileName(pass);
	}
	
	public String getTextureName(int aMeta, int pass)
	{
		return textureLocations.get(itemBehaviors.name(aMeta)).getTextureName(pass);
	}
}