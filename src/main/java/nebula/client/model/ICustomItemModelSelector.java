/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ICustomItemModelSelector extends ItemMeshDefinition
{
	List<ResourceLocation> getAllowedResourceLocations(Item item);
}
