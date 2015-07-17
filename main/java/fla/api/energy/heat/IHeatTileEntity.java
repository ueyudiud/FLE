package fla.api.energy.heat;

import net.minecraftforge.common.util.ForgeDirection;
import fla.api.block.IInfomationBlock;

public interface IHeatTileEntity extends IInfomationBlock
{
	public boolean canEmmit(ForgeDirection d, int pkg);

	public boolean canReseave(ForgeDirection d, int pkg);
	
	public int getHeatCorrect(ForgeDirection d);
	
	public void catchHeat(ForgeDirection d, int pkg);
}
