package fle.api.soild;

import net.minecraftforge.common.util.ForgeDirection;

public interface ISolidHandler
{
    int fillS(ForgeDirection from, SolidStack resource, boolean doFill);
    SolidStack drainS(ForgeDirection from, SolidStack resource, boolean doDrain);
    SolidStack drainS(ForgeDirection from, int maxDrain, boolean doDrain);
    boolean canFillS(ForgeDirection from, Solid Solid);
    boolean canDrainS(ForgeDirection from, Solid Solid);
    SolidTankInfo[] getSolidTankInfo(ForgeDirection from);
}