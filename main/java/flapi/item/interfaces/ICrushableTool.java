package flapi.item.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ICrushableTool
{
	boolean doCrush(World aWorld, int x, int y, int z, ItemStack aStack);
}
