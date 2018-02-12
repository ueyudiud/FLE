/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.load;

import nebula.common.util.ModCompator.ICompatible;

/**
 * @author ueyudiud
 */
public class JEICompact implements ICompatible
{
	@Override
	public void call(String phase) throws Exception
	{
		switch (phase)
		{
		case "post" :
			//			LanguageManager.registerLocal("jei.compat.solid.amount", "Amount: %dL");
			break;
		}
	}
}
