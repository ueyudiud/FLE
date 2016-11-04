package farcore.energy.electric.instance;

import farcore.energy.electric.IACHandler;
import farcore.energy.electric.IElectricalElement;
import farcore.energy.electric.IElectricalNode;
import farcore.energy.electric.ISidedElectricalElement;
import farcore.energy.thermal.IThermalHandler;
import farcore.event.EnergyEvent;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.tile.TESynchronization;
import farcore.lib.util.Direction;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An instance of electrical handler, change electrical energy to
 * heat.
 * @author ueyudiud
 *
 */
public class TEResistance extends TESynchronization
implements IElectricalHandlerHelper, IThermalHandler, IACHandler
{
	private static final float thermalConductivity = 1.1F;
	private static final float heatCapacity = 3.8E5F;
	NodeDefault node1 = new NodeDefault(0);
	NodeDefault node2 = new NodeDefault(1);
	ElementResistance resistance = new ElementResistance(1E-2D);
	ElementBlockFace face1 = new ElementBlockFace();
	ElementBlockFace face2 = new ElementBlockFace();
	double heat;
	
	public TEResistance()
	{
		resistance.setHelper(this);
		face1.setHelper(node1, Direction.N);
		face2.setHelper(node2, Direction.S);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("heat", heat);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		heat = nbt.getDouble("heat");
		super.readFromNBT(nbt);
	}

	@Override
	protected void initServer()
	{
		super.initServer();
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Add(this));
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		FarCoreEnergyHandler.BUS.post(new EnergyEvent.Remove(this));
	}
	
	@Override
	public void providePowerByVoltage(double power)
	{
	}
	
	@Override
	public void heatByCurrent(double power)
	{
		heat += power;
	}

	@Override
	public boolean canConnectTo(Direction direction)
	{
		return direction == Direction.N || direction == Direction.S;
	}

	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (heat / heatCapacity);
	}

	@Override
	public double getThermalConductivity(Direction direction)
	{
		return thermalConductivity;
	}

	@Override
	public void onHeatChange(Direction direction, double value)
	{
		heat += value;
	}
	
	@Override
	public int getNodeSize()
	{
		return 1;
	}
	
	@Override
	public IElectricalNode getNode(int id)
	{
		return id == 0 ? node1 : node2;
	}
	
	@Override
	public IElectricalElement getElement(int id1, int id2)
	{
		return resistance;
	}
	
	@Override
	public ISidedElectricalElement getEnelementFromFace(Direction direction)
	{
		return direction == Direction.S ? face2 :
			direction == Direction.N ? face1 : null;
	}
}