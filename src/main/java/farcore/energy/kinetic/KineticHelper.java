package farcore.energy.kinetic;

import farcore.energy.thermal.ThermalNet;
import farcore.enums.Direction;
import farcore.interfaces.energy.kinetic.IKineticAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * A example kinetic energy handler, make an instance in tile.
 * @author ueyudiud
 *
 */
public class KineticHelper
{
	public final float minTorque;
	public final float minSpeed;
	public final float maxSpeed;
	public final float maxTorque;
	public final float maxEnergy;
	
	private float postDeltaEnergy;
	private float deltaEnergy;
	private float energy;

	public KineticHelper(float maxE)
	{
		this(Float.MAX_VALUE, maxE);
	}
	public KineticHelper(float maxT, float maxE)
	{
		this(maxT, Float.MAX_VALUE, maxE);
	}
	public KineticHelper(float maxT, float maxS, float maxE)
	{
		this(0, 0, maxT, maxS, maxE);
	}
	public KineticHelper(float minT, float minS, float maxT, float maxS, float maxE)
	{
		this.minTorque = minT;
		this.minSpeed = minS;
		this.maxTorque = maxT;
		this.maxSpeed = maxS;
		this.maxEnergy = maxE;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		writeToNBT(nbt, "kinetic");
	}
	public void writeToNBT(NBTTagCompound nbt, String tag)
	{
		nbt.setFloat(tag, energy);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		readFromNBT(nbt, "kinetic");
	}
	public void readFromNBT(NBTTagCompound nbt, String tag)
	{
		energy = nbt.getFloat(tag);
	}
	
	public void init(TileEntity tile)
	{
		
	}
	
	public void update(TileEntity tile)
	{
		postDeltaEnergy = deltaEnergy;
		deltaEnergy = 0;
	}
	
	public boolean rotatable(KineticPkg pkg)
	{
		return energy < maxEnergy && pkg.speed >= minSpeed && pkg.torque >= minTorque;
	}
	
	public void emit(KineticPkg pkg)
	{
		energy -= pkg.energy();
		deltaEnergy -= pkg.energy();
	}

	public void receive(KineticPkg pkg)
	{
		if(pkg.speed > maxSpeed)
		{
			pkg.speed -= (float) Math.random() * (pkg.speed - maxSpeed);
		}
		energy += pkg.energy();
		deltaEnergy += pkg.energy();
	}
	
	public boolean stuck(float speed, float torque)
	{
		if(torque >= maxTorque)
		{
			return true;
		}
		return false;
	}
	
	public KineticPkg send(float maxSpeed, float torque)
	{
		if(energy == 0) return null;
		return new KineticPkg(torque, Math.min(energy / torque, maxSpeed));
	}

	public void setEnergy(float energy)
	{
		this.energy = energy;
	}
	
	public float getEnergy()
	{
		return energy;
	}
	
	public float getDeltaEnergy()
	{
		return postDeltaEnergy;
	}
}