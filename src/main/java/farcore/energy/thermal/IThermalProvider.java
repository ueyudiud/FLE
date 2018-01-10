/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.energy.thermal;

import nebula.common.world.ICoord;

/**
 * The thermal handler provider, implemented by TileEntity.
 * <p>
 * The thermal net will get handler by {@link #getThermalHandler()} and
 * linked the handler into the energy net when add the tile into net.
 * 
 * @author ueyudiud
 */
public interface IThermalProvider extends ICoord
{
	IThermalHandler getThermalHandler();
}
