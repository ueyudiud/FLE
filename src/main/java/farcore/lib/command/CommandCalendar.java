package farcore.lib.command;

import farcore.util.CalendarHandler;
import farcore.util.U;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandCalendar extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "date";
	}

    /**
     * Return the required permission level for this command.
     */
	@Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/date help Get help of calendar handler.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] strings)
	{
		if("help".equals(strings[0]))
		{
			sender.addChatMessage(new ChatComponentTranslation("/date get Get date for this world."));
		}
		if(strings.length == 1)
		{
			if("get".equals(strings[0]))
			{
				World world = sender.getEntityWorld();
				sender.addChatMessage(new ChatComponentTranslation(U.Time.getDateDescription(world, CalendarHandler.getCalendar(world))));
			}
		}
		else
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}
}