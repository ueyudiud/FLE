/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.config;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * @author ueyudiud
 */
public interface IConfigGuiContext
{
	IConfigElement addElement(@Nonnull Object arg) throws Exception;
	
	IConfigElement addElement(@Nonnull Class<?> arg) throws Exception;
}
