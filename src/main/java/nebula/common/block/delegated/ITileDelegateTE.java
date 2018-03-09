/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import javax.annotation.Nullable;

import nebula.common.inventory.IBasicInventory;
import nebula.common.stack.AbstractStack;
import nebula.common.tile.delegate.TEDelegate;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

/**
 * @author ueyudiud
 */
public interface ITileDelegateTE<B extends BlockDelegatedTE, T extends TEDelegate> extends ITileDelegate<B, T>
{
	void onTECreated(T tile);
	
	void onTELoad(T tile);
	
	void onTEUnload(T tile);
	
	void onServerUpdate(T tile);
	
	void onClientUpdate(T tile);
	
	interface ITDTE_HasGui<B extends BlockDelegatedTE, T extends TEDelegate,
	C extends ContainerDelegate<? extends T>, G extends GuiContainerDelegate<? extends T>> extends ITileDelegateTE<B, T>
	{
		default C openContainer(T tile, EntityPlayer player, int id)
		{
			return (C) new ContainerDelegate(tile, player, this);
		}
		
		void addContainerSlots(T tile, EntityPlayer player);
		
		ItemStack onToolClick(T tile, C container, ItemStack tool, IInventory inventory, int index);
		
		void onContainerClosed(T tile, C container, EntityPlayer player);
		
		void onRecieveGUIAction(T tile, C container, byte type, long value);
		
		@SideOnly(Side.CLIENT)
		default G OpenGui(T tile, EntityPlayer player, int id)
		{
			return (G) new GuiContainerDelegate(tile, player, this, id, getGuiLocation());
		}
		
		@SideOnly(Side.CLIENT)
		ResourceLocation getGuiLocation();
		
		@SideOnly(Side.CLIENT)
		void drawBackgroundLayer(T tile, G gui, int mouseX, int mouseY, int x, int y);
		
		@SideOnly(Side.CLIENT)
		void drawForegroundLayer(T tile, G gui, int mouseX, int mouseY, int x, int y);
		
		@SideOnly(Side.CLIENT)
		void addGuiButton(T tile, G gui, int x, int y);
		
		@SideOnly(Side.CLIENT)
		void onGuiButtonClicked(T tile, G gui, int id);
		
		@SideOnly(Side.CLIENT)
		void onKeyTyped(T tile, G gui, char typedChar, int keyCode);
	}
	
	interface ITDTE_ItemInventory<B extends BlockDelegatedTE, T extends TEDelegate> extends ITileDelegateTE<B, T>
	{
		IBasicInventory createInventoryFor(T tile);
	}
	
	interface ITDTE_ItemPlayerIO<B extends BlockDelegatedTE, T extends TEDelegate> extends ITileDelegateTE<B, T>
	{
		boolean canExtractItemByPlayer(T tile, Direction to);
		
		boolean canInsertItemByPlayer(T tile, Direction from, @Nullable ItemStack stack);
		
		ItemStack extractItemByPlayer(T tile, int size, Direction to, boolean simulate);
		
		ItemStack extractItemByPlayer(T tile, AbstractStack suggested, Direction to, boolean simulate);
		
		int insertItemByPlayer(T tile, ItemStack stack, Direction from, boolean simulate);
		
		ActionResult<ItemStack> onPlayerTryUseIO(T tile, @Nullable ItemStack current, EntityPlayer player, Direction side, float x, float y, float z, boolean isActiveHeld);
	}
	
	interface ITDTE_ItemMachineIO<B extends BlockDelegatedTE, T extends TEDelegate> extends ITileDelegateTE<B, T>
	{
		IItemHandler getItemHandlerFromSide(T tile, Direction side);
	}
}
