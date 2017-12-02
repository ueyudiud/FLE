/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.command;

import farcore.lib.world.CalendarHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class CommandDate extends CommandBase
{
	@Override
	public String getName()
	{
		return "date";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.date.usage";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 0) throw new CommandException("commands.date.arg.err");
		World world = server.getEntityWorld();
		notifyCommandListener(sender, this, CalendarHandler.getDateInfo(world));
	}
}
