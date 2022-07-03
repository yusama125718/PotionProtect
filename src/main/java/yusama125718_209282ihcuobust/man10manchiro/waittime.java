package yusama125718_209282ihcuobust.man10manchiro;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static yusama125718_209282ihcuobust.man10manchiro.Man10Manchiro.*;

public class waittime extends Thread
{
    @Override
    public void run()
    {
        loop: for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                if (playerperson == sitperson)
                {
                    activegame = false;
                    break loop;
                }
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (!(40 - (i +1) * 20 == 0))
            {
                for (Player player: Bukkit.getOnlinePlayers())
                {
                    if (!disableplayers.contains(player.getUniqueId()))
                    {
                        player.sendMessage("§l[§e§lManchiro§f§l]§r§a§l" + Bukkit.getOfflinePlayer(parentname).getName() + "§rが§l一人あたり" + String.format("%,.0f", betvalue) + "§l円で§e§lマンチロ§f§lを子" + playerperson + "人で募集中！ /mcr join で参加しましょう！§7残り" + (40 - (i +1) * 20) + "秒");
                    }
                }
            }
        }
        MySQLManager mysql = new MySQLManager(manchiro,"manchiro");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        starttime = dateFormat.format(date);
        if (playerperson == sitperson)
        {
            Game gamethread = new Game();
            gamethread.start();
            mysql.execute("insert into mcr_data(starttime,betvalue,playercount,parent,parentuuid)values('"+dateFormat.format(date)+"','"+betvalue+"','"+sitperson+"','"+Bukkit.getOfflinePlayer(parentname).getName()+"','"+parentname+"');");
            for (int i = 0;i<sitperson;i++)
            {
                mysql.execute("update mcr_data set child"+i+"='"+Bukkit.getOfflinePlayer(childplayer.get(i)).getName()+"',child"+i+"uuid='"+childplayer.get(i)+"' where starttime='"+starttime+"';");
            }
        }
        else
        {
            mysql.execute("insert into mcr_data(starttime,endtime,betvalue,playercount,parent,parentuuid)values('"+dateFormat.format(date)+"','"+dateFormat.format(date)+"','"+betvalue+"','"+playerperson+"','"+Bukkit.getOfflinePlayer(parentname).getName()+"','"+parentname+"');");
            for (int i = 0;i < sitperson;i++)
            {
                mysql.execute("update mcr_data set child"+i+"='"+Bukkit.getOfflinePlayer(childplayer.get(i)).getName()+"',child"+i+"uuid='"+childplayer.get(i)+"' where starttime='"+starttime+"';");
            }
            operation = false;
            activegame = false;
            vaultapi.deposit(parentname,betvalue * playerperson);
            for (int i = 0;i < sitperson;i++)
            {
                vaultapi.deposit((childplayer.get(i)),betvalue);
            }
            for (Player player: Bukkit.getOnlinePlayers())
            {
                if (!disableplayers.contains(player.getUniqueId()))
                {
                    player.sendMessage("§l[§e§lManchiro§f§l]§r§a§l" + Bukkit.getOfflinePlayer(parentname).getName() + "§rの部屋は人が集まらなかったので解散しました");
                }
            }
            parentname = null;
            childplayer.clear();
            sitperson = 0;
        }
    }
}
