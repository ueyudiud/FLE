package farcore.lib.model;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.util.ResourceLocation;

public interface ICustomItemModelSelector extends ItemMeshDefinition
{
	List<ResourceLocation>	getAllowedResourceLocations();
}