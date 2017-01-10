/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.crop.dna;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import farcore.lib.bio.DNAHandler;
import farcore.lib.bio.DNAPair;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;

/**
 * @author ueyudiud
 */
public class DNAHCrop extends DNAHandler<ICropAccess>
{
	//grain, growth, coldResistance, hotResistance, weedResistance, dryResistance;
	public Function<Character, int[]> expression;
	
	public DNAHCrop(String name)
	{
		super(name);
	}
	public DNAHCrop(int id, String name)
	{
		super(id, name);
	}
	
	@Override
	public void expressTrait(ICropAccess target, DNAPair<ICropAccess> pair)
	{
		if(this.expression != null)
		{
			int[] apply = this.expression.apply(pair.DNA1);
			if(apply != null)
			{
				CropInfo info = target.info();
				info.grain += apply[0];
				info.growth += apply[1];
				info.coldResistance += apply[2];
				info.hotResistance += apply[3];
				info.weedResistance += apply[4];
				info.dryResistance += apply[5];
			}
			apply = this.expression.apply(pair.DNA2);
			if(apply != null)
			{
				CropInfo info = target.info();
				info.grain += apply[0];
				info.growth += apply[1];
				info.coldResistance += apply[2];
				info.hotResistance += apply[3];
				info.weedResistance += apply[4];
				info.dryResistance += apply[5];
			}
		}
	}
	
	@Override
	public DNAPair<ICropAccess> createNative(@Nullable ICropAccess target, Random random)
	{
		return super.createNative(target);
	}
	
	@Override
	public DNAPair<ICropAccess> createGamete(ICropAccess target, DNAPair<ICropAccess> pair, boolean mutate)
	{
		return super.createGamete(target, pair, mutate);
	}
}