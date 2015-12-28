package fle.core.te.argil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import flapi.chem.MatterDictionary;
import flapi.chem.MeltingRecipe;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMatterInputHatch;
import flapi.chem.base.MatterStack;
import flapi.energy.IThermalTileEntity;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.te.TEIFluidTank;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.net.FleTEPacket;
import fle.core.recipe.RecipeHelper;

public class TileEntityTerrine extends TEIFluidTank implements IThermalTileEntity, IMatterInputHatch, IChemCondition
{
	private ThermalTileHelper heatCurrect = new ThermalTileHelper(Materials.Argil);
	public int mode;
	public GuiCondition type = GuiError.DEFAULT;
	public MeltingRecipe recipe;
	private int scale;
	public double recipeTime;
	
	public TileEntityTerrine() 
	{
		super(2, 3000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		mode = nbt.getShort("Mode");
		heatCurrect.readFromNBT(nbt);
		recipe = MatterDictionary.getMeltingRecipe(nbt.getString("Recipe"));
		if(recipe != null)
		{
			scale = nbt.getInteger("RecipeScale");
			recipeTime = nbt.getDouble("RecipeTime");
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("Mode", (short) mode);
		heatCurrect.writeToNBT(nbt);
		if(recipe != null)
		{
			nbt.setString("Recipe", recipe.getName());
			nbt.setInteger("RecipeScale", scale);
			nbt.setDouble("RecipeTime", recipeTime);
		}
	}
	
	private int buf = 0;

	@Override
	public void update() 
	{
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		switch(mode)
		{
		case 0 :
		{
			if(rand.nextFloat() < 0.03125F)
			{
				tryDrainFluid(ForgeDirection.DOWN, 1, true, true);
			}
			if(RecipeHelper.fillOrDrainInventoryTank(this, tank, 0, 1))
			{
				type = GuiError.DEFAULT;
			}
			else
			{
				type = GuiError.CAN_NOT_OUTPUT;
			}
		}
		break;
		case 1 :
		{
			if(isClient())
			{
				return;
			}
			if(recipe == null)
			{
				Object[] o = MatterDictionary.getAndInputMeltingRecipe(this, this);
				if(o != null)
				{
					recipe = (MeltingRecipe) o[0];
					scale = (Integer) o[1];
				}
			}
			if(recipe != null && stacks[1] == null)
			{
				if(recipe.input.req.match(this))
				{
					float value = recipe.getSpeed() * recipe.input.req.speed(this);
					onHeatEmit(ForgeDirection.UNKNOWN, value);
					recipeTime += value;
					if(recipeTime >= recipe.energyRequire)
					{
						FluidStack resource = recipe.fluid.copy();
						resource.amount *= scale;
						resource.amount /= 4;
						if(tank.fill(resource, false) == 0)
						{
							type = GuiError.CAN_NOT_OUTPUT;
						}
						else
						{
							if(!isClient())
							{
								tank.fill(resource, true);
							}
							recipe = null;
							recipeTime = 0;
							scale = 0;
							decrStackSize(0, 1);
							type = GuiError.DEFAULT;
						}
					}
					FLE.fle.getNetworkHandler().sendToNearBy(new FleTEPacket(this, (byte) 1), this, 16.0F);
				}
			}
			else
			{
				mode = 0;
				recipeTime = 0;
			}
		}
		}
		heatCurrect.update();
		if(buf++ == 50)
		{
			buf = 0;
			sendToNearBy(new FleTEPacket(this, (byte) 2), 16.0F);
			sendToNearBy(new FleTEPacket(this, (byte) 3), 16.0F);
		}
	}
	
	@Override
	protected FluidTank getFluidTankFromSide(ForgeDirection aSide)
	{
		return aSide == ForgeDirection.UP ? tank : null;
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
	
	public GuiCondition getError()
	{
		return type;
	}

	public void setClose() 
	{
		type = GuiError.DEFAULT;
		mode = 1;
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return heatCurrect.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return heatCurrect.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir) 
	{
		return heatCurrect.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		heatCurrect.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		heatCurrect.emitHeat(heatValue);
	}
	
	public double getProgress()
	{
		return recipeTime / recipe.energyRequire;
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		switch(aType)
		{
		case 1 : return recipeTime;
		case 2 : return mode;
		case 3 : return heatCurrect.getHeat();
		}
		return null;
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 1)
		{
			recipeTime = (Double) contain;
		}
		else if(type == 2)
		{
			mode = (Integer) contain;
		}
		else if(type == 3)
		{
			heatCurrect.syncHeat((Double) contain);
		}
	}
	@Override
	public double getPreHeatEmit()
	{
		return heatCurrect.getPreHeatEmit();
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_terrine;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP ? true : false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return new int[]{0};
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	protected boolean isInputSlot(int i)
	{
		return i == 0;
	}

	protected boolean isOutputSlot(int i)
	{
		return false;
	}

	@Override
	public int getMatterHatchSize()
	{
		return 1;
	}

	@Override
	public MatterStack getMatter(int idx)
	{
		return MatterDictionary.toMatter(stacks[0]);
	}
	
	@Override
	public MatterStack decrMatter(int idx, int size)
	{
		return MatterDictionary.toMatter(decrStackSize(idx, size));
	}

	@Override
	public void setMatter(int idx, MatterStack stack)
	{
		stacks[idx] = MatterDictionary.toItem(stack);
	}

	@Override
	public EnumEnviorment isOpenEnviorment()
	{
		return mode == 0 ? EnumEnviorment.Open : EnumEnviorment.Close;
	}

	@Override
	public EnumPH getPHLevel()
	{
		return EnumPH.Water;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return EnumOxide.Default;
	}

	@Override
	public int getTemperature()
	{
		return getTemperature(ForgeDirection.UNKNOWN);
	}
}