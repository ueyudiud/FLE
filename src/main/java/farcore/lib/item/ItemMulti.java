package farcore.lib.item;

import java.util.List;

import farcore.FarCore;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.model.NebulaItemModelLoader;
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
	
	public final MatCondition condition;
	protected boolean enableChemicalFormula = true;
	
	public ItemMulti(MatCondition mc)
	{
		this(FarCore.ID, mc);
		this.hasSubtypes = true;
	}
	public ItemMulti(String modid, MatCondition mc)
	{
		super(modid, "multi." + mc.name);
		this.condition = mc;
		this.hasSubtypes = true;
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
			LanguageManager.registerLocal(getTranslateName(templete), this.condition.getLocal(material));
			this.condition.registerOre(material, templete);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaItemModelLoader.registerModel(this, new ResourceLocation(this.modid, "group/" + this.condition.name));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.filt(this.condition))
		{
			subItems.add(new ItemStack(itemIn, 1, material.id));
		}
	}
	
	protected Mat getMaterialFromItem(ItemStack stack)
	{
		return Mat.material(getBaseDamage(stack));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		ItemStack stack2 = stack;
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