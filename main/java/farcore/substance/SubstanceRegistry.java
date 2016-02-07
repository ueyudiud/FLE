package farcore.substance;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import farcore.util.Part;
import farcore.util.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SubstanceRegistry
{
	private static final Map<Key$, String> substanceMap = new HashMap();
	private static final Map<String, Key$> stackMap = new HashMap();

	private static ItemStack decodelizeI(String code)
	{
		if(code.startsWith("item:"))
		{
			String c = code.substring("item:".length());
			int meta = 0;
			if(c.indexOf('@') != -1)
			{
				String[] strings = c.split("@");
				c = strings[0];
				meta = Integer.valueOf(strings[1]);
			}
			Item item = GameData.getItemRegistry().getObject(c);
			if(item == null) return null;
			return new ItemStack(item, 1, meta);
		}
		else if(code.startsWith("ore:"))
		{
			String c = code.substring("ore:".length());
			if(!OreDictionary.getOres(c).isEmpty())
				return null;
			ItemStack stack = OreDictionary.getOres(c).get(0).copy();
			if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE 
					&& stack.getHasSubtypes())
				stack.setItemDamage(0);
			return stack;
		}
		else throw new RuntimeException("Can not decode " + code + ".");
	}
	private static String codelize(ItemStack stack, boolean flag)
	{
		return stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && !flag ?
				"item:" + GameData.getItemRegistry().getNameForObject(stack.getItem()) :
				"item:" + GameData.getItemRegistry().getNameForObject(stack.getItem()) 
				+ "@" + stack.getItemDamage();
	}
	private static String keycodelize(ItemStack stack, boolean flag)
	{
		return flag ?
				"item:" + GameData.getItemRegistry().getNameForObject(stack.getItem()) :
				"item:" + GameData.getItemRegistry().getNameForObject(stack.getItem()) 
				+ "@" + stack.getItemDamage();
	}
	private static String codelize(String string)
	{
		return "ore:" + string;
	}

	public static void register(Substance substance, Part part, 
			String oreName)
	{
		Key$ key$ = new Key$(substance, part);
		String code = codelize(oreName);
		if(stackMap.containsKey(code))
		{
			throw new RuntimeException(
					String.format(
							"Ore name %s has already "
							+ "register a substance stack, "
							+ "please check your mod weather "
							+ "it is out of date.", oreName));
		}
		else
		{
			stackMap.put(code, key$);
			if(!substanceMap.containsKey(key$))
			{
				substanceMap.put(key$, code);
			}
		}
	}
	public static void register(Substance substance, Part part, 
			ItemStack stack)
	{
		register(substance, part, stack, false);
	}
	public static void register(Substance substance, Part part, 
			ItemStack stack, boolean flag)
	{
		Key$ key$ = new Key$(substance, part);
		String code = codelize(stack, flag);
		if(stackMap.containsKey(code))
		{
			throw new RuntimeException(
					String.format(
							"Item stack %s has already "
							+ "register a substance stack, "
							+ "please check your mod weather "
							+ "it is out of date.", code));
		}
		else
		{
			stackMap.put(code, key$);
			if(!substanceMap.containsKey(key$))
			{
				substanceMap.put(key$, code);
			}
		}
//		OreDictionary.registerOre(
//				Util.oreDictFormat(part.name, "\\.") + 
//				Util.oreDictFormat(substance.getName(), " ", false), stack);
	}
	public static ItemStack getItemStack(Substance substance, Part part)
	{
		Key$ key$ = new Key$(substance, part);
		return substanceMap.containsKey(key$) ?
				decodelizeI(substanceMap.get(key$)) : null;
	}
	public static Key$ getSubstance(ItemStack stack)
	{
		String code = keycodelize(stack, false);
		if(stackMap.containsKey(code))
			return stackMap.get(code);
		code = keycodelize(stack, true);
		if(stackMap.containsKey(code))
			return stackMap.get(code);
		return null;
	}
	public static Key$ getSubstance(ItemStack stack, boolean flag)
	{
		if(flag)
		{
			String code = codelize(stack, false);
			if(stackMap.containsKey(code))
				return stackMap.get(code);
			return null;
		}
		else
		{
			return getSubstance(stack);
		}
	}
	
	public static class Key$
	{
		public final Substance substance;
		public final Part part;
		
		public Key$(Substance substance, Part part)
		{
			//Use equivalence part because prevent same part (eg: ingot and ingotSimple) registered.
			this.substance = substance;
			this.part = part.getEquivalencePart();
		}
		
		@Override
		public int hashCode()
		{
			return substance.hashCode() * 31 + part.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				!(obj instanceof Key$) ? false :
					substance == ((Key$) obj).substance && part == ((Key$) obj).part;
		}
	}
}