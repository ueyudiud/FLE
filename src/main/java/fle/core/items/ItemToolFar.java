package fle.core.items;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumItem;
import farcore.data.EnumToolType;
import farcore.data.MC;
import farcore.lib.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import farcore.lib.item.ItemTool;
import farcore.lib.util.SubTag;
import farcore.util.U;
import fle.core.FLE;
import fle.core.items.behavior.BehaviorTool;
import fle.core.items.tool.ToolAxe;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolFar extends ItemTool implements IIP_CustomOverlayInGui
{
	public ItemToolFar()
	{
		super(FLE.MODID, "tool");
		EnumItem.tool.set(this);
		initalizeItems();
	}
	
	protected void initalizeItems()
	{
		addSubItem(1, "adz.rock", "Adz", null, MC.adz_rock, new ToolAxe(EnumToolType.adz, 1.2F), true, true, SubTag.TOOL, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.adz), new BehaviorTool());
	}

	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x,
			int z, String text)
	{
		U.Client.renderItemSubscirptInGUI(render, fontRenderer, stack, x, z, text);
		U.Client.renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z);
		U.Client.renderItemCooldownInGUI(render, fontRenderer, stack, x, z);
		return true;
	}
}