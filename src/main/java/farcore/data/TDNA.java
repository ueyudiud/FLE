package farcore.data;

import farcore.lib.tree.dna.TreeCharacter;

public class TDNA
{
	public static final TreeCharacter EMPTY = new TreeCharacter((char) 0x0);
	public static final TreeCharacter GROWTH_I = new TreeCharacter((char) 0x1);
	public static final TreeCharacter GROWTH_II = new TreeCharacter((char) 0x2);
	public static final TreeCharacter GROWTH_III = new TreeCharacter((char) 0x3);
	public static final TreeCharacter GROWTH_IV = new TreeCharacter((char) 0x4);
	public static final TreeCharacter GROWTH_V = new TreeCharacter((char) 0x5);
	public static final TreeCharacter HEIGHT_I = new TreeCharacter((char) 0x6);
	public static final TreeCharacter HEIGHT_II = new TreeCharacter((char) 0x7);
	public static final TreeCharacter HEIGHT_III = new TreeCharacter((char) 0x8);
	public static final TreeCharacter HEIGHT_IV = new TreeCharacter((char) 0x9);
	public static final TreeCharacter HEIGHT_V = new TreeCharacter((char) 0xA);
	public static final TreeCharacter HOT_I = new TreeCharacter((char) 0xB);
	public static final TreeCharacter HOT_II = new TreeCharacter((char) 0xC);
	public static final TreeCharacter HOT_III = new TreeCharacter((char) 0xD);
	public static final TreeCharacter COLD_I = new TreeCharacter((char) 0xE);
	public static final TreeCharacter COLD_II = new TreeCharacter((char) 0xF);
	public static final TreeCharacter COLD_III = new TreeCharacter((char) 0x10);
	public static final TreeCharacter DRY_I = new TreeCharacter((char) 0x11);
	public static final TreeCharacter DRY_II = new TreeCharacter((char) 0x12);
	public static final TreeCharacter DRY_III = new TreeCharacter((char) 0x13);
	
	static
	{
		GROWTH_I.growth = 1;
		GROWTH_II.growth = 2;
		GROWTH_III.growth = 3;
		GROWTH_IV.growth = 4;
		GROWTH_V.growth = 5;
		HEIGHT_I.height = 1;
		HEIGHT_II.height = 2;
		HEIGHT_III.height = 3;
		HEIGHT_IV.height = 4;
		HEIGHT_V.height = 5;
		HOT_I.hotResistance = 1;
		HOT_II.hotResistance = 2;
		HOT_III.hotResistance = 3;
		COLD_I.coldResistance = 1;
		COLD_II.coldResistance = 2;
		COLD_III.coldResistance = 3;
		DRY_I.dryResistance = 1;
		DRY_II.dryResistance = 2;
		DRY_III.dryResistance = 3;
	}
}