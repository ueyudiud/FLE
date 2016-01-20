package fle.core.render.model;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class BakedFace extends BakedQuad
{
	public static EnumFacing getFacingFromVertexData(int[] ver1, int[] ver2, int[] ver3, int[] ver4)
    {
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat(ver1[0]), Float.intBitsToFloat(ver1[1]), Float.intBitsToFloat(ver1[2]));
        Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(ver2[7]), Float.intBitsToFloat(ver2[8]), Float.intBitsToFloat(ver2[9]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(ver3[14]), Float.intBitsToFloat(ver3[15]), Float.intBitsToFloat(ver3[16]));
        Vector3f vector3f3 = new Vector3f();
        Vector3f vector3f4 = new Vector3f();
        Vector3f vector3f5 = new Vector3f();
        vector3f3.sub(vector3f, vector3f1);
        vector3f4.sub(vector3f2, vector3f1);
        vector3f5.cross(vector3f4, vector3f3);
        vector3f5.normalize();
        EnumFacing enumfacing = null;
        float f = 0.0F;
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing1 = aenumfacing[j];
            Vec3i vec3i = enumfacing1.getDirectionVec();
            Vector3f vector3f6 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            float f1 = vector3f5.dot(vector3f6);

            if (f1 >= 0.0F && f1 > f)
            {
                f = f1;
                enumfacing = enumfacing1;
            }
        }

        if (enumfacing == null)
        {
            return EnumFacing.UP;
        }
        else
        {
            return enumfacing;
        }
    }
	
	public BakedFace(ModelPart part, int idx)
	{
		this(part, part.getConntection()[idx]);
	}
	BakedFace(ModelPart part, int[] idx)
	{
		this(new int[][]{
				part.getVertex(idx[0]),
				part.getVertex(idx[1]),
				part.getVertex(idx[2]),
				part.getVertex(idx[3])
		}, idx[4], getFacingFromVertexData(part.getVertex(idx[0]),
				part.getVertex(idx[1]),
				part.getVertex(idx[2]),
				part.getVertex(idx[3])));
	}
	BakedFace(int[][] idxs, int idx, EnumFacing face)
	{
		this(
				idxs[0][0], idxs[0][1], idxs[0][2], idxs[0][3], idxs[0][4], idxs[0][5], //idxs[0][6], 
				idxs[1][0], idxs[1][1], idxs[1][2], idxs[1][3], idxs[1][4], idxs[1][5], //idxs[1][6], 
				idxs[2][0], idxs[2][1], idxs[2][2], idxs[2][3], idxs[2][4], idxs[2][5], //idxs[2][6], 
				idxs[3][0], idxs[3][1], idxs[3][2], idxs[3][3], idxs[3][4], idxs[3][5], //idxs[3][6], 
				idx, face);
	}
	public BakedFace(
			int x1, int y1, int z1, int color1, int u1, int v1, 
			int x2, int y2, int z2, int color2, int u2, int v2, 
			int x3, int y3, int z3, int color3, int u3, int v3, 
			int x4, int y4, int z4, int color4, int u4, int v4, int iconIdx, EnumFacing facing)
	{
		super(new int[]{
				x1, y1, z1, color1, u1, v1, 0,
				x2, y2, z2, color2, u2, v2, 0,
				x3, y3, z3, color3, u3, v3, 0,
				x4, y4, z4, color4, u4, v4, 0}, iconIdx, facing);
	}
}