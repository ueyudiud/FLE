package farcore.lib.command;

import farcore.enums.UpdateType;
import farcore.lib.world.WorldDatas;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandWorldData extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "fwm";
	}

    /**
     * Return the required permission level for this command.
     */
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/fwm help Get help of world data handler.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] strings)
	{
		if(strings.length >= 2)
		{
			if("help".equals(strings[0]))
			{
				sender.addChatMessage(new ChatComponentTranslation("/fwm get [x] [y] [z]"));
				sender.addChatMessage(new ChatComponentTranslation("/fwm set [x] [y] [z] [value]"));
			}
			else if("get".equals(strings[0]))
			{
				if(strings.length != 4)
				{
					throw new WrongUsageException(getCommandUsage(sender));
				}
				ChunkCoordinates coordinates = sender.getPlayerCoordinates();
	            int i = MathHelper.floor_double(func_110666_a(sender, coordinates.posX, strings[1]));
	            int j = MathHelper.floor_double(func_110666_a(sender, coordinates.posY, strings[2]));
	            int k = MathHelper.floor_double(func_110666_a(sender, coordinates.posZ, strings[3]));
	            World world = sender.getEntityWorld();
	            if(!world.blockExists(i, j, k))
	            {
	            	throw new CommandException("commands.setblock.outOfWorld");
	            }
	            else
	            {
	            	sender.addChatMessage(new ChatComponentTranslation("Data at coord %s, %s, %s is %s", 
	            			Integer.toString(i), 
	            			Integer.toString(j), 
	            			Integer.toString(k), 
	            			Short.toString(WorldDatas.getBlockData(world, i, j, k))));
	            }
			}
			else if("set".equals(strings[0]))
			{
				if(strings.length != 5)
				{
					throw new WrongUsageException(getCommandUsage(sender));
				}
				ChunkCoordinates coordinates = sender.getPlayerCoordinates();
	            int i = MathHelper.floor_double(func_110666_a(sender, coordinates.posX, strings[1]));
	            int j = MathHelper.floor_double(func_110666_a(sender, coordinates.posY, strings[2]));
	            int k = MathHelper.floor_double(func_110666_a(sender, coordinates.posZ, strings[3]));
	            int value;
	            try
	            {
	            	value = Short.valueOf(strings[4]);
	            }
	            catch(Throwable throwable)
	            {
	            	throw new CommandException("Invalid value : %s.", strings[4]);
	            }
	            World world = sender.getEntityWorld();
	            if(!world.blockExists(i, j, k))
	            {
	            	throw new CommandException("commands.setblock.outOfWorld");
	            }
	            else
	            {
	            	WorldDatas.setBlockData(world, i, j, k, value, UpdateType.CALL_CLIENT);
	            	sender.addChatMessage(new ChatComponentTranslation("Set data at %s, %s, %s.", 
	            			Integer.toString(i), 
	            			Integer.toString(j), 
	            			Integer.toString(k)));
	            }
			}
		}
		else
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}
}