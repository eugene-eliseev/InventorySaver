package ru.greatray.invsaver;

import me.dpohvar.powernbt.nbt.NBTContainerEntity;
import me.limito.bukkit.jsonnbt.NBTToJson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTime extends BukkitRunnable
{
    private final Main plugin;

    public SaveTime(Main plugin)
    {
        this.plugin = plugin;
    }

    public void run()
    {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            NBTContainerEntity nbt = new NBTContainerEntity(p);
            String res = NBTToJson.encode(nbt.readTag());
            Main.thread.addData(res);
        }
    }
}

