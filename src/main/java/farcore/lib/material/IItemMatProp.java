package farcore.lib.material;

import farcore.lib.util.UnlocalizedList;
import farcore.lib.world.IEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemMatProp
{
	void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition);
	
	int getMetaOffset(ItemStack stack, Mat material, MatCondition condition);
	
	ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition,
			IEnvironment environment);
	
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list);
}