package farcore.lib.model.item;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;

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
		Map<String, ResourceLocation> map = function.apply();
		ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		for (Entry<String, ResourceLocation> entry : map.entrySet())
		{
			String key = entry.getKey();
			int idx = key.indexOf(':');
			if (idx != -1)
			{
				String domain = key.substring(0, idx);
				String path = key.substring(idx);
				key = (this.domain == null ? domain : this.domain) + ":" + prefix + key + postfix;
			}
			else
			{
				key = (domain == null ? "" : domain + ":") + prefix + key + postfix;
			}
			ResourceLocation location = entry.getValue();
			builder.put(key, location);
		}
		return builder.build();
	}
}