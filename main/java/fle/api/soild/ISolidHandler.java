package fle.api.soild;

import net.minecraftforge.common.util.ForgeDirection;

public interface ISolidHandler
{
    int fill(ForgeDirection from, SolidStack resource, boolean doFill);
    SolidStack drain(ForgeDirection from, SolidStack resource, boolean doDrain);
    SolidStack drain(ForgeDirection from, int maxDrain, boolean doDrain);
    boolean canFill(ForgeDirection from, Solid Solid);
    boolean canDrain(ForgeDirection from, Solid Solid);
    SolidTankInfo[] getTankInfo(ForgeDirection from);
}