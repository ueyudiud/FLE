package fle.core.tile;

import com.sun.org.apache.regexp.internal.recompile;

import farcore.energy.thermal.ThermalHelper;
import farcore.enums.Direction;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.inventory.Inventory;
import farcore.lib.tile.TileEntitySyncable;
import fle.api.fuel.FuelHandler;
import fle.api.fuel.IFuelValue;
import fle.api.recipe.smelting.SmeltingRecipes;
import fle.api.recipe.smelting.SmeltingRecipes.SmeltingRecipe;
import fle.api.util.TemperatureHandler;
import fle.load.Langs;
import net.minecraft.item.ItemStack;

public class TileEntityCampfire extends TileEntitySyncable implements IThermalTile
{
	private static final float efficiency = 0.08F;

	public static final int smeltingInput1 = 0;
	public static final int smeltingInput2 = 1;
	public static final int smeltingOutput1 = 2;
	public static final int smeltingOutput2 = 3;
	public static final int fuel1 = 10;
	public static final int fuel2 = 11;
	
	public Inventory inventory = new Inventory(12, Langs.inventoryCampfire, 1);
	private ThermalHelper helper = new ThermalHelper(1.6E6F, 30F);
	private long upgrades = 0L;
	private float burningTemp;
	private float power;
	private float burningEnergy;
	private float burnedEnergy;
	private ItemStack output;
	private SmeltingRecipe recipe1;
	private SmeltingRecipe recipe2;
	private float progress1;
	private float progress2;
	
	@Override
	protected void updateServer1()
	{
		if(burningEnergy <= 0)
		{
			if(inventory.addStack(fuel1, inventory.stacks[fuel2], false) != 0)
			{
				inventory.decrStackSize(fuel2, inventory.addStack(fuel1, inventory.stacks[fuel2], true));
			}
			IFuelValue value = FuelHandler.getFuelValue(inventory.stacks[fuel1]);
			if(value != null && helper.temperature() >= value.getMinBurnPoint())
			{
				burningTemp = value.getMaxTempreture();
				power = value.getMaxPower(helper.temperature());
				burningEnergy = value.getEnergyCurrent();
				output = ItemStack.copyItemStack(value.getOutput());
			}
		}
		float amount;
		if(burningEnergy > 0)
		{
			amount = Math.min(power, burningEnergy);
			burnedEnergy += amount * efficiency;
			burningEnergy -= amount;
		}
		else
		{
			burningTemp = helper.temperature();
		}
		if(burnedEnergy > 0)
		{
			amount = Math.min((burningTemp - helper.temperature() + 1) * helper.thermalConductivity, burnedEnergy);
			helper.receive(amount);
			burnedEnergy -= amount;
		}
		inventory.stacks[smeltingInput1] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingInput1]);
		inventory.stacks[smeltingInput2] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingInput2]);
		SmeltingRecipe recipe;
		if((recipe = SmeltingRecipes.getMatchedRecipe(inventory.stacks[smeltingInput1])) != null)
		{
			if(recipe != recipe1)
			{
				progress1 = 0;
				recipe1 = recipe;
			}
			amount = (helper.temperature() - recipe1.minTemp1) * helper.thermalConductivity;
			helper.emit(amount);
			progress1 += amount;
			if(progress1 >= recipe1.energy)
			{
				ItemStack output = recipe1.getOutput(inventory.stacks[smeltingInput1]);
				if(inventory.addStack(smeltingOutput1, output, false) == output.stackSize)
				{
					inventory.decrStack(smeltingInput1, recipe1.input, true);
					inventory.addStack(smeltingOutput1, output, true);
					recipe1 = null;
					progress1 = 0;
				}
			}
		}
		else
		{
			recipe1 = null;
			progress1 = 0;
		}
		if((recipe = SmeltingRecipes.getMatchedRecipe(inventory.stacks[smeltingInput2])) != null)
		{
			if(recipe != recipe2)
			{
				progress2 = 0;
				recipe2 = recipe;
			}
			amount = (helper.temperature() - recipe2.minTemp1) * helper.thermalConductivity;
			helper.emit(amount);
			progress2 += amount;
			if(progress2 >= recipe2.energy)
			{
				ItemStack output = recipe2.getOutput(inventory.stacks[smeltingInput2]);
				if(inventory.addStack(smeltingOutput2, output, false) == output.stackSize)
				{
					inventory.decrStack(smeltingInput2, recipe2.input, true);
					inventory.addStack(smeltingOutput2, output, true);
					recipe2 = null;
					progress2 = 0;
				}
			}
		}
		else
		{
			recipe2 = null;
			progress2 = 0;
		}
	}
	
	@Override
	protected void updateClient1()
	{
		super.updateClient1();
	}

	@Override
	public boolean canConnectTo(Direction direction)
	{
		return true;
	}

	@Override
	public float getTemperature(Direction direction)
	{
		return helper.temperature();
	}

	@Override
	public float getThermalConductivity(Direction direction)
	{
		return helper.thermalConductivity;
	}

	@Override
	public void receiveThermalEnergy(Direction direction, float value)
	{
		helper.receive(value);
	}

	@Override
	public void emitThermalEnergy(Direction direction, float value)
	{
		helper.emit(value);
	}
}