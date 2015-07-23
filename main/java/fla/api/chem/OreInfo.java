package fla.api.chem;

public class OreInfo 
{
	public static final OreInfo NativeCopper = new OreInfo("NativeCopper", Matter.mCu, 0xFF834C);
	public static final OreInfo Tetrahedrite = new OreInfo("Tetrahedrite", Matter.mCu10Fe2Sb4S13, 0xDCBC74);
	public static final OreInfo Enargite = new OreInfo("Enargite", Matter.mCu3AsS4, 0x7F6A68);
	public static final OreInfo Cuprite = new OreInfo("Cuprite", Matter.mCu2O, 0xD83E26);
	public static final OreInfo Tenorite = new OreInfo("Tenorite", Matter.mCuO, 0x5F6A73);
	public static final OreInfo Covellite = new OreInfo("Covellite", Matter.mCuS, 0x3FC2F8, 0x643EB7);
	public static final OreInfo Chalcocite = new OreInfo("Chalcocite", Matter.mCu2S, 0xA5B2C0);
	public static final OreInfo Malachite = new OreInfo("Malachite", Matter.mCu_OH2_CO3, 0x43D393);
	public static final OreInfo Azurite = new OreInfo("Azurite", Matter.mCu_OH2_2CO3, 0x434BD3);
	public static final OreInfo Chalcopyrite = new OreInfo("Chalcopyrite", Matter.mCuFeS2, 0xF8E3A2);
	public static final OreInfo Bornite = new OreInfo("Bornite", Matter.mCu5FeS4, 0xDCBC74, 0x9D7797);
	
	private String name;
	private Matter matter;
	protected int[] color;

	public OreInfo(String aName, Matter aMatter)
	{
		this(aName, aMatter, 0xFFFFFF, 0xFFFFFF);
	}
	public OreInfo(String aName, Matter aMatter, int aColor) 
	{
		this(aName, aMatter, new int[]{aColor, aColor});
	}
	public OreInfo(String aName, Matter aMatter, int...aColor) 
	{
		name = aName;
		matter = aMatter;
		color = aColor;
	}
	
	public final String getChemicalFormulaName()
	{
		return matter.toString();
	}
	
	public final String getOreName()
	{
		return name;
	}
	
	public int[] getColor()
	{
		return color;
	}
}