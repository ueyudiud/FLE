package fle.core.te.argil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.MatterDictionary;
import flapi.chem.MeltingRecipe;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMatterInputHatch;
import flapi.chem.base.MatterStack;
import flapi.energy.IThermalTileEntity;
import flapi.te.TEIFluidTank;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.recipe.RecipeHelper;

public class TileEntityCeramicFurnaceCrucible extends TEIFluidTank implements IThermalTileEntity, IChemCondition, IMatterInputHatch
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	public MeltingRecipe recipe;
	public int scale;
	public float progress;
	public TileEntityCeramicFurnaceCrucible()
	{
		super(3, 3000);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		if(recipe != null)
		{
			nbt.setString("Recipe", recipe.getName());
			nbt.setInteger("Scale", scale);
			nbt.setInteger("Progress", (int) progress);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		recipe = MatterDictionary.getMeltingRecipe(nbt.getString("Recipe"));
		if(recipe != null)
		{
			scale = nbt.getInteger("Scale");
			progress = nbt.getInteger("Progress");
		}
	}
	
	@Override
	public void update()
	{
		if(recipe == null)
		{
			Object[] obj = MatterDictionary.getAndInputMeltingRecipe(this, this);
			if(obj != null)
			{
				recipe = (MeltingRecipe) obj[0];
				scale = (Integer) obj[1];
			}
		}
		if(recipe != null)
		{
			if(recipe.input.req.match(this))
			{
				float tick = recipe.input.req.speed(this) * recipe.defaultSpeed;
				onHeatEmit(ForgeDirection.UNKNOWN, tick);
				progress += tick;
				if(progress >= recipe.energyRequire * scale)
				{
					if(RecipeHelper.matchOutFluidStack(tank, recipe.fluid))
					{
						FluidStack fluid = recipe.fluid.copy();
						fluid.amount *= scale;
						RecipeHelper.onOutputFluidStack(tank, fluid);
						recipe = null;
						progress = 0;
						scale = 0;
					}
					else
					{
						progress = recipe.energyRequire;
					}
				}
			}
		}
		syncFluidTank();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return dir == ForgeDirection.DOWN && getBlockPos().toPos(dir).getBlockTile() instanceof IThermalTileEntity ?
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

	@Override
	public EnumPH getPHLevel()
	{
		return EnumPH.Water;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return EnumOxide.C;
	}

	@Override
	public int getTemperature()
	{
		return getTemperature(ForgeDirection.UNKNOWN);
	}

	public void drain()
	{
		tank.drain(tank.getCapacity(), true);
	}

	@Override
	public EnumEnviorment isOpenEnviorment()
	{
		return EnumEnviorment.Open;
	}
	
	public int getProgressBar(int length)
	{
		return (int) (progress * length / recipe.energyRequire);
	}
	
	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return direction == ForgeDirection.UP ? MatterDictionary.toMatter(resource) != null : false;
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
		return new int[0];
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_ceramicFurnace;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i < 3;
	}

	@Override
	public int getMatterHatchSize()
	{
		return stacks.length;
	}

	@Override
	public MatterStack getMatter(int idx)
	{
		return MatterDictionary.toMatter(stacks[idx]);
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
}