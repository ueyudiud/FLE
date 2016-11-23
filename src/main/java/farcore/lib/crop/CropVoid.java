package farcore.lib.crop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.material.Mat;

public class CropVoid extends CropBase
{
	public CropVoid()
	{
		super(Mat.VOID);
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
	public List<String> getAllowedState()
	{
		return ImmutableList.of("void");
	}

	@Override
	public String getState(ICropAccess access)
	{
		return "void";
	}

	@Override
	public String getRegisteredName()
	{
		return "";
	}
}