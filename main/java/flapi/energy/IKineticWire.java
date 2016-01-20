package flapi.energy;

import net.minecraft.util.BlockPos;
import farcore.util.Direction;

public interface IKineticWire extends IEnergyStorage
{
	int getWindSpeed(BlockPos pos);
	
	void onReseve(Direction dir, KineticEnergyPacket energy);
}