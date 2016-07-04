package farcore.alpha.util;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.alpha.interfaces.INamedLangRegister;
import farcore.util.LanguageManager;

public class LangHook implements INamedLangRegister
{
	public static final LangHook instance = new LangHook();
	
	private LangHook()
	{
		
	}

	@Override
	public void registerLang(String locale, String unlocalized, String localized)
	{
		FarCoreSetup.lang.registerLocal(locale, unlocalized, localized);
	}

	@Override
	public String translateLang(String unlocalized, Object... translation)
	{
		return FarCoreSetup.lang.translateToLocal(unlocalized, translation);
	}
	
	public static class UnlocalizedList
	{
		List<String> list;
		
		public UnlocalizedList()
		{
			this(new ArrayList());
		}
		public UnlocalizedList(List<String> list)
		{
			this.list = list;
		}

		public void add(String arg, Object...translation)
		{
			list.add(instance.translateLang(arg, translation));
		}
		
		public void addLocal(String arg)
		{
			list.add(arg);
		}
		
		public List<String> list()
		{
			return list;
		}
	}
}