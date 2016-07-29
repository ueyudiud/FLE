package farcore.lib.crop.instance;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;

public class CropFlax extends CropAbstract
{
	public CropFlax(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 6;
		growReq = 1500;
	}
}