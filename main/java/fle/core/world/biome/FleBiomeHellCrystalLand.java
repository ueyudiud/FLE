package fle.core.world.biome;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CUSTOM;

import java.util.Random;

import fle.core.world.FleCrystalGen;
import fle.core.world.dim.FLEBiomeDecoratorBase;
import fle.core.world.dim.FLEBiomeDecoratorHell;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.TerrainGen;

public class FLEBiomeHellCrystalLand extends FLEBiomeHellBase
{
	public FLEBiomeHellCrystalLand(String name, int index)
	{
		super(name, index);
		spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 3, 2, 2));
        spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 8, 4, 4));
        theBiomeDecorator.generateLakes = false;
        setTemperatureRainfall(6F, 0.0F);
	}
	
	@Override
	public void decorate(World aWorld, Random aRNG, int x, int z)
	{
        boolean doGen = TerrainGen.decorate(aWorld, aRNG, x, z, CUSTOM);
        if (doGen)
        {
            for (int j = 0; j < 5; ++j)
            {
            	 new FleCrystalGen().generate(aWorld, aRNG, x * 16, aRNG.nextInt(100) + 10, z * 16);
            }
        }
		super.decorate(aWorld, aRNG, x, z);
	}
}