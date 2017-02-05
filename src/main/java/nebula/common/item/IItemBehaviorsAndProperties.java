package nebula.common.item;

import javax.annotation.Nullable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemBehaviorsAndProperties
{
	/**
	 * After a thinking, I think we need't this interface to get real dig speed.
	 * @author ueyudiud
	 *
	 */
	@Deprecated
	public static interface IIP_DigSpeed
	{
		float replaceDigSpeed(ItemStack stack, BreakSpeed event);
	}
	
	public static interface IIB_BlockHarvested
	{
		void onBlockHarvested(ItemStack stack, HarvestDropsEvent event);
	}
	
	public static interface IIP_CustomOverlayInGui
	{
		@SideOnly(Side.CLIENT)
		boolean renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, @Nullable String text);
	}
}