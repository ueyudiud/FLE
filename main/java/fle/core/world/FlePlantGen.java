package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.enums.EnumWorldNBT;
import fle.api.world.BlockPos;
import fle.core.block.plant.PlantBase;
import fle.core.init.IB;

public class FlePlantGen extends FleSurfaceGen
{
    private Block block = IB.plant;
    private int data;

    public FlePlantGen(int size, int count, PlantBase card)
    {
    	super(size, count);
    	data = FLE.fle.getPlantRegister().getPlantID(card);
	}

	@Override
	public boolean generateAt(World aWorld, Random aRand, int x, int y, int z)
	{
		if(aWorld.getBiomeGenForCoords(x, z).getFloatTemperature(x, y, z) < 0.1F) return false;
		Block target = aWorld.getBlock(x, y, z);
		if(block.canBlockStay(aWorld, x, y, z) && (target.isAir(aWorld, x, y, z) || target.isReplaceable(aWorld, x, y, z)))
		{
			aWorld.setBlock(x, y, z, block);
			FLE.fle.getWorldManager().setData(new BlockPos(aWorld, x, y, z), EnumWorldNBT.Metadata, data);
			return true;
		}
		return false;
	}
}