package fle.core.render;

import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.core.te.creative.TileEntityThermal;
import fle.core.util.FLEMath;

public class TESRTD extends TESRBase<TileEntityThermal>
{
	@Override
	public void renderTileEntityAt(TileEntityThermal tile, double xPos,
			double yPos, double zPos)
	{
		renderFont(new String[]{
				"Energy Currect : " + FleValue.format_MJ.format(tile.getThermalEnergyCurrect(ForgeDirection.UNKNOWN)),
				"Energy Reseave Speed : " + FleValue.format_MJ.format(tile.getPreHeatEmit()) + "/Tick"}, 
				(float) xPos + 0.5F, (float) yPos + 1.5F, (float) zPos + 0.5F, (float) 0.015625F, getDefaultRotation());
	}
}