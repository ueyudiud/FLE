package farcore.world;

public interface IWorldManager
{
	boolean isWater(BlockPos pos);
	
	boolean isSourceOfFire(BlockPos pos);
}