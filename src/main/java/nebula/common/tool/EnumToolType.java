/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tool;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import nebula.base.IRegister;
import nebula.base.Register;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.OreStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * The tool type.
 * <p>
 * For most type of tool.
 * 
 * @author ueyudiud
 */
public class EnumToolType
{
	public static final IRegister<EnumToolType>	REGISTER	= new Register<>();
	/** The player hand. */
	public static final EnumToolType			HAND		= new EnumToolType("hand", "Hand");		// The
																									// player
																									// hand
																									// current,
																									// do
																									// not
																									// register
																									// tool
																									// with
																									// this.
	
	/**
	 * The general 5 vanilla types of tools exist in Minecraft.
	 */
	public static final EnumToolType AXE = new EnumToolType("axe", "Axe"), PICKAXE = new EnumToolType("pickaxe", "Pickaxe"), SHOVEL = new EnumToolType("shovel", "Shovel"), HOE = new EnumToolType("hoe", "Hoe"), SWORD = new EnumToolType("sword", "Sword");
	
	public static final List<EnumToolType> HAND_USABLE_TOOL = ImmutableList.of(HAND);
	
	public static IRegister<EnumToolType> getToolList()
	{
		return REGISTER;
	}
	
	public final String		name;
	protected Set<String>	toolClass;
	final String			dictName;
	final AbstractStack		stack;
	
	protected EnumToolType(String registeredName, String name, String dictName, AbstractStack stack)
	{
		REGISTER.register(registeredName, this);
		this.name = name;
		this.dictName = dictName;
		this.stack = stack;
	}
	
	/**
	 * Register a new tool type.
	 * 
	 * @param name the name of tool.
	 * @param dictName the OD name.
	 */
	public EnumToolType(String name, String dictName)
	{
		REGISTER.register(name, this);
		this.name = name;
		this.toolClass = ImmutableSet.of(name);
		this.stack = new OreStack(this.dictName = ("craftingTool" + dictName));
	}
	
	/**
	 * Create standard tool stack for uses.
	 * 
	 * @return
	 */
	public AbstractStack stack()
	{
		return this.stack;
	}
	
	/**
	 * Get the OD name, it is start with <code>"craftingTool"</code> and with
	 * other postfix behind.
	 * 
	 * @return
	 */
	public String ore()
	{
		return this.dictName;
	}
	
	/**
	 * Match the stack is tool.
	 * 
	 * @param stack The matched stack.
	 * @return
	 */
	public boolean match(ItemStack stack)
	{
		return stack != null && stack().similar(stack);
	}
	
	public boolean toolMatch(ItemStack stack)
	{
		if (stack == null) return this == HAND;
		Item item = stack.getItem();
		return item.getToolClasses(stack).contains(this.name);
	}
	
	public int toolLevelMatch(ItemStack stack)
	{
		if (stack == null) return this == HAND ? 0 : -1;
		Item item = stack.getItem();
		return item.getHarvestLevel(stack, this.name);
	}
	
	public Set<String> getToolClasses()
	{
		return this.toolClass;
	}
	
	/**
	 * Match tool can be used in this tool tag.
	 * 
	 * @param tool the matched tool tag.
	 * @return <code>true</code> if this tool can be used as this tool tag.
	 */
	public boolean isToolClass(String tool)
	{
		return this.toolClass.contains(tool);
	}
}
