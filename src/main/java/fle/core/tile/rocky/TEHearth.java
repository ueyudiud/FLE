/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.rocky;

import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.data.V;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.material.Mat;
import fle.api.mat.StackContainer;
import fle.api.recipes.instance.FuelHandler;
import fle.api.tile.IBellowsAccepter;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.TEInventorySingleSlot;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEHearth extends TEInventorySingleSlot implements IBellowsAccepter, IThermalHandler, IToolableTile, ITB_BlockActived
{
	private static final byte	IsBurining	= 3;
	private static final Mat material = M.argil;
	
	private int					burningPower;
	private int					normalBurningPower;
	private long				fuelValue;
	
	private long energy;
	private float maxTemp;
	
	public TEHearth()
	{
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("power1", this.burningPower);
		compound.setInteger("power2", this.normalBurningPower);
		compound.setLong("fuelValue", this.fuelValue);
		compound.setLong("energy", this.energy);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.burningPower = compound.getInteger("power1");
		this.normalBurningPower = compound.getInteger("power2");
		this.fuelValue = compound.getLong("fuelValue");
		this.energy = compound.getLong("energy");
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (hand == EnumHand.MAIN_HAND && side.horizontal)
		{
			if (isServer())
			{
				int size;
				if (stack != null && FuelHandler.isFuel(stack) && (size = incrItem(0, stack, true)) > 0)
				{
					if (stack.stackSize == size)
						player.setHeldItem(hand, null);
					else
						stack.stackSize -= size;
					return EnumActionResult.SUCCESS;
				}
				else if (this.stack != null && stack == null)
				{
					Players.giveOrDrop(player, this.stack);
					this.stack = null;
					return EnumActionResult.SUCCESS;
				}
			}
			else
				return EnumActionResult.SUCCESS;
		}
		return super.onBlockActivated(player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.FIRESTARTER)
		{
			if (!is(IsBurining))
			{
				enable(IsBurining);
				this.maxTemp = 200F;
				tryBuringItem();
				return new ActionResult<>(EnumActionResult.SUCCESS, 1.0F);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, 0.0F);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		
		if (is(IsBurining))
		{
			if (this.fuelValue == 0)
			{
				tryBuringItem();
			}
			if (this.fuelValue > 0)
			{
				long i = Math.min(this.fuelValue, this.burningPower);
				this.fuelValue -= i;
				
				if (this.burningPower > this.normalBurningPower)
				{
					this.burningPower -= (this.burningPower - this.normalBurningPower + 1) / 2;
				}
				else if (this.burningPower < this.normalBurningPower)
				{
					this.burningPower += (this.normalBurningPower - this.burningPower + 1) / 2;
				}
			}
		}
	}
	
	private void tryBuringItem()
	{
		FuelHandler.FuelKey key = FuelHandler.getFuel(this.stack);
		if (key != null)
		{
			if (ThermalNet.getRealHandlerTemperature(this, Direction.Q) >= key.flameTemperature)
			{
				decrStackSize(0, key.fuel);
				this.fuelValue = key.fuelValue;
				this.normalBurningPower = key.normalPower;
				this.maxTemp = key.normalTemperature;
				return;
			}
		}
		this.maxTemp = 0.0F;
		disable(IsBurining);
	}
	
	@Override
	public void onAcceptWindFromBellow(Direction from, float windSpeed, StackContainer<Mat> airContain)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBellowEffect(Direction from, float windSpeed)
	{
		
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return true;
	}
	
	private float getCurrentTemperature(float Tenv, float Tmax)
	{
		return (float) - Math.expm1(- ((this.energy / material.heatCapacity + Tenv) / Tmax)) * Tmax;
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		float Tenv = ThermalNet.getEnvironmentTemperature(this.world, this.pos);
		float Tmax = MathHelper.sqrt(Tenv * Tenv + this.maxTemp * this.maxTemp);
		
		return Tmax - Tenv;
	}
	
	@Override
	public double getHeatCapacity(Direction direction)
	{
		float Tenv = ThermalNet.getEnvironmentTemperature(this.world, this.pos);
		float Tmax = MathHelper.sqrt(Tenv * Tenv + this.maxTemp * this.maxTemp);
		
		return material.heatCapacity / (1 - getCurrentTemperature(Tenv, Tmax) / Tmax);
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return direction == Direction.U ? V.airHeatConductivity : M.stone.thermalConductivity;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.energy += value;
	}
}
