package yusama125718_209282ihcuobust.man10manchiro;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static yusama125718_209282ihcuobust.man10manchiro.Game.*;
import static yusama125718_209282ihcuobust.man10manchiro.Man10Manchiro.*;

public class Finish extends Thread
{
    @Override
    public void run()
    {
        for (Player player: Bukkit.getOnlinePlayers())
        {
            if (!disableplayers.contains(player.getUniqueId()))
            {
                player.sendMessage("§l[§e§lManchiro§f§l]§r§a§l" + Bukkit.getOfflinePlayer(parentname).getName() + "§rの§l部屋が終了しました");
                player.sendMessage("§l[§e§lManchiro§f§l]§r§a§l" + Bukkit.getOfflinePlayer(parentname).getName() + "§r§l: §c§l"+String.format("%,.0f", betvalue * sitperson)+"円 §e§l→ §c§l"+String.format("%,.0f", parentwin)+"円§r§e(うち手数料"+String.format("%,.0f", parentwin - parentprice)+"円)");
            }
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MySQLManager mysql = new MySQLManager(manchiro,"manchiro");
        mysql.execute("update mcr_data set endtime='"+dateFormat.format(date)+"',tax='"+taxprice+"',parentyaku='"+parentyaku+"',parentwin='"+parentprice+"' where starttime='"+starttime+"';");
        for (int i=0;i<sitperson;i++)
        {
            mysql.execute("update mcr_data set child"+i+"yaku='"+childyaku[i]+"',child"+i+"win='"+childprice[i]+"' where starttime='"+starttime+"';");
        }
        jackpot = manchiro.getConfig().getDouble("jackpot");
        jackpot = jackpot + taxprice;
        manchiro.getConfig().set("jackpot",jackpot);
        manchiro.saveConfig();
        operation = false;
        sitperson = 0;
        parentname = null;
        childplayer.clear();
        taxprice = 0;
        for (int i= 0;i<sitperson;i++)
        {
            childyaku[i] = 0;
        }
        sitperson = 0;
        parentprice = 0;
        parentwin = 0;
    }
}
