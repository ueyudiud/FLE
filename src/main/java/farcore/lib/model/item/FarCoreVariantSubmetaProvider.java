package farcore.lib.model.item;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FarCoreVariantSubmetaProvider implements Function<ItemStack, String>
{
	private Function<ItemStack, String> parent;
	private Map<String, String> variant = ImmutableMap.of();
	private Map<String, String> replacement = ImmutableMap.of();
	private String prefix = "";
	private String postfix = "";

	public FarCoreVariantSubmetaProvider(Function<ItemStack, String> parent)
	{
		this.parent = parent;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public void setPostfix(String postfix)
	{
		this.postfix = postfix;
	}
	
	public void setVariant(Map<String, String> variant)
	{
		this.variant = variant;
	}

	public void setReplacement(Map<String, String> replacement)
	{
		this.replacement = replacement;
	}

	@Override
	public String apply(ItemStack stack)
	{
		String value = parent.apply(stack);
		value = variant.getOrDefault(value, value);
		for (Entry<String, String> entry : replacement.entrySet())
		{
			value.replaceAll(entry.getKey(), entry.getValue());
		}
		return prefix + value + postfix;
	}
}