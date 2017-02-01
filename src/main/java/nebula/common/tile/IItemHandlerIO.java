package nebula.common.tile;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface IItemHandlerIO
{
	/**
	 * Match side can extract item.
	 * @return
	 */
	boolean canExtractItem(Direction to);

	/**
	 * Match side can insert item.
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canInsertItem(Direction from, @Nullable ItemStack stack);

	ItemStack extractItem(int size, Direction to, boolean simulate);
	
	ItemStack extractItem(AbstractStack suggested, Direction to, boolean simulate);

	int insertItem(ItemStack stack, Direction from, boolean simulate);
	
	ActionResult<ItemStack> onPlayerTryUseIO(@Nullable ItemStack current, EntityPlayer player, Direction side, float x, float y, float z, boolean isActiveHeld);

	//	public static class Instance implements IItemHandlerIO
	//	{
	//		@Override
	//		public boolean canExtractItem()
	//		{
	//			return false;
	//		}
	//
	//		@Override
	//		public boolean canInsertItem()
	//		{
	//			return false;
	//		}
	//
	//		@Override
	//		public ItemStack extractItem(int size, Direction direction, boolean simulate)
	//		{
	//			return null;
	//		}
	//
	//		@Override
	//		public int tryInsertItem(ItemStack stack, Direction direction, boolean simulate)
	//		{
	//			return 0;
	//		}
	//	}
	//
	//	public static class ItemHandlerIOStorage implements IStorage<IItemHandlerIO>
	//	{
	//		@Override
	//		public NBTBase writeNBT(Capability<IItemHandlerIO> capability, IItemHandlerIO instance, EnumFacing side)
	//		{
	//			return new NBTTagString("");
	//		}
	//
	//		@Override
	//		public void readNBT(Capability<IItemHandlerIO> capability, IItemHandlerIO instance, EnumFacing side,
	//				NBTBase nbt)
	//		{
	//
	//		}
	//	}
}