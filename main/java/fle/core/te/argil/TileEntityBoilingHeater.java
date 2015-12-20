package fle.core.te.argil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.energy.IThermalTileEntity;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.gui.IToolClickHandler;
import flapi.material.IChemCondition;
import flapi.material.IFluidChemInfo;
import flapi.recipe.SingleInputRecipe;
import flapi.te.TEIFluidTank;
import flapi.te.interfaces.IMachineConditionable;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.net.FleTEPacket;
import fle.core.recipe.FLEBoilingHeaterRecipe;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHKey;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;
import fle.core.recipe.FluidSolidMixRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.tool.BurnHandler;

public class TileEntityBoilingHeater extends TEIFluidTank implements IThermalTileEntity, IChemCondition, IToolClickHandler, IMachineConditionable
{	
	protected final int power = Config.getInteger("pBoilingHeater");
	
	private static final int[] a = {0, 1};
	private static final int[] b = {2, 3};
	private static final int[] c = {4};
	
	private ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	public GuiCondition type = GuiError.DEFAULT;	
	public BHKey key;
	private int recipeTick;
	private long burnTime;
	private long currectItemBurntime;
	private boolean isBurning;
	
	public TileEntityBoilingHeater()
	{
		super(6, 2000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		FLEBoilingHeaterRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
		recipeTick = nbt.getInteger("RecipeTick");
		burnTime = nbt.getLong("BurnTime");
		currectItemBurntime = nbt.getLong("CurrectBurnTime");
		isBurning = nbt.getBoolean("Burning");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		if(key != null) nbt.setString("Recipe", key.toString());
		nbt.setInteger("RecipeTick", recipeTick);
		nbt.setLong("BurnTime", burnTime);
		nbt.setLong("CurrectBurnTime", currectItemBurntime);
		nbt.setBoolean("Burning", isBurning);
	}

	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		if(key != null && i == 2) return null;
		return super.decrStackSize(i, size);
	}
		
	@Override
	protected void update()
	{
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());

		if(burnTime == 0 && isBurning)
		{
			if(FleAPI.getFulBuf(stacks[4]) > 0)
			{
				currectItemBurntime = burnTime = FleAPI.getFulBuf(stacks[4]);
				RecipeHelper.onInputItemStack(this, 4);
			}
			else
			{
				isBurning = false;
			}
		}
		if(burnTime > 0)
		{
			if(getTemperature() < 1100)
			{
				isBurning = true;
				long heat = Math.min(burnTime, power);
				burnTime -= heat;
				onHeatReceive(ForgeDirection.UNKNOWN, power);
			}
			else
			{
				--burnTime;
			}
		}
		if(isClient()) return;
		if(key == null)
		{
			FluidSolidMixRecipe.mixSolidAndFluid(tank, this, 0, this);
			if(!RecipeHelper.fillOrDrainInventoryTank(this, tank, 0, 1))
			{
			}
			recipeTick = 0;
		}
		if(tank.getFluid() != null && key == null)
		{
			BHRecipe recipe = FLEBoilingHeaterRecipe.getInstance().getRecipe(new BHKey(stacks[2], tank.getFluid()));
			if(recipe != null)
			{
				key = (BHKey) recipe.getRecipeKey();
			}
		}
		if(key != null)
		{
			double heat = (getTemperature(ForgeDirection.UNKNOWN) - FleValue.WATER_FREEZE_POINT - 100) * getThermalConductivity(ForgeDirection.UNKNOWN);
			if(heat > 0)
			{
				recipeTick += (int) heat;
				onHeatEmit(ForgeDirection.UNKNOWN, heat);
				if(recipeTick >= key.energyNeed)
				{
					BHRecipe recipe = FLEBoilingHeaterRecipe.getInstance().getRecipe(key);
					if(recipe != null)
					{
						if(RecipeHelper.matchOutput(this, 3, recipe.output))
						{
							tank.drain(recipe.getFluidRequire(), true);
							RecipeHelper.onOutputItemStack(this, 3, recipe.output);
							type = GuiError.DEFAULT;
							stacks[2] = recipe.onOutput(stacks[2]);
							key = null;
							recipeTick = 0;
							type = GuiError.DEFAULT;
						}
						else
						{
							type = GuiError.CAN_NOT_OUTPUT;
							recipeTick = key.energyNeed;
						}
					}
					else
					{
						key = null;
						recipeTick = 0;
					}
					
				}
			}
		}
		sendToNearBy(new FleTEPacket(this, (byte) 0), 16.0F);
		sendToNearBy(new FleTEPacket(this, (byte) 1), 16.0F);
		tc.update();
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + tc.getTempreture();
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
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

	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return key == null ? 0 : (int) ((float) recipeTick / (float) key.energyNeed * i);
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnProgress(int i)
	{
		return currectItemBurntime == 0 ? 0 : (int) ((float) burnTime / (float) currectItemBurntime * i);
	}	

	public ItemStack onToolClick(ItemStack stack, EntityLivingBase entity, int activeID)
	{
		if(activeID == 5)
		{
			SingleInputRecipe recipe = BurnHandler.getRecipe(stack);
			if(recipe != null)
			{
				setBurning();
				return recipe.getResult(stack);
			}
		}
		return stack;
	}

	public void setBurning()
	{
		isBurning = true;
	}
	
	public boolean isWorking()
	{
		return key != null || recipeTick != 0;
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		if(aType == 0) return burnTime;
		if(aType == 1) return currectItemBurntime;
		return null;
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 0)
			burnTime = (Long) contain;
		if(type == 1)
			currectItemBurntime = (Long) contain;
	}

	public void resetRecipe()
	{
		key = null;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? a : dir.getOpposite() == dir ? c : dir == ForgeDirection.DOWN ? null : b;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID != 1 && aSlotID != 3 && aSlotID == 5 ? !(aSlotID == 2 && key != null) : false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID != 0 && aSlotID != 2 && aSlotID == 5 ? !(aSlotID == 2 && key != null) : false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.DOWN && from != ForgeDirection.UP && key == null;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from == ForgeDirection.UP && key == null;
	}
	
	@Override
	protected FluidTank getFluidTankFromSide(ForgeDirection aSide)
	{
		return aSide != ForgeDirection.DOWN ? tank : null;
	}

	@Override
	public EnumPH getPHLevel()
	{
		return tank.getFluid() != null ? tank.getFluid().getFluid() instanceof IFluidChemInfo ? ((IFluidChemInfo) tank.getFluid().getFluid()).getFluidPH(tank.getFluid()) : EnumPH.Water : EnumPH.Water;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return tank.getFluid() != null ? tank.getFluid().getFluid() instanceof IFluidChemInfo ? ((IFluidChemInfo) tank.getFluid().getFluid()).getFluidOxide(tank.getFluid()) : EnumOxide.Default : EnumOxide.Default;
	}
	
	@Override
	public int getTemperature()
	{
		return getTemperature(ForgeDirection.UNKNOWN);
	}

	public long getBurnTime()
	{
		return burnTime;
	}

	public long getCurrectBurnTime()
	{
		return isBurning ? currectItemBurntime : -1;
	}

	@Override
	public EnumEnviorment isOpenEnviorment()
	{
		return EnumEnviorment.Open;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_boilingHeater;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i == 0 || i == 1;
	}
	
	@Override
	public int getProgressSize()
	{
		return 1;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? recipeTick : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) recipeTick = value;
	}

	@Override
	public Condition getCondition()
	{
		return isBurning ? Condition.Working : Condition.Waiting;
	}
}