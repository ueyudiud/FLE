package farcore.data;

import farcore.FarCore;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.instance.CropCabbage;
import farcore.lib.crop.instance.CropCotton;
import farcore.lib.crop.instance.CropFlax;
import farcore.lib.crop.instance.CropMillet;
import farcore.lib.crop.instance.CropPotato;
import farcore.lib.crop.instance.CropReed;
import farcore.lib.crop.instance.CropSoybean;
import farcore.lib.crop.instance.CropSweetPotato;
import farcore.lib.crop.instance.CropWheat;
import farcore.lib.material.Mat;
import farcore.lib.tree.TreeVoid;
import farcore.lib.tree.instance.TreeAcacia;
import farcore.lib.tree.instance.TreeAspen;
import farcore.lib.tree.instance.TreeBirch;
import farcore.lib.tree.instance.TreeCeiba;
import farcore.lib.tree.instance.TreeMorus;
import farcore.lib.tree.instance.TreeOak;
import farcore.lib.tree.instance.TreeOakBlack;
import farcore.lib.tree.instance.TreeWillow;

public class M
{
	public static final Mat VOID = new Mat(-1, false, "farcore", "void", "Void", "Void").setCrop(ICrop.VOID);
	
	public static final Mat stone = new Mat(7001, "minecraft", "stone", "Stone", "Stone").setRock(6, 1.5F, 8F, 20).setToolable(7, 16, 1.2F, 0.8F, 1.0F, 8).setRGBa(0x626262FF);
	public static final Mat compact_stone = new Mat(7002, "farcore", "stone-compact", "CompactStone", "Compact Stone").setRock(9, 2.0F, 12F, 20).setToolable(9, 22, 1.8F, 0.7F, 1.0F, 6).setRGBa(0x686868FF);
	public static final Mat andesite = new Mat(7003, "farcore", "andesite", "Andesite", "Andesite").setRock(9, 4.9F, 16.6F, 20).setToolable(10, 32, 2.3F, 0.8F, 1.5F, 8).setRGBa(0x616162FF);
	public static final Mat basalt = new Mat(7004, "farcore", "basalt", "Basalt", "Basalt").setRock(10, 5.3F, 18.3F, 20).setToolable(11, 38, 2.5F, 0.8F, 1.2F, 8).setRGBa(0x3A3A3AFF);
	public static final Mat diorite = new Mat(7005, "farcore", "diorite", "Diorite", "Diorite").setRock(12, 6.5F, 23.3F, 20).setToolable(13, 42, 2.7F, 0.8F, 1.4F, 8).setRGBa(0xC9C9CDFF);
	public static final Mat gabbro = new Mat(7006, "farcore", "gabbro", "Gabbro", "Gabbro").setRock(13, 6.9F, 25.2F, 20).setToolable(14, 40, 2.6F, 0.8F, 1.5F, 6).setRGBa(0x53524EFF);
	public static final Mat granite = new Mat(7007, "farcore", "granite", "Granite", "Granite").setRock(13, 7.4F, 29.8F, 20).setToolable(14, 44, 2.8F, 0.8F, 1.8F, 4).setRGBa(0x986C5DFF);
	public static final Mat kimberlite = new Mat(7008, "farcore", "kimberlite", "Kimberlite", "Kimberlite").setRock(14, 7.8F, 31.4F, 20).setToolable(15, 46, 3.1F, 0.8F, 1.8F, 4).setRGBa(0x4D4D49FF);
	public static final Mat limestone = new Mat(7009, "farcore", "limestone", "Lime", "Limestone").setRock(5, 1.3F, 5.5F, 20).setRGBa(0xC9C9C8FF);
	public static final Mat marble = new Mat(7010, "farcore", "marble", "Marble", "Marble").setRock(7, 7.8F, 8.4F, 20).setRGBa(0xE2E6F0FF);
	public static final Mat netherrack = new Mat(7011, "minecraft", "netherrack", "Netherrack", "Netherrack").setRock(4, 1.3F, 3.8F, 180).setRGBa(0x5F3636FF);
	public static final Mat obsidian = new Mat(7012, "farcore", "obsidian", "Obsidian", "Obsidian").setRock(21, 9.8F, 4.2F, 20).setToolable(16, 8, 5.2F, 2.7F, 3F, 12).setRGBa(0x12121BFF);
	public static final Mat peridotite = new Mat(7013, "farcore", "peridotite", "Peridotite", "Peridotite").setRock(14, 7.7F, 30.5F, 20).setToolable(15, 45, 3.0F, 0.8F, 2.0F, 5).setRGBa(0x717A5CFF);
	public static final Mat rhyolite = new Mat(7014, "farcore", "rhyolite", "Rhyolite", "Rhyolite").setRock(11, 6.0F, 21.7F, 20).setToolable(12, 39, 2.6F, 0.8F, 2.0F, 8).setRGBa(0x4F535AFF);
	public static final Mat graniteP = new Mat(7015, "farcore", "graniteP", "GranitePegmatite", "Granite Pegmatite").setRock(12, 7.6F, 30.1F, 20).setToolable(13, 45, 2.8F, 0.8F, 1.8F, 6).setRGBa(0x4F535AFF);
	
