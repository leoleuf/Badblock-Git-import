package fr.badblock.api.command;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.data.rank.RankBean;
import fr.badblock.api.data.rank.RankData;
import fr.badblock.api.utils.CenteredMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankCommand extends AbstractCommand implements TabCompleter {
    private HashMap<Integer, List<String>> pages;

    public RankCommand(BadBlockAPI badBlockAPI) {
        super(badBlockAPI);
        pages = new HashMap<>();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        int asize = args.length;
        if (asize == 0) {
            sendHelp(player, 1);
        } else if (asize == 2) {
            if (args[0].equalsIgnoreCase("getperm")) {
                String rankName = args[1];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                if (rankData == null) {
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
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
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                    return false;
                } else {
                    rankData.addPermissions(perm);
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.setperm"));
                }
            } else if (args[0].equalsIgnoreCase("delperm")) {
                String rankName = args[1];
                String perm = args[2];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                if (rankData == null) {
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                    return false;
                } else {
                    rankData.removePermission(perm);
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.delperm"));
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                String playerName = args[1];
                String rank = args[2];
                RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rank);
                if (rankData == null) {
                    player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                    return false;
                } else {
                    PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(playerName);
                    playerData.setRankID(rankData.getRankID());
                }


            }
        } else if (asize == 4) {
            if (args[0].equalsIgnoreCase("modify")) {
                if (args[1].equalsIgnoreCase("power")) {
                    String rankName = args[1];
                    int power = Integer.valueOf(args[2]);
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                        return false;
                    } else {
                        rankData.setPower(power);
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.setpower"));
                    }
                } else if (args[1].equalsIgnoreCase("tag")) {
                    String rankName = args[1];
                    String tag = args[2];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                        return false;
                    } else {
                        rankData.setTag(tag);
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.settag"));
                    }
                } else if (args[1].equalsIgnoreCase("prefix")) {
                    String rankName = args[1];
                    String prefix = args[2];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                        return false;
                    } else {
                        rankData.setPrefix(prefix);
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.setprefix"));
                    }
                } else if (args[1].equalsIgnoreCase("suffix")) {
                    String rankName = args[1];
                    String suffix = args[2];
                    RankData rankData = badBlockAPI.getRankManager().getRankDataByName(rankName);
                    if (rankData == null) {
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.noexist"));
                        return false;
                    } else {
                        rankData.setSuffix(suffix);
                        player.sendMessage(badBlockAPI.getConfig().getString("rank.setsuffix"));
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
                player.sendMessage(badBlockAPI.getConfig().getString("rank.alreadyexist"));
                return false;
            } else {
                long rankId = badBlockAPI.getRankDataManager().getRankList().size() + 1;
                badBlockAPI.getRankDataManager().createRank(new RankBean(rankId,
                        rankName,
                        rankPower,
                        rankTag,
                        rankPrefix,
                        rankSuffix,
                        null));
                badBlockAPI.getRankManager().loadRank(rankId);
                player.sendMessage(badBlockAPI.getConfig().getString("rank.create"));
            }

        }
        return false;
    }

    private void sendHelp(Player player, int page) {
        player.sendMessage("§c      ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(CenteredMessage.getCenteredMessage("§e§lRank Manager"));
        player.sendMessage("");
        pages.get(page).forEach(player::sendMessage);
        player.sendMessage("");
        player.sendMessage("§c      ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    public void initPages() {
        pages.put(1, new ArrayList<>());
        pages.get(1).add("§6◆ §b/rank list §7Vous donne la listes des ranks");
        pages.get(1).add("§6◆ §b/rank set <player> <rank> §7Permet de mettre un grade à un joueur");
        pages.get(1).add("§6◆ §b/rank remove <player> <rank> §7Permet de retirer un grade à un joueur");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
