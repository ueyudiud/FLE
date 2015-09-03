package fle.core.item;

import static net.minecraft.entity.SharedMonsterAttributes.attackDamage;
import static net.minecraft.entity.SharedMonsterAttributes.knockbackResistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.enums.EnumCraftingType;
import fle.api.enums.EnumDamageResource;
import fle.api.enums.EnumTool;
import fle.api.item.IArrowItem;
import fle.api.item.ICrushableTool;
import fle.api.item.IDestoryCheck;
import fle.api.item.IItemBehaviour;
import fle.api.item.IPolishTool;
import fle.api.item.ISubPolishTool;
import fle.api.item.ItemFleMetaBase;
import fle.api.item.ItemFleTool;
import fle.api.material.MaterialAbstract;
import fle.api.recipe.CraftingState;
import fle.api.util.FleLog;
import fle.api.util.IDataChecker;
import fle.api.util.ISubTagContainer;
import fle.api.util.ITextureLocation;
import fle.api.util.SubTag;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.behavior.BehaviorArrow;
import fle.core.item.behavior.BehaviorAwl;
import fle.core.item.behavior.BehaviorAxe;
import fle.core.item.behavior.BehaviorChisel;
import fle.core.item.behavior.BehaviorFirestarter;
import fle.core.item.behavior.BehaviorHoe;
import fle.core.item.behavior.BehaviorMetalHammer;
import fle.core.item.behavior.BehaviorPickaxe;
import fle.core.item.behavior.BehaviorShovel;
import fle.core.item.behavior.BehaviorStoneHammer;
import fle.core.item.behavior.BehaviorTool;
import fle.core.item.behavior.BehaviorWhetstone;
import fle.core.item.behavior.BehaviorWoodHammer;
import fle.core.item.tool.ToolMaterialInfo;
import fle.core.util.TextureLocation;

public class ItemTool extends ItemFleTool implements IFluidContainerItem, ICrushableTool, IPolishTool, IArrowItem
{
	static final Set<MaterialAbstract> materials = new HashSet();

	public static int getToolIDFromName(String toolName)
	{
		return ((ItemTool) IB.tool).itemBehaviors.serial(toolName);
	}

	public static ItemStack a(String toolTip, MaterialAbstract base)
	{
		return a(toolTip, base, 0);
	}
	public static ItemStack a(String toolTip, int size, MaterialAbstract base)
	{
		ItemStack ret = a(toolTip, base);
		ret.stackSize = size;
		return ret;
	}
	public static ItemStack a(String toolTip, MaterialAbstract base, int damage)
	{
		return a(toolTip, base, null, 0.0F, null, damage);
	}
	public static ItemStack a(String toolTip, MaterialAbstract base, MaterialAbstract cover, float area, MaterialAbstract mosaic, int damage)
	{
		try
		{
			ItemStack aStack = new ItemStack(IB.tool, 1, ((ItemTool) IB.tool).itemBehaviors.serial(toolTip));
			aStack.stackTagCompound = new NBTTagCompound();
			new ToolMaterialInfo(base, cover, area, mosaic).writeToNBT(aStack.getTagCompound());
			((ItemTool) IB.tool).setDisplayDamage(aStack, damage * 100);
			return aStack;
		}
		catch(Throwable e)
		{
			//Use a null item.
			FleLog.getLogger().warn("Fle: some mod use empty item with a bug, please check your fle-addon "
					+ "had already update, or report this bug to mod editer.");
			return null; //Return null.
		}
	}
	
