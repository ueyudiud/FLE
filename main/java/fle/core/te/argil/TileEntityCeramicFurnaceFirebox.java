package fle.core.te.argil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.energy.IThermalTileEntity;
import flapi.gui.IToolClickHandler;
import flapi.recipe.SingleInputRecipe;
import flapi.te.TEInventory;
import flapi.te.interfaces.IMachineConditionable;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.tool.BurnHandler;

public class TileEntityCeramicFurnaceFirebox extends TEInventory implements IThermalTileEntity, IToolClickHandler, IMachineConditionable
{
	protected final int ceramicPower = Config.getInteger("pCeramicFirebox");
	
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	long burnTime;
	long currectBurnTime;
	boolean isBurning;
	
	public TileEntityCeramicFurnaceFirebox()
	{
		super(4);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		nbt.setLong("BurnTime", burnTime);
		nbt.setBoolean("IsBurning", isBurning);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		currectBurnTime = burnTime = nbt.getLong("BurnTime");
		isBurning = nbt.getBoolean("IsBurning");
	}
	
	private ForgeDirection dirs[] = {ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.SOUTH};

	@Override
	public void update()
	{
		if(burnTime > 0)
		{
			if(getTemperature(ForgeDirection.UNKNOWN) < 1300)
			{
				burnTime -= ceramicPower;
				onHeatReceive(ForgeDirection.UNKNOWN, ceramicPower);
			}
			else
			{
				--burnTime;
			}
			for(ForgeDirection dir : dirs)
			{
				if(getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
				{
					for(int i = 0; i < 10; ++i)
						FLE.fle.getThermalNet().emmitHeatTo(getBlockPos(), dir);
					break;
				}
			}
		}
		if(burnTime <= 0 && isBurning)
		{
			burnTime = 0;
			for(int i = 0; i < 3; ++i)
			{
				long buf = FleAPI.getFulBuf(stacks[i], FLE.fle.getAirConditionProvider().getAirLevel(getBlockPos()));
				if(buf > 0)
				{
					currectBurnTime = burnTime = buf;
					decrStackSize(i, 1);
					markForUpdate();
					break;
				}
			}
			if(burnTime <= 0)
			{
				markForUpdate();
			}
		}
		if(burnTime <= 0)
		{
			isBurning = false;
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_ceramicFireBox;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[]{0, 1, 2};
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aTarget,
			int aSide)
	{
		return false;
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP && getBlockPos().toPos(ForgeDirection.UP).getBlockTile() instanceof IThermalTileEntity ?
				tc.getThermalConductivity() * 6 : tc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return tc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		tc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		tc.emitHeat(heatValue);
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}

	public boolean isBurning()
	{
		return isBurning;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnProgress(int length)
	{
		return (int) ((double) burnTime / (double) currectBurnTime * (float) length);
	}
	
	@Override
	public ItemStack onToolClick(ItemStack stack, EntityLivingBase player,
			int activeID)
	{
		if(activeID == 3)
		{
			SingleInputRecipe recipe = BurnHandler.getRecipe(stack);
			if(recipe != null)
			{
		    	isBurning = true;
				return recipe.getResult(stack);
			}
		}
		return stack;
	}
	
	@Override
	public int getProgressSize()
	{
		return 2;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? (int) burnTime : id == 1 ? (int) currectBurnTime : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0)
		{
			isBurning = (burnTime = value) > 0 ? true : false;
		}
		if(id == 1)
		{
			currectBurnTime = value;
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return FleAPI.getFulBuf(itemstack) >= 0;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir != ForgeDirection.DOWN ? new int[]{0, 1, 2} : null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return isItemValidForSlot(slotID, resource);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return !isItemValidForSlot(slotID, resource);
	}

	@Override
	public Condition getCondition()
	{
		return isBurning() ? Condition.Working : Condition.Waiting;
	}
}