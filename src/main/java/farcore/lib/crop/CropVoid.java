package farcore.lib.crop;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.material.Mat;

public class CropVoid extends Crop
{
	public CropVoid()
	{
		this.material = Mat.VOID;
		this.maxStage = 1;
	}
	
	@Override
	public String getLocalName(GeneticMaterial dna)
	{
		return "VOID";
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return Integer.MAX_VALUE;
	}
}