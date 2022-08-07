package yusama125718.potionprotect;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;
import static yusama125718.potionprotect.PotionProtect.*;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {   //コマンド処理
        if (!sender.hasPermission("potp.op")) {         //permission確認
            sender.sendMessage("§c[PotionProtect] You don't have Permission");
            return true;
        }

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "help":        //help
                        sender.sendMessage("§9§l[PotionProtect] §7/potp on/off §rシステムをon/offします");
                        sender.sendMessage("§9§l[PotionProtect] §7/potp add [数字] §r手に持っているアイテムの指定したカスタムモデルデータのポーションへの使用を許可します");
                        sender.sendMessage("§9§l[PotionProtect] §7/potp delete [数字] §r手に持っているアイテムの指定したカスタムモデルデータのポーションへの使用を禁止します");
                        sender.sendMessage("§9§l[PotionProtect] §7/potp check §r手に持っているアイテムの許可されているカスタムモデルデータを確認します");
                        return true;

                    case "check":       //登録確認
                        Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                        if (!allowitem.containsKey(inhand)) {
                            sender.sendMessage("§9§l[PotionProtect] §cそのアイテムは使えません");
                            return true;
                        }
                        sender.sendMessage("===== 許可されているカスタムモデルデータ =====");
                        for (Integer number : allowitem.get(inhand)) {
                            sender.sendMessage(String.valueOf(number));
                        }
                        return true;

                    case "on":      //システムon
                        if (operation) {
                            sender.sendMessage("§9§l[PotionProtect] §cすでに有効になっています");
                            return true;
                        }
                        operation = true;
                        potp.getConfig().set("system", operation);
                        potp.saveConfig();
                        sender.sendMessage("§9§l[PotionProtect] §r有効化しました");
                        return true;

                    case "off":     //システムoff
                        if (!operation) {
                            sender.sendMessage("§9§l[PotionProtect] §cすでに無効になっています");
                            return true;
                        }
                        operation = false;
                        potp.getConfig().set("system", operation);
                        potp.saveConfig();
                        sender.sendMessage("§9§l[PotionProtect] §r無効化しました");
                        return true;

                    default:
                        sender.sendMessage("§9§l[PotionProtect] §7/potp help §rでhelpを表示");
                        return true;
                }

            case 2:
                if (args[0].equals("add")) {     //追加処理
                    Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                    if (!allowitem.containsKey(inhand)) {
                        sender.sendMessage("§9§l[PotionProtect] §cそのアイテムは使えません");
                        return true;
                    }
                    boolean isNumeric = args[1].matches("-?\\d+");
                    if (!isNumeric) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    if (args[1].length() >= 10) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    int addnumber = parseInt(args[1]);
                    if (addnumber < 0) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    if (allowitem.get(inhand).contains(addnumber)) {
                        sender.sendMessage("§9§l[PotionProtect] §cその番号はすでに登録されています");
                        return true;
                    }
                    List<Integer> addlist = allowitem.get(inhand);
                    addlist.add(addnumber);
                    allowitem.replace(inhand, addlist);
                    potp.getConfig().set(inhand.toString(), allowitem.get(inhand));
                    potp.saveConfig();
                    sender.sendMessage("§9§l[PotionProtect] §r追加しました");
                }

                else if (args[0].equals("delete")) {     //削除処理
                    Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                    if (!allowitem.containsKey(inhand)) {
                        sender.sendMessage("§9§l[PotionProtect] §cそのアイテムは使えません");
                        return true;
                    }
                    boolean isNumeric = args[1].matches("-?\\d+");
                    if (!isNumeric) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    if (args[1].length() >= 10) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    int removenumber = parseInt(args[1]);
                    if (removenumber < 0) {
                        sender.sendMessage("§9§l[PotionProtect] §c無効な数字です");
                        return true;
                    }
                    if (!allowitem.get(inhand).contains(removenumber)) {
                        sender.sendMessage("§9§l[PotionProtect] §cその番号は存在しません");
                        return true;
                    }
                    List<Integer> removelist = allowitem.get(inhand);
                    for (int i = 0;i < removelist.size() - 1;i++) {
                        if (removelist.get(i) == removenumber) {
                            removelist.remove(i);
                            break;
                        }
                    }
                    allowitem.replace(inhand, removelist);
                    potp.getConfig().set(inhand.toString(), allowitem.get(inhand));
                    potp.saveConfig();
                    sender.sendMessage("§9§l[PotionProtect] §r削除しました");
                }

                else{
                    sender.sendMessage("§9§l[PotionProtect] §7/potp help §rでhelpを表示");
                    return true;
                }

            default:
                sender.sendMessage("§9§l[PotionProtect] §7/potp help §rでhelpを表示");
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("potp.op")) return null;      //permission確認

        if(command.getName().equalsIgnoreCase("potp")){
            if (args.length == 1){
                if (args[0].length() == 0)
                {
                    return Arrays.asList("add","check","delete","help","off","on");
                }

                else {
                    if ("on".startsWith(args[0])&&"off".startsWith(args[0]))
                    {
                        return Arrays.asList("off","on");
                    }
                    else if ("add".startsWith(args[0]))
                    {
                        return Collections.singletonList("add");
                    }
                    else if ("check".startsWith(args[0]))
                    {
                        return Collections.singletonList("check");
                    }
                    else if ("delete".startsWith(args[0]))
                    {
                        return Collections.singletonList("delete");
                    }
                    else if ("help".startsWith(args[0]))
                    {
                        return Collections.singletonList("help");
                    }
                    else if ("on".startsWith(args[0]))
                    {
                        return Collections.singletonList("on");
                    }
                    else if ("off".startsWith(args[0]))
                    {
                        return Collections.singletonList("off");
                    }
                }
            }
            else if (args.length == 2){
                if (args[0].equals("add")||args[0].equals("delete")){
                    return Collections.singletonList("[カスタムモデルデータ]");
                }
            }
        }
        return null;
    }
}
