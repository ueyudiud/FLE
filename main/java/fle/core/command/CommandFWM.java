package fle.core.command;

import java.lang.reflect.Array;
import java.util.Arrays;

import flapi.world.BlockPos;
import fle.FLE;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandFWM extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "fwm";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/fwm help\n"
				+ "Use to search more command.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] strings)
	{
		if(strings.length == 0)
		{
			displayHelp(sender);
		}
		else if("help".equals(strings[0]))
		{
			displayHelp(sender);
		}
		else if("set".equals(strings[0]))
		{
			if(strings.length != 6)
			{
				sender.addChatMessage(new ChatComponentText("/fwm set <x> <y> <z> <type> <value>"));
				sender.addChatMessage(new ChatComponentText("Set fwm metadata at this dimention coordinates"));
			}
			else
			{
				try
				{
					FLE.fle.getWorldManager().setData(
							new BlockPos(sender.getEntityWorld(), 
									Integer.valueOf(strings[1]), 
									Integer.valueOf(strings[2]), 
									Integer.valueOf(strings[3])), 
							Integer.valueOf(strings[4]), Integer.valueOf(strings[5]));
				}
				catch(Throwable throwable)
				{
					sender.addChatMessage(new ChatComponentText("/fwm set <x> <y> <z> <type> <value>"));
					sender.addChatMessage(new ChatComponentText("Set fwm metadata at this dimention coordinates"));
				}
			}
		}
		else if("get".equals(strings[0]))
		{
			if(strings.length == 4)
			{
				try
				{
					short[] value = FLE.fle.getWorldManager().getDatas(
							new BlockPos(sender.getEntityWorld(), 
									Integer.valueOf(strings[1]), 
									Integer.valueOf(strings[2]), 
									Integer.valueOf(strings[3])));
					sender.addChatMessage(new ChatComponentText("Meta : " + Arrays.toString(value)));
				}
				catch(Throwable throwable)
				{
					sender.addChatMessage(new ChatComponentText("/fwm get <x> <y> <z>"));
					sender.addChatMessage(new ChatComponentText("Get all fwm metadata at this dimention with coordinates."));
				}
			}
			else if(strings.length == 5)
			{
				try
				{
					short value = FLE.fle.getWorldManager().getData(
							new BlockPos(sender.getEntityWorld(), 
									Integer.valueOf(strings[1]), 
									Integer.valueOf(strings[2]), 
									Integer.valueOf(strings[3])), 
							Integer.valueOf(strings[4]));
					sender.addChatMessage(new ChatComponentText("Meta : " + value));
				}
				catch(Throwable throwable)
				{
					sender.addChatMessage(new ChatComponentText("/fwm get <x> <y> <z> [type]"));
					sender.addChatMessage(new ChatComponentText("Get fwm metadata at this dimention with coordinates (Remain empty type will return all meta)."));
				}
			}
			else
			{
				sender.addChatMessage(new ChatComponentText("/fwm get <x> <y> <z> [type]"));
				sender.addChatMessage(new ChatComponentText("Get fwm metadata at this dimention with coordinates (Remain empty type will return all meta)."));
			}
		}
		else if("remove".equals(strings))
		{
			if(strings.length == 4)
			{
				try
				{
					FLE.fle.getWorldManager().removeData(
							new BlockPos(sender.getEntityWorld(), 
									Integer.valueOf(strings[1]), 
									Integer.valueOf(strings[2]), 
									Integer.valueOf(strings[3])));
				}
				catch(Throwable throwable)
				{
					sender.addChatMessage(new ChatComponentText("/fwm remove <x> <y> <z>"));
					sender.addChatMessage(new ChatComponentText("Remove all fwm metadata at this dimention with coordinates."));
				}
			}
			else if(strings.length == 5)
			{
				try
				{
					FLE.fle.getWorldManager().removeData(
							new BlockPos(sender.getEntityWorld(), 
									Integer.valueOf(strings[1]), 
									Integer.valueOf(strings[2]), 
									Integer.valueOf(strings[3])), 
							Integer.valueOf(strings[4]));
				}
				catch(Throwable throwable)
				{
					sender.addChatMessage(new ChatComponentText("/fwm remove <x> <y> <z> [type]"));
					sender.addChatMessage(new ChatComponentText("Remove fwm metadata at this dimention with coordinates (Remain empty type will remove all meta)."));
				}
			}
			else
			{
				sender.addChatMessage(new ChatComponentText("/fwm remove <x> <y> <z> [type]"));
				sender.addChatMessage(new ChatComponentText("Remove fwm metadata at this dimention with coordinates (Remain empty type will remove all meta)."));
			}
		}
		else
		{
			displayHelp(sender);
		}
	}
	
	protected void displayHelp(ICommandSender sender)
	{
		sender.addChatMessage(new ChatComponentText("/fwm set <x> <y> <z> <type> <value>"));
		sender.addChatMessage(new ChatComponentText("Set fwm metadata at this dimention with coordinates."));
		sender.addChatMessage(new ChatComponentText("/fwm get <x> <y> <z> [type]"));
		sender.addChatMessage(new ChatComponentText("Get fwm metadata at this dimention with coordinates (Remain empty type will return all meta)."));
		sender.addChatMessage(new ChatComponentText("/fwm remove <x> <y> <z> [type]"));
		sender.addChatMessage(new ChatComponentText("Remove fwm metadata at this dimention with coordinates (Remain empty type will remove all meta)."));
	}
}