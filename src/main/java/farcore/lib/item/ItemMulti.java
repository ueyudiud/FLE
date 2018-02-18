/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.item;

import java.util.List;

import farcore.FarCore;
import farcore.data.Capabilities;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.material.behavior.MaterialPropertyManager.MaterialHandler;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.environment.EnviornmentEntity;
import nebula.common.environment.IEnvironment;
import nebula.common.item.IUpdatableItem;
import nebula.common.item.ItemBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Material provided, multi-generated item type. Used in many items with similar
 * function and different properties.
 * 
 * @author ueyudiud
 * @see nebula.common.item.ItemSubBehavior
 */
public class ItemMulti extends ItemBase implements IUpdatableItem, IMaterialCapabilityCreative
{
	/**
	 * General method for multiple item, use to get material from stack. Return
	 * VOID material if stack is invalid.
	 * 
	 * @see farcore.lib.material.Mat
	 * @param stack
	 * @return
	 */
	public static Mat getMaterial(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemMulti)
			return ((ItemMulti) stack.getItem()).getMaterialFromItem(stack);
		else
			return Mat.VOID;
	}
	
	public static MatCondition getMaterialCondition(ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemMulti ?
				((ItemMulti) stack.getItem()).condition : null;
	}
	
	/**
	 * Get sub meta from stack.
	 * 
	 * @param stack the stack should be predicated that
	 *              <code>stack.getItem() instanceof ItemMulti == true</code>
	 * @return the sub meta of stack.
	 * @see nebula.common.item.ItemBase#getStackMetaOffset
	 */
	public static int getSubMeta(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemMulti)
			return ((ItemMulti) stack.getItem()).getStackMetaOffset(stack);
		else
			return 0;
	}
	
	public static ItemStack createStack(Mat material, MatCondition condition)
	{
		return createStack(material, condition, 1);
	}
	
	public static ItemStack createStack(Mat material, MatCondition condition, int size)
	{
		if (condition.instance != null)
		{
			if (condition.isBelongTo(material))
			{
				ItemStack stack = new ItemStack(condition.instance, size);
				condition.instance.setMaterialToItem(stack, material);
				return stack;
			}
			else if (material == Mat.VOID)
			{
				return new ItemStack(condition.instance, size);
			}
		}
		return null;
	}
	
	public final MatCondition	condition;
	protected boolean			enableChemicalFormula	= true;
	protected boolean			registerToOreDict		= true;
	
	public ItemMulti(MatCondition mc)
	{
		this(FarCore.ID, mc);
		this.hasSubtypes = true;
	}
	
	public ItemMulti(String modid, MatCondition mc)
	{
		super(modid, "multi." + mc.name);
		this.condition = mc;
		this.condition.instance = this;
		this.hasSubtypes = true;
	}
	
	public ItemMulti setDisableRegisterToOreDict()
	{
		this.registerToOreDict = false;
		return this;
	}
	
	/**
	 * Get translation of displaying tool tip.
	 * 
	 * @param stack
	 * @param tag
	 * @return
	 */
	protected String getTranslateInformation(ItemStack stack, String tag)
	{
		return getUnlocalizedName(stack) + "." + tag + ".info";
	}
	
	@Override
	public void postInitalizedItems()
	{
		for (Mat material : Mat.filt(this.condition, false))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			if (material.itemProp != null)
			{
				for (int i = 0; i < material.itemProp.getOffsetMetaCount(); ++i)
				{
					ItemStack stack = templete.copy();
					stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).setMetaOffset(i, material);
					LanguageManager.registerLocal(getTranslateName(stack), this.condition.getLocal(material.itemProp.getReplacedLocalName(i, material)));
				}
			}
			else
			{
				LanguageManager.registerLocal(getTranslateName(templete),
						// If there will any replaced exist.
						this.condition.getLocal(material));
			}
			if (this.registerToOreDict)
			{
				this.condition.registerOre(material, templete);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(this.modid, "group/" + this.condition.name));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (Mat material : Mat.filt(this.condition))
		{
			if (material.itemProp != null)
			{
				int length = material.itemProp.getOffsetMetaCount();
				for (int i = 0; i < length; ++i)
				{
					ItemStack stack = new ItemStack(itemIn, 1, material.id);
					stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).setMetaOffset(i, material);
					subItems.add(stack);
				}
			}
			else
				subItems.add(new ItemStack(itemIn, 1, material.id));
		}
	}
	
	protected Mat getMaterialFromItem(ItemStack stack)
	{
		return Mat.material(getBaseDamage(stack), Mat.VOID);
	}
	
	protected void setMaterialToItem(ItemStack stack, Mat material)
	{
		setDamage(stack, material.id);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!entityIn.world.isRemote)
		{
			stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityIn), stack);
			if (entityIn instanceof EntityPlayer)
			{
				if (stack == null)
				{
					((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
					return;
				}
			}
			if (stack.getItem() != this) return;
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if (!entityItem.world.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityItem), entityItem.getEntityItem());
			if (stack == null)
			{
				entityItem.setDead();
				return false;
			}
			else if (stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if (stack.getItem() != this) return true;
		}
		return false;
	}
	
	@Override
	public ItemStack updateItem(IEnvironment environment, ItemStack stack)
	{
		return stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).updateItem(stack, environment);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return this.condition.stackLimit;
	}
	
	@Override
	public int getStackMetaOffset(ItemStack stack)
	{
		return stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).getMetaOffset();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if (this.enableChemicalFormula)
		{
			unlocalizedList.addNotNull("info.material.chemical.formula." + getMaterialFromItem(stack));
		}
		unlocalizedList.addToolTip("info.material.custom." + getMaterialFromItem(stack).name);
		stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).addInformation(unlocalizedList);
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		super.setDamage(stack, damage & 0x7FFF);
		Mat material = getMaterialFromItem(stack);
		stack.getCapability(Capabilities.CAPABILITY_MATERIAL, null).setMetaOffset(damage >> 15, material);
	}
	
	@Override
	protected boolean hasCapability()
	{
		return true;
	}
	
	public MaterialHandler createMaterialHandler(ItemStack stack)
	{
		return new MaterialHandler(this.condition, getMaterialFromItem(stack));
	}
}
