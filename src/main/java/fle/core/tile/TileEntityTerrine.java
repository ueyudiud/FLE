package fle.core.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.thermal.ThermalHelper;
import farcore.enums.Direction;
import farcore.interfaces.gui.IHasGui;
import farcore.interfaces.tile.IFluidTanks;
import farcore.inventory.Inventory;
import farcore.util.U;
import fle.api.tile.ITileInventory;
import fle.api.tile.TileEntityThermalable;
import fle.api.util.TemperatureHandler;
import fle.core.container.alpha.ContainerTerrine;
import fle.core.gui.alpha.GuiTerrine;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileEntityTerrine extends TileEntityThermalable 
implements IFluidHandler, IFluidTanks, ITileInventory, IHasGui
{
	private int mode = 0;
	private FluidTank tank = new FluidTank(16000);
	private Inventory inventory = new Inventory(2, "terrine", 64);
	
	public TileEntityTerrine()
	{
		super(new ThermalHelper(1.2E4F, 3.5E-2F));
	}
	
	public void writeNBTToItem(ItemStack stack)
	{
		if(stack != null)
		{
			NBTTagCompound compound = U.Inventorys.setupNBT(stack, true);
			inventory.writeToNBT(compound);
			compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
			TemperatureHandler.setTemperature(stack, getTemperature(Direction.Q));
		}
	}
	
	public void readNBTFromItem(ItemStack stack)
	{
		if(stack != null)
		{
			NBTTagCompound compound = U.Inventorys.setupNBT(stack, false);
			inventory.readFromNBT(compound);
			tank.readFromNBT(compound.getCompoundTag("tank"));
			setTemp(TemperatureHandler.getTemperature(stack));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setByte("mode", (byte) mode);
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		nbt.setByte("m", (byte) mode);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		mode = nbt.getByte("mode");
		if(nbt.hasKey("tank"))
		{
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		mode = nbt.getByte("m");
	}

	@Override
	protected void updateServer2()
	{
		if(mode == 1)
		{
			inventory.fillOrDrainInventoryTank(tank, 0, 1);
		}
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return tank.getFluid() == null || tank.getFluid().getFluid() == resource.getFluid() ?
				tank.drain(resource.amount, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
	}
	
	@Override
	public IInventory getInventory()
	{
		return inventory;
	}
	
	public int getTankSize()
	{
		return 1;
	}
	
	public IFluidTank getTank(int index)
	{
		return tank;
	}
	
	@Override
	public void setFluidStackInTank(int index, FluidStack stack)
	{
		tank.setFluid(stack);
	}
	
	@SideOnly(Side.CLIENT)
	public Gui openGUI(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GuiTerrine(this, player);
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ContainerTerrine(this, player);
	}

	public void switchMode()
	{
		switch(mode)
		{
		case 0 :
			ItemStack stack;
			if(inventory.stacks[0] != null)
			{
				stack = inventory.stacks[0];
				if(!(FluidContainerRegistry.isContainer(stack) ||
						stack.getItem() instanceof IFluidContainerItem))
				{
					return;
				}
			}
			if(inventory.stacks[1] != null) return;
			mode = 1;
			break;
		case 1 :
			if(tank.getFluid() != null)
			{
				return;
			}
			mode = 0;
			break;
		}
		syncToNearby();
	}

	public int getMode()
	{
		return mode;
	}
}