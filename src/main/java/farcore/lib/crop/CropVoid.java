package farcore.lib.crop;

import farcore.data.M;

public class CropVoid extends CropBase
{
	public CropVoid()
	{
		super(M.VOID);
		maxStage = 1;
	}

	@Override
	public String getTranslatedName(String dna)
	{
		return "void";
	}
	
	@Override
	public String getLocalName(String dna)
	{
		return "VOID";
	}

	@Override
	public long tickUpdate(ICropAccess access)
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public String getIconKey(ICropAccess access)
	{
		return "void";
	}
}