/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.common.block.IToolableBlock;
import nebula.common.item.ITool;
import nebula.common.nbt.NBTTagCompoundEmpty;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.ArrayStack;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import nebula.common.tool.EnumToolType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ueyudiud
 */
public final class ItemStacks
{
	public static final UnaryOperator<ItemStack> COPY_ITEMSTACK = ItemStack::copyItemStack;
	
	private ItemStacks() {}
	
	@Nullable
	public static NBTTagCompound writeItemStackToNBT(@Nullable ItemStack stack)
	{
		return stack == null ? null : stack.writeToNBT(new NBTTagCompound());
	}
	
	/**
	 * Some item may override item meta get method,
	 * this method will give a stack with item select
	 * meta.
	 * @param item
	 * @param meta
	 * @return
	 */
	public static ItemStack stack(Item item, int meta)
	{
		ItemStack stack = new ItemStack(item, 1);
		stack.setItemDamage(meta);
		return stack;
	}
	
	/**
	 * Validate item stack, the stack size is negative will returned as null.
	 * @param stack
	 * @return
	 */
	@Nullable
	public static ItemStack valid(@Nullable ItemStack stack)
	{
		if(stack == null || stack.stackSize <= 0)
			return null;
		if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			stack = stack.copy();
			stack.setItemDamage(0);
		}
		return stack;
	}
	
	/**
	 * Get item stack NBT or create a new stack NBT.
	 * @param stack The stack.
	 * @param createTag Should create a new NBT for stack, or only give a simulated NBT as empty NBT.
	 * @return
	 */
	@Nonnull
	public static NBTTagCompound getOrSetupNBT(ItemStack stack, boolean createTag)
	{
		if(!stack.hasTagCompound())
		{
			if(createTag)
			{
				stack.setTagCompound(new NBTTagCompound());
				return stack.getTagCompound();
			}
			return NBTTagCompoundEmpty.INSTANCE;
		}
		return stack.getTagCompound();
	}
	
	/**
	 * Get sub NBT compound.
	 * @param stack
	 * @param tag
	 * @param createTag
	 * @return
	 */
	public static NBTTagCompound getSubOrSetupNBT(ItemStack stack, String tag, boolean createTag)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
		{
			if(!createTag) return NBTTagCompoundEmpty.INSTANCE;
			stack.setTagInfo(tag, nbt = new NBTTagCompound());
			return nbt;
		}
		return NBTs.getCompound(nbt, tag, createTag);
	}
	
	public static ImmutableList<ItemStack> sizeOf(List<ItemStack> stacks, int size)
	{
		if(stacks == null || stacks.isEmpty()) return ImmutableList.of();
		ImmutableList.Builder builder = ImmutableList.builder();
		for(ItemStack stack : stacks)
			if(stack != null)
			{
				ItemStack stack2 = stack.copy();
				stack2.stackSize = size;
				builder.add(valid(stack2.copy()));
			}
		return builder.build();
	}
	
	/**
	 * Copy a new stack with same data of old stack but different size.
	 * @param stack
	 * @param size
	 * @return
	 */
	public static ItemStack sizeOf(ItemStack stack, int size)
	{
		ItemStack ret;
		(ret = stack.copy()).stackSize = size;
		return ret;
	}
	
	/**
	 * Copy a new stack no more than selected size.<p>
	 * This method can be used in inventory by decreasing stack size.
	 * @param stack
	 * @param size
	 * @return
	 */
	public static ItemStack copyNomoreThan(@Nullable ItemStack stack, int size)
	{
		return stack == null ? null : stack.stackSize > size ? sizeOf(stack, size) : stack.copy();
	}
	
	@Deprecated
	public static AbstractStack sizeOf(AbstractStack stack, int size)
	{
		return size <= 0 ? null : stack instanceof BaseStack ?
				BaseStack.sizeOf((BaseStack) stack, size) :
					stack instanceof ArrayStack ?
							ArrayStack.sizeOf((ArrayStack) stack, size) :
								stack instanceof OreStack ?
										OreStack.sizeOf((OreStack) stack, size) : null;
	}
	
	/**
	 * Matched is item and tag is equal.
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public static boolean isItemAndTagEqual(ItemStack stack1, ItemStack stack2)
	{
		return stack1 == null || stack2 == null ?
				stack1 == stack2 :
					stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
	
	public static boolean areTagEqual(NBTTagCompound nbt1, NBTTagCompound nbt2)
	{
		return nbt1 == null || nbt2 == null ? nbt1 == nbt2 : nbt1.equals(nbt2);
	}
	
	/**
	 * This method should called by item onItemUse.
	 * @param stack The using stack.
	 * @param player The player.
	 * @param world
	 * @param pos
	 * @param side
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return
	 */
	public static EnumActionResult onUseOnBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		List<EnumToolType> toolTypes;
		if(stack == null)
		{
			toolTypes = EnumToolType.HAND_USABLE_TOOL;
		}
		else if(stack.getItem() instanceof ITool)
		{
			toolTypes = ((ITool) stack.getItem()).getToolTypes(stack);
		}
		else return EnumActionResult.PASS;
		Direction direction = Direction.of(side);
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof IToolableBlock)
		{
			IToolableBlock block = (IToolableBlock) state.getBlock();
			for(EnumToolType toolType : toolTypes)
			{
				ActionResult<Float> result = block.onToolClick(player, toolType, stack, world, pos, direction, hitX, hitY, hitZ);
				if(result.getType() != EnumActionResult.PASS)
				{
					if (stack.getItem() instanceof ITool)
					{
						ITool tool = (ITool) stack.getItem();
						tool.onToolUse(player, stack, toolType, L.cast(result.getResult()));
					}
					return result.getType();
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
	/**
	 * Get all kinds of tool type this stack can be.
	 * @param stack
	 * @return
	 */
	public static List<EnumToolType> getCurrentToolType(@Nullable ItemStack stack)
	{
		if(stack == null) return EnumToolType.HAND_USABLE_TOOL;
		if(stack.getItem() instanceof ITool)
			return ((ITool) stack.getItem()).getToolTypes(stack);
		Set<String> toolClasses = stack.getItem().getToolClasses(stack);
		List<EnumToolType> list = new ArrayList<>();
		for (String toolClass : toolClasses)
		{
			EnumToolType toolType = EnumToolType.getToolList().get(toolClass);
			if (toolType != null)
			{
				list.add(toolType);
			}
			else //If the tool type does not present, create a new tool type.
			{
				list.add(new EnumToolType(toolClass, Strings.validateOre(true, toolClass)));
			}
		}
		return list;
	}
	
	/**
	 * Get tool level with supposed tool type.
	 * @param stack
	 * @param toolType
	 * @return
	 */
	public static int getToolLevel(@Nullable ItemStack stack, EnumToolType toolType)
	{
		if(stack == null)
			return -1;
		if(stack.getItem() instanceof ITool)
			return ((ITool) stack.getItem()).getToolLevel(stack, toolType);
		return toolType.match(stack) ? 1 : -1;
	}
	
	public static ItemStack getFromOreDict(String ore)
	{
		List<ItemStack> list = OreDictionary.getOres(ore);
		return list.size() > 0 ? list.get(0).copy() : null;
	}
	
	/**
	 * The custom damage getter.
	 * @param stack
	 * @return
	 */
	public static int getCustomDamage(ItemStack stack)
	{
		return getOrSetupNBT(stack, false).getInteger("damage");
	}
	
	/**
	 * The custom damage setter.
	 * @param stack
	 * @param damage
	 */
	public static void setCustomDamage(ItemStack stack, int damage)
	{
		if (damage == 0)
		{
			if (stack.hasTagCompound())
			{
				stack.getTagCompound().removeTag("damage");
				if (stack.getTagCompound().hasNoTags())
				{
					stack.setTagCompound(null);
				}
			}
		}
		else
		{
			getOrSetupNBT(stack, true).setInteger("damage", damage);
		}
	}
	
	public static void damageTool(ItemStack stack, float amount, EntityLivingBase user, EnumToolType type)
	{
		if (stack.getItem() instanceof ITool)
		{
			((ITool) stack.getItem()).onToolUse(user, stack, type, amount);
		}
		else
		{
			stack.damageItem(MathHelper.ceil(amount), user);
		}
	}
	
	public static ItemStack[] copyStacks(ItemStack[] stacks)
	{
		return A.transform(stacks, ItemStack.class, COPY_ITEMSTACK);
	}
	
	public static void copyItemStackWithOutNBT(ItemStack src, ItemStack target)
	{
		target.setItem(src.getItem());
		target.setItemDamage(src.getItemDamage());
	}
	
	public static void copyItemStackWithNBT(ItemStack src, ItemStack target)
	{
		int size = target.stackSize;
		target.readFromNBT(src.serializeNBT());
		target.stackSize = size;
	}
}