package cn.iqianye.MinecraftPlugins.AT;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    public final Logger logger = Logger.getLogger("Minecraft");
    public static Main plugin;
    public final HashMap<Player, Boolean> at = new HashMap<Player, Boolean>();
    public final List<Player> cooldown = new ArrayList<Player>();
    public String prefix = ChatColor.AQUA + "[@] " + ChatColor.YELLOW + ">>> ";

    @Override
    public void onEnable() {
        getLogger().info(prefix + ChatColor.GREEN + "正在加载中....");
        getLogger().info(prefix + ChatColor.GREEN + "插件魔改: 真心");
        getLogger().info(prefix + ChatColor.GREEN + "QQ: 1307993674");
        getLogger().info(prefix + ChatColor.GREEN + "原作者: Brian & fyfly & nFeng & connection_lost | Bryan_lzh");
        getServer().getPluginManager().registerEvents(this, this);
        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration
                .loadConfiguration(configFile);
        if (!configFile.exists()) {
            config.set("sound", 1);
            config.set("cooldown", 10);
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getLogger().info(prefix + ChatColor.GREEN + "插件已加载完成!");
    }

    @Override
    public void onDisable() {
        getLogger().info(prefix + ChatColor.GREEN + "正在卸载中....");
        getLogger().info(prefix + ChatColor.GREEN + "插件魔改: 真心");
        getLogger().info(prefix + ChatColor.GREEN + "QQ: 1307993674");
        getLogger().info(prefix + ChatColor.GREEN + "原作者: Brian & fyfly & nFeng & connection_lost | Bryan_lzh");
        getLogger().info(prefix + ChatColor.GREEN + "插件已卸载完成!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable,
                             String[] args) {
        if (lable.equalsIgnoreCase("at")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage(prefix + ChatColor.GREEN
                            + "AT-@插件 使用帮助 魔改By: 真心");
                    player.sendMessage(prefix + ChatColor.GREEN + "/at help : 提示");
                    player.sendMessage(prefix + ChatColor.GREEN + "/at disable : 打开免打扰功能");
                    player.sendMessage(prefix + ChatColor.GREEN + "/at enable : 关闭免打扰功能");
                    return true;
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        player.sendMessage(prefix + ChatColor.GREEN
                                + "Help:请在输入人名字之后输入空格或者按tab补全");
                        return true;
                    } else if (args[0].equalsIgnoreCase("disable")) {
                        player.sendMessage(prefix + ChatColor.GREEN + "你打开了免打扰功能");
                        at.put(player, false);
                        return true;
                    } else if (args[0].equalsIgnoreCase("enable")) {
                        player.sendMessage(prefix + ChatColor.GREEN + "你关闭了免打扰功能");
                        at.put(player, true);
                        return true;
                    }
                }
            } else {
                sender.sendMessage("[@] >>> 只有玩家才能用@");
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void chat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        final Player player = event.getPlayer();
        int sum = 0;
        if (msg.contains("@")) {
            if (!cooldown.contains(player)) {
                List<Player> ap = new ArrayList<Player>();
                ap.addAll(Bukkit.getServer().getOnlinePlayers());
                int sound = getConfig().getInt("sound");
                int cooldownsec = getConfig().getInt("cooldown");
                if (player.hasPermission("at.cooldown") || player.isOp()) {
                    cooldownsec = 0;
                }
                if (msg.contains("@全体玩家")) {
                    if (player.hasPermission("at.use.all") || player.isOp()) {
                        for (Player target : ap) {
                            if (!(at.containsKey(target) && at.get(target) == false)) {
                                target.sendMessage(ChatColor.AQUA + "[@] "
                                        + ChatColor.YELLOW + ">>> "
                                        + ChatColor.GREEN
                                        + player.getDisplayName()
                                        + ChatColor.AQUA + " @了你");
                                target.sendTitle(ChatColor.AQUA
                                                + "有全体消息",
                                        ChatColor.AQUA + player.getDisplayName()
                                                + ChatColor.AQUA
                                                + " @了全体成员");
                                switch (sound) {
                                    case 1:
                                        target.playSound(target.getLocation(),
                                                Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        break;
                                    case 2:
                                        target.playSound(target.getLocation(),
                                                Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                                        break;
                                    case 3:
                                        target.playSound(target.getLocation(),
                                                Sound.ENTITY_GHAST_SCREAM, 1.0F, 1.0F);
                                        break;
                                    case 4:
                                        target.playSound(target.getLocation(),
                                                Sound.ENTITY_FIREWORK_TWINKLE, 1.0F, 1.0F);
                                        break;
                                    case 5:
                                        target.playSound(target.getLocation(),
                                                Sound.ENTITY_WITHER_DEATH, 1.0F, 1.0F);
                                        break;
                                    default:
                                        player.sendMessage(prefix + ChatColor.GREEN
                                                + "请联系op,@插件配置有误");
                                        break;
                                }
                            }
                        }

                        msg = msg.replaceAll("@全体玩家", ChatColor.AQUA + "@全体玩家"
                                + ChatColor.RESET);
                        sum++;
                    } else {
                        player.sendMessage(prefix + ChatColor.GREEN + "你没有权限@全体玩家！");
                    }
                } else {
                    if (player.hasPermission("at.use") || player.isOp()) {
                        for (Player target : ap) {
                            if (msg.endsWith("@" + target.getName())
                                    || msg.contains("@" + target.getName()
                                    + " ")) {
                                msg = msg.replaceAll("@" + target.getName(),
                                        ChatColor.AQUA + "@" + target.getName()
                                                + ChatColor.RESET);
                                if (!(at.containsKey(target) && at.get(target) == false)) {
                                    target.sendMessage(ChatColor.AQUA + "[@] "
                                            + ChatColor.YELLOW + ">>> "
                                            + ChatColor.GREEN
                                            + player.getDisplayName()
                                            + ChatColor.AQUA + " @了你");
                                    target.sendTitle(ChatColor.AQUA
                                                    + "有人@你",
                                            ChatColor.AQUA + player.getDisplayName()
                                                    + ChatColor.AQUA
                                                    + " @了你");
                                    switch (sound) {
                                        case 1:
                                            target.playSound(target.getLocation(),
                                                    Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                            break;
                                        case 2:
                                            target.playSound(target.getLocation(),
                                                    Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                                            break;
                                        case 3:
                                            target.playSound(target.getLocation(),
                                                    Sound.ENTITY_GHAST_SCREAM, 1.0F, 1.0F);
                                            break;
                                        case 4:
                                            target.playSound(target.getLocation(),
                                                    Sound.ENTITY_FIREWORK_TWINKLE, 1.0F, 1.0F);
                                            break;
                                        case 5:
                                            target.playSound(target.getLocation(),
                                                    Sound.ENTITY_WITHER_DEATH, 1.0F, 1.0F);
                                            break;
                                        default:
                                            player.sendMessage(prefix + ChatColor.GREEN
                                                    + "请联系op,@插件配置有误");
                                            break;
                                    }
                                }
                            }
                            sum++;
                        }
                        if (sum == 0) {
                            player.sendMessage(prefix + ChatColor.GREEN
                                    + "玩家不在线或不存在");
                        }
                    } else {
                        player.sendMessage(prefix + ChatColor.GREEN + "你没有权限使用@");
                    }
                }

                if (sum != 0) {
                    event.setMessage(msg);
                    cooldown.add(player);
                    Bukkit.getServer().getScheduler()
                            .scheduleSyncDelayedTask(this, new Runnable() {
                                public void run() {
                                    cooldown.remove(player);
                                }
                            }, cooldownsec * 20);

                }
            } else {
                int cooldownsec = getConfig().getInt("cooldown");
                player.sendMessage(prefix + ChatColor.GREEN + "冷却中，冷却时间为"
                        + cooldownsec + "秒");
            }
        }
    }

    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent event) {
        String uncomplete = event.getChatMessage();
        if (uncomplete.contains("@") && !uncomplete.endsWith(" ")) {
            List<Player> allplayers = new ArrayList<Player>();
            allplayers.addAll(Bukkit.getServer().getOnlinePlayers());
            String at = uncomplete.substring(uncomplete.lastIndexOf("@") + 1);
            List<String> fit = new ArrayList<String>();
            for (Player p : allplayers) {
                if (p.getName().toLowerCase().startsWith(at.toLowerCase())) {
                    if (uncomplete.contains(" ")) {
                        fit.add(uncomplete.substring(
                                uncomplete.lastIndexOf(" ") + 1,
                                uncomplete.lastIndexOf("@"))
                                + "@" + p.getName());
                        // event.getPlayer().sendMessage(ChatColor.RED+event.getTabCompletions().toString()+ChatColor.GREEN+event.getLastToken());
                    } else {
                        fit.add(uncomplete.substring(0,
                                uncomplete.lastIndexOf("@"))
                                + "@" + p.getName());
                        // event.getPlayer().sendMessage(ChatColor.RED+event.getTabCompletions().toString()+ChatColor.GREEN+event.getLastToken());
                    }
                }
            }
            event.getTabCompletions().clear();
            event.getTabCompletions().addAll(fit);
            // event.getPlayer().sendMessage(ChatColor.RED+event.getTabCompletions().toString()+ChatColor.GREEN+event.getLastToken());
        }
    }

}
