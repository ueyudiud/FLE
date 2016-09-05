package farcore.lib.model.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IModelLayerProvider
{
	int getRenderPasses();
	
	String getModelLayer(int pass, ItemStack stack);
}