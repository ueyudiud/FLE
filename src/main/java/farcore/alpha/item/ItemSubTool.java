package farcore.alpha.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.alpha.interfaces.item.IHookedIconInfo;
import farcore.alpha.interfaces.item.IToolProp;
import farcore.alpha.item.behavior.IBehavior;
import farcore.alpha.util.LangHook.UnlocalizedList;
import farcore.enums.EnumDamageResource;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.recipe.ICraftingInventory;
import farcore.lib.substance.SubstanceHandle;
import farcore.lib.substance.SubstanceTool;
import farcore.util.U;
import fle.load.Langs;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockMeta;

public class ItemSubTool<T extends ItemSubBehavior> extends ItemSubBehaviorII<T>
implements ICustomDamageItem
{
	Map<Short, IToolProp> toolMap = new HashMap();
	private ItemSubToolHead toolHead;
	
	protected ItemSubTool(String unlocalized)
	{
		super(unlocalized);
		this.toolHead = new ItemSubToolHead(this, unlocalized + ".head");
		maxStackSize = 1;
	}
	protected ItemSubTool(String modid, String unlocalized)
	{
		super(modid, unlocalized);
		this.toolHead = new ItemSubToolHead(this, modid, unlocalized + ".head");
		maxStackSize = 1;
	}
	
	public void addSubItem(int id, String name, String local, IBehavior<T> itemInfo, IHookedIconInfo iconInfo, IToolProp toolProp)
	{
		super.addSubItem(id, name, local, itemInfo, iconInfo);
		toolMap.put(Short.valueOf((short) id), toolProp);
		if(toolProp.hasToolHead())
		{
			toolHead.addSubItem(id, name, local, IBehavior.NONE, iconInfo);
		}
	}
	
	public static SubstanceTool getToolMaterial(ItemStack stack)
	{
		return SubstanceTool.getSubstance(U.Inventorys.setupNBT(stack, false).getString("head"));
	}
	public static SubstanceHandle getHandleMaterial(ItemStack stack)
	{
		return SubstanceHandle.getSubstance(U.Inventorys.setupNBT(stack, false).getString("handle"));
	}

	public ItemStack createTool(String type, SubstanceTool head, SubstanceHandle handle)
	{
		return createTool(type, head, handle, (int) (head.maxUses * handle.usesMul));
	}
	public ItemStack createTool(String type, SubstanceTool head, SubstanceHandle handle, int maxDamage)
	{
		return createTool(type, head, handle, maxDamage, 0);
	}
	public ItemStack createTool(String type, SubstanceTool head, SubstanceHandle handle, int maxDamage, float damage)
	{
		if(!register.contain(type)) return null;
		ItemStack ret = new ItemStack(this, 1, register.id(type));
		NBTTagCompound nbt = U.Inventorys.setupNBT(ret, true);
		setHeadMaterial(nbt, head);
		setHandleMaterial(nbt, handle);
		nbt.setInteger("maxDamage", maxDamage);
		setCustomDamage(ret, damage);
		return ret;
	}
	public ItemStack createTool(String type, SubstanceTool head)
	{
		return createTool(type, head, head.maxUses);
	}
	public ItemStack createTool(String type, SubstanceTool head, int maxDamage)
	{
		return createTool(type, head, maxDamage, 0F);
	}
	public ItemStack createTool(String type, SubstanceTool head, int maxDamage, float damage)
	{
		if(!register.contain(type)) return null;
		ItemStack ret = new ItemStack(this, 1, register.id(type));
		NBTTagCompound nbt = U.Inventorys.setupNBT(ret, true);
		setHeadMaterial(nbt, head);
		nbt.setInteger("maxDamage", maxDamage);
		setCustomDamage(ret, damage);
		return ret;
	}
	public ItemStack createToolHead(String type, SubstanceTool head)
	{
		if(!register.contain(type)) return null;
		if(toolMap.get(register.id(type)).hasToolHead())
		{
			return createToolHeadUnsafe(type, head);
		}
		return null;
	}
	public ItemStack createToolHeadUnsafe(String type, SubstanceTool head)
	{
		return new ItemStack(toolHead, 1, register.id(type));
	}
	
	public static void setHeadMaterial(NBTTagCompound nbt, SubstanceTool tool)
	{
		nbt.setString("head", tool.getName());
	}
	public static void setHandleMaterial(NBTTagCompound nbt, SubstanceHandle handle)
	{
		nbt.setString("handle", handle.getName());
	}
	
	boolean hasTool(ItemStack stack)
	{
		return toolMap.containsKey(Short.valueOf((short) getDamage(stack)));
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(IToolProp prop : toolMap.values())
		{
			prop.getSubItem(this, list);
		}
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			return prop.getTool();
		}
		return ImmutableSet.of();
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			return prop.getTool().contains(toolClass) ?
					getToolMaterial(stack).harvestLevel : -1;
		}
		return 0;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int metadata)
	{
		if(ForgeHooks.isToolEffective(stack, block, metadata))
		{
			float mul = 1.0F;
			if(hasTool(stack))
			{
				IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
				mul = prop.getBaseDigSpeed(block, metadata);
			}
			return mul * getToolMaterial(stack).digSpeed;
		}
		return 1.0F;
	}
	
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		SubstanceTool tool = getToolMaterial(stack);
		SubstanceHandle handle = getHandleMaterial(stack);
		return (int) (tool.maxUses * handle.usesMul);
	}
	
	public static void setCustomDamage(ItemStack stack, float damage)
	{
		U.Inventorys.setupNBT(stack, true).setFloat("damage", damage);
	}
	
	public static float getCustomDamage(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getFloat("damage");
	}
	
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x,
    		int y, int z, EntityLivingBase entity)
    {
		boolean flag = super.onBlockDestroyed(stack, world, block, x, y, z, entity);
		if(ForgeHooks.isToolEffective(stack, block, world.getBlockMetadata(x, y, z)))
		{
			if(hasTool(stack))
			{
				IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
				damangeItem(stack, prop.getDestoryBlockDamage(), entity, EnumDamageResource.KNOCK);
			}
			return true;
		}
		return flag;
    }

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		boolean flag = super.onLeftClickEntity(stack, player, entity);
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			if(prop.isWeapon())
			{
				damangeItem(stack, prop.getAttackEntityDamage(), player, EnumDamageResource.HIT);
				return true;
			}
		}
		return flag;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getCustomDamage(stack) != 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) getCustomDamage(stack) / (double) getMaxCustomDamgae(stack);
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = super.getAttributeModifiers(stack);
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			prop.getAttributeModifiers(this, multimap, stack);
		}
		return multimap;
	}
	
	@Override
	public void damangeItem(ItemStack stack, float amount, EntityLivingBase user, EnumDamageResource resource) 
	{
		float damage = getCustomDamage(stack);
		int max = getMaxCustomDamgae(stack);
		float ret = applyDamage(stack, amount, damage, max, user, resource);
		if(ret >= max)
		{
			stack.stackSize--;
			setCustomDamage(stack, 0);
		}
		else
		{
			setCustomDamage(stack, ret);
		}
	}
	
	protected float applyDamage(ItemStack target, float amount, float damage, int max, EntityLivingBase user, EnumDamageResource resource)
	{
		return amount + damage;
	}
	
	@SideOnly(Side.CLIENT)
	public void addUnlocalInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H)
	{
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			prop.addInfomation(stack, player, list, F3H);
			
			int maxUse = getMaxCustomDamgae(stack);
			float lastUse = (float) maxUse - getCustomDamage(stack);
			list.add(Langs.infoToolHeadMaterial, getToolMaterial(stack).getLocalName());
			
			if(prop.hasHandle())
			{
				SubstanceHandle handle = getHandleMaterial(stack);
				list.add(Langs.infoToolHandleMaterial, handle.getLocalName());
			}
			list.add(Langs.infoToolDamage, (int) (lastUse * 100), (int) (maxUse * 100));
		}
	}

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			if(prop.isBlockable())
			{
				return EnumAction.block;
			}
		}
        return EnumAction.none;
    }
	
	@Override
	public ItemStack getCraftedItem(ItemStack stack, ICraftingInventory crafting)
	{
		if(hasTool(stack))
		{
			IToolProp prop = toolMap.get(Short.valueOf((short) getDamage(stack)));
			damangeItem(stack, prop.getCraftDamage(), null, EnumDamageResource.CRAFT);
		}
		return U.Inventorys.valid(stack);
	}
}