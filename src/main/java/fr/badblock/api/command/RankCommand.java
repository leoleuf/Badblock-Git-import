package fr.badblock.api.command;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.chat.ChatUtilities;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.data.rank.RankBean;
import fr.badblock.api.data.rank.RankData;
import fr.badblock.api.utils.CenteredMessage;
import fr.badblock.api.utils.TeamTag;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankCommand implements CommandExecutor {
    private HashMap<Integer, List<String>> pages;
    private BadBlockAPI badBlockAPI;
    /** Get Main Class **/
    public RankCommand(BadBlockAPI badBlockAPI) {
        this.badBlockAPI = badBlockAPI;
        pages = new HashMap<>();
    }
    /** Ranks command **/
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        int asize = args.length;
        if(player.hasPermission("*") || player.hasPermission("admin.rank")){
        if (asize == 0) {
            sendHelp(player, 1);
        }else if (asize == 1){
            if(args[0].equalsIgnoreCase("reload")){
                badBlockAPI.getRankManager().cache.values().forEach(RankData::refreshData);

            }
        } else if (asize == 2) {
            if (args[0].equalsIgnoreCase("getperm")) {
                String rankName = args[1];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                if (rankData == null) {
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                    return false;
                } else {
                    rankData.getPermissions().forEach(perm -> player.sendMessage("§f" + perm + ","));
                }
            }
        } else if (asize == 3) {
            if (args[0].equalsIgnoreCase("addperm")) {
                String rankName = args[1];
                String perm = args[2];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                if (rankData == null) {
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                    return false;
                } else {
                    rankData.addPermissions(perm);
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.setperm")));
                }
            } else if (args[0].equalsIgnoreCase("delperm")) {
                String rankName = args[1];
                String perm = args[2];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                if (rankData == null) {
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                    return false;
                } else {
                    rankData.removePermission(perm);
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.delperm")));
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                String playerName = args[1];
                String rank = args[2];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rank);
                if (rankData == null) {
                    player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                    return false;
                } else {
                    PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(playerName);
                    playerData.setRankID(rankData.getRankID());
                    RankData rankData1 = badBlockAPI.getRankManager().getRankData(playerData.getRankID());
                    try {
                        TeamTag teamTag = new TeamTag(rankData1.getRankName(), rankData1.getRankTag(), rankData1.getRankSuffix());
                        teamTag.set(player);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        } else if (asize == 4) {
            if (args[0].equalsIgnoreCase("modify")) {
                if (args[1].equalsIgnoreCase("power")) {
                    String rankName = args[2];
                    int power = Integer.valueOf(args[3]);
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                        return false;
                    } else {
                        rankData.setPower(power);
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.setpower")));
                    }
                } else if (args[1].equalsIgnoreCase("tag")) {
                    String rankName = args[2];
                    String tag = args[3];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                        return false;
                    } else {
                        rankData.setTag(tag);
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.settag")));
                    }
                } else if (args[1].equalsIgnoreCase("prefix")) {
                    String rankName = args[2];
                    String prefix = args[3];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                        return false;
                    } else {
                        rankData.setPrefix(prefix);
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.setprefix")));
                    }
                } else if (args[1].equalsIgnoreCase("suffix")) {
                    String rankName = args[2];
                    String suffix = args[3];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.noexist")));
                        return false;
                    } else {
                        rankData.setSuffix(suffix);
                        player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.setsuffix")));
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            String rankName = args[1];
            int rankPower = args[2] != null ? Integer.valueOf(args[2]) : 0;
            String rankTag = args[3];
            String rankPrefix = args[4];
            String rankSuffix = args[5];

            RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
            if (rankData != null) {
                player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.alreadyexist")));
                return false;
            } else {
                int rankId = badBlockAPI.getRankDataManager().getRankList().size() + 1;
                badBlockAPI.getRankDataManager().createRank(new RankBean(rankId,
                        rankName,
                        rankPower,
                        rankTag,
                        rankPrefix,
                        rankSuffix,
                        null));
                badBlockAPI.getRankManager().loadRank(rankId);
                player.sendMessage(ChatUtilities.f(badBlockAPI.getConfig().getString("rank.create")));
            }
        }
        }
        return false;
    }
    /** Help Message Book **/
    private void sendHelp(Player player, int page) {
        player.sendMessage(ChatColor.RED+"      ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(CenteredMessage.getCenteredMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"Rank Manager"));
        player.sendMessage("");
        pages.get(page).forEach(player::sendMessage);
        player.sendMessage("");
        player.sendMessage(ChatColor.RED+"      ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
    /** Help Message Initialization **/
    public void initPages() {
        pages.put(1, new ArrayList<>());
        pages.get(1).add(ChatColor.GOLD + "◆ " + ChatColor.AQUA + "/rank list " + ChatColor.GRAY + "Vous donne la listes des ranks");
        pages.get(1).add(ChatColor.GOLD + "◆ " + ChatColor.AQUA + "/rank set <player> <rank> " + ChatColor.GRAY + "Permet de mettre un grade à un joueur");
        pages.get(1).add(ChatColor.GOLD + "◆ " + ChatColor.AQUA + "/rank remove <player> <rank> " + ChatColor.GRAY + "Permet de retirer un grade à un joueur");
    }

}
