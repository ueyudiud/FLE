package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeMushroomIsland extends BiomeBase
{
	public BiomeMushroomIsland(int id)
	{
		super(id);
        theBiomeDecorator.treesPerChunk = -100;
        theBiomeDecorator.flowersPerChunk = -100;
        theBiomeDecorator.grassPerChunk = -100;
        theBiomeDecorator.mushroomsPerChunk = 1;
        theBiomeDecorator.bigMushroomsPerChunk = 1;
        topBlock = Blocks.mycelium;
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityMooshroom.class, 8, 4, 8));
	}
}