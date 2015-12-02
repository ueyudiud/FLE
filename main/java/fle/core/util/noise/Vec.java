package fle.core.util.noise;

import net.minecraft.util.Vec3;

public class Vec extends Vec3
{
	public Vec(double x, double y, double z)
	{
		super(x, y, z);
	}
	
	public Vec mutiply(double r)
	{
		xCoord *= r;
		yCoord *= r;
		zCoord *= r;
		return this;
	}
	
	@Override
	public Vec addVector(double x, double y,
			double z)
	{
		return new Vec(x + xCoord, y + yCoord, z + zCoord);
	}
	
	public Vec add(double x, double y,
			double z)
	{
		xCoord += x;
		yCoord += y;
		zCoord += z;
		return this;
	}
	
	public Vec add(Vec3 vec)
	{
		xCoord += vec.xCoord;
		yCoord += vec.yCoord;
		zCoord += vec.zCoord;
		return this;
	}
}