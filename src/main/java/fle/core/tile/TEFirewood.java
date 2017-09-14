/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MP;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.material.Mat;
import fle.api.energy.thermal.ThermalEnergyHelper;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.Worlds;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

/**
 * @author ueyudiud
 */
public class TEFirewood extends TESynchronization
implements IThermalHandler
{
	private static final byte Smoldering = 3;
	private static final byte Burning = 4;
	private static final byte Carbonate = 5;
	
	private Mat material;
	private long remainEnergy = 10000000L;
	private int carbonateProgress;
	private ThermalEnergyHelper helper = new ThermalEnergyHelper();
	
	private TimeMarker recheckingStructure = new TimeMarker(20, this::recheckStructure);
	
	public TEFirewood()
	{
		setMaterial(M.oak);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", this.material.name);
		nbt.setLong("energy", this.remainEnergy);
		nbt.setInteger("progress", this.carbonateProgress);
		this.helper.writeToNBT(nbt, "thermal");
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setMaterial(Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID));
		this.remainEnergy = nbt.getLong("energy");
		this.helper.readFromNBT(nbt, "thermal");
		this.carbonateProgress = nbt.getInteger("progress");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", this.material.id);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("m"))
		{
			this.material = Mat.getMaterialByIDOrDefault(nbt, "m", this.material);
			markBlockRenderUpdate();
		}
	}
	
	private void setMaterial(Mat material)
	{
		this.material = material;
		this.helper = new ThermalEnergyHelper(0, material.heatCapacity, 50F, 2E-2F);
	}
	
	private void recheckStructure()
	{
		MutableBlockPos pos = new MutableBlockPos();
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
				label:
				{
					for (int i = -1; i <= 1; ++i)
						for (int j = -1; j <= 1; ++j)
							for (int k = -1; k <= 1; ++k)
							{
								if (i == 0 || j == 0 || k == 0) continue;
								pos.setPos(this.pos.getX() + i, this.pos.getY() + j, this.pos.getZ() + k);
								if (Worlds.isAir(this.world, pos))
									break label;
							}
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
						this.remainEnergy = 16000000L;
					}
				}
			}
		}
		else
		{
			if (getBlockState(Direction.U).getBlock() == EnumBlock.fire.block ||
					ThermalNet.getTemperature(this) >= 380F || //Log flame temperature
					(getTE(Direction.N) instanceof TEFirewood && ((TEFirewood) getTE(Direction.N)).is(Burning)) ||
					(getTE(Direction.N) instanceof TEFirewood && ((TEFirewood) getTE(Direction.S)).is(Burning)) ||
					(getTE(Direction.N) instanceof TEFirewood && ((TEFirewood) getTE(Direction.E)).is(Burning)) ||
					(getTE(Direction.N) instanceof TEFirewood && ((TEFirewood) getTE(Direction.W)).is(Burning)))
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
			setBlockState(EnumBlock.fire.apply(false), 3);
		}
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
			long value = (long) (1000 * this.material.getProperty(MP.property_wood).burnHeat);
			this.remainEnergy -= value;
			this.helper.addInternalEnergy(value);
			if (this.remainEnergy <= 0)
			{
				removeBlock();
				return;
			}
		}
		super.updateServer();
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return this.helper.getTemperature();
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.helper.addInternalEnergy(value);
	}
	
	
	public Mat getMaterial()
	{
		return this.material;
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