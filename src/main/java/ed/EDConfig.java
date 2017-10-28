/*
 * copyright© 2016-2017 ueyudiud
 */
package ed;

import nebula.common.config.Config;
import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;

/**
 * @author ueyudiud
 */
@Config("ed")
public class EDConfig
{
	@ConfigProperty(category = "common", defValue = "true")
	@ConfigComment("The Nebula data loader will pre-build meta map if enable this option or get meta from block state each time.")
	public static boolean buildStateIn;
}