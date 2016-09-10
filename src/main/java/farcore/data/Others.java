package farcore.data;

import java.io.IOException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class Others
{
	public static final Item ITEM_AIR = Item.getItemFromBlock(Blocks.AIR);
	
	public static final IAttribute PROJECTILE_DAMAGE = (new RangedAttribute((IAttribute)null, "far.projectile.damage", 0.0D, 0, Double.MAX_VALUE)).setShouldWatch(true);
	
	public static final DataSerializer<ItemStack> SERIALIZER_STACK = new DataSerializer<ItemStack>()
	{
		@Override
		public void write(PacketBuffer buf, ItemStack value)
		{
			buf.writeItemStackToBuffer(value);
		}

		@Override
		public ItemStack read(PacketBuffer buf) throws IOException
		{
			ItemStack stack = buf.readItemStackFromBuffer();
			return stack == null ? new ItemStack(Blocks.AIR) : stack;
		}

		@Override
		public DataParameter<ItemStack> createKey(int id)
		{
			return new DataParameter(id, this);
		}
	};
}