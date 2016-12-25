/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class FarCoreItemLayerTable extends FarCoreItemLayer
{
	private Function<ItemStack, String> rowFun;
	private Function<ItemStack, String> columnFun;
	private Table<String, String, List<BakedQuad>> quads;
	
	public FarCoreItemLayerTable(int layer,
			Function<ItemStack, String> rowFun, Function<ItemStack, String> columnFun, Table<String, String, List<BakedQuad>> quads)
	{
		super(layer);
		this.rowFun = rowFun;
		this.columnFun = columnFun;
		this.quads = quads;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack)
	{
		List<BakedQuad> list = this.quads.get(this.rowFun.apply(stack), this.columnFun.apply(stack));
		return list == null ? ImmutableList.of() : list;
	}
}