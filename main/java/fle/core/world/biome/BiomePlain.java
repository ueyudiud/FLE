package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomePlain extends BiomeBase
{
	public BiomePlain(int id)
	{
		super(id);
        spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 5, 2, 6));
        biomeDecorator.treesPerChunk = -999;
        biomeDecorator.flowersPerChunk = 4;
        biomeDecorator.grassPerChunk = 10;
        flowers.clear();
        addFlower(Blocks.red_flower,    4,  3);
        addFlower(Blocks.red_flower,    5,  3);
        addFlower(Blocks.red_flower,    6,  3);
        addFlower(Blocks.red_flower,    7,  3);
        addFlower(Blocks.red_flower,    0, 20);
        addFlower(Blocks.red_flower,    3, 20);
        addFlower(Blocks.red_flower,    8, 20);
        addFlower(Blocks.yellow_flower, 0, 30);
	}

    public String func_150572_a(Random rand, int x, int y, int z)
    {
        double d0 = plantNoise.func_151601_a((double)x / 200.0D, (double)z / 200.0D);
        int l;

        if (d0 < -0.8D)
        {
            l = rand.nextInt(4);
            return BlockFlower.field_149859_a[4 + l];
        }
        else if (rand.nextInt(3) > 0)
        {
            l = rand.nextInt(3);
            return l == 0 ? BlockFlower.field_149859_a[0] : (l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8]);
        }
        else
        {
            return BlockFlower.field_149858_b[0];
        }
    }

    public void decorate(World world, Random random, int x, int z)
    {
        double d0 = plantNoise.func_151601_a((double)(x + 8) / 200.0D, (double)(z + 8) / 200.0D);
        int k;
        int l;
        int i1;
        int j1;

        if (d0 < -0.8D)
        {
            this.biomeDecorator.flowersPerChunk = 15;
            this.biomeDecorator.grassPerChunk = 5;
        }
        else
        {
            this.biomeDecorator.flowersPerChunk = 4;
            this.biomeDecorator.grassPerChunk = 10;
            genTallFlowers.func_150548_a(2);

            for (k = 0; k < 7; ++k)
            {
                l = x + random.nextInt(16) + 8;
                i1 = z + random.nextInt(16) + 8;
                j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(world, random, l, j1, i1);
            }
        }

        if (Math.abs(x * 3749174L + z * 37947191L) % 103 < 2)
        {
            genTallFlowers.func_150548_a(0);

            for (k = 0; k < 10; ++k)
            {
                l = x + random.nextInt(16) + 8;
                i1 = z + random.nextInt(16) + 8;
                j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(world, random, l, j1, i1);
            }
        }

        super.decorate(world, random, x, z);
    }
}