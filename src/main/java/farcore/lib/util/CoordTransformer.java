/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.util;

import java.util.function.Function;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

import net.minecraft.util.EnumFacing;

/**
 * @author ueyudiud
 */
public class CoordTransformer
{
	double[]
			oppisite  = {0.0, 0.0, 0.0},
			rotation  = {0.0, 0.0, 0.0, 1.0},
			scale     = {1.0, 1.0, 1.0},
			transform = {0.0, 0.0, 0.0};
	boolean changed = false;
	Function<Tuple3d, Tuple3d> function = Function.identity();//If no change detected, use identity function.
	
	public CoordTransformer() { }
	CoordTransformer(double[] oppisite, double[] rotation, double[] scale, double[] transform)
	{
		this.oppisite = oppisite;
		this.rotation = rotation;
		this.scale = scale;
		this.transform = transform;
		this.changed = true;
	}
	
	public CoordTransformer copy()
	{
		return new CoordTransformer(this.oppisite, this.rotation, this.scale, this.transform);
	}
	
	public CoordTransformer multiple(CoordTransformer transformer)
	{
		Point3d point = new Point3d(this.transform);
		point = transformer.transform(point);
		this.rotation = mulQuat(transformer.rotation, this.rotation);
		this.transform[0] = point.x;
		this.transform[1] = point.y;
		this.transform[2] = point.z;
		this.scale[0] *= transformer.scale[0];
		this.scale[1] *= transformer.scale[1];
		this.scale[2] *= transformer.scale[2];
		return this;
	}
	
	public CoordTransformer setScale(double scale)
	{
		return setScale(scale, scale, scale);
	}
	
	public CoordTransformer setScale(double x, double y, double z)
	{
		this.scale[0] = x;
		this.scale[1] = y;
		this.scale[2] = z;
		markChanged();
		return this;
	}
	
	public CoordTransformer setOppisite(double x, double y, double z)
	{
		this.oppisite[0] = x;
		this.oppisite[1] = y;
		this.oppisite[2] = z;
		markChanged();
		return this;
	}
	
	public CoordTransformer setTransform(CoordTransformer transformer)
	{
		return setTransform(transformer.transform[0], transformer.transform[1], transformer.transform[2]);
	}
	
	public CoordTransformer setTransform(double x, double y, double z)
	{
		this.transform[0] = x;
		this.transform[1] = y;
		this.transform[2] = z;
		markChanged();
		return this;
	}
	
	public CoordTransformer addTransform(double x, double y, double z)
	{
		this.transform[0] += x;
		this.transform[1] += y;
		this.transform[2] += z;
		markChanged();
		return this;
	}
	
	public CoordTransformer setRotationArray(double x, double y, double z, int size, int count)
	{
		return setRotation(x, y, size, 360.0 * count / size);
	}
	
	public CoordTransformer setRotation(double x, double y, double z, double angle)
	{
		double inv = 1.0 / Math.sqrt(x * x + y * y + z * z);
		x *= inv;
		y *= inv;
		z *= inv;
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad / 2);
		double cos = Math.cos(rad / 2);
		this.rotation[0] = x * sin;
		this.rotation[1] = y * sin;
		this.rotation[2] = z * sin;
		this.rotation[3] = cos;
		markChanged();
		return this;
	}
	
	public CoordTransformer setAxisRotetion(EnumFacing facing, double angle)
	{
		return setRotation(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ(), angle);
	}
	
	public CoordTransformer applyRotation(double x, double y, double z, double angle)
	{
		double inv = 1.0 / Math.sqrt(x * x + y * y + z * z);
		x *= inv;
		y *= inv;
		z *= inv;
		double rad = Math.toRadians(angle);
		double sin = Math.sin(Math.toRadians(rad) / 2);
		double cos = Math.cos(Math.toRadians(rad) / 2);
		this.rotation = mulQuat(new double[]{x * sin, y * sin, z * sin, cos}, this.rotation);
		markChanged();
		return this;
	}
	
	private void markChanged()
	{
		this.changed = true;
		this.function = null;
	}
	
	private Function<Tuple3d, Tuple3d> buildMatrix()
	{
		if(this.changed)
		{
			final Matrix3d rotation = new Matrix3d();
			rotation.set(new Quat4d(this.rotation));
			this.function = tuple ->
			{
				tuple.x -= this.oppisite[0];
				tuple.y -= this.oppisite[1];
				tuple.z -= this.oppisite[2];
				tuple.x *= this.scale[0];
				tuple.y *= this.scale[1];
				tuple.z *= this.scale[2];
				rotation.transform(tuple);
				tuple.x += this.oppisite[0] + this.transform[0];
				tuple.y += this.oppisite[1] + this.transform[1];
				tuple.z += this.oppisite[2] + this.transform[2];
				return tuple;
			};
		}
		return this.function;
	}
	
	public float[] transform(float[] tuple)
	{
		Tuple3d point = transform(new Point3d(tuple[0], tuple[1], tuple[2]));
		tuple[0] = (float) point.x;
		tuple[1] = (float) point.y;
		tuple[2] = (float) point.z;
		return tuple;
	}
	
	public <V extends Tuple3f> V transform(V tuple)
	{
		tuple.set(transform(new Point3d(tuple)));
		return tuple;
	}
	
	public <V extends Tuple3d> V transform(V tuple)
	{
		return (V) buildMatrix().apply(tuple);
	}
	
	public <V extends Tuple3f> V normal(V tuple)
	{
		Quat4d rot = new Quat4d(this.rotation);
		Quat4d vec = new Quat4d(tuple.x, tuple.y, tuple.z, 0);
		vec.mul(rot, vec);
		rot.x = -rot.x;
		rot.y = -rot.y;
		rot.z = -rot.z;
		vec.mul(rot);
		tuple.set((float) vec.x, (float) vec.y, (float) vec.z);
		return tuple;
	}
	
	public <V extends Tuple3d> V normal(V tuple)
	{
		Quat4d rot = new Quat4d(this.rotation);
		Quat4d vec = new Quat4d(tuple.x, tuple.y, tuple.z, 0);
		vec.mul(rot, vec);
		rot.x = -rot.x;
		rot.y = -rot.y;
		rot.z = -rot.z;
		vec.mul(rot);
		tuple.set(vec.x, vec.y, vec.z);
		return tuple;
	}
	
	private static double[] mulQuat(double[] e1, double[] e2)
	{
		return new double[]{
				+e1[0]*e2[3]+e1[1]*e2[2]-e1[2]*e2[1]+e1[3]*e2[0],
				-e1[0]*e2[2]+e1[1]*e2[3]+e1[2]*e2[0]+e1[3]*e2[1],
				+e1[0]*e2[1]-e1[1]*e2[1]+e1[2]*e2[3]+e1[3]*e2[2],
				+e1[0]*e2[0]-e1[1]*e2[1]-e1[2]*e2[2]-e1[3]*e2[3]};
	}
}