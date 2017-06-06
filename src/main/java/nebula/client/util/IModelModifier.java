/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IModelModifier
{
	default void recolor(Vector4f color) {};
	
	default void transform(Point3f point) {};
	
	default void transform(Vector3f normal) {};
	
	default EnumFacing rotateFacing(EnumFacing facing)
	{
		Vector3f vec = new Vector3f(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
		transform(vec);
		return EnumFacing.getFacingFromVector(vec.x, vec.y, vec.z);
	}
}