package farcore.data;

import farcore.lib.crop.dna.CropCharacter;

public class CDNA
{
	public static final CropCharacter EMPTY = new CropCharacter((char) 0x0);
	public static final CropCharacter GROWTH_I = new CropCharacter((char) 0x1);
	public static final CropCharacter GROWTH_II = new CropCharacter((char) 0x2);
	public static final CropCharacter GROWTH_III = new CropCharacter((char) 0x3);
	public static final CropCharacter GROWTH_IV = new CropCharacter((char) 0x4);
	public static final CropCharacter GROWTH_V = new CropCharacter((char) 0x5);
	public static final CropCharacter GRAIN_I = new CropCharacter((char) 0x6);
	public static final CropCharacter GRAIN_II = new CropCharacter((char) 0x7);
	public static final CropCharacter GRAIN_III = new CropCharacter((char) 0x8);
	public static final CropCharacter GRAIN_IV = new CropCharacter((char) 0x9);
	public static final CropCharacter GRAIN_V = new CropCharacter((char) 0xA);
	public static final CropCharacter HOT_I = new CropCharacter((char) 0xB);
	public static final CropCharacter HOT_II = new CropCharacter((char) 0xC);
	public static final CropCharacter HOT_III = new CropCharacter((char) 0xD);
	public static final CropCharacter COLD_I = new CropCharacter((char) 0xE);
	public static final CropCharacter COLD_II = new CropCharacter((char) 0xF);
	public static final CropCharacter COLD_III = new CropCharacter((char) 0x10);
	public static final CropCharacter WEED_I = new CropCharacter((char) 0x11);
	public static final CropCharacter WEED_II = new CropCharacter((char) 0x12);
	public static final CropCharacter WEED_III = new CropCharacter((char) 0x13);
	public static final CropCharacter DRY_I = new CropCharacter((char) 0x14);
	public static final CropCharacter DRY_II = new CropCharacter((char) 0x15);
	public static final CropCharacter DRY_III = new CropCharacter((char) 0x16);
	
	static
	{
		GROWTH_I.growth = 1;
		GROWTH_II.growth = 2;
		GROWTH_III.growth = 3;
		GROWTH_IV.growth = 4;
		GROWTH_V.growth = 5;
		GRAIN_I.grain = 1;
		GRAIN_II.grain = 2;
		GRAIN_III.grain = 3;
		GRAIN_IV.grain = 4;
		GRAIN_V.grain = 5;
		HOT_I.hotResistance = 1;
		HOT_II.hotResistance = 2;
		HOT_III.hotResistance = 3;
		COLD_I.coldResistance = 1;
		COLD_II.coldResistance = 2;
		COLD_III.coldResistance = 3;
		WEED_I.weedResistance = 1;
		WEED_II.weedResistance = 2;
		WEED_III.weedResistance = 3;
		DRY_I.dryResistance = 1;
		DRY_II.dryResistance = 2;
		DRY_III.dryResistance = 3;
	}
}