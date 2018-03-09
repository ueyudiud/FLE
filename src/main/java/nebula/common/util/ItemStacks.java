/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.base.A;
import nebula.base.Judgable;
import nebula.common.block.IToolableBlock;
import nebula.common.item.ITool;
import nebula.common.nbt.NBTTagCompoundEmpty;
import nebula.common.stack.AbstractStack;
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
	
	private ItemStacks()
	{
	}
	
	@Nullable
	public static NBTTagCompound writeItemStackToNBT(@Nullable ItemStack stack)
	{
		return stack == null ? null : stack.writeToNBT(new NBTTagCompound());
	}
	
	/**
	 * Some item may override item meta get method, this method will give a
	 * stack with item select meta.
	 * 
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
	 * Validate item stack, the result ItemStack will be
	 * <li>
	 * <code>null</code> - when input is null or its stack size is non-positive.
	 * <li>
	 * The copied non-Wildcard meta stack. - when stack damage is {@link OreDictionary#WILDCARD_VALUE}.
	 * <li>
	 * The input stack - for other condition.
	 * </li>
	 * 
	 * @param stack the input stack.
	 * @return the validated stack.
	 */
	@Nullable
	public static ItemStack valid(@Nullable ItemStack stack)
	{
		if (stack == null || stack.stackSize <= 0) return null;
		if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			stack = stack.copy();
			stack.setItemDamage(0);
		}
		return stack;
	}
	
	/**
	 * Get item stack NBT or create a new stack NBT.
	 * 
	 * @param stack The stack.
	 * @param createTag Should create a new NBT for stack, or only give a
	 *            simulated NBT as empty NBT.
	 * @return the result NBT.
	 */
	@Nonnull
	public static NBTTagCompound getOrSetupNBT(@Nonnull ItemStack stack, boolean createTag)
	{
		if (!stack.hasTagCompound())
		{
			if (createTag)
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
	 * 
	 * @param stack
	 * @param tag
	 * @param createTag
	 * @return
	 */
	public static NBTTagCompound getSubOrSetupNBT(ItemStack stack, String tag, boolean createTag)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
		{
			if (!createTag) return NBTTagCompoundEmpty.INSTANCE;
			stack.setTagInfo(tag, nbt = new NBTTagCompound());
			return nbt;
		}
		return NBTs.getCompound(nbt, tag, createTag);
	}
	
	@SuppressWarnings("unused")
	public static ImmutableList<ItemStack> setSizeOf(List<ItemStack> stacks, int size)
	{
		if (stacks == null || stacks.isEmpty())
			return ImmutableList.of();
		ImmutableList.Builder builder = ImmutableList.builder();
		for (ItemStack stack : stacks)
		{
			if (stack != null)
			{
				builder.add(setSizeOf(stack, size));
			}
		}
		return builder.build();
	}
	
	/**
	 * Copy a new stack with same data of old stack but different size.
	 * 
	 * @param stack
	 * @param size
	 * @return
	 */
	public static ItemStack setSizeOf(ItemStack stack, int size)
	{
		ItemStack ret;
		(ret = stack.copy()).stackSize = size;
		return ret;
	}
	
	public static int sizeOf(@Nullable ItemStack stack)
	{
		return stack == null ? 0 : stack.stackSize;
	}
	
	/**
	 * Copy a new stack no more than selected size.
	 * <p>
	 * This method can be used in inventory by decreasing stack size.
	 * 
	 * @param stack
	 * @param size
	 * @return
	 */
	public static ItemStack copyNomoreThan(@Nullable ItemStack stack, int size)
	{
		return stack == null ? null : stack.stackSize > size ? setSizeOf(stack, size) : stack.copy();
	}
	
	/**
	 * Matched is item and tag is equal.
	 * 
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public static boolean areItemAndTagEqual(ItemStack stack1, ItemStack stack2)
	{
		return stack1 == null || stack2 == null ? stack1 == stack2 : stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
	
	public static boolean areTagEqual(NBTTagCompound nbt1, NBTTagCompound nbt2)
	{
		return nbt1 == null || nbt2 == null ? nbt1 == nbt2 : nbt1.equals(nbt2);
	}
	
	/**
	 * This method should called by item onItemUse.
	 * 
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
		if (stack == null)
		{
			toolTypes = EnumToolType.HAND_USABLE_TOOL;
		}
		else if (stack.getItem() instanceof ITool)
		{
			toolTypes = ((ITool) stack.getItem()).getToolTypes(stack);
		}
		else
			return EnumActionResult.PASS;
		Direction direction = Direction.of(side);
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IToolableBlock)
		{
			IToolableBlock block = (IToolableBlock) state.getBlock();
			for (EnumToolType toolType : toolTypes)
			{
				ActionResult<Float> result = block.onToolClick(player, toolType, getToolLevel(stack, toolType), stack, world, pos, direction, hitX, hitY, hitZ);
				if (result.getType() != EnumActionResult.PASS)
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
	 * 
	 * @param stack
	 * @return
	 */
	public static List<EnumToolType> getCurrentToolType(@Nullable ItemStack stack)
	{
		if (stack == null) return EnumToolType.HAND_USABLE_TOOL;
		if (stack.getItem() instanceof ITool) return ((ITool) stack.getItem()).getToolTypes(stack);
		Set<String> toolClasses = stack.getItem().getToolClasses(stack);
		List<EnumToolType> list = new ArrayList<>();
		for (String toolClass : toolClasses)
		{
			EnumToolType toolType = EnumToolType.getToolList().get(toolClass);
			if (toolType != null)
			{
				list.add(toolType);
			}
			else // If the tool type does not present, create a new tool type.
			{
				list.add(new EnumToolType(toolClass, Strings.validateOre(true, toolClass)));
			}
		}
		return list;
	}
	
	/**
	 * Get tool level with supposed tool type.
	 * 
	 * @param stack
	 * @param toolType
	 * @return
	 */
	public static int getToolLevel(@Nullable ItemStack stack, EnumToolType toolType)
	{
		return stack == null ? (toolType == EnumToolType.HAND ? 0 : -1) : stack.getItem() instanceof ITool ? ((ITool) stack.getItem()).getToolLevel(stack, toolType) : stack.getItem().getHarvestLevel(stack, toolType.name);
	}
	
	public static ItemStack getFromOreDict(String ore)
	{
		List<ItemStack> list = OreDictionary.getOres(ore);
		return list.size() > 0 ? list.get(0).copy() : null;
	}
	
	/**
	 * The custom damage getter.
	 * 
	 * @param stack
	 * @return
	 */
	public static int getCustomDamage(ItemStack stack)
	{
		return getOrSetupNBT(stack, false).getInteger("damage");
	}
	
	/**
	 * The custom damage setter.
	 * 
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
	
	public static List<List<ItemStack>> expandStacks(Collection<? extends AbstractStack> collection)
	{
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		collection.stream().map(AbstractStack::display).forEach(builder::add);
		return builder.build();
	}
}
