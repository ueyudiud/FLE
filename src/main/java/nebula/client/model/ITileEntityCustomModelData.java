/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The custom model data provider for TileEntity.
 * 
 * @author ueyudiud
 */
public interface ITileEntityCustomModelData
{
	/**
	 * Get model data by specific key.
	 * 
	 * @param key the key of model data.
	 * @return the data, might be <code>null</code> if key is not valid key of this TileEntity.
	 */
	@SideOnly(Side.CLIENT)
	@Nullable String getCustomModelData(String key);
}
