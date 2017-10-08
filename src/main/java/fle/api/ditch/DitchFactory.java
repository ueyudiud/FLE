/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.ditch;

import farcore.lib.material.Mat;
import fle.api.tile.IDitchTile;
import nebula.client.util.UnlocalizedList;
import nebula.common.fluid.FluidTankN;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface DitchFactory
{
	/**
	 * Return <code>true</code> to let this factory handle
	 * material type.
	 * @param material
	 * @return
	 */
	boolean access(Mat material);
	
	FluidTankN apply(IDitchTile tile);
	
	void onUpdate(IDitchTile tile);
	
	/**
	 * Get multiple speed, use mL/tick for unit.
	 * @param tile
	 * @return
	 */
	int getSpeedMultiple(IDitchTile tile);
	
	/**
	 * Get max fluid transfer limit, use L for unit.
	 * @param tile
	 * @return
	 */
	int getMaxTransferLimit(IDitchTile tile);
	
	@SideOnly(Side.CLIENT)
	TextureAtlasSprite getMaterialIcon(Mat material);
	
	@SideOnly(Side.CLIENT)
	void addTooltip(Mat material, UnlocalizedList list);
}