package fle.load;

import farcore.enums.EnumBiome;
import farcore.util.FleLog;
import fle.core.world.biome.BiomeBeach;
import fle.core.world.biome.BiomeIsland;
import fle.core.world.biome.BiomeMountain;
import fle.core.world.biome.BiomeOcean;
import fle.core.world.biome.BiomePlain;
import fle.core.world.biome.BiomeRiver;
import fle.core.world.biome.BiomeSwamp;
import fle.core.world.biome.BiomeValley;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase.Height;

public class Biomes
{
	static final Height height_River = new Height(-0.5F, 0.02F);
	static final Height height_Plain = new Height(0.1F, 0.05F);
	static final Height height_LowHills = new Height(0.8F, 0.5F);
    static final Height height_MidHills = new Height(1.5F, 0.8F);
    static final Height height_HighHills = new Height(2.3F, 1.1F);
    static final Height height_Plateau = new Height(1.2F, 0.1F);
    static final Height height_Valley = new Height(0.3F, 0.9F);
    static final Height height_LowIslands = new Height(0.2F, 0.3F);
    
	public static void init()
	{
		FleLog.getLogger().info("Start register biomes.");
		EnumBiome.ocean.setBiome(				new BiomeOcean(0, false, false)			.setColor(0x6368B7));
		EnumBiome.plain.setBiome(				new BiomePlain(1)						.setColor(0x8DB360).setHeight(height_Plain));
		EnumBiome.ocean_deep.setBiome(			new BiomeOcean(2, false, true)			.setColor(0x0002B2));
		EnumBiome.continental_shelf.setBiome(	new BiomeOcean(3, true, false)			.setColor(0x269DCC));
		EnumBiome.low_hill.setBiome(			new BiomePlain(4)						.setColor(0x9EBC65).setHeight(height_LowHills));
		EnumBiome.mid_hill.setBiome(			new BiomePlain(5)						.setColor(0xA2C269).setHeight(height_MidHills));
		EnumBiome.mid_mountain.setBiome(		new BiomeMountain(6, true, true)		.setColor(0x609860).setHeight(height_MidHills));
		EnumBiome.plateau.setBiome(				new BiomePlain(7)						.setColor(0x9EB287).setHeight(height_Plateau));
		EnumBiome.high_mountain.setBiome(		new BiomeMountain(8, false, false)		.setColor(0x606060).setHeight(height_HighHills).setEnableSnow());
		EnumBiome.island.setBiome(				new BiomeIsland(9, Blocks.grass, false)	.setColor(0x11950C).setHeight(height_LowIslands));
		EnumBiome.beach.setBiome(				new BiomeBeach(10)						.setColor(0xFADE55));
		EnumBiome.river.setBiome(				new BiomeRiver(11, false)				.setColor(0x1700E8).setHeight(height_River));
		EnumBiome.valley.setBiome(				new BiomeValley(12)						.setColor(0x5DD140));
		EnumBiome.swamp.setBiome(				new BiomeSwamp(13)						.setColor(0x07F9B2).func_76733_a(9154376));
	}
}