package farcore.lib.tile;

import farcore.lib.util.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public interface IItemHandlerIO
{
	/**
	 * Match side can extract item.
	 * @return
	 */
	boolean canExtractItem();
	
	/**
	 * Match side can insert item.
	 * @return
	 */
	boolean canInsertItem();
	
	ItemStack extractItem(int size, Direction direction, boolean simulate);
	
	int tryInsertItem(ItemStack stack, Direction direction, boolean simulate);

	public static class Instance implements IItemHandlerIO
	{
		@Override
		public boolean canExtractItem()
		{
			return false;
		}

		@Override
		public boolean canInsertItem()
		{
			return false;
		}

		@Override
		public ItemStack extractItem(int size, Direction direction, boolean simulate)
		{
			return null;
		}

		@Override
		public int tryInsertItem(ItemStack stack, Direction direction, boolean simulate)
		{
			return 0;
		}
	}
	
	public static class ItemHandlerIOStorage implements IStorage<IItemHandlerIO>
	{
		@Override
		public NBTBase writeNBT(Capability<IItemHandlerIO> capability, IItemHandlerIO instance, EnumFacing side)
		{
			return new NBTTagString("");
		}
		
		@Override
		public void readNBT(Capability<IItemHandlerIO> capability, IItemHandlerIO instance, EnumFacing side,
				NBTBase nbt)
		{
			
		}
	}
}