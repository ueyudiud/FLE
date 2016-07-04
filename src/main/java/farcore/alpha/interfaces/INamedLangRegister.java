package farcore.alpha.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.util.LanguageManager;

public interface INamedLangRegister
{
	default void registerLang(String unlocalized, String localized)
	{
		registerLang(LanguageManager.ENGLISH, unlocalized, localized);
	}
	
	void registerLang(String locale, String unlocalized, String localized);
	
	default List<String> translateLang(List<String> unlocalizeds, Object[]...translation)
	{
		if(unlocalizeds == null)
		{
			return ImmutableList.of();
		}
		else
		{
			if(translation == null || translation.length == 0)
			{
				ArrayList<String> list = new ArrayList();
				for(String name : unlocalizeds)
				{
					list.add(translateLang(name));
				}
				return list;
			}
			else if(unlocalizeds.size() != translation.length)
				throw new RuntimeException();
			else
			{
				ArrayList<String> list = new ArrayList();
				for(int i = 0; i < unlocalizeds.size(); ++i)
				{
					list.add(translateLang(unlocalizeds.get(i), translation[i]));
				}
				return list;
			}
		}
	}
	
	String translateLang(String unlocalized, Object...translation);
}