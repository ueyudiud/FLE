package fle.core.tile.statics;

import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceDirt;
import fle.api.tile.TileEntitySubstance;

public class TileEntityDirt extends TileEntitySubstance<SubstanceDirt>
{
	@Override
	public Register<SubstanceDirt> getRegister() 
	{
		return SubstanceDirt.getDirts();
	}
}