package fle.core.world.rock;

import farcore.enums.EnumItem;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;
import fle.api.block.ItemSubstance;
import net.minecraft.block.Block;

public class RockLayer
{
	public static RockLayer layerAcid;
	public static RockLayer layerMid;
	public static RockLayer layerBasic;
	public static RockLayer layerUltramafic;
	
	private static boolean init = false;
	
	private static void init()
	{
		if(!init)
		{
			SubstanceRock rhyolite = SubstanceRock.getSubstance("rhyolite");
			SubstanceRock andesite = SubstanceRock.getSubstance("andesite");
			SubstanceRock basalt = SubstanceRock.getSubstance("basalt");
			SubstanceRock peridotite = SubstanceRock.getSubstance("peridotite");
			layerAcid = new RockLayer(rhyolite, rhyolite, rhyolite);
			layerMid = new RockLayer(andesite, andesite, andesite);
			layerBasic = new RockLayer(basalt, basalt, basalt);
			layerUltramafic = new RockLayer(peridotite, peridotite, peridotite);
			init = true;
		}
	}
	
	public static Block provide(double rockPH, double rockDeep)
	{
		init();
		if(rockPH > 6D)
		{
			return layerAcid.rocks[U.Lang.range(0, 2, (int) (rockDeep / 100D))];
		}
		else if(rockPH > 4D)
		{
			return layerMid.rocks[U.Lang.range(0, 2, (int) (rockDeep / 100D))];
		}
		else if(rockPH > 2D)
		{
			return layerBasic.rocks[U.Lang.range(0, 2, (int) (rockDeep / 100D))];
		}
		else
		{
			return layerUltramafic.rocks[U.Lang.range(0, 2, (int) (rockDeep / 100D))];
		}
	}

	public Block[] rocks = new Block[3];
	
	public RockLayer(SubstanceRock rock1, SubstanceRock rock2, SubstanceRock rock3)
	{
		this.rocks[0] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock1);
		this.rocks[1] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock2);
		this.rocks[2] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock3);
	}
}