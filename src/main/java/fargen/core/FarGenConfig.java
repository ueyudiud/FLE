/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core;

import nebula.common.config.Config;
import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;

/**
 * @author ueyudiud
 */
@Config("fargen")
public class FarGenConfig
{
	@ConfigProperty(category = "world", defValue = "true")
	@ConfigComment("The vanilla world type will be rewrite to Far Generation world type if enable this option," + "this may can not take effect and cause crash in some server side, enable this option and each world" + "type provided by FG will add a 'far_' prefix on world type name.")
	public static boolean hardOverride = true;
}
