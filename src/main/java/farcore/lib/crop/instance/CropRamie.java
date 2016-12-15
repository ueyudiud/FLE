package farcore.lib.crop.instance;

import farcore.lib.bio.DNAHandler;
import farcore.lib.crop.CropTemplate;
import farcore.lib.material.Mat;

public class CropRamie extends CropTemplate
{
	public CropRamie(Mat material)
	{
		super(material);
		this.helper = new DNAHandler[0];
		this.maxStage = 7;
		this.growReq = 1250;
	}
}