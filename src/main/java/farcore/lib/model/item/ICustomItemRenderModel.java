package farcore.lib.model.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ICustomItemRenderModel extends IBakedModel
{
	List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand);
}
