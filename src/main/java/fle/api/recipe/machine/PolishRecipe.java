package fle.api.recipe.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.enums.EnumToolType;
import farcore.interfaces.ICondition;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.stack.AbstractStack;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class PolishRecipe 
{
	private static Map<AbstractStack, PolishResource> resourceMap = new HashMap();
	private static Map<EnumToolType, PolishTool> toolMap = new EnumMap(EnumToolType.class);
	private static List<$Recipe> recipes = new ArrayList();
	
	public static final IRegister<PolishCondition> conditionRegister = new Register();
	
	public static final PolishCondition DEFAULT = new PolishCondition("default", "Default", ' ').setTextureName("fle:default");
	
	public static interface IPolishableTool
	{
		int getPolishLevel(ItemStack stack);
	}
	
	public static int getToolLevel(ItemStack stack)
	{
		if(stack == null) return -1;
		for(Entry<EnumToolType, PolishTool> entry : toolMap.entrySet())
		{
			if(entry.getKey().match(stack))
			{
				int level;
				for(IPolishableTool tool : entry.getValue().craftLevel)
				{
					if((level = tool.getPolishLevel(stack)) >= 0)
					{
						return level;
					}
				}
			}
		}
		return -1;
	}
	
	public static AbstractStack isResource(ItemStack stack)
	{
		for(Entry<AbstractStack, PolishResource> entry : resourceMap.entrySet())
		{
			if(entry.getKey().contain(stack))
			{
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static int getResourceLevel(ItemStack stack)
	{
		if(stack == null) return -1;
		for(Entry<AbstractStack, PolishResource> entry : resourceMap.entrySet())
		{
			if(entry.getKey().contain(stack))
			{
				return entry.getValue().craftLevel;
			}
		}
		return -1;
	}
	
	public static PolishCondition onCrafting(ItemStack tool, PolishCondition condition)
	{
		for(Entry<EnumToolType, PolishTool> entry : toolMap.entrySet())
		{
			if(entry.getKey().match(tool))
			{
				PolishCondition condition1 = entry.getValue().craftMap.get(condition);
				if(condition1 != null)
				{
					return condition1;
				}
			}
		}
		return condition;
	}
	
	public static ItemStack getResult(ItemStack input, PolishCondition[][] conditions)
	{
		for($Recipe recipe : recipes)
		{
			if(recipe.input.contain(input) && Arrays.deepEquals(recipe.conditions, conditions))
				return recipe.output.copy();
		}
		return null;
	}
	
	public static void registerResource(AbstractStack stack, int level)
	{
		PolishResource resource = new PolishResource();
		resource.craftLevel = level;
		resourceMap.put(stack, resource);
	}

	public static void registerTool(EnumToolType toolType, AbstractStack stack, int level)
	{
		registerTool(toolType, (ItemStack target) -> {return stack.contain(target) ? level : -1;});
	}
	public static void registerTool(EnumToolType toolType, IPolishableTool target)
	{
		if(!toolMap.containsKey(toolType))
		{
			toolMap.put(toolType, new PolishTool());
		}
		toolMap.get(toolType).craftLevel.add(target);
	}
	
	public static void registerToolCrafting(EnumToolType toolType, char input, char output)
	{
		if(!toolMap.containsKey(toolType))
		{
			toolMap.put(toolType, new PolishTool());
		}
		if(!conditionRegister.contain(input) || !conditionRegister.contain(output))
			throw new RuntimeException();
		toolMap.get(toolType).craftMap.put(conditionRegister.get(input), conditionRegister.get(output));
	}
	
	public static void registerRecipe(AbstractStack input, String map, ItemStack output)
	{
		$Recipe recipe = new $Recipe();
		recipe.input = input;
		if(map.length() != 9) throw new IllegalArgumentException("The input map length must be 9!");
		recipe.conditions = new PolishCondition[3][3];
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				recipe.conditions[i][j] = conditionRegister.get(map.charAt(i * 3 + j), DEFAULT);
		recipe.output = output.copy();
		recipes.add(recipe);
	}
	
	private static class $Recipe
	{
		public AbstractStack input;
		public PolishCondition[][] conditions;
		public ItemStack output;
	}
	
	public static class PolishTool
	{
		public List<IPolishableTool> craftLevel = new ArrayList();
		public Map<PolishCondition, PolishCondition> craftMap = new HashMap();
	}
	
	public static class PolishResource
	{
		public int craftLevel;
	}
	
	public static class PolishCondition implements ICondition
	{
		public final char chr;
		
		private final String name;
		
		int passes;
		String textureName;

		public PolishCondition(String name, String localized, char chr)
		{
			this(name, chr);
			FarCoreSetup.lang.registerLocal(name + ".name", localized);
		}
		public PolishCondition(String name, char chr)
		{
			if(conditionRegister.contain(name) ||
					conditionRegister.contain(chr)) throw new RuntimeException("The name " + name + " or character of condition '" + chr + "'' has already registered.");
			this.chr = chr;
			this.name = name;
			conditionRegister.register(chr, name, this);
		}
		
		@SideOnly(Side.CLIENT)
		IIcon[] icons;
		
		public String getLocalizedName()
		{
			return FarCore.translateToLocal(name + ".name");
		}

		public PolishCondition setTextureName(String name)
		{
			this.textureName = name;
			this.passes = 1;
			return this;
		}
		public PolishCondition setTextureName(String name, int pass)
		{
			this.textureName = name;
			this.passes = pass;
			return this;
		}
		
		public String name()
		{
			return name;
		}
		
		@SideOnly(Side.CLIENT)
		public IIcon[] getIcons()
		{
			return icons;
		}
		
		@SideOnly(Side.CLIENT)
		public void registerIcons(IIconRegister register)
		{
			if(passes != 1)
			{
				icons = new IIcon[passes];
				for(int i = 0; i < passes; ++i)
				{
					icons[i] = register.registerIcon(textureName + "/" + i);
				}
			}
			else
			{
				icons = new IIcon[]{register.registerIcon(textureName)};
			}
		}
		
		@Override
		public int hashCode()
		{
			return Character.hashCode(chr);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				!(obj instanceof PolishCondition) ? false :
					((PolishCondition) obj).chr == chr;
		}
	}
}