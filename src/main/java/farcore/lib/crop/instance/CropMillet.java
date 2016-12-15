package farcore.lib.crop.instance;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.bio.DNAHandler;
import farcore.lib.crop.CropTemplate;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;

public class CropMillet extends CropTemplate
{
	public CropMillet(Mat material)
	{
		super(material);
		this.helper = new DNAHandler[0];
		this.maxStage = 8;
		this.growReq = 900;
	}
	//
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
			builder.add("millet_stage_" + i);
			builder.add("bristlegrass_stage_" + i);
		}
		return builder.build();
	}
	
	@Override
	public int getGrowReq(ICropAccess access)
	{
		return super.getGrowReq(access) / (access.info().map.get("type") == 1 ? 2 : 1);
	}
}