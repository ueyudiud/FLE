package farcore.enums;

public enum Langs
{
	english("en_US");
	
	public final String locale;
	
	private Langs(String locale)
	{
		this.locale = locale;
	}
}
