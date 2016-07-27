package farcore.debug;

import static farcore.lib.util.SubTag.ROCK;
import static farcore.lib.util.SubTag.WOOD;

import farcore.FarCore;
import farcore.lib.material.Mat;

public class DebugMaterial
{
	public static final Mat stone = new Mat(7001, "minecraft", "stone", "Stone", "Stone").setTag(ROCK);
	public static final Mat compact_stone = new Mat(7002, "farcore", "stone-compact", "CompactStone", "Compact Stone").setTag(ROCK);
	public static final Mat andesite = new Mat(7003, "farcore", "andesite", "Andesite", "Andesite").setTag(ROCK);
	public static final Mat basalt = new Mat(7004, "farcore", "basalt", "Basalt", "Basalt").setTag(ROCK);
	public static final Mat diorite = new Mat(7005, "farcore", "diorite", "Diorite", "Diorite").setTag(ROCK);
	public static final Mat gabbro = new Mat(7006, "farcore", "gabbro", "Gabbro", "Gabbro").setTag(ROCK);
	public static final Mat granite = new Mat(7007, "farcore", "granite", "Granite", "Granite").setTag(ROCK);
	public static final Mat kimberlite = new Mat(7008, "farcore", "kimberlite", "Kimberlite", "Kimberlite").setTag(ROCK);
	public static final Mat limestone = new Mat(7009, "farcore", "limestone", "Lime", "Limestone").setTag(ROCK);
	public static final Mat marble = new Mat(7010, "farcore", "marble", "Marble", "Marble").setTag(ROCK);
	public static final Mat netherrack = new Mat(7011, "minecraft", "netherrack", "Netherrack", "Netherrack").setTag(ROCK);
	public static final Mat obsidian = new Mat(7012, "farcore", "obsidian", "Obsidian", "Obsidian").setTag(ROCK);
	public static final Mat peridotite = new Mat(7013, "farcore", "peridotite", "Peridotite", "Peridotite").setTag(ROCK);
	public static final Mat rhyolite = new Mat(7014, "farcore", "rhyolite", "Rhyolite", "Rhyolite").setTag(ROCK);
	public static final Mat graniteP = new Mat(7015, "farcore", "graniteP", "GranitePegmatite", "Granite Pegmatite").setTag(ROCK);

	public static final Mat oak = new Mat(8001, "minecraft", "oak", "Oak", "Oak").setTag(WOOD);
	public static final Mat spruce = new Mat(8002, "minecraft", "spruce", "Spruce", "Spruce").setTag(WOOD);
	public static final Mat birch = new Mat(8003, "minecraft", "birch", "Birch", "Birch").setTag(WOOD);
	public static final Mat ceiba = new Mat(8004, "minecraft", "ceiba", "Ceiba", "Ceiba").setTag(WOOD);
	public static final Mat acacia = new Mat(8005, "minecraft", "acacia", "Acacia", "Acacia").setTag(WOOD);
	public static final Mat oak_black = new Mat(8006, "minecraft", "oak-black", "DarkOak", "Dark Oak").setTag(WOOD);
	public static final Mat aspen = new Mat(8011, FarCore.ID, "aspen", "Aspen", "Aspen").setTag(WOOD);
	public static final Mat morus = new Mat(8012, FarCore.ID, "morus", "Morus", "Morus").setTag(WOOD);
	public static final Mat willow = new Mat(8013, FarCore.ID, "willow", "Willow", "Willow").setTag(WOOD);
	
	public static void init(){	}
}