package fle.core.render;

import org.lwjgl.opengl.GL11;

public class RIThermalWire extends RIBase
{
	@Override
	public void renderItem()
	{
		setColor(stack.getItem().getColorFromItemStack(stack, 0));
		//renderIcon(icon);
		GL11.glColor4d(rgb_r, rgb_g, rgb_b, 1.0D);
		float f1 = 0.5F - getSize(),
				f2 = 0.5F + getSize();
		if(type == ItemRenderType.INVENTORY)
		{
			//renderIcon(16.0D, 0.0D, 0.0F, 0.0F, -1.0F);
			renderIcon(0F, f1 * 16D, 16F, f2 * 16D, 0F, 0.0F, 0.0F, -1.0F, 0, (int) f1 * 16, 16, (int) f2 * 16);
		}
		else
		{
			//renderIcon(1.0D, -0.0078125D, 0.0F, 0.0F, 1.0F);
			//renderIcon(1.0D, -0.0625D, 0.0F, 0.0F, -1.0F);
			renderIcon(0F, f1, 1F, f2,- 0.0078125D, 0.0F, 0.0F, 1.0F, 0, (int) f1 * 16, 16, (int) f2 * 16);
			renderIcon(0F, f1, 1F, f2, -0.0625D, 0.0F, 0.0F, -1.0F, 0, (int) f1 * 16, 16, (int) f2 * 16);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private float getSize()
	{
		return 0.125F;
	}
}