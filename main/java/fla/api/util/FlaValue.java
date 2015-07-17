package fla.api.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import fla.api.recipe.IItemChecker.ItemChecker;

public class FlaValue 
{
	public static final int MAX_STACK_SIZE = 64;
	
	public static final int SEALEVEL = 128;
	public static final double BASE_TEMPRETURE = 5D;
	public static final int MAX_GENERATEHEIGHTFROMSEA = 72;
	
	public static final String TEXT_FILE_NAME = "fla";
	public static final String VOID_FILE_NAME = "fla:void";
	
	//Minecraft check.
	public static final ItemChecker AXE_WOODEN = new ItemChecker(Items.wooden_axe, OreDictionary.WILDCARD_VALUE);
	public static final ItemChecker AXE_STONE = new ItemChecker(Items.stone_axe, OreDictionary.WILDCARD_VALUE);
	public static final ItemChecker AXE_IRON = new ItemChecker(Items.iron_axe, OreDictionary.WILDCARD_VALUE);
	public static final ItemChecker AXE_GOLD = new ItemChecker(Items.golden_axe, OreDictionary.WILDCARD_VALUE);
	public static final ItemChecker AXE_DIAMOND = new ItemChecker(Items.diamond_axe, OreDictionary.WILDCARD_VALUE);
	//Tool name.
	public static final String axe = "axe";
	public static final String sword = "sword";
	public static final String pickaxe = "pickaxe";
	public static final String shovel = "shovel";
	public static final String hammer = "hammer";
	public static final String hammer_rock = "rockHammer";
	public static final String hammer_soft = "softHammer";
	public static final String whetstone = "whetstone";
	public static final String oil_lamp = "lamp";
	public static final String drilling_firing = "drillingFiring";
	public static final String awl = "awl";
	//Tool material.
	public static final ToolMaterial flint_a = EnumHelper.addToolMaterial("flint_hard", 0, 6, 1.2F, 1.5F, 3);
	public static final ToolMaterial flint_b = EnumHelper.addToolMaterial("flint_hard", 0, 18, 1.5F, 1.5F, 3);
	public static final ToolMaterial stone_a = EnumHelper.addToolMaterial("stone_hard", 0, 16, 1.8F, 1.5F, 2);
	public static final ToolMaterial stone_b = EnumHelper.addToolMaterial("stone_hard", 0, 32, 2.0F, 1.5F, 2);
	
	public static final int CAPACITY_OIL_LAMP = 1000;
	
	public static int ALL_RENDER_ID;
}