	public ItemTool init()
	{
		addToolMaterial(Materials.Flint);
		addToolMaterial(Materials.Obsidian);
		addToolMaterial(Materials.HardWood);
		addToolMaterial(Materials.Stone);
		addToolMaterial(Materials.CompactStone);
		addToolMaterial(Materials.Copper);
		addToolMaterial(Materials.CuAs);
		addSubItem(1, "rough_stone_axe", "Rough Stone Axe", SubTag.TOOL_stone, 
				new String[]{EnumTool.axe.toString()},
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.0F)},
				new TextureLocation("tools/axe/rough_axe_head", "tools/axe/rough_axe_rust", FleValue.VOID_ICON_FILE, "tools/axe/rough_axe_stick", "tools/axe/rough_axe_tie"), 
				new BehaviorAxe(0.6F));
		addSubItem(2, "stone_axe", "Stone Axe", SubTag.TOOL_stone, 
				new String[]{EnumTool.axe.toString()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 2.0F)},
				new TextureLocation("tools/axe/stone_axe_head", "tools/axe/stone_axe_rust", FleValue.VOID_ICON_FILE, "tools/axe/stone_axe_stick", "tools/axe/stone_axe_tie"), 
				new BehaviorAxe(0.9F));
		addSubItem(3, "stone_shovel", "Stone Shovel", SubTag.TOOL_stone, 
				new String[]{EnumTool.shovel.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.0F)},
				new TextureLocation("tools/shovel/stone_shovel_head", "tools/shovel/stone_shovel_rust", FleValue.VOID_ICON_FILE, "tools/shovel/stone_shovel_stick", "tools/shovel/stone_shovel_tie"), 
				new BehaviorShovel(0.9F));
		addSubItem(4, "flint_hammer", "Flint Hammer", SubTag.TOOL_flint, 
				new String[]{EnumTool.stone_hammer.name(), EnumTool.abstract_hammer.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.5F)},
				new TextureLocation("tools/hammer/flint_hammer_head", "tools/hammer/flint_hammer_rust", FleValue.VOID_ICON_FILE, "tools/hammer/flint_hammer_stick", "tools/hammer/flint_hammer_tie"), 
				new BehaviorStoneHammer(1, 0.7F));
		addSubItem(5, "stone_hammer", "Stone Hammer", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.stone_hammer.name(), EnumTool.abstract_hammer.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 2.0F)},
				new TextureLocation("tools/hammer/stone_hammer_head", "tools/hammer/stone_hammer_rust", FleValue.VOID_ICON_FILE, "tools/hammer/stone_hammer_stick", "tools/hammer/stone_hammer_tie"), 
				new BehaviorStoneHammer(2, 0.9F));
		addSubItem(6, "wooden_hammer", "Wooden Hammer", SubTag.TOOL_wood, 
				new String[]{EnumTool.wood_hammer.name()}, 
				new AttributesInfo[]{new AttributesInfo(knockbackResistance, 0.5F)},
				new TextureLocation(FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE, "tools/hammer/wood_hammer"), 
				new BehaviorWoodHammer());
		addSubItem(7, "wooden_drilling_firing", "Drilling Firing", SubTag.TOOL_wood, 
				new String[]{EnumTool.firestarter.name()},
				new TextureLocation(FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE, "tools/firestarter/wood_drilling_firing"), 
				new BehaviorFirestarter(0.3F));
		addSubItem(8, "whetstone", "Whetstone", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.whetstone.name()},
				new TextureLocation("tools/whetstone/whetstone", "tools/whetstone/whetstone_rust", FleValue.VOID_ICON_FILE), 
				new BehaviorWhetstone());
		addSubItem(9, "flint_awl", "Awl", SubTag.TOOL_flint, 
				new String[]{EnumTool.awl.name()},
				new TextureLocation("tools/awl/awl", FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE), 
				new BehaviorAwl());
		addSubItem(10, "flint_arrow", "Arrow", SubTag.TOOL_flint, 
				new String[]{EnumTool.arrow.name()},
				new TextureLocation("tools/arrow/flint_arrow_head", FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE, "tools/arrow/flint_arrow_stick"), 
				new BehaviorArrow());
		addSubItem(11, "stone_sickle", "Stone Sickle", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.hoe.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.0F)},
				new TextureLocation("tools/hoe/stone_sickle_head", "tools/hoe/stone_sickle_rust", FleValue.VOID_ICON_FILE, "tools/hoe/stone_sickle_stick", "tools/hoe/stone_sickle_tie"), 
				new BehaviorHoe(true, false));
		addSubItem(12, "stone_spade_hoe", "Stone Spade-Hoe", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.hoe.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 0.5F)},
				new TextureLocation("tools/hoe/spade_hoe_head", "tools/hoe/spade_hoe_rust", FleValue.VOID_ICON_FILE, "tools/hoe/spade_hoe_stick", "tools/hoe/spade_hoe_tie"), 
				new BehaviorHoe(false, true));
		addSubItem(13, "stone_decorticating_plate", "Decorticating Plate", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.decorticating_plate.name()},
				new TextureLocation("tools/plate/stone_decorticating_plate", FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE), 
				new BehaviorTool());
		addSubItem(14, "stone_decorticating_stick", "Decorticating Stick", SubTag.TOOL_stone_real, 
				new String[]{EnumTool.decorticating_stick.name()},
				new TextureLocation("tools/plate/stone_decorticating_stick", FleValue.VOID_ICON_FILE, FleValue.VOID_ICON_FILE), 
				new BehaviorTool());
		addSubItem(101, "metal_axe", "Metal Axe", SubTag.TOOL_metal, 
				new String[]{EnumTool.axe.toString()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 3.0F)},
				new TextureLocation("tools/axe/metal_axe_head", "tools/axe/metal_axe_rust", "tools/axe/metal_axe_mosaic", "tools/axe/metal_axe_stick"), 
				new BehaviorAxe(1.0F));
		addSubItem(102, "metal_pickaxe", "Metal Pickaxe", SubTag.TOOL_metal, 
				new String[]{EnumTool.pickaxe.toString()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.0F)},
				new TextureLocation("tools/pickaxe/metal_pickaxe_head", "tools/pickaxe/metal_pickaxe_rust", "tools/pickaxe/metal_pickaxe_mosaic", "tools/pickaxe/metal_pickaxe_stick"), 
				new BehaviorPickaxe());
		addSubItem(103, "metal_hammer", "Metal Hammer", SubTag.TOOL_metal, 
				new String[]{EnumTool.metal_hammer.toString(), EnumTool.abstract_hammer.name()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 2.0F), new AttributesInfo(knockbackResistance, 0.5F)},
				new TextureLocation("tools/hammer/metal_hammer_head", "tools/hammer/metal_hammer_rust", "tools/hammer/metal_hammer_mosaic", "tools/hammer/metal_hammer_stick"), 
				new BehaviorMetalHammer());
		addSubItem(104, "metal_shovel", "Metal Shovel", SubTag.TOOL_metal, 
				new String[]{EnumTool.shovel.toString()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.3F)},
				new TextureLocation("tools/shovel/metal_shovel_head", "tools/shovel/metal_shovel_rust", "tools/shovel/metal_shovel_mosaic", "tools/shovel/metal_shovel_stick"), 
				new BehaviorShovel(1.0F));
		addSubItem(105, "metal_chisel", "Metal Chisel", SubTag.TOOL_metal, 
				new String[]{EnumTool.chisel.toString()}, 
				new AttributesInfo[]{new AttributesInfo(attackDamage, 1.0F)},
				new TextureLocation("tools/chisel/metal_chisel_head", "tools/chisel/metal_chisel_rust", "tools/chisel/metal_chisel_mosaic", "tools/chisel/metal_chisel_stick"), 
				new BehaviorChisel());
		heightLightList.add(8);
		heightLightList.add(13);
		heightLightList.add(14);
		stackLimitList.add(10);
		return this;
	}
	
	public static void addToolMaterial(MaterialAbstract aMaterial)
	{
		materials.add(aMaterial);
	}
	
	private List<Integer> stackLimitList = new ArrayList();
	
	public ItemTool(String aUnlocalized, String aUnlocalizedTooltip) 
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setFull3D();
	}
	
	@Override
	public int getItemStackLimit(ItemStack aStack)
	{
		return stackLimitList.contains(getDamage(aStack)) ? 16 : 1;
	}
	
	@Override
	public int getMaxDamage(ItemStack aStack) 
	{
		return new ToolMaterialInfo(setupNBT(aStack)).getMaxUse() * 100;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack aStack) 
	{
		return getDisplayDamage(aStack) != 0;
	}
	
	@Override
	public int getDisplayDamage(ItemStack aStack) 
	{
		return setupNBT(aStack).getInteger("ToolDamage");
	}
	
	public void setDisplayDamage(ItemStack aStack, int aDamage) 
	{
		setupNBT(aStack).setInteger("ToolDamage", aDamage);
	}
	
	@Override
	public void damageItem(ItemStack aStack, EntityLivingBase aUser,
			EnumDamageResource aReource, float aDamage) 
	{
		ToolMaterialInfo info = new ToolMaterialInfo(setupNBT(aStack));
        if (!(aUser instanceof EntityPlayer) || !((EntityPlayer) aUser).capabilities.isCreativeMode)
        {
        	int tDamage = (int) (aDamage * 100);
        	if(aReource != EnumDamageResource.UseTool)
        	{
            	float hardness = info.getMaterialBase().getPropertyInfo().getHardness();
            	float birttleness = info.getMaterialBase().getPropertyInfo().getBrittleness();
            	float toughness = info.getMaterialBase().getPropertyInfo().getToughness();
            	float denseness = info.getMaterialBase().getPropertyInfo().getDenseness();
            	if(itemRand.nextDouble() < birttleness / 10F)
            	{
            		tDamage *= (5 - Math.log(toughness + 1.0F));
            	}
        	}
        	int aMax = getMaxDamage(aStack);
        	int retDamage = getDisplayDamage(aStack) + tDamage;
        	if(retDamage >= aMax)
        	{
        		aStack.stackSize--;
        		onToolDestoryed(aStack, (EntityPlayer) aUser);
        	}
        	else
        	{
        		setDisplayDamage(aStack, retDamage);
        	}
        }
	}
	
	public void onToolDestoryed(ItemStack aTool, EntityPlayer aPlayer)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aTool)));
	    try
	    {
	    	if (tBehavior instanceof IDestoryCheck)
	    	{
	            ((IDestoryCheck) tBehavior).onToolDestoryed(aPlayer.worldObj, aTool, aPlayer);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}

	public final ItemTool addSubItem(int aMetaValue, String aTagName, String aLocalized, IDataChecker<ISubTagContainer> aTag, String[] aToolClass, AttributesInfo[] aInfo, ITextureLocation aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		addSubItem(aMetaValue, aTagName, aLocalized, aTag, aToolClass, aLocate, aBehavior);
		arrtibuteMap.put(aTagName, new HashMap());
		for(int i = 0; i < aInfo.length; ++i)
			arrtibuteMap.get(aTagName).put(aInfo[i].a, new Float(aInfo[i].value).doubleValue());
		return this;
	}
	
	public final ItemTool addSubItem(int aMetaValue, String aTagName, String aLocalized, IDataChecker<ISubTagContainer> aTag, String[] aToolClass, ITextureLocation aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		addSubItem(aMetaValue, aTagName, aLocalized, aLocate, aBehavior);
		registryTool(aTagName, aToolClass);
		tagMap.put(aTagName, aTag);
		return this;
	}
	
	public static class AttributesInfo
	{
		IAttribute a;
		float value;
		
		public AttributesInfo(IAttribute aA, float aValue) 
		{
			a = aA;
			value = aValue;
		}
	}
	
	@Override
	public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList) 
	{
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			IDataChecker<ISubTagContainer> tTag = tagMap.get(itemBehaviors.name(tBehavior));
			if(tTag != null)
			{
				for(MaterialAbstract material : materials)
					if(tTag.isTrue(material))
					{
						ItemStack tStack = new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior));
						setupNBT(tStack);
						new ToolMaterialInfo(material, null, 0.0F, null).writeToNBT(tStack.stackTagCompound);
						aList.add(tStack);
					}
			}
			else
			{
				for(MaterialAbstract material : materials)
				{
					ItemStack tStack = new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior));
					setupNBT(tStack);
					new ToolMaterialInfo(material, null, 0.0F, null).writeToNBT(tStack.stackTagCompound);
					aList.add(tStack);
				}
			}
		}
	}
	
	List<Integer> heightLightList = new ArrayList();
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		if(pass == 1)
		{
	    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(stack));
	    	if(tInfo.getMaterialSurface() == null)
	    		return itemIcon;
		}
		if(pass == 2)
		{
	    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(stack));
	    	if(tInfo.getMaterialMosaic() == null)
	    		return itemIcon;
		}
		return super.getIcon(stack, pass);
	}
	
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack aStack, int pass)
    {
    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(aStack));
    	int colorIndex = 0xFFFFFF;
    	if(tInfo.getMaterialBase() == null) return colorIndex;
    	switch(pass)
    	{
    	case 0 :
    		colorIndex = tInfo.getMaterialBase().getPropertyInfo().getColors()[0];
    		break;
    	case 1 :
    		if(tInfo.getCoverLevel() < 0.3F)
    		{
    			colorIndex = tInfo.getMaterialBase().getPropertyInfo().getColors()[1];
    		}
    		else if(tInfo.getMaterialSurface() != null)
    		{
    			colorIndex = tInfo.getMaterialSurface().getPropertyInfo().getColors()[0];
    		}
    		break;
    	case 2 : 
    		colorIndex = tInfo.getMaterialMosaic() == null ? 0xFFFFFF : tInfo.getMaterialMosaic().getPropertyInfo().getColors()[0];
    		break;
    	default : return 0xFFFFFF;
    	}
    	if(heightLightList.contains(getDamage(aStack)))
    	{
            int r = ((colorIndex >> 16 & 255) + 0xFF) / 2;
            int g = ((colorIndex >> 8 & 255) + 0xFF) / 2;
            int b = ((colorIndex & 255) + 0xFF) / 2;
            return (r << 16) + (g << 8) + b;
    	}
    	return colorIndex;
    }
	
	@Override
	public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) 
	{
		ToolMaterialInfo aInfo = new ToolMaterialInfo(setupNBT(aStack));
		aInfo.onRusting(aPlayer.worldObj, (int) aPlayer.posX, (int) aPlayer.posY, (int) aPlayer.posZ);
		aInfo.writeToNBT(aStack.stackTagCompound);
		super.onUpdate(aStack, aWorld, aPlayer, aTimer, aIsInHand);
	}
	
	public float getBaseEffective(ItemStack aStack)
	{
		ToolMaterialInfo aInfo = new ToolMaterialInfo(setupNBT(aStack));
		return aInfo.getHardness();
	}
	
	@Override
	public EnumRarity getRarity(ItemStack aStack)
	{
		return EnumRarity.common;
	}
	
	protected Map<String, Map<IAttribute, Double>> arrtibuteMap = new HashMap();
	
	@Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
		Multimap map = super.getAttributeModifiers(stack);
		if(arrtibuteMap.containsKey(itemBehaviors.name(stack.getItemDamage())))
			for(IAttribute tA : arrtibuteMap.get(itemBehaviors.name(stack.getItemDamage())).keySet())
				map.put(tA.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", arrtibuteMap.get(itemBehaviors.name(stack.getItemDamage())).get(tA), 0));
    	return map;
    }

	static Map<String, IDataChecker<ISubTagContainer>> tagMap = new HashMap();
	static Map<String, Set<String>> toolMap = new HashMap();
	
	protected void registryTool(String aSubName, String...toolClass)
	{
		if(toolClass == null || aSubName == null) return;
		if(!toolMap.containsKey(aSubName))
		{
			toolMap.put(aSubName, new HashSet());
		}
		Set<String> tSet = toolMap.get(aSubName);
		try
		{
			tSet.addAll(Arrays.asList(toolClass));
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) 
	{
		return getToolClasses(stack).contains(toolClass) ? new ToolMaterialInfo(setupNBT(stack)).getToolLevel() : -1;
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack) 
	{
		return toolMap.containsKey(itemBehaviors.name(stack.getItemDamage())) ? toolMap.get(itemBehaviors.name(stack.getItemDamage())) : new HashSet();
	}

	@Override
	public FluidStack getFluid(ItemStack container)
	{
	    isItemStackUsable(container);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(container)));
	    try
	    {
	    	if (tBehavior instanceof IFluidContainerItem)
	    	{
	            return ((IFluidContainerItem) tBehavior).getFluid(container);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public int getCapacity(ItemStack container)
	{
	    isItemStackUsable(container);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(container)));
	    try
	    {
	    	if (tBehavior instanceof IFluidContainerItem)
	    	{
	            return ((IFluidContainerItem) tBehavior).getCapacity(container);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 0;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
	    isItemStackUsable(container);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(container)));
	    try
	    {
	    	if (tBehavior instanceof IFluidContainerItem)
	    	{
	            return ((IFluidContainerItem) tBehavior).fill(container, resource, doFill);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
	    isItemStackUsable(container);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(container)));
	    try
	    {
	    	if (tBehavior instanceof IFluidContainerItem)
	    	{
	            return ((IFluidContainerItem) tBehavior).drain(container, maxDrain, doDrain);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public boolean doCrush(World aWorld, int x, int y, int z, ItemStack aStack) 
	{
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior instanceof ICrushableTool)
	    	{
	            return ((ICrushableTool) tBehavior).doCrush(aWorld, x, y, z, aStack);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}

	@Override
	public ItemStack getOutput(EntityPlayer player, ItemStack aStack) 
	{
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior instanceof ISubPolishTool)
	    	{
	            return ((ISubPolishTool) tBehavior).getOutput(this, aStack, player);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return aStack;
	}

	@Override
	public CraftingState getState(ItemStack aStack, EnumCraftingType aType, CraftingState aState) 
	{
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior instanceof ISubPolishTool)
	    	{
	            return ((ISubPolishTool) tBehavior).getState(this, aStack, aType, aState);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return aState;
	}

	@Override
	public boolean isShootable(ItemStack aTool, ItemStack aStack)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
		{
			if (tBehavior instanceof IArrowItem)
			{
				return ((IArrowItem) tBehavior).isShootable(aTool, aStack);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Entity onShoot(EntityLivingBase aEntity)
	{
		return null;
	}
}