package fle.api.plant;

import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import fle.api.util.DropInfo;

/**
 * The plant id.
 * @author ueyudiud
 *
 */
public abstract class PlantCard
{
	public abstract String getPlantName();
	
	public abstract String getPlantTextureName();

	public abstract EnumPlantType getPlantType();
	
	public abstract DropInfo getDropInfo();
	
	public abstract boolean shouldUseBiomeColor();
	
	public void updatePlant(World aWorld, int x, int y, int z)
	{
		
	}
}