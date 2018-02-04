/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.util;

import static nebula.common.util.L.SQRT2;

import java.util.function.Function;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class CoordTransformer implements Cloneable
{
	static final double[][] MODEL_ROTATION_VAL = { { 1, 0 }, { SQRT2 / 2, SQRT2 / 2 }, { 0, 1 }, { -SQRT2 / 2, SQRT2 / 2 }, { -1, 0 } };
	
	double[]					oppisite, rotation, scale, transform;
	boolean						changed;
	// If no change detected, use identity function.
	Function<Tuple3d, Tuple3d>	function	= Function.identity();
	
	public CoordTransformer()
	{
		normalize();
	}
	
	CoordTransformer(double[] oppisite, double[] rotation, double[] scale, double[] transform)
	{
		this.oppisite = oppisite;
		this.rotation = rotation;
		this.scale = scale;
		this.transform = transform;
		this.changed = true;
	}
	
	@Override
	protected CoordTransformer clone()
	{
		return copy();
	}
	
	public CoordTransformer copy()
	{
		return new CoordTransformer(this.oppisite.clone(), this.rotation.clone(), this.scale.clone(), this.transform.clone());
	}
	
	public CoordTransformer normalize()
	{
		this.oppisite = new double[] { 0.0, 0.0, 0.0 };
		this.rotation = new double[] { 0.0, 0.0, 0.0, 1.0 };
		this.scale = new double[] { 1.0, 1.0, 1.0 };
		this.transform = new double[] { 0.0, 0.0, 0.0 };
		this.changed = false;
		return this;
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
	
	public CoordTransformer setOppisite(CoordTransformer transformer)
	{
		this.oppisite = transformer.oppisite.clone();
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
		this.transform = transformer.transform.clone();
		markChanged();
		return this;
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
	
	public CoordTransformer setRotation(CoordTransformer transformer)
	{
		this.rotation = transformer.rotation.clone();
		return this;
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
		this.rotation = mulQuat(new double[] { x * sin, y * sin, z * sin, cos }, this.rotation);
		markChanged();
		return this;
	}
	
	public CoordTransformer applyRotation(ModelRotation rotation)
	{
		int x, y;
		switch (rotation)
		{
		default:
		case X0_Y0    : x = 0; y = 0; break;
		case X0_Y90   : x = 0; y = 1; break;
		case X0_Y180  : x = 0; y = 2; break;
		case X0_Y270  : x = 0; y = 3; break;
		case X90_Y0   : x = 1; y = 0; break;
		case X90_Y90  : x = 1; y = 1; break;
		case X90_Y180 : x = 1; y = 2; break;
		case X90_Y270 : x = 1; y = 3; break;
		case X180_Y0  : x = 2; y = 0; break;
		case X180_Y90 : x = 2; y = 1; break;
		case X180_Y180: x = 2; y = 2; break;
		case X180_Y270: x = 2; y = 3; break;
		case X270_Y0  : x = 3; y = 0; break;
		case X270_Y90 : x = 3; y = 1; break;
		case X270_Y180: x = 3; y = 2; break;
		case X270_Y270: x = 3; y = 3; break;
		}
		double[] ds1 = MODEL_ROTATION_VAL[x];
		double[] ds2 = MODEL_ROTATION_VAL[y];
		this.rotation = mulQuat(new double[] { ds1[0] * ds2[0], ds1[1] * ds2[0], ds1[0] * ds2[1], ds1[1] * ds2[1] }, this.rotation);
		markChanged();
		return this;
	}
	
	private void markChanged()
	{
		this.changed = true;
		this.function = Function.identity();
	}
	
	private Function<Tuple3d, Tuple3d> buildMatrix()
	{
		if (this.changed)
		{
			Matrix3d rotation = new Matrix3d();
			rotation.set(new Quat4d(this.rotation));
			this.function = tuple -> {
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
	
	public EnumFacing transform(EnumFacing facing)
	{
		Vec3i vec = facing.getDirectionVec();
		float[] fs = new float[] { vec.getX(), vec.getY(), vec.getZ() };
		transform(fs);
		return EnumFacing.getFacingFromVector(fs[0], fs[1], fs[2]);
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
		rot.conjugate();
		vec.mul(rot);
		tuple.set(vec.x, vec.y, vec.z);
		return tuple;
	}
	
	private static double[] mulQuat(double[] e1, double[] e2)
	{
		return new double[] { +e1[0] * e2[3] + e1[1] * e2[2] - e1[2] * e2[1] + e1[3] * e2[0], -e1[0] * e2[2] + e1[1] * e2[3] + e1[2] * e2[0] + e1[3] * e2[1], +e1[0] * e2[1] - e1[1] * e2[1] + e1[2] * e2[3] + e1[3] * e2[2], +e1[0] * e2[0] - e1[1] * e2[1] - e1[2] * e2[2] - e1[3] * e2[3] };
	}
}
