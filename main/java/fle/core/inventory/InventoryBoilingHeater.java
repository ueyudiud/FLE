package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.gui.GuiError;
import fle.api.inventory.InventoryWithFluidTank;
import fle.api.material.IFluidChemInfo;
import fle.api.util.IChemCondition;
import fle.core.init.Config;
import fle.core.init.Lang;
import fle.core.net.FleTEPacket;
import fle.core.recipe.FLEBoilingHeaterRecipe;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHKey;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;
import fle.core.recipe.FluidSolidMixRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.te.argil.TileEntityBoilingHeater;

public class InventoryBoilingHeater extends InventoryWithFluidTank<TileEntityBoilingHeater> implements IChemCondition
{
	protected final int power = Config.getInteger("pBoilingHeater", 400000);
	
	private static final int[] a = {0, 1};
	private static final int[] b = {2, 3};
	private static final int[] c = {4};
	
	public BHKey key;
	private int recipeTick;
	private long burnTime;
	private long currectItemBurntime;
	private boolean isBurning;
	
	public InventoryBoilingHeater()
	{
		super(Lang.inventory_boilingHeater, 6, 2000);
		maxHeat = 850;
	}
	
	@SideOnly(Side.CLIENT)
	public void setBT(long i)
	{
		burnTime = i;
	}
	
	@SideOnly(Side.CLIENT)
	public void setCBT(long i)
	{
		currectItemBurntime = i;
		isBurning = i > 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
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
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		FLEBoilingHeaterRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
		recipeTick = nbt.getInteger("RecipeTick");
		burnTime = nbt.getLong("BurnTime");
		currectItemBurntime = nbt.getLong("CurrectBurnTime");
		isBurning = nbt.getBoolean("Burning");
	}
	
	public void setBurning()
	{
		isBurning = true;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnProgress(int i)
	{
		return currectItemBurntime == 0 ? 0 : (int) ((float) burnTime / (float) currectItemBurntime * i);
	}
	
	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return key == null ? 0 : (int) ((float) recipeTick / (float) key.energyNeed * i);
	}
	
	int tem = 295;

	@Override
	public void updateEntity(TileEntityBoilingHeater tile)
	{
		tem = tile.getTemperature(ForgeDirection.UNKNOWN);
		super.updateEntity(tile);
		boolean flag = false;
		if(burnTime == 0 && isBurning)
		{
			if(FleAPI.getFulBuf(stacks[4]) > 0)
			{
				currectItemBurntime = burnTime = FleAPI.getFulBuf(stacks[4]);
				RecipeHelper.onInputItemStack(this, 4);
				flag = true;
			}
			else
			{
				isBurning = false;
			}
		}
		if(burnTime > 0)
		{
			isBurning = true;
			long heat = Math.min(burnTime, power);
			burnTime -= heat;
			tile.onHeatReceive(ForgeDirection.UNKNOWN, power);
		}
		if(key == null)
		{
			FluidSolidMixRecipe.mixSolidAndFluid(tank, this, 0, this);
			if(!RecipeHelper.fillOrDrainInventoryTank(this, tank, 0, 1))
			{
				flag = true;
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
			double heat = (tile.getTemperature(ForgeDirection.UNKNOWN) - FleValue.WATER_FREEZE_POINT - 100) * tile.getThermalConductivity(ForgeDirection.UNKNOWN);
			if(heat > 0)
			{
				recipeTick += (int) heat;
				tile.onHeatEmit(ForgeDirection.UNKNOWN, heat);
				if(recipeTick >= key.energyNeed)
				{
					BHRecipe recipe = FLEBoilingHeaterRecipe.getInstance().getRecipe(key);
					if(recipe != null)
					{
						if(RecipeHelper.matchOutput(this, 3, recipe.output))
						{
							tank.drain(recipe.getFluidRequire(), true);
							RecipeHelper.onOutputItemStack(this, 3, recipe.output);
							tile.type = GuiError.DEFAULT;
							stacks[2] = recipe.onOutput(stacks[2]);
							key = null;
							recipeTick = 0;
							flag = true;
						}
						else
						{
							tile.type = GuiError.CAN_NOT_OUTPUT;
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
		syncSlot(tile, 0, 4);
		syncTank(tile);
		tile.sendToNearBy(new FleTEPacket(tile, (byte) 0), 16.0F);
		tile.sendToNearBy(new FleTEPacket(tile, (byte) 1), 16.0F);
	}
	
	@Override
	protected void onMelted(TileEntityBoilingHeater tile)
	{
		if(getFluid() != null)
			if(getFluid().getFluid().canBePlacedInWorld() && getFluidAmount() >= 1000)
			{
				tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, getFluid().getFluid().getBlock());
				tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
				tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
			}
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
		return tem;
	}

	public long getBurnTime()
	{
		return burnTime;
	}

	public long getCurrectBurnTime()
	{
		return isBurning ? currectItemBurntime : -1;
	}
}