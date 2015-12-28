package flapi.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.chem.MatterDictionary;
import flapi.chem.base.MatterStack;
import flapi.collection.Register;
import flapi.enums.EnumDamageResource;
import flapi.item.interfaces.IItemBehaviour;
import flapi.solid.ISolidContainerItem;
import flapi.solid.SolidRegistry;
import flapi.solid.SolidStack;
import flapi.solid.SolidTankInfo;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.util.ITI;
import flapi.util.ITextureHandler;
import flapi.util.ItemTextureHandler;

public class ItemFleMetaBase extends ItemFle
{
	protected final Register<IItemBehaviour<ItemFleMetaBase>> itemBehaviors = new Register();
	private Map<String, ItemTextureHandler> textureHandlers = new HashMap();
	
	protected ItemFleMetaBase(String aUnlocalized, String aUnlocalizedTooltip) 
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setHasSubtypes(true);
		setMaxDamage(0);
		setTextureName(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
	}
	
	public final ItemFleMetaBase addSubItem(int metaValue, String tag, String localized, ITextureHandler<ITI> handler, IItemBehaviour<ItemFleMetaBase> behaviour)
	{
		if ((metaValue < 0) || (metaValue >= 32766) || (behaviour == null))
		{
			return this;
	    }
	    itemBehaviors.register(metaValue, behaviour, tag);
	    textureHandlers.put(tag, new ItemTextureHandler(handler));
		ItemStack aStack = new ItemStack(this, 1);
		setDamage(aStack, metaValue);
		FleAPI.langManager.registerLocal(getUnlocalizedName(aStack) + ".name", localized);
		FleAPI.langManager.registerLocal(getUnlocalizedName(aStack) + ".tooltip", "No tool tip.");
	    return this;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack aStack)
	{
		return FleAPI.langManager.translateToLocal(getUnlocalizedName(aStack) + ".name", new Object[0]);
	}
	  
	public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity)
	{
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
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(item.getEntityItem())));
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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

	public final void addInformation(ItemStack stack, EntityPlayer player, List list, boolean aF3_H)
	{
		String tKey = getUnlocalizedName(stack) + ".tooltip";
		String tString = FleAPI.langManager.translateToLocal(tKey);
	    list.add(tString);
	    if(FluidContainerRegistry.isContainer(stack))
	    {
	    	int cap = FluidContainerRegistry.getContainerCapacity(stack);
	    	if(cap > 0)
	    	{
	    		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
	    		if(fluid != null)
	    		{
	    			list.add(String.format("%s %s / %s", EnumChatFormatting.WHITE.toString() + fluid.getLocalizedName(), FleValue.format_L.format_c(fluid.amount), FleValue.format_L.format(cap)));
	    		}
	    		else
	    		{
	    			list.add(String.format("0L / %s", FleValue.format_L.format(cap)));
	    		}
	    	}
	    }
	    else if(this instanceof IFluidContainerItem)
	    {
	    	int cap = ((IFluidContainerItem) this).getCapacity(stack);
	    	if(cap > 0)
	    	{
	    		FluidStack fluid = ((IFluidContainerItem) this).getFluid(stack);
	    		if(fluid != null)
	    		{
	    			list.add(String.format("%s %s / %s", EnumChatFormatting.WHITE.toString() + fluid.getLocalizedName(), FleValue.format_L.format_c(fluid.amount), FleValue.format_L.format(cap)));
	    		}
	    		else
	    		{
	    			list.add(String.format("0L / %s", FleValue.format_L.format(cap)));
	    		}
	    	}
	    }
	    if(SolidRegistry.isContainer(stack))
	    {
	    	int cap = SolidRegistry.getContainerCapacity(stack);
	    	if(cap > 0)
	    	{
		    	SolidStack solid = SolidRegistry.getSolidForFilledItem(stack);
	    		if(solid != null)
	    		{
	    			list.add(String.format("%s %s / %s", EnumChatFormatting.WHITE.toString() + solid.get().getLocalizedName(solid), FleValue.format_L.format_c(solid.size()), FleValue.format_L.format(cap)));
	    		}
	    		else
	    		{
	    			list.add(String.format("0L / %s", FleValue.format_L.format(cap)));
	    		}
	    	}
	    }
	    else if(this instanceof ISolidContainerItem)
	    {
	    	SolidTankInfo info = ((ISolidContainerItem) this).getTankInfo(stack);
	    	if(info != null && info.capacity > 0)
	    	{
	    		SolidStack solid = info.solid;
	    		if(solid != null)
	    		{
	    			list.add(String.format("%s %s / %s", EnumChatFormatting.WHITE.toString() + solid.get().getLocalizedName(solid), FleValue.format_L.format_c(solid.size()), FleValue.format_L.format(info.capacity)));
	    		}
	    		else
	    		{
	    			list.add(String.format("0L / %s", FleValue.format_L.format(info.capacity)));
	    		}
	    	}
	    }
	    MatterStack mStack = MatterDictionary.toMatter(stack);
	    if(mStack != null)
	    {
	    	list.add(String.format("%s%s %s", mStack.getMatter().getName(), EnumChatFormatting.LIGHT_PURPLE, FleValue.format_mol.format_c(mStack.getPart().resolution)));
	    }
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(stack)));
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
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
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			ItemTextureHandler handler = textureHandlers.get(itemBehaviors.name(tBehavior));
			if(handler != null)
				handler.registerIcon(register);
		}
	}
	
	@Override
	public IIcon getIconIndex(ItemStack itemstack)
	{
		return getIcon(itemstack, 0);
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return getIconIndex(new ItemStack(this, 1, meta));
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		return getIcon(new ItemStack(this, 1, meta), pass);
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) 
	{
		try
		{
			return textureHandlers.get(itemBehaviors.name(getDamage(stack))).getIcon(stack, pass);
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
	public int getRenderPasses(int meta)
	{
		try
		{
			return textureHandlers.get(itemBehaviors.name(meta)).getRenderPasses(meta);
		}
		catch(Throwable e)
		{
			return 1;
		}
	}
	
	@Override
	public MovingObjectPosition getMovingObjectPositionFromPlayer(
			World aWorld, EntityPlayer aPlayer, boolean aFlag)
	{
		return super.getMovingObjectPositionFromPlayer(aWorld, aPlayer,	aFlag);
	}
}