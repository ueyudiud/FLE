/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.bio.DNAHandler;
import farcore.lib.bio.DNAHandlerGeneral;
import farcore.lib.bio.IBiology;
import farcore.lib.crop.dna.DNAHCrop;
import farcore.lib.crop.dna.DNAHCropSimple;

/**
 * Vanilla DNA handler of Far Core.
 * @author ueyudiud
 *
 */
public final class DNAs
{
	public static final String MAP_ENG = "abcdefghijklmnopqrstuvwxyz";
	
	public static final DNAHandler<IBiology>[] GENERALS;
	
	public static DNAHandler<IBiology> get(char chr)
	{
		int i = MAP_ENG.indexOf(chr);
		if(i != -1) return GENERALS[i];
		return null;
	}
	
	/**
	 * No other feathers decorating DNA.<p>
	 * You can use it on first element of helper for some misty character :D
	 */
	public static final DNAHCrop GRAIN_I		= DNAHCropSimple.builder(1001, "grainI"		).putNativeDNA('g').putMutateDNA('g', 500, 'G').putEffect('G', new int[]{2, 0, 0, 0, 0, 0}).build();
	public static final DNAHCrop GRAIN_II		= DNAHCropSimple.builder(1002, "grainII"	).putNativeDNA('g').putMutateDNA('g', 250, 'G').putEffect('G', new int[]{3, 0, 0, 0, 0, 0}).build();
	public static final DNAHCrop GRAIN_III		= DNAHCropSimple.builder(1003, "grainIII"	).putNativeDNA('g').putMutateDNA('g', 125, 'G').putEffect('G', new int[]{4, 0, 0, 0, 0, 0}).build();
	public static final DNAHCrop GRAIN_IV		= DNAHCropSimple.builder(1004, "grainIV"	).putNativeDNA('g').putMutateDNA('g',  63, 'G').putEffect('G', new int[]{5, 0, 0, 0, 0, 0}).build();
	public static final DNAHCrop GRAIN_V		= DNAHCropSimple.builder(1005, "grainV"		).putNativeDNA('g').putMutateDNA('g',  31, 'G').putEffect('G', new int[]{6, 0, 0, 0, 0, 0}).build();
	public static final DNAHCrop GROWTH_I		= DNAHCropSimple.builder(1011, "growthI"	).putNativeDNA('s').putMutateDNA('s', 500, 'S').putEffect('S', new int[]{0, 2, 0, 0, 0, 0}).build();
	public static final DNAHCrop GROWTH_II		= DNAHCropSimple.builder(1012, "growthII"	).putNativeDNA('s').putMutateDNA('s', 250, 'S').putEffect('S', new int[]{0, 3, 0, 0, 0, 0}).build();
	public static final DNAHCrop GROWTH_III		= DNAHCropSimple.builder(1013, "growthIII"	).putNativeDNA('s').putMutateDNA('s', 125, 'S').putEffect('S', new int[]{0, 4, 0, 0, 0, 0}).build();
	public static final DNAHCrop GROWTH_IV		= DNAHCropSimple.builder(1014, "growthIV"	).putNativeDNA('s').putMutateDNA('s',  63, 'S').putEffect('S', new int[]{0, 5, 0, 0, 0, 0}).build();
	public static final DNAHCrop GROWTH_V		= DNAHCropSimple.builder(1015, "growthV"	).putNativeDNA('s').putMutateDNA('s',  31, 'S').putEffect('S', new int[]{0, 6, 0, 0, 0, 0}).build();
	
	static
	{
		GENERALS = new DNAHandler[MAP_ENG.length()];
		for(int i = 0; i < MAP_ENG.length(); ++i)
		{
			GENERALS[i] = new DNAHandlerGeneral(i, MAP_ENG.charAt(i));
		}
	}
}