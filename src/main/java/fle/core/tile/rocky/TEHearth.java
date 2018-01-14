/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.rocky;

import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.IThermalProvider;
import farcore.energy.thermal.ThermalNet;
import farcore.energy.thermal.instance.ThermalHandlerLitmited;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEHearth extends TEInventorySingleSlot implements IBellowsAccepter, IThermalProvider, IToolableTile, ITB_BlockActived
{
	private static final byte Burining = 3;
	private static final Mat material = M.argil;
	
	private int		burningPower;
	private int		normalBurningPower;
	private long	fuelValue;
	
	private ThermalHandlerLitmited handler = new ThermalHandlerLitmited(this);
	
	{
		this.handler.material = material;
	}
	
	public TEHearth()
	{
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("power1", this.burningPower);
		compound.setInteger("power2", this.normalBurningPower);
		compound.setLong("fuelValue", this.fuelValue);
		this.handler.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.burningPower = compound.getInteger("power1");
		this.normalBurningPower = compound.getInteger("power2");
		this.fuelValue = compound.getLong("fuelValue");
		this.handler.readFromNBT(compound);
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
			if (!is(Burining))
			{
				enable(Burining);
				tryBuringItem(500.0F);
				return new ActionResult<>(EnumActionResult.SUCCESS, 1.0F);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, 0.0F);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		
		if (is(Burining))
		{
			if (this.fuelValue == 0)
			{
				tryBuringItem(ThermalNet.getRealHandlerTemperature(this.handler, Direction.Q));
			}
			if (this.fuelValue > 0)
			{
				long i = Math.min(this.fuelValue, this.burningPower);
				this.fuelValue -= i;
				this.handler.energy += i;
				
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
	
	private void tryBuringItem(float temp)
	{
		FuelHandler.FuelKey key = FuelHandler.getFuel(this.stack);
		if (key != null)
		{
			if (temp >= key.flameTemperature)
			{
				decrStackSize(0, key.fuel);
				this.fuelValue = key.fuelValue;
				this.normalBurningPower = key.normalPower;
				this.handler.Tlimit = key.normalTemperature;
				return;
			}
		}
		this.handler.Tlimit = 0.0F;
		disable(Burining);
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
	public IThermalHandler getThermalHandler()
	{
		return this.handler;
	}
}
