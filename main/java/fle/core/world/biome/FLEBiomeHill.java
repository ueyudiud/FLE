package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class FLEBiomeHill extends FLEBiome
{	
	public FLEBiomeHill(String name, int index)
	{
		super(name, index);
		setTemperatureRainfall(0.7F, 0.4F);
		setHeight(height_LowPlains);
		theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.flowersPerChunk = 4;
        theBiomeDecorator.grassPerChunk = 10;
        flowers.clear();
        addFlower(Blocks.red_flower,    4,  1);
        addFlower(Blocks.red_flower,    5,  1);
        addFlower(Blocks.red_flower,    6,  1);
        addFlower(Blocks.red_flower,    7,  1);
        addFlower(Blocks.red_flower,    0,  9);
        addFlower(Blocks.red_flower,    3,  9);
        addFlower(Blocks.red_flower,    8,  9);
        addFlower(Blocks.yellow_flower, 0,  8);
	}
	


    public String func_150572_a(Random aRand, int x, int y, int z)
    {
        double d0 = plantNoise.func_151601_a((double)x / 200.0D, (double)z / 200.0D);
        int l;

        if (d0 < -0.8D)
        {
            l = aRand.nextInt(4);
            return BlockFlower.field_149859_a[4 + l];
        }
        else if (aRand.nextInt(3) > 0)
        {
            l = aRand.nextInt(3);
            return l == 0 ? BlockFlower.field_149859_a[0] : (l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8]);
        }
        else
        {
            return BlockFlower.field_149858_b[0];
        }
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        double d0 = plantNoise.func_151601_a((double)(x + 8) / 200.0D, (double)(z + 8) / 200.0D);
        int k;
        int l;
        int i1;
        int j1;

        if (d0 < -0.8D)
        {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        else
        {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            genTallFlowers.func_150548_a(2);

            for (k = 0; k < 7; ++k)
            {
                l = x + aRand.nextInt(16) + 8;
                i1 = z + aRand.nextInt(16) + 8;
                j1 = aRand.nextInt(aWorld.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(aWorld, aRand, l, j1, i1);
            }
        }

        super.decorate(aWorld, aRand, x, z);
    }

    @Override
    protected void genTargetBlockAt(int aLayer, float aTemp, Random aRand,
    		Block[] aBlocks, byte[] aBytes, int targetID)
    {
		Block placeBlock;
		byte metadata;
		switch(aLayer)
		{
		case 0 : ;
		placeBlock = Blocks.grass;
		metadata = 0;
		break;
		case 1 : ;
		case 2 : ;
		placeBlock = Blocks.dirt;
		metadata = 0;
		break;
		case 3 : ;
		placeBlock = Blocks.gravel;
		metadata = 0;
		break;
		default: ;
		placeBlock = Blocks.stone;
		metadata = 0;
		}
		
		aBlocks[targetID] = placeBlock;
		aBytes[targetID] = metadata;
    }
}