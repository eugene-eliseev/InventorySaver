package ru.greatray.invsaver;

import me.dpohvar.powernbt.nbt.NBTContainerEntity;
import me.limito.bukkit.jsonnbt.NBTToJson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin implements Listener {

    static ThreadQueue thread = new ThreadQueue();

    static class ThreadQueue extends Thread{

        ThreadQueue(){

        }
        boolean started = false;
        LinkedBlockingDeque<PacketData> linkedBlockingDeque = new LinkedBlockingDeque();

        public void addData(String res){
            linkedBlockingDeque.addLast(new PacketData(res, System.currentTimeMillis()));
            if(!started) {
                started = true;
                thread.start();
            }
        }

        @Override
        public void run() {
            while(true) {
                try {
                    PacketData packetData = linkedBlockingDeque.pollFirst(1000, TimeUnit.HOURS);
                    if(packetData == null)
                        continue;
                    PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("inventory_saver/" + packetData.time / 1000000 + ".txt", true)), true);
                    out.println(packetData.time % 1000000 + "," + packetData.res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PacketData{
        String res;
        long time;

        public PacketData(String res, long time) {
            this.res = res;
            this.time = time;
        }
    }

    public void onEnable() {
        getLogger().info("InvSaver Started");
        Bukkit.getPluginManager().registerEvents(this, this);
        new SaveTime(this).runTaskTimer(this, 0L, 20L * 10L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player f = event.getEntity();
        NBTContainerEntity nbt = new NBTContainerEntity(f);
        String res = NBTToJson.encode(nbt.readTag());
        thread.addData(res);
    }
}