	public static final Mat oak = new Mat(8001, "minecraft", "oak", "Oak", "Oak").setWood(5.3F, 1.0F, 20.0F);
	public static final Mat spruce = new Mat(8002, "minecraft", "spruce", "Spruce", "Spruce").setWood(2.3F, 1.0F, 20.0F);
	public static final Mat birch = new Mat(8003, "minecraft", "birch", "Birch", "Birch").setWood(4.0F, 1.0F, 20.0F);
	public static final Mat ceiba = new Mat(8004, "minecraft", "ceiba", "Ceiba", "Ceiba").setWood(1.1F, 1.0F, 20.0F);
	public static final Mat acacia = new Mat(8005, "minecraft", "acacia", "Acacia", "Acacia").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat oak_black = new Mat(8006, "minecraft", "oak-black", "DarkOak", "Dark Oak").setWood(5.4F, 1.0F, 20.0F);
	
	public static final Mat aspen = new Mat(8011, FarCore.ID, "aspen", "Aspen", "Aspen").setWood(1.6F, 1.0F, 20.0F);
	public static final Mat morus = new Mat(8012, FarCore.ID, "morus", "Morus", "Morus").setWood(3.0F, 1.0F, 20.0F);
	public static final Mat willow = new Mat(8013, FarCore.ID, "willow", "Willow", "Willow").setWood(3.0F, 1.0F, 20.0F);
	
	public static final Mat wheat = new Mat(9001, FarCore.ID, "wheat", "Wheat", "Wheat");
	public static final Mat millet = new Mat(9002, FarCore.ID, "millet", "Millet", "Millet");
	public static final Mat soybean = new Mat(9003, FarCore.ID, "soybean", "Soybean", "Soybean");
	public static final Mat potato = new Mat(9004, FarCore.ID, "potato", "Potato", "Potato");
	public static final Mat sweet_potato = new Mat(9005, FarCore.ID, "sweetpotato", "SweetPotato", "Sweet Potato");
	public static final Mat cabbage = new Mat(9006, FarCore.ID, "cabbage", "Cabbage", "Cabbage");
	public static final Mat reed = new Mat(9007, FarCore.ID, "reed", "Reed", "Reed");
	public static final Mat flax = new Mat(9008, FarCore.ID, "flax", "Flax", "Flax");
	public static final Mat cotton = new Mat(9009, FarCore.ID, "cotton", "Cotton", "Cotton");

	public static final Mat native_copper = new Mat(10001, "farcore", "nativeCopper", "NativeCopper", "Native Copper").setOreProperty(7, 8.0F, 9.0F);
	
	static
	{
		VOID.setTree(new TreeVoid(), false);
		
		oak.setTree(new TreeOak(oak));
		spruce.setTree(new TreeBirch(spruce));
		birch.setTree(new TreeBirch(birch));
		ceiba.setTree(new TreeCeiba(ceiba));
		acacia.setTree(new TreeAcacia(acacia));
		oak_black.setTree(new TreeOakBlack(oak_black));
		aspen.setTree(new TreeAspen(aspen));
		morus.setTree(new TreeMorus(morus));
		willow.setTree(new TreeWillow(willow));
		
		wheat.setCrop(new CropWheat(wheat));
		millet.setCrop(new CropMillet(millet));
		soybean.setCrop(new CropSoybean(soybean));
		potato.setCrop(new CropPotato(potato));
		sweet_potato.setCrop(new CropSweetPotato(sweet_potato));
		cabbage.setCrop(new CropCabbage(cabbage));
		reed.setCrop(new CropReed(reed));
		flax.setCrop(new CropFlax(flax));
		cotton.setCrop(new CropCotton(cotton));
	}
	
	public static void init(){}
}