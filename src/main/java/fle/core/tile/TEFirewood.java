/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MP;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.IThermalProvider;
import farcore.energy.thermal.instance.ThermalHandlerLitmited;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.material.Mat;
import fle.api.recipes.instance.FlamableRecipes;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import nebula.common.world.ICoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;

/**
 * @author ueyudiud
 */
public class TEFirewood extends TESynchronization implements IThermalProvider
{
	private static final byte	Smoldering	= 3;
	private static final byte	Burning		= 4;
	private static final byte	Carbonate	= 5;
	
	private long	remainEnergy;
	private int		carbonateProgress;
	
	private ThermalHandlerLitmited thermalHandler = new ThermalHandlerLitmited(this);
	
	private TimeMarker recheckingStructure = new TimeMarker(20, this::recheckStructure);
	
	public TEFirewood()
	{
		this.thermalHandler.material = M.oak;
		this.remainEnergy = (long) (24_0000L * this.thermalHandler.material.getProperty(MP.property_wood).burnHeat);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", this.thermalHandler.material.name);
		nbt.setLong("energyRem", this.remainEnergy);
		nbt.setInteger("progress", this.carbonateProgress);
		this.thermalHandler.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.thermalHandler.material = Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID);
		this.remainEnergy = nbt.getLong("energyRem");
		this.thermalHandler.readFromNBT(nbt);
		this.carbonateProgress = nbt.getInteger("progress");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", this.thermalHandler.material.id);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("m"))
		{
			this.thermalHandler.material = Mat.getMaterialByIDOrDefault(nbt, "m", this.thermalHandler.material);
			markBlockRenderUpdate();
		}
	}
	
	private void recheckStructure()
	{
		if (isCatchingRain(true))
		{
			disable(Burning);
			disable(Smoldering);
			return;
		}
		if (is(Smoldering))
		{
			if (isAirNearby(true))
			{
				enable(Burning);
				syncToNearby();
			}
			else
			{
				disable(Burning);
				if (is(Carbonate))
				{
					disable(Smoldering);
				}
				else
				{
					if (Worlds.checkForMinDistance(this.world, this.pos, false, 3, ICoord::isAirBlock) > 3)
					{
						disable(Smoldering);
						syncToNearby();
					}
					if (!is(Carbonate))
					{
						++this.carbonateProgress;
						if (this.carbonateProgress == 100)
						{
							enable(Carbonate);
							disable(Smoldering);
							syncToNearby();
							this.remainEnergy = 1600_0000L;
						}
					}
				}
			}
		}
		else
		{
			if (FlamableRecipes.isFlamable(this, 380))
			{
				if (!is(Carbonate))
				{
					enable(Smoldering);
				}
				else
				{
					enable(Burning);
				}
				syncToNearby();
			}
		}
		if (is(Burning) && isAirBlock(Direction.U))
		{
			setBlockState(Direction.U, EnumBlock.fire.apply(false), 3);
		}
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		this.thermalHandler.setLimitTemperature(600);
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	protected void updateClient()
	{
		if (isSmoldering())
		{
			if (this.random.nextInt(5) == 0)
			{
				double d0 = this.pos.getX() + this.random.nextDouble();
				double d1 = this.pos.getY() + this.random.nextDouble() * 0.5D + 0.75D;
				double d2 = this.pos.getZ() + this.random.nextDouble();
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	protected void updateServer()
	{
		this.recheckingStructure.onUpdate();
		if (is(Burning))
		{
			long value = Math.min(this.remainEnergy, 2000);
			this.remainEnergy -= value;
			byte side = 0;
			float temp = this.thermalHandler.getTemperatureDifference(Direction.Q);
			for (Direction direction : Direction.DIRECTIONS_3D)
			{
				Direction op = direction.opposite();
				TileEntity te = getTE(direction);
				if ((te instanceof IThermalHandler && ((IThermalHandler) te).canConnectTo(op) && ((IThermalHandler) te).getTemperatureDifference(op) < temp) ||
						te instanceof IThermalProvider && ((IThermalProvider) te).getThermalHandler().canConnectTo(op) && ((IThermalProvider) te).getThermalHandler().getThermalConductivity(op) < temp)
					side |= direction.flag;
			}
			if (side != 0)
			{
				int i = L.bitCounts(side);
				long allocate;
				for (Direction direction : Direction.DIRECTIONS_3D)
				{
					if ((side & direction.flag) != 0)
					{
						Direction op = direction.opposite();
						TileEntity te = getTE(direction);
						IThermalHandler handler;
						if (te instanceof IThermalHandler)
						{
							handler = (IThermalHandler) te;
						}
						else
						{
							handler = ((IThermalProvider) te).getThermalHandler();
						}
						if (handler.canConnectTo(op))
						{
							allocate = value / (i --);
							handler.onHeatChange(op, allocate);
							value -= allocate;
						}
					}
				}
			}
			
			this.thermalHandler.energy1 += value;
			if (this.remainEnergy == 0)
			{
				removeBlock();
				return;
			}
		}
		super.updateServer();
	}
	
	@Override
	public IThermalHandler getThermalHandler()
	{
		return this.thermalHandler;
	}
	
	public Mat getMaterial()
	{
		return this.thermalHandler.material;
	}
	
	public void setFlammed()
	{
		enable(Smoldering);
	}
	
	public boolean isBurning()
	{
		return is(Burning);
	}
	
	public boolean isSmoldering()
	{
		return is(Smoldering);
	}
	
	public boolean isCarbonate()
	{
		return is(Carbonate);
	}
}
