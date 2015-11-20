package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import fle.api.block.IWorldNBT;
import fle.api.material.MaterialRock;
import fle.core.block.BlockFleRock;
import fle.core.block.BlockRock;
import fle.core.init.IB;

public class FleRockGen extends FleMineableGen
{
	public FleRockGen(Block base, MaterialRock rock, int number)
	{
		super(BlockFleRock.a(rock), (short) 0, number, base);
	}
}