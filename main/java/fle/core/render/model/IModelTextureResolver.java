package fle.core.render.model;

import java.util.List;

public interface IModelTextureResolver
{
	public String resolveTextureName(String name);
	
	public List<ModelPart> getModelParts();
}