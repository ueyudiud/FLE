package farcore.evt;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import farcore.biology.plant.ICoverablePlantSpecies;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SeedSpreadEvent extends Event
{
	public World world;
	public BlockPos pos;
	public ICoverablePlantSpecies species;
	
	public SeedSpreadEvent(World world, BlockPos pos,
			ICoverablePlantSpecies species)
	{
		this.world = world;
		this.pos = pos;
		this.species = species;
	}
}