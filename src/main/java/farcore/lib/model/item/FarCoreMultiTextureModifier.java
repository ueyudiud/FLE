package farcore.lib.model.item;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The multi texture modifier, get base key from parent and add
 * a prefix and postfix for it.
 * @since 1.3
 * @author ueyudiud
 * 
 */
@SideOnly(Side.CLIENT)
public class FarCoreMultiTextureModifier implements IMultiTextureCollection
{
	private final IMultiTextureCollection function;
	private String domain;
	private String prefix = "";
	private String postfix = "";
	
	public FarCoreMultiTextureModifier(IMultiTextureCollection function)
	{
		this.function = function;
	}
	
	public void setDomain(String domain)
	{
		this.domain = domain;
	}
	
	public void setPostfix(String postfix)
	{
		this.postfix = postfix;
	}
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	@Override
	public Map<String, ResourceLocation> apply()
	{
		Map<String, ResourceLocation> map = this.function.apply();
		ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		for (Entry<String, ResourceLocation> entry : map.entrySet())
		{
			String key = entry.getKey();
			int idx = key.indexOf(':');
			if (idx != -1)
			{
				String domain = key.substring(0, idx);
				String path = key.substring(idx);
				key = (this.domain == null ? domain : this.domain) + ":" + this.prefix + key + this.postfix;
			}
			else
			{
				key = (this.domain == null ? "" : this.domain + ":") + this.prefix + key + this.postfix;
			}
			ResourceLocation location = entry.getValue();
			builder.put(key, location);
		}
		return builder.build();
	}
}