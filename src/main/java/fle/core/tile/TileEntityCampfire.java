package fle.core.tile;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.thermal.ThermalHelper;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.Direction;
import farcore.enums.EnumDamageResource;
import farcore.enums.EnumToolType;
import farcore.event.EnergyEvent;
import farcore.handler.FarCoreEnergyHandler;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.interfaces.gui.IHasGui;
import farcore.interfaces.tile.IDebugableTile;
import farcore.interfaces.tile.IToolClickHandler;
import farcore.lib.tile.TileEntityInventory;
import farcore.util.U;
import fle.api.fuel.FuelHandler;
import fle.api.fuel.IFuelValue;
import fle.api.recipe.smelting.SmeltingRecipes;
import fle.api.recipe.smelting.SmeltingRecipes.SmeltingRecipe;
import fle.api.util.TemperatureHandler;
import fle.core.container.alpha.ContainerCampfire;
import fle.core.gui.alpha.GuiCampfire;
import fle.load.Langs;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityCampfire extends TileEntityInventory
implements IThermalTile, IDebugableTile, IToolClickHandler, IHasGui
{
	private static final float efficiency = 0.08F;

	public static final int smeltingInput1 = 0;
	public static final int smeltingInput2 = 1;
	public static final int smeltingOutput1 = 2;
	public static final int smeltingOutput2 = 3;
	public static final int burningTool = 8;
	public static final int fuel1 = 9;
	public static final int fuel2 = 10;
	public static final int fuelOutput = 11;
	
	private ThermalHelper helper = new ThermalHelper(1.6E4F, 1.8E-1F);
	private float enviorTempCache = -1;
	private boolean isBurning;
	private long upgrades = 0L;
	private float burningTemp;
	private float power;
	public float currentBurningEnergy;
	public float burningEnergy;
	private float burnedEnergy;
	private ItemStack output;
	private SmeltingRecipe recipe1;
	private SmeltingRecipe recipe2;
	public float progress1;
	public float progress2;
	
	public TileEntityCampfire()
	{
		super(12, Langs.inventoryCampfire, 1);
	}
	
	public void setBurning(boolean isBurning)
	{
		this.isBurning = isBurning;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		helper.readFromNBT(nbt);
		upgrades = nbt.getLong("u");
		burningTemp = nbt.getFloat("b1");
		power = nbt.getFloat("b2");
		currentBurningEnergy = nbt.getFloat("b3");
		burningEnergy = nbt.getFloat("b4");
		burnedEnergy = nbt.getFloat("b5");
		output = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("s1"));
		
		recipe1 = SmeltingRecipes.loadRecipe(nbt, "r1");
		recipe2 = SmeltingRecipes.loadRecipe(nbt, "r2");
		if(recipe1 != null && !recipe1.matchInput(inventory.stacks[smeltingInput1]))
			recipe1 = null;
		if(recipe2 != null && !recipe2.matchInput(inventory.stacks[smeltingInput2]))
			recipe2 = null;
		if(recipe1 != null)
		{
			progress1 = nbt.getFloat("p1");
		}
		if(recipe2 != null)
		{
			progress2 = nbt.getFloat("p2");
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		helper.writeToNBT(nbt);
		nbt.setLong("u", upgrades);
		nbt.setFloat("b1", burningTemp);
		nbt.setFloat("b2", power);
		nbt.setFloat("b3", currentBurningEnergy);
		nbt.setFloat("b4", burningEnergy);
		nbt.setFloat("b5", burnedEnergy);
		if(output != null)
		{
			nbt.setTag("s1", output.writeToNBT(new NBTTagCompound()));
		}
		if(recipe1 != null)
		{
			nbt.setString("r1", recipe1.name());
			nbt.setFloat("p1", progress1);
		}
		if(recipe2 != null)
		{
			nbt.setString("r2", recipe2.name());
			nbt.setFloat("p2", progress2);
		}
	}
	
	@Override
	protected boolean init()
	{
		if(super.init())
		{
			FarCoreEnergyHandler.BUS.post(new EnergyEvent.Add(this));
			return true;
		}
		return false;
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	protected void updateGeneral() 
	{
		enviorTempCache = ThermalNet.getEnviormentTemp(worldObj, xCoord, yCoord, zCoord);
	}
	
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
			if(value != null && burningTemp >= value.getMinBurnPoint(inventory.stacks[fuel1]))
			{
				burningTemp = value.getMaxTempreture(inventory.stacks[fuel1]);
				power = value.getMaxPower(inventory.stacks[fuel1], helper.temperature());
				currentBurningEnergy = burningEnergy = value.getEnergyCurrent(inventory.stacks[fuel1]);
				output = ItemStack.copyItemStack(value.getOutput(inventory.stacks[fuel1]));
				inventory.decrStackSize(fuel1, 1);
			}
			else
			{
				currentBurningEnergy = 0;
			}
		}
		float amount;
		if(burningEnergy > 0)
		{
			amount = Math.min(power, burningEnergy);
			burnedEnergy += amount * efficiency;
			burningEnergy -= amount;
			if(burningEnergy <= 0)
			{
				inventory.addStack(fuelOutput, output, true);
				output = null;
			}
		}
		else if(burningTemp > helper.temperature())
		{
			burningTemp--;
		}
		else
		{
			burningTemp = helper.temperature();
		}
		if(burnedEnergy > 0)
		{
			amount = Math.min((burningTemp - helper.temperature() - enviorTempCache + 1) * helper.thermalConductivity * 100, burnedEnergy);
			helper.receive(amount);
			burnedEnergy -= amount;
		}
		inventory.stacks[smeltingInput1] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingInput1]);
		inventory.stacks[smeltingInput2] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingInput2]);
		inventory.stacks[smeltingOutput1] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingOutput1]);
		inventory.stacks[smeltingOutput2] = TemperatureHandler.updateThermalItem(this, inventory.stacks[smeltingOutput2]);
		SmeltingRecipe recipe;
		if((recipe = SmeltingRecipes.getMatchedRecipe(inventory.stacks[smeltingInput1])) != null)
		{
			if(recipe != recipe1)
			{
				progress1 = 0;
				recipe1 = recipe;
			}
			amount = (helper.temperature() + enviorTempCache - recipe1.minTemp1) * helper.thermalConductivity * 100;
			helper.emit(amount);
			progress1 += amount;
			if(progress1 >= recipe1.energy)
			{
				ItemStack output = recipe1.getOutput(inventory.stacks[smeltingInput1]);
				if(output == null || inventory.addStack(smeltingOutput1, output, false) == output.stackSize)
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
			amount = (helper.temperature() + enviorTempCache - recipe2.minTemp1) * helper.thermalConductivity * 100;
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
	protected int calculateLightValue()
	{
		return burningEnergy > 0 ? 10 : 0;
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return true;
	}

	@Override
	public float getTemperature(Direction direction)
	{
		return helper.temperature() + enviorTempCache;
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

	@Override
	public void addDebugInformation(List<String> list)
	{
		list.add("Temp : " + getTemperature(Direction.Q));
		if(recipe1 != null)
		{
			list.add("Progress : " + progress1 / recipe1.energy);
		}
	}

	@Override
	public ItemStack onToolClick(ItemStack stack, EntityPlayer player, int id)
	{
		if(id == burningTool)
		{
			if(!isBurning() && EnumToolType.firestarter.match(stack))
			{
				burningTemp += 100;
				if(burningTemp > 1000)
				{
					burningTemp = 1000;
				}
				U.Inventorys.damage(stack, player, 7.5E-1F, EnumDamageResource.USE, false);
				if(stack.stackSize <= 0)
				{
					return null;
				}
				return stack;
			}
		}
		return stack;
	}
	
	public boolean isBurning()
	{
		return burningEnergy > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurningProgress(int scale)
	{
		return (int) (burningEnergy * scale / currentBurningEnergy);
	}

	@SideOnly(Side.CLIENT)
	public Gui openGUI(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GuiCampfire(this, player);
	}

	@Override
	public Container openContainer(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ContainerCampfire(this, player);
	}
}