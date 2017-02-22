/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance.interfaces;

import javax.annotation.Nullable;

import nebula.common.inventory.IMatrixInventory;
import nebula.common.world.ICoord;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author ueyudiud
 */
public interface ICraftingRecipeHandler extends IMatrixInventory, ICoord
{
	default boolean isInMaterial(Material material)
	{
		EntityPlayer player = getPlayer();
		if (player != null)
		{
			AxisAlignedBB aabb = player.getCollisionBoundingBox();
			return world().handleMaterialAcceleration(aabb, material, player);
		}
		return false;
	}
	
	@Nullable EntityPlayer getPlayer();
}