package farcore.interfaces.energy.thermal;

import farcore.enums.Direction;
import net.minecraft.world.World;

public interface IThermalTile
{
	World getWorldObj();

	boolean canConnectTo(Direction direction);
	
	float getTemperature(Direction direction);
	
	float getThermalConductivity(Direction direction);

	void receiveThermalEnergy(Direction direction, float value);

	void emitThermalEnergy(Direction direction, float value);
	
	float getDeltaHeat();
}