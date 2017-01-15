package farcore.energy.electric.instance;

import farcore.energy.electric.IElectricalElement;
import farcore.energy.electric.IElectricalHandler;
import farcore.energy.electric.IElectricalNet;
import farcore.energy.electric.IElectricalNode;
import farcore.energy.electric.ISidedElectricalElement;
import farcore.energy.thermal.IThermalHandler;
import farcore.event.EnergyEvent;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.tile.abstracts.TESynchronization;
import farcore.lib.util.Direction;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An instance of electrical handler, change electrical energy to
 * heat.
 * @author ueyudiud
 *
 */
public class TEResistance extends TESynchronization
implements IElectricalHandlerHelper, IThermalHandler, IElectricalHandler
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
		this.resistance.setHelper(this);
		this.face1.setHelper(this.node1, Direction.N);
		this.face2.setHelper(this.node2, Direction.S);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("heat", this.heat);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.heat = nbt.getDouble("heat");
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
		this.heat += power;
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return direction == Direction.N || direction == Direction.S;
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (this.heat / heatCapacity);
	}
	
	@Override
	public void onElectricNetTicking(IElectricalNet net)
	{
		
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return thermalConductivity;
	}
	
	@Override
	public void onHeatChange(Direction direction, double value)
	{
		this.heat += value;
	}
	
	@Override
	public int getNodeSize()
	{
		return 1;
	}
	
	@Override
	public IElectricalNode getNode(int id)
	{
		return id == 0 ? this.node1 : this.node2;
	}
	
	@Override
	public IElectricalElement getElement(int id1, int id2)
	{
		return this.resistance;
	}
	
	@Override
	public ISidedElectricalElement getEnelementFromFace(Direction direction)
	{
		return direction == Direction.S ? this.face2 :
			direction == Direction.N ? this.face1 : null;
	}
}