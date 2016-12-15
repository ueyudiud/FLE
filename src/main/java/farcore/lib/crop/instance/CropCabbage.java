package farcore.lib.crop.instance;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.bio.DNAHandler;
import farcore.lib.crop.CropTemplate;
import farcore.lib.material.Mat;

public class CropCabbage extends CropTemplate
{
	public CropCabbage(Mat material)
	{
		super(material);
		this.helper = new DNAHandler[0];
		this.maxStage = 6;
		this.growReq = 1500;
	}
	
	//	@Override
	//	public String getTranslatedName(GeneticMaterial dna)
	//	{
	//		return this.pair.matchDominant(dna.getSlotAt(0)) ? "millet" : "bristlegrass";
	//	}
	
	@Override
	public List<String> getAllowedState()
	{
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for(int i = 0; i < getMaxStage(); ++i)
		{
			builder.add("cabbage_stage_" + i);
		}
		return builder.build();
	}
}