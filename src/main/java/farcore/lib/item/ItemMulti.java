package farcore.lib.item;

import java.util.List;

import farcore.FarCore;
import farcore.data.M;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.util.EnviornmentEntity;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.lib.world.IEnvironment;
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
	public static Mat getMaterial(ItemStack stack)
	{
		if(stack != null && stack.getItem() instanceof ItemMulti)
			return ((ItemMulti) stack.getItem()).getMaterialFromItem(stack);
		else
			return M.VOID;
	}
	
	public final MatCondition condition;
	protected boolean enableChemicalFormula = true;

	public ItemMulti(MatCondition mc)
	{
		this(FarCore.ID, mc);
		hasSubtypes = true;
	}
	public ItemMulti(String modid, MatCondition mc)
	{
		super(modid, "multi." + mc.name);
		condition = mc;
		hasSubtypes = true;
	}

	protected String getTranslateInformation(ItemStack stack, String tag)
	{
		return getUnlocalizedName(stack) + "." + tag + ".info";
	}

	@Override
	public void postInitalizedItems()
	{
		for(Mat material : Mat.filt(condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			LanguageManager.registerLocal(getTranslateName(templete), condition.getLocal(material));
			condition.registerOre(material, templete);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		FarCoreItemModelLoader.registerModel(this, new ResourceLocation(modid, "group/" + condition.name));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(Mat material : Mat.filt(condition))
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
		if(!entityIn.worldObj.isRemote)
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
		if(!entityItem.worldObj.isRemote)
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
			stack = material.itemProp.updateItem(stack, material, condition, environment);
		}
		return stack;
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return condition.stackLimit;
	}
	
	@Override
	public int getStackMetaOffset(ItemStack stack)
	{
		Mat material = getMaterialFromItem(stack);
		if(material != null && material.itemProp != null)
			return material.itemProp.getMetaOffset(stack, material, condition);
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if(enableChemicalFormula)
		{
			unlocalizedList.addNotNull("info.material.chemical.formula." + getMaterialFromItem(stack));
		}
		unlocalizedList.addNotNull("info.material.custom." + getMaterialFromItem(stack).name);
		Mat material = getMaterialFromItem(stack);
		if(material.itemProp != null)
		{
			material.itemProp.addInformation(stack, material, condition, unlocalizedList);
		}
	}

	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		super.setDamage(stack, damage & 0x7FFF);
		Mat material = getMaterialFromItem(stack);
		if(material.itemProp != null)
		{
			material.itemProp.setInstanceFromMeta(stack, damage >> 15, material, condition);
		}
	}
}