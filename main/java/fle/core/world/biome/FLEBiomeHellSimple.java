package fle.core.world.biome;

import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.world.biome.BiomeGenBase;

public class FLEBiomeHellSimple extends FLEBiomeHellBase
{
	public FLEBiomeHellSimple(String name, int index)
	{
		super(name, index);
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 1, 4, 4));
	}
}