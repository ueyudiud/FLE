/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common;

import nebula.common.capability.CapabilityHelper;
import nebula.common.world.IBiomeExtended;
import nebula.common.world.IBiomeRegetter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.capabilities.Capability;

/**
 * All method are used by ASM, do not call them.
 * 
 * @author ueyudiud
 */
public class CommonOverride
{
	public static void registerCapabilityType(Class<?> clazz, Capability<?> capability)
	{
		CapabilityHelper.registerCapabilityType(clazz, capability);
	}
	
	public static boolean isRainingAtBiome(Biome biome, World world, BlockPos pos)
	{
		if (biome instanceof IBiomeExtended) return ((IBiomeExtended) biome).canRainingAt(world, pos);
		return biome.canRain();
	}
	
	public static Biome regetBiome(int saveID, BlockPos pos, BiomeProvider provider)
	{
		if (provider instanceof IBiomeRegetter) return ((IBiomeRegetter) provider).getBiome(saveID, pos);
		return Biome.getBiome(saveID);
	}
}
