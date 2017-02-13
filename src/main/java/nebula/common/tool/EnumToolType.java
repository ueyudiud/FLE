/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tool;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import nebula.common.base.IRegister;
import nebula.common.base.Register;
import nebula.common.stack.OreStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * The tool type.<br>
 * For most type of tool.
 * @author ueyudiud
 *
 */
public class EnumToolType
{
	public static final IRegister<EnumToolType> REGISTER = new Register();
	public static final EnumToolType HAND = new EnumToolType("hand", "Hand");//The player hand current, do not register tool with this.
	
	public static final EnumToolType
	AXE = new EnumToolType("axe", "Axe"),
	PICKAXE = new EnumToolType("pickaxe", "Pickaxe"),
	SHOVEL = new EnumToolType("shovel", "Shovel"),
	HOE = new EnumToolType("hoe", "Hoe"),
	SWORD = new EnumToolType("sword", "Sword");
	
	public static final List<EnumToolType> HAND_USABLE_TOOL = ImmutableList.of(HAND);
	
	public static IRegister<EnumToolType> getToolList()
	{
		return REGISTER;
	}
	
	public final String name;
	Set<String> toolClass;
	final String dictName;
	final OreStack stack;
	
	public EnumToolType(String name, String dictName)
	{
		REGISTER.register(name, this);
		this.name = name;
		this.toolClass = ImmutableSet.of(name);
		this.stack = new OreStack(this.dictName = ("craftingTool" + dictName));
	}
	
	/**
	 * Create standard tool stack for uses.
	 * @return
	 */
	public OreStack stack()
	{
		return this.stack;
	}
	
	/**
	 * Get ore name.
	 * @return
	 */
	public String ore()
	{
		return this.name;
	}
	
	/**
	 * Match the stack is tool.
	 * @param stack The matched stack.
	 * @return
	 */
	public boolean match(ItemStack stack)
	{
		return stack != null && stack().similar(stack);
	}
	
	public boolean toolMatch(ItemStack stack)
	{
		Item item = stack.getItem();
		return item.getToolClasses(stack).contains(this.name);
	}
	
	public int toolLevelMatch(ItemStack stack)
	{
		Item item = stack.getItem();
		return item.getHarvestLevel(stack, this.name);
	}
	
	public Set<String> getToolClasses()
	{
		return this.toolClass;
	}
	
	/**
	 * Match is tool class.
	 * @param tool The matched tool type.
	 * @return
	 */
	public boolean isToolClass(String tool)
	{
		return this.toolClass.contains(tool);
	}
}