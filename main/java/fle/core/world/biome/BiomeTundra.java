package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTundra extends BiomeBase
{
    private static final WorldGenTaiga1 tree = new WorldGenTaiga1();
    
	public BiomeTundra(int id)
	{
		super(id);
		topBlock = Blocks.dirt;
		field_150604_aj = 2;
		theBiomeDecorator.grassPerChunk = 2;
		spawnableCreatureList.clear();

        spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
	}
	
	@Override
	public WorldGenAbstractTree func_150567_a(Random random)
	{
		return tree;
	}

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
    }
}