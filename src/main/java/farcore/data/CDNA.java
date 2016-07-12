package farcore.data;

import farcore.lib.crop.dna.DNACharacter;

public class CDNA
{
	public static final DNACharacter EMPTY = new DNACharacter();
	public static final DNACharacter GROWTH_I = new DNACharacter();
	public static final DNACharacter GROWTH_II = new DNACharacter();
	public static final DNACharacter GROWTH_III = new DNACharacter();
	public static final DNACharacter GROWTH_IV = new DNACharacter();
	public static final DNACharacter GROWTH_V = new DNACharacter();
	public static final DNACharacter GRAIN_I = new DNACharacter();
	public static final DNACharacter GRAIN_II = new DNACharacter();
	public static final DNACharacter GRAIN_III = new DNACharacter();
	public static final DNACharacter GRAIN_IV = new DNACharacter();
	public static final DNACharacter GRAIN_V = new DNACharacter();
	public static final DNACharacter HOT_I = new DNACharacter();
	public static final DNACharacter HOT_II = new DNACharacter();
	public static final DNACharacter HOT_III = new DNACharacter();
	public static final DNACharacter COLD_I = new DNACharacter();
	public static final DNACharacter COLD_II = new DNACharacter();
	public static final DNACharacter COLD_III = new DNACharacter();
	public static final DNACharacter WEED_I = new DNACharacter();
	public static final DNACharacter WEED_II = new DNACharacter();
	public static final DNACharacter WEED_III = new DNACharacter();
	public static final DNACharacter DRY_I = new DNACharacter();
	public static final DNACharacter DRY_II = new DNACharacter();
	public static final DNACharacter DRY_III = new DNACharacter();
	
	static
	{
		EMPTY.chr = 0x0;
		GROWTH_I.chr = 0x1;
		GROWTH_I.growth = 1;
		GROWTH_II.chr = 0x2;
		GROWTH_II.growth = 2;
		GROWTH_III.chr = 0x3;
		GROWTH_III.growth = 3;
		GROWTH_IV.chr = 0x4;
		GROWTH_IV.growth = 4;
		GROWTH_V.chr = 0x5;
		GROWTH_V.growth = 5;
		GRAIN_I.chr = 0x6;
		GRAIN_I.grain = 1;
		GRAIN_II.chr = 0x7;
		GRAIN_II.grain = 2;
		GRAIN_III.chr = 0x8;
		GRAIN_III.grain = 3;
		GRAIN_IV.chr = 0x9;
		GRAIN_IV.grain = 4;
		GRAIN_V.chr = 0xA;
		GRAIN_V.grain = 5;
		HOT_I.chr = 0xB;
		HOT_I.hotResistance = 1;
		HOT_II.chr = 0xC;
		HOT_II.hotResistance = 2;
		HOT_III.chr = 0xD;
		HOT_III.hotResistance = 3;
		COLD_I.chr = 0xE;
		COLD_I.coldResistance = 1;
		COLD_II.chr = 0xF;
		COLD_II.coldResistance = 2;
		COLD_III.chr = 0x10;
		COLD_III.coldResistance = 3;
		WEED_I.chr = 0x11;
		WEED_I.weedResistance = 1;
		WEED_II.chr = 0x12;
		WEED_II.weedResistance = 2;
		WEED_III.chr = 0x13;
		WEED_III.weedResistance = 3;
		DRY_I.chr = 0x14;
		DRY_I.dryResistance = 1;
		DRY_II.chr = 0x15;
		DRY_II.dryResistance = 2;
		DRY_III.chr = 0x16;
		DRY_III.dryResistance = 3;
	}
}