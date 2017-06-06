package farcore.lib.block.state;

import net.minecraft.block.properties.IProperty;

public interface IFarProperty<T extends Comparable<T>> extends IProperty<T>
{
	T instance();
}