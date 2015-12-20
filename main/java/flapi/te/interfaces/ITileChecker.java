package flapi.te.interfaces;

import java.lang.reflect.ParameterizedType;

import net.minecraft.tileentity.TileEntity;
import flapi.util.IDataChecker;

public abstract class ITileChecker<T> implements IDataChecker<TileEntity>
{
	Class<T> checkingClass;
	
	public ITileChecker()
	{
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		checkingClass = (Class) type.getActualTypeArguments()[0];
	}
	
	protected boolean isInstance(TileEntity tile)
	{
		return checkingClass.isInstance(tile);
	}
	
	protected abstract boolean match(T target);
	
	@Override
	public boolean isTrue(TileEntity target)
	{
		return isInstance(target) ? match((T) target) : false;
	}
}