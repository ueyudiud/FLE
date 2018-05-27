/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.tile;

import farcore.lib.material.Mat;
import fle.api.mat.StackContainer;
import nebula.common.util.Direction;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The Bellow accepter interface.
 * <p>
 * Let tile entity implement this type then the tile can access wind from
 * bellow.
 * 
 * @author ueyudiud
 */
public interface IBellowsAccepter
{
	/**
	 * Called when wind accept from bellow (Or other wind generating machine),
	 * the method will only called in server world.
	 * 
	 * @param from the direction where the air come from.
	 * @param windSpeed the speed of wind.
	 * @param airContain the contains materials in air.
	 */
	void onAcceptWindFromBellow(Direction from, float windSpeed, StackContainer<Mat> airContain);
	
	/**
	 * Called at client side, to rendering wind effect.
	 * 
	 * @param windSpeed the speed of wind.
	 */
	@SideOnly(Side.CLIENT)
	void onRenderBellowEffect(Direction from, float windSpeed);
}
