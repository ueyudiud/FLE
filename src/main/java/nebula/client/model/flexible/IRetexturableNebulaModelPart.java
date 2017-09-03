/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Map;

/**
 * @author ueyudiud
 */
@Deprecated
public interface IRetexturableNebulaModelPart extends INebulaModelPart
{
	INebulaModelPart retexture(Map<String, String> retexture);
}