package fla.api.util;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface InfoBuilder<T>
{
	public List<String> getInfo(T t);
}
