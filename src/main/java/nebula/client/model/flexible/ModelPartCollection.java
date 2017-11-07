/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializer;

import nebula.common.util.Jsons;

/**
 * @author ueyudiud
 */
public class ModelPartCollection
{
	private ImmutableMap<String, INebulaModelPart> variants;
	
	private ModelPartCollection(Map<String, INebulaModelPart> variants)
	{
		this.variants = ImmutableMap.copyOf(variants);
	}
	
	public INebulaModelPart getModelPart(String key)
	{
		if (!this.variants.containsKey(key)) throw new RuntimeException("The model part variant '" + key + "' not found.");
		return this.variants.get(key);
	}
	
	static final JsonDeserializer<ModelPartCollection> DESERIALIZER = (json, typeOfType, context) -> new ModelPartCollection(Jsons.getAsMap(json.getAsJsonObject(), j -> context.deserialize(j, INebulaModelPart.class)));
}
