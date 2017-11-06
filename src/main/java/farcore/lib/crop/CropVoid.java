/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.crop;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.material.Mat;

class CropVoid extends Crop
{
	CropVoid()
	{
		this.maxStage = 1;
	}
	
	@Override
	public String getRegisteredName()
	{
		return Mat.VOID.name;
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