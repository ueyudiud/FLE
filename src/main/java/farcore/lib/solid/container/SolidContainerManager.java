/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import farcore.lib.solid.Solid;
import farcore.lib.solid.SolidStack;
import nebula.common.util.L;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public final class SolidContainerManager
{
	private static final Multimap<SolidContainerKey, SolidContainerData> MAP1 = HashMultimap.create();
	private static final Map<SolidContainerKey, SolidContainerData> MAP2 = new HashMap<>();
	
	static class SolidContainerKey
	{
		int hashcode;
		Item item;
		Solid solid;
		int meta;
		
		public SolidContainerKey(Item item, Solid solid, int meta)
		{
			this.item = item;
			this.solid = solid;
			this.meta = meta;
			this.hashcode = Objects.hash(solid, item, meta);
		}
		
		@Override
		public int hashCode()
		{
			return this.hashcode;
		}
		
		@Override
		@SuppressWarnings("unlikely-arg-type")
		public boolean equals(Object obj)
		{
			if (obj instanceof SolidContainerKeySearching)
				return ((SolidContainerKeySearching) obj).equals(this);
			return obj == this || (obj instanceof SolidContainerKey &&
					((SolidContainerKey) obj).item == this.item &&
					((SolidContainerKey) obj).meta == this.meta &&
					((SolidContainerKey) obj).solid == this.solid);
		}
	}
	
	static class SolidContainerKeySearching
	{
		ItemStack stack;
		Solid solid;
		
		SolidContainerKeySearching(ItemStack stack, Solid solid)
		{
			this.stack = stack;
			this.solid = solid;
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(this.solid, this.stack.getItem(), this.stack.getItemDamage());
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof SolidContainerKey &&
					L.equals(((SolidContainerKey) obj).solid, this.solid) &&
					((SolidContainerKey) obj).item == this.stack.getItem() &&
					((SolidContainerKey) obj).meta == this.stack.getItemDamage();
		}
	}
	
	public static class SolidContainerData
	{
		public final ItemStack empty;
		public final ItemStack filled;
		public final SolidStack contain;
		
		public SolidContainerData(ItemStack empty, SolidStack contain, ItemStack filled)
		{
			this.empty = empty;
			this.contain = contain;
			this.filled = filled;
		}
	}
	
	public static void addContainerItem(@Nonnull SolidStack contain, @Nonnull ItemStack filled)
	{
		addContainerItem(null, contain, filled);
	}
	
	public static void addContainerItem(@Nonnull Item item, int emptyMeta, int filledMeta, @Nonnull SolidStack contain)
	{
		Objects.requireNonNull(item);
		Solid solid = contain.getSolid();
		SolidContainerData data = new SolidContainerData(new ItemStack(item, 1, emptyMeta), contain.copy(), new ItemStack(item, 1, filledMeta));
		MAP2.put(new SolidContainerKey(item, solid, emptyMeta), data);
		MAP2.put(new SolidContainerKey(item, null, filledMeta), data);
		MAP1.put(new SolidContainerKey(item, null, emptyMeta), data);
	}
	
	public static void addContainerItem(@Nullable ItemStack empty,
			@Nonnull SolidStack contain, @Nonnull ItemStack filled)
	{
		Solid solid = contain.getSolid();
		SolidContainerData data = new SolidContainerData(ItemStack.copyItemStack(empty), contain.copy(), filled.copy());
		if (empty != null)
		{
			MAP2.put(new SolidContainerKey(empty.getItem(), solid, empty.getItemDamage()), data);
			MAP1.put(new SolidContainerKey(empty.getItem(), null, empty.getItemDamage()), data);
		}
		MAP2.put(new SolidContainerKey(filled.getItem(), null, filled.getItemDamage()), data);
	}
	
	public static boolean isEmptyContainer(ItemStack empty)
	{
		return MAP1.containsKey(new SolidContainerKeySearching(empty, null));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static SolidContainerData getFilledContainer(ItemStack empty, SolidStack stack)
	{
		if (stack == null) return null;
		SolidContainerData data = MAP2.get(new SolidContainerKeySearching(empty, stack.getSolid()));
		return data != null && data.contain.amount <= stack.amount ? data : null;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static SolidContainerData getFilledContainer(ItemStack empty, Solid solid)
	{
		return MAP2.get(new SolidContainerKeySearching(empty, solid));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static SolidContainerData getDrainedContainer(ItemStack filled)
	{
		return MAP2.get(new SolidContainerKeySearching(filled, null));
	}
	
	public static Collection<SolidContainerData> getContainerDatas()
	{
		return MAP1.values();
	}
}
