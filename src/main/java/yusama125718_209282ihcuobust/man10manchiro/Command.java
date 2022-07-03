package yusama125718_209282ihcuobust.man10manchiro;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args)
    {
        if(command.getName().equalsIgnoreCase("mcr"))
        {
            if (args.length == 1)
            {
                if (args[0].length() == 0)
                {
                    if (sender.hasPermission("mce.op"))
                    {
                        return Arrays.asList("help","hide","join","off","on","show","start");
                    }
                }
                else
                {
                    if ("on".startsWith(args[0])&&"off".startsWith(args[0]))
                    {
                        if (sender.hasPermission("mce.op"))
                        {
                            return Arrays.asList("off","on");
                        }
                    }
                    else if ("on".startsWith(args[0]))
                    {
                        if (sender.hasPermission("mcr.op"))
                        {
                            return Collections.singletonList("on");
                        }
                    }
                    else if ("off".startsWith(args[0]))
                    {
                        if (sender.hasPermission("mcr.op"))
                        {
                            return Collections.singletonList("off");
                        }
                    }
                    else if ("help".startsWith(args[0])&&"hide".startsWith(args[0]))
                    {
                        return Arrays.asList("help","hide");
                    }
                    else if ("help".startsWith(args[0]))
                    {
                        return Collections.singletonList("help");
                    }
                    else if ("hide".startsWith(args[0]))
                    {
                        return Collections.singletonList("hide");
                    }
                    else if ("join".startsWith(args[0]))
                    {
                        return Collections.singletonList("join");
                    }
                    else if ("show".startsWith(args[0])&&"start".startsWith(args[0]))
                    {
                        return Arrays.asList("show","start");
                    }
                    else if ("show".startsWith(args[0]))
                    {
                        return Collections.singletonList("show");
                    }
                    else if ("start".startsWith(args[0]))
                    {
                        return Collections.singletonList("start");
                    }
                }
            }
            if (args.length == 2)
            {
                if (args[0].equals("start"))
                {
                    return Collections.singletonList("<bet額>");
                }
            }
            if (args.length == 3)
            {
                if (args[0].equals("start"))
                {
                    return Collections.singletonList("<人数>");
                }
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return true;
    }
}
