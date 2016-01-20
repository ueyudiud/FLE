package simElectricity.Common.Blocks.TileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import simElectricity.API.Common.TileStandardSEMachine;
import simElectricity.API.Energy;
import simElectricity.API.FluidUtil;
import simElectricity.API.IEnergyNetUpdateHandler;
import simElectricity.API.INetworkEventHandler;
import simElectricity.Common.Core.SEItems;

import java.util.List;

public class TileIceMachine extends TileStandardSEMachine implements IFluidHandler, IEnergyNetUpdateHandler, INetworkEventHandler {
    public static float waterPerIceIngot = 1000 / 9;
    public int maxCapacity = 10000;
    public float onResistance = 1000F;
    public float energyPerItem = 4000F;
    public FluidTank tank = new FluidTank(maxCapacity);

    public float energyStored;
    public float resistance = Float.MAX_VALUE;

    public boolean isWorking;
    public boolean operationalVoltage;
    public int fluidID, amountP, progress, isPowered;


    void doWork(FluidStack fluid) {
        if (this.tank.getFluidAmount() >= waterPerIceIngot && operationalVoltage &&    //Enough water
                (inv[2] == null || inv[2].stackSize < inv[2].getMaxStackSize())) {    //Enough space
            if (resistance != onResistance) {
                resistance = onResistance;
                Energy.postTileChangeEvent(this);
            }

            isWorking = true;
        } else {
            if (resistance != Float.MAX_VALUE) {
                resistance = Float.MAX_VALUE;
                Energy.postTileChangeEvent(this);
            }

            isWorking = false;
        }

        energyStored += Math.pow(Energy.getVoltage(this), 2) / resistance;

        if (energyStored >= energyPerItem) {
            energyStored -= energyPerItem;
            fluid.amount -= waterPerIceIngot;

            if (inv[2] == null)
                inv[2] = new ItemStack(SEItems.iceIngot, 1);
            else
                inv[2].stackSize++;
        }


        progress = (int) (100 * energyStored / energyPerItem);
    }

    @Override
    public void update() {
        super.update();

        if (worldObj.isRemote)
            return;

        FluidStack fluid = tank.getFluid();

        if (fluid != null)
            if (fluid.amount == 0)
                fluid = null;
            else
                doWork(fluid);

        FluidStack l = FluidUtil.drainContainer(maxCapacity, fluid, inv, 0, 1);
        if (l != null) {
            if (fluid == null)
                fluid = l;
            else
                fluid.amount += l.amount;
        }

        tank.setFluid(fluid);

        if (fluid != null) {
            fluidID = fluid.getFluidID();
            amountP = fluid.amount * 1000 / maxCapacity;
        } else
            fluidID = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);

        isWorking = n.getBoolean("isWorking");
        energyStored = n.getFloat("energyStored");

        tank.readFromNBT(n);
    }

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);

        n.setBoolean("isWorking", isWorking);
        n.setFloat("energyStored", energyStored);

        tank.writeToNBT(n);
    }


    @Override
    public void addNetworkFields(List fields) {
        fields.add("isWorking");
        worldObj.markBlockForUpdate(pos);
    }

    @Override
    public void onFieldUpdate(String[] fields, Object[] values) {

    }


    @Override
    public void onEnergyNetUpdate() {
        operationalVoltage = Energy.getVoltage(this) >= 200;
        isPowered = operationalVoltage ? 1 : 0;
    }


    @Override
    public double getResistance() {
        return resistance;
    }

    @Override
    public int getInventorySize() {
        return 3;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1 || index == 2;
    }


    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

	@Override
	public String getName()
	{
		return "Ice Machine";
	}
}