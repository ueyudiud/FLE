/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;

import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public interface INebulaDirectResourcesModelPart extends INebulaModelPart
{
	Collection<ResourceLocation> getDirectResources();
}