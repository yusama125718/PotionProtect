package yusama125718_209282ihcuobust.man10manchiro;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static java.lang.Integer.parseInt;

public final class Man10Manchiro extends JavaPlugin implements CommandExecutor, TabCompleter
{
    public static UUID parentname;
    public static double betvalue;
    static boolean operation = false;
    static boolean activegame = false;
    static boolean ongame = false;
    static public List<UUID> disableplayers=new ArrayList<>();
    private OfflinePlayer playerid;
    static int playerperson;
    static int sitperson = 0;
    static public List<UUID> childplayer = new ArrayList<>();
    public static JavaPlugin manchiro;
    public static VaultAPI vaultapi;
    public static String starttime;

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        this.manchiro = this;
        saveDefaultConfig();
        MySQLManager mysql = new MySQLManager(manchiro,"manchiro");
        mysql.execute("create table if not exists mcr_data(id int auto_increment,starttime varchar(19),endtime varchar(19),betvalue varchar(15),playercount varchar(1),tax varchar(15),parent varchar(50),parentuuid varchar(50),parentyaku varchar(3),parentwin varchar(15),child0 varchar(50),child0uuid varchar(50),child0yaku varchar(3),child0win varchar(15),child1 varchar(50),child1uuid varchar(50),child1yaku varchar(3),child1win varchar(15),child2 varchar(50),child2uuid varchar(50),child2yaku varchar(3),child2win varchar(15),child3 varchar(50),child3uuid varchar(50),child3yaku varchar(3),child3win varchar(15),child4 varchar(50),child4uuid varchar(50),child4yaku varchar(3),child4win varchar(15),primary key(id));");
        vaultapi = new VaultAPI();
        ongame = manchiro.getConfig().getBoolean("canPlay");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(("§c[manchiro]Player以外は実行できません"));
            return true;
        }
        Player playerid = (Player) sender;
        if (args.length == 1)
        {
            if (args[0].equals("help"))
            {
                double jackpot = manchiro.getConfig().getDouble("jackpot");
                if (sender.hasPermission("mcr.op"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr on : §lマンチロをonにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr off : §lマンチロをoffにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                else if (sender.hasPermission("mcr.father"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr hide : §l非表示にします");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr show : §l表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr yaku : §l役の説明を表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr join : §l現在立っている部屋に入ります");
                sender.sendMessage("§l[§e§lManchiro§f§l] §eJackPot : §f"+ String.format("%,.0f", jackpot) +"円");
                return true;
            }
            if (args[0].equals("hide"))
            {
                if (disableplayers.contains(playerid.getUniqueId()))
                {
                    return true;
                }
                else
                {
                    disableplayers.add(playerid.getUniqueId());
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§7§l非表示にします");
                    return true;
                }
            }

            if (args[0].equals("show"))
            {
                if (disableplayers.contains(playerid.getUniqueId()))
                {
                    disableplayers.remove(playerid.getUniqueId());
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§7§l表示します");

                    return true;
                }
                else
                {
                    return true;
                }
            }
        }
        if (!(sender.hasPermission("mcr.player")))
        {
            sender.sendMessage(("§c[manchiro]You don't have permissions!"));
            return true;
        }
        switch (args.length)
        {
            case 1:
            {
                if (sender.hasPermission("mcr.op"))
                {
                    if (args[0].equals("on"))
                    {
                        ongame = true;
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§lonにしました");
                        return true;
                    }
                    if (args[0].equals("off"))
                    {
                        ongame = false;
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§loffにしました");
                        return true;
                    }
                }
                if (args[0].equals("join"))
                {
                    if (!activegame)
                    {
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§l人数がいっぱいかゲームが開かれていません");
                        return true;
                    }
                    if (sitperson == playerperson)
                    {
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§l人数がいっぱいかゲームが開かれていません");
                        return true;
                    }
                    if (((Player) sender).getUniqueId().equals(parentname)||childplayer.contains(((Player) sender).getUniqueId()))
                    {
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§lあなたはすでに参加しています！");
                        return true;
                    }
                    if (!(vaultapi.getBalance(((Player) sender).getUniqueId()) > betvalue))
                    {
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§l所持金が足りません！");
                        return true;
                    }
                    if (disableplayers.contains(playerid.getUniqueId()))
                    {
                        disableplayers.remove(playerid.getUniqueId());
                        sender.sendMessage("§l[§e§lManchiro§f§l]§r§7§l表示します");
                    }
                    vaultapi.withdraw(((Player) sender).getUniqueId(),betvalue);
                    childplayer.add(((Player) sender).getUniqueId());
                    for (Player player: Bukkit.getOnlinePlayers())
                    {
                        if (childplayer.contains(player.getUniqueId()))
                        {
                            player.sendMessage("§l[§e§lManchiro§f§l]§r§b§l" + sender.getName() + "§rが§l部屋に入りました。");
                        }
                        if(player.getUniqueId() == parentname)
                        {
                            player.sendMessage("§l[§e§lManchiro§f§l]§r§b§l" + sender.getName() + "§rが§l部屋に入りました。");
                        }
                    }
                    sitperson = sitperson + 1;
                    return true;
                }
                if (args[0].equals("yaku"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §b§l役説明(強い順)");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §r1,1,1 : §7JackPot全額かかけ金の10倍の低いほうが払われます");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §r6,6,6 : §7子も親も全額没収され、JackPotに入ります");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §rX,X,X : §7勝てばかけ金の3倍もらえます");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §rX,Y,Z(X+Y+Z=10) : §7かけ金の2倍が貰えます");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §r4,5,6 : §7かけ金の2倍が貰えます");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §rX,X,Y : §7Yが役の強さとなり、勝てばかけ金の2倍が貰えます");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §rX,Y,Z(X+Y+Z=5) : §7かけ金の2倍を失います");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §r1,2,3 : §7かけ金の2倍を失います");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §rションベン : §7低確率で発生し、かけ金の1倍を失います");
                    return true;
                }
                double jackpot = manchiro.getConfig().getDouble("jackpot");
                if (sender.hasPermission("mcr.op"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr on : §lマンチロをonにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr off : §lマンチロをoffにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                else if (sender.hasPermission("mcr.father"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr hide : §l非表示にします");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr show : §l表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr yaku : §l役の説明を表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr join : §l現在立っている部屋に入ります");
                sender.sendMessage("§l[§e§lManchiro§f§l] §eJackPot : §f"+ String.format("%,.0f", jackpot) +"円");
                return true;
            }
            case 3:
            {
                if (!(args[0].equals("start")))
                {
                    sender.sendMessage(("§l[§e§lManchiro§f§l]/mcr help でコマンドを確認できます"));
                    return true;
                }
                if (!(sender.hasPermission("mcr.parent")))
                {
                    sender.sendMessage(("§c[manchiro]You don't have permissions!"));
                    return true;
                }
                if (!ongame)
                {
                    sender.sendMessage(("§l[§e§lManchiro§f§l]現在マンチロはoffです"));
                    return true;
                }
                if (operation)
                {
                    sender.sendMessage(("§l[§e§lManchiro§f§l]現在ゲーム中です"));
                    return true;
                }
                boolean isNumeric = args[1].matches("-?\\d+");
                if (!isNumeric)
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§lかけ金は数字にしてください");
                    return true;
                }
                boolean isNumeric1 = args[2].matches("-?\\d+");
                if (!isNumeric1)
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§l人数は数字にしてください");
                    return true;
                }
                betvalue = Double.parseDouble(args[1]);
                double minrate = manchiro.getConfig().getDouble("minRate");
                if (!(betvalue >= minrate))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§lかけ金は"+ minrate +"円以上にしてください");
                    return true;
                }
                double maxrate = manchiro.getConfig().getDouble("maxRate");
                if (!(betvalue <= maxrate))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§lかけ金は"+ maxrate +"円以下にしてください");
                    return true;
                }
                playerperson = parseInt(args[2]);
                if (!(playerperson >= 1))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§l人数は1人以上にしてください");
                    return true;
                }
                if (!(playerperson <= 5))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§l人数は5人以下にしてください");
                    return true;
                }
                if (!(vaultapi.getBalance(((Player) sender).getUniqueId()) > betvalue * playerperson))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§l所持金が足りません！");
                    return true;
                }
                operation = true;
                activegame = true;
                vaultapi.withdraw(((Player) sender).getUniqueId(),betvalue * playerperson);

                waittime wait = new waittime();
                wait.start();

                if (disableplayers.contains(playerid.getUniqueId()))
                {
                    disableplayers.remove(playerid.getUniqueId());
                    sender.sendMessage("§l[§e§lManchiro§f§l]§r§7§l表示します");
                }
                for (Player player: Bukkit.getOnlinePlayers())
                {
                    if (!disableplayers.contains(player.getUniqueId()))
                    {
                        player.sendMessage("§l[§e§lManchiro§f§l]§r§a§l" + sender.getName() + "§rが§l一人あたり" + String.format("%,.0f", betvalue) + "§l円で§e§lマンチロ§f§lを子" + playerperson + "人で開始しました！ /mcr join で参加しましょう！");
                    }
                }
                parentname = ((Player) sender).getUniqueId();
                return true;
            }
            default:
            {
                double jackpot = manchiro.getConfig().getDouble("jackpot");
                if (sender.hasPermission("mcr.op"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr on : §lマンチロをonにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr off : §lマンチロをoffにします");
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                else if (sender.hasPermission("mcr.father"))
                {
                    sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr start [かけ金] [人数] : §l部屋を立て、親となります(かけ金を人数分支払います)");
                }
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr hide : §l非表示にします");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr show : §l表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr yaku : §l役の説明を表示します");
                sender.sendMessage("§l[§e§lManchiro§f§l] §7/mcr join : §l現在立っている部屋に入ります");
                sender.sendMessage("§l[§e§lManchiro§f§l] §eJackPot : §f"+ jackpot +"円");
                return true;
            }
        }
    }

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
    public void onDisable() {
        // Plugin shutdown logic
    }
}
