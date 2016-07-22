package farcore.debug;

import static farcore.lib.util.SubTag.ROCK;

import farcore.lib.material.Mat;

@Deprecated
public class Debug
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

	public static void main(String[] args)
	{
		String srcDirName = "D:/Program Files/minecraft/f/forge-1.10.2-12.18.1.2011-mdk/src/main/resources/assets/farcore/textures/blocks/A";
		String destDirName = "D:/Program Files/minecraft/f/forge-1.10.2-12.18.1.2011-mdk/src/main/resources/assets/farcore/textures/blocks/rock";
		String formatName = "chiseled.png";
		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		//		for (Mat material : Mat.register)
		//		{
		//			//			if (material.contain(SubTag.ROCK))
		//			{
		//				ModelFileCreator.provideRockInfo("", material);
		//			}
		//		}
	}
}