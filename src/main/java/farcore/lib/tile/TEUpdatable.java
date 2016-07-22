package farcore.lib.tile;

import farcore.data.V;
import farcore.lib.util.Log;
import net.minecraft.util.ITickable;

public class TEUpdatable extends TELockable implements ITickable
{
	@Override
	public void update()
	{
		try
		{
			isUpdating = false;
			updateEntity1();
			isUpdating = true;
		}
		catch(Exception exception)
		{
			if(worldObj != null)
				if(!worldObj.isRemote)
				{
					Log.error("Tile entity throws an exception during ticking in the world, "
							+ "if your enable the option of remove errored tile, this tile will "
							+ "be removed soon. Please report this bug to modder.", exception);
					if(V.removeErroredTile)
						removeBlock();
					else
						throw exception;
				}
				else
					Log.warn("The tile might disconnect from server caused an exception, "
							+ "if not, please report this bug to modder. If you are playing "
							+ "client world, this exception might cause this world can not "
							+ "load next time, if you can not load the world second time, "
							+ "you can try to remove errored block.", exception);
			else
				Log.warn("Tile entity throws an exception when not ticking in the world. "
						+ "Something might update out of world!", exception);
		}
	}

	protected void updateEntity1()
	{

	}
}