package fle.core.init;

import flapi.chem.base.Part;

public class Parts
{
	public static final Part single;
	public static final Part fluidy;

	public static final Part smallNugget;
	
	public static final Part tinyDust;
	public static final Part nugget;
	
	public static final Part stick;

	public static final Part smallCube;
	public static final Part chunk;
	public static final Part smallDust;
	public static final Part slice;

	public static final Part cube;
	public static final Part ingot;
	public static final Part plate;
	public static final Part dust;
	public static final Part pile;
	public static final Part longStick;
	
	public static final Part largeCube;
	
	public static final Part block;

	public static final Part chip;
	
	static
	{
		single = Part.part("single", 1);
		fluidy = Part.part("fluidy", 1);
		smallNugget = Part.part("small_nugget", 4);
		nugget = Part.part("nugget", 16);
		tinyDust = Part.part("tiny_dust", 16);
		chunk = Part.part("chunk", 36);
		smallDust = Part.part("small_dust", 36);
		slice = Part.part("slice", 36);
		smallCube = Part.part("small_cube", 48);
		stick = Part.part("stick", 48);
		ingot = Part.part("ingot", 144);
		plate = Part.part("plate", 144);
		dust = Part.part("dust", 144);
		longStick = Part.part("long_stick", 144);
		cube = Part.part("cube", 144);
		pile = Part.part("pile", 216);
		largeCube = Part.part("large_cube", 432);
		block = Part.part("block", 1296);
		
		chip = Part.part("chip", 36);
	}
}