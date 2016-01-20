package fle.core.render.model;

import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;

import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

public class ModelState implements IModelState
{	
	private static final double PIXEL_PER_BLOCK = 64D;
	public static final ModelState DEFAULT = newState().finalized();
	
	public static ModelState newState()
	{
		return new ModelState();
	}
	
	private boolean finalized;
	private Matrix4d matrix;
	private Matrix4d cache = new Matrix4d();
	
	private ModelState()
	{
		matrix = new Matrix4d();
		matrix.setIdentity();
	}
	
	private void check()
	{
		if(finalized) throw new RuntimeException("The model state is already finnalized, can not change it!");
	}
	
	public ModelState finalized()
	{
		finalized = true;
		return this;
	}
	
	public void translate(int x, int y, int z)
	{
		check();
		double X = (double) x / PIXEL_PER_BLOCK;
		double Y = (double) y / PIXEL_PER_BLOCK;
		double Z = (double) z / PIXEL_PER_BLOCK;
		matrix.setTranslation(new Vector3d(X, Y, Z));
	}
	
	public void rotate(int x, int y, int z)
	{
		check();
		if(x % 360 != 0)
		{
			double X = (double) x * Math.PI / 180D;
			cache.rotX(X);
			matrix.mul(cache);
		}
		if(y % 360 != 0)
		{
			double Y = (double) y * Math.PI / 180D;
			cache.rotY(Y);
			matrix.mul(cache);
		}
		if(z % 360 != 0)
		{
			double Z = (double) z * Math.PI / 180D;
			cache.rotZ(Z);
			matrix.mul(cache);
		}
		
	}
	
	public void scale(int x, int y, int z)
	{
		check();
		double X = (double) x / PIXEL_PER_BLOCK;
		double Y = (double) y / PIXEL_PER_BLOCK;
		double Z = (double) z / PIXEL_PER_BLOCK;
		cache.setIdentity();
		cache.m00 = X;
		cache.m11 = Y;
		cache.m22 = Z;
		matrix.mul(cache);
	}
	
	public void scale(int scale)
	{
		check();
		double S = (double) scale / PIXEL_PER_BLOCK;
		cache.m00 = cache.m11 = cache.m22 = S;
		matrix.mul(cache);
	}
	
	@Override
	public TRSRTransformation apply(IModelPart part)
	{
		return new TRSRTransformation(new Matrix4f(matrix));
	}
}