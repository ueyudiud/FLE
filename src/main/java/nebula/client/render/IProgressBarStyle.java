/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IProgressBarStyle
{
	@SideOnly(Side.CLIENT)
	double getProgressScale(ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	int[] getProgressColor(ItemStack stack, double progress);
}
