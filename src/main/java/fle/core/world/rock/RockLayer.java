package fle.core.world.rock;

import java.util.List;

import farcore.enums.EnumItem;
import farcore.lib.substance.SubstanceMineral;
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
	public static RockLayer layerLime;
	
	private static boolean init = false;
	
	public static void init()
	{
		if(!init)
		{
			SubstanceRock rhyolite = SubstanceRock.getSubstance("rhyolite");
			SubstanceRock andesite = SubstanceRock.getSubstance("andesite");
			SubstanceRock basalt = SubstanceRock.getSubstance("basalt");
			SubstanceRock peridotite = SubstanceRock.getSubstance("peridotite");
			SubstanceRock granite = SubstanceRock.getSubstance("granite");
			SubstanceRock diorite = SubstanceRock.getSubstance("diorite");
			SubstanceRock gabbro = SubstanceRock.getSubstance("gabbro");
			SubstanceRock limestone = SubstanceRock.getSubstance("limestone");
			layerAcid		= new RockLayer(rhyolite, granite, granite, null, null);
			layerMid		= new RockLayer(andesite, diorite, diorite, null, null);
			layerBasic		= new RockLayer(basalt, gabbro, gabbro, null, null);
			layerUltramafic	= new RockLayer(peridotite, peridotite, peridotite, null, null);
			layerLime		= new RockLayer(limestone, limestone, limestone, null, null);
			init = true;
		}
	}
	
	public Block[] rocks = new Block[5];
	
	public RockLayer(SubstanceRock rock1, SubstanceRock rock2, SubstanceRock rock3,
			SubstanceRock rock4, SubstanceRock rock5)
	{
		this.rocks[0] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock1);
		this.rocks[1] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock2);
		this.rocks[2] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock3);
		if(rock4 != null)
			this.rocks[3] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock4);
		if(rock5 != null)
			this.rocks[4] = ((ItemSubstance) EnumItem.rock_block.item()).block(rock5);
	}
}