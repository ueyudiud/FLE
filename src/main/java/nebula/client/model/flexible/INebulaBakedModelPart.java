/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

/**
 * @author ueyudiud
 */
public interface INebulaBakedModelPart
{
	static INebulaBakedModelPart EMPTY = new BakedModelPart(ImmutableMap.of());
	
	List<BakedQuad> getQuads(EnumFacing facing, String key);
	
	class BakedModelPart implements INebulaBakedModelPart
	{
		private final ImmutableMap<String, List<BakedQuad>> map;
		
		BakedModelPart(Map<String, List<BakedQuad>> map)
		{
			this.map = ImmutableMap.copyOf(map);
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			return facing == null ? this.map.getOrDefault(key, ImmutableList.of()) : ImmutableList.of();
		}
	}
}