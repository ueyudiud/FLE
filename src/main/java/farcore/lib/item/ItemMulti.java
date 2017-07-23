/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.item;

import java.util.List;

import farcore.FarCore;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.enviornment.EnviornmentEntity;
import nebula.common.enviornment.IEnvironment;
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

public class ItemMulti extends ItemBase implements IUpdatableItem
{
	/**
	 * General method for multiple item, use to get material from stack.
	 * Return VOID material if stack is invalid.
	 * @see farcore.lib.material.Mat
	 * @param stack
	 * @return
	 */
	public static Mat getMaterial(ItemStack stack)
	{
		if(stack != null && stack.getItem() instanceof ItemMulti)
			return ((ItemMulti) stack.getItem()).getMaterialFromItem(stack);
		else
			return Mat.VOID;
	}
	
	/**
	 * Get sub meta from stack.
	 * @param stack the stack should be predicated that {@code stack.getItem() instanceof ItemMulti == true}.
	 * @return the sub meta of stack.
	 * @see nebula.common.item.ItemBase#getStackMetaOffset
	 */
	public static int getSubMeta(ItemStack stack)
	{
		if(stack != null && stack.getItem() instanceof ItemMulti)
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
		if (condition.isBelongTo(material) && condition.instance != null)
		{
			ItemStack stack = new ItemStack(condition.instance, size);
			condition.instance.setMaterialToItem(stack, material);
			return stack;
		}
		return null;
	}
	
	public final MatCondition condition;
	protected boolean enableChemicalFormula = true;
	protected boolean registerToOreDict = true;
	
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
		for(Mat material : Mat.filt(this.condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			if (material.itemProp != null)
			{
				for (int i = 0; i < material.itemProp.getOffsetMetaCount(); ++i)
				{
					ItemStack s1 = templete.copy();
					material.itemProp.setInstanceFromMeta(s1, i, material, this.condition);
					LanguageManager.registerLocal(getTranslateName(s1),
							this.condition.getLocal(material.itemProp.getReplacedLocalName(i, material)));
				}
			}
			else
			{
				LanguageManager.registerLocal(
						getTranslateName(templete),//If there will any replaced exist.
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
		for(Mat material : Mat.filt(this.condition))
		{
			ItemStack stack = new ItemStack(itemIn, 1, material.id);
			if (material.itemProp != null)
			{
				for (int i = 0; i < material.itemProp.getOffsetMetaCount(); ++i)
				{
					ItemStack stack2 = stack.copy();
					material.itemProp.setInstanceFromMeta(stack2, i, material, this.condition);
					subItems.add(stack2);
				}
			}
			else
				subItems.add(stack);
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
		if(!entityIn.world.isRemote)
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
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if(!entityItem.world.isRemote)
		{
			ItemStack stack = ((IUpdatableItem) this).updateItem(new EnviornmentEntity(entityItem), entityItem.getEntityItem());
			if(stack == null)
			{
				entityItem.setDead();
				return false;
			}
			else if(stack != entityItem.getEntityItem())
			{
				entityItem.setEntityItemStack(stack);
			}
			if(stack.getItem() != this)
				return true;
		}
		return false;
	}
	
	@Override
	public ItemStack updateItem(IEnvironment environment, ItemStack stack)
	{
		Mat material = getMaterialFromItem(stack);
		if(material.itemProp != null)
		{
			stack = material.itemProp.updateItem(stack, material, this.condition, environment);
		}
		return stack;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return this.condition.stackLimit;
	}
	
	@Override
	public int getStackMetaOffset(ItemStack stack)
	{
		Mat material = getMaterialFromItem(stack);
		if(material != null && material.itemProp != null)
			return material.itemProp.getMetaOffset(stack, material, this.condition);
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if(this.enableChemicalFormula)
		{
			unlocalizedList.addNotNull("info.material.chemical.formula." + getMaterialFromItem(stack));
		}
		unlocalizedList.addToolTip("info.material.custom." + getMaterialFromItem(stack).name);
		Mat material = getMaterialFromItem(stack);
		if(material.itemProp != null)
		{
			material.itemProp.addInformation(stack, material, this.condition, unlocalizedList);
		}
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		super.setDamage(stack, damage & 0x7FFF);
		Mat material = getMaterialFromItem(stack);
		if(material.itemProp != null)
		{
			material.itemProp.setInstanceFromMeta(stack, damage >> 15, material, this.condition);
		}
	}
}