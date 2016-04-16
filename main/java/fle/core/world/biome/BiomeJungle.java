package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeJungle extends BiomeBase
{
    private boolean type;
	
	public BiomeJungle(int id, boolean flag)
	{
		super(id);
        this.type = flag;

        if (flag)
        {
            this.theBiomeDecorator.treesPerChunk = 2;
        }
        else
        {
            this.theBiomeDecorator.treesPerChunk = 50;
        }

        this.theBiomeDecorator.grassPerChunk = 25;
        this.theBiomeDecorator.flowersPerChunk = 4;

        if (!flag)
        {
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }

        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
	}

    public WorldGenAbstractTree func_150567_a(Random random)
    {
        return (WorldGenAbstractTree)(random.nextInt(10) == 0 ? this.worldGeneratorBigTree : (random.nextInt(2) == 0 ? new WorldGenShrub(3, 0) : (!this.type && random.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, 3, 3) : new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true))));
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(4) == 0 ? 2 : 1);
    }

    public void decorate(World world, Random random, int x, int z)
    {
        super.decorate(world, random, x, z);
        int k = x + random.nextInt(16) + 8;
        int l = z + random.nextInt(16) + 8;
        int height = world.getHeightValue(k, l) * 2; //This was the original input for the nextInt below.  But it could == 0, which crashes nextInt
        if (height < 1) height = 1;
        int i1 = random.nextInt(height);
        (new WorldGenMelon()).generate(world, random, k, i1, l);
        WorldGenVines worldgenvines = new WorldGenVines();

        for (l = 0; l < 50; ++l)
        {
            i1 = x + random.nextInt(16) + 8;
            short short1 = 128;
            int j1 = z + random.nextInt(16) + 8;
            worldgenvines.generate(world, random, i1, short1, j1);
        }
    }
}