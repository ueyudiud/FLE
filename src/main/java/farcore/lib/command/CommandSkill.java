/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.command;

import static nebula.common.LanguageManager.registerLocal;

import farcore.lib.skill.ISkill;
import nebula.common.util.EnumChatFormatting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * @author ueyudiud
 */
public class CommandSkill extends CommandBase
{
	public static void addCommandInformations()
	{
		registerLocal("commands.skill.help", "/skill help");
		registerLocal("commands.skill.help.1", "/skill help %sGet skill command info.", EnumChatFormatting.YELLOW.toString());
		registerLocal("commands.skill.help.2", "/skill get [player] [skillname] %sGet player skill state.", EnumChatFormatting.YELLOW.toString());
		registerLocal("commands.skill.help.3", "/skill set [player] [skillname] [lvl] %sSet player skill level, only can used by creative player or OP.", EnumChatFormatting.YELLOW.toString());
		registerLocal("commands.skill.help.4", "/skill list %sGet skill list.", EnumChatFormatting.YELLOW.toString());
		registerLocal("commands.skill.get.player.notexist.err", "The player %s, does not exist.");
		registerLocal("commands.skill.get.onlyusablebyplayer.err", "This command can only used by player.");
		registerLocal("commands.skill.get.skill.notexist.err", "The skill %s, does not exist");
		registerLocal("commands.skill.set.succeed", "Set skill level succeed.");
	}
	
	@Override
	public String getName()
	{
		return "skill";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.skill.help";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0)
			throw new CommandException("commands.skill.help");
		else
		{
			try
			{
				switch (args[0])
				{
				case "help":
				case "h":
				case "?":
					notifyCommandListener(sender, this, "commands.skill.help.1");
					notifyCommandListener(sender, this, "commands.skill.help.2");
					notifyCommandListener(sender, this, "commands.skill.help.3");
					notifyCommandListener(sender, this, "commands.skill.help.4");
					break;
				case "list":
				case "l":
					notifyCommandListener(sender, this, ISkill.REGISTER.toString());
					break;
				case "get":
				case "g":
					if (args.length > 3) throw new CommandException("commands.skill.help.2");
					int i;
					EntityPlayer player;
					if (args.length == 3)
					{
						i = 2;
						player = server.getPlayerList().getPlayerByUsername(args[2]);
						if (player == null) throw new CommandException("commands.skill.get.player.notexist.err", args[2]);
					}
					else
					{
						i = 1;
						if (!(sender instanceof ICommandSender)) throw new CommandException("commands.skill.get.onlyusablebyplayer.err");
						player = (EntityPlayer) sender.getCommandSenderEntity();
					}
					ISkill skill = ISkill.REGISTER.get(args[i]);
					if (skill == null) throw new CommandException("commands.skill.get.skill.notexist.err", args[i]);
					notifyCommandListener(sender, this, skill.getSkillInfo(player));
					break;
				case "set":
				case "s":
					if (sender.canUseCommand(2, ""))
					{
						if (args.length > 4 || args.length == 1) throw new CommandException("commands.skill.help.3");
						if (args.length == 4)
						{
							i = 2;
							player = server.getPlayerList().getPlayerByUsername(args[2]);
							if (player == null) throw new CommandException("commands.skill.get.player.notexist.err", args[2]);
						}
						else
						{
							i = 1;
							if (!(sender instanceof ICommandSender)) throw new CommandException("commands.skill.get.onlyusablebyplayer.err");
							player = (EntityPlayer) sender.getCommandSenderEntity();
						}
						skill = ISkill.REGISTER.get(args[i]);
						if (skill == null) throw new CommandException("commands.skill.get.skill.notexist.err", args[i]);
						int level;
						try
						{
							level = Integer.parseInt(args[i + 1]);
						}
						catch (Exception exception)
						{
							throw new CommandException("Can not covert %s as a integer.", args[i + 1]);
						}
						skill.set(player, level);
						notifyCommandListener(sender, this, "commands.skill.set.succeed");
						break;
					}
				default:
					throw new CommandException("commands.skill.help");
				}
			}
			catch (RuntimeException exception)
			{
				notifyCommandListener(sender, this, exception.getClass().toString());
			}
		}
	}
}
