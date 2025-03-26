package com.battlebox.commands;

import com.battlebox.BattleBoxPlugin;
import com.battlebox.arena.Arena;
import com.battlebox.game.GameSession;
import com.battlebox.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattleBoxCommand implements CommandExecutor {
    
    private final BattleBoxPlugin plugin;
    
    public BattleBoxCommand(BattleBoxPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }
        
        if (args.length == 0) {
            this.sendHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "join" -> this.handleJoin(player, args);
            case "leave" -> this.handleLeave(player);
            case "list" -> this.handleList(player);
            case "create" -> this.handleCreate(player, args);
            case "delete" -> this.handleDelete(player, args);
            default -> this.sendHelp(player);
        }
        
        return true;
    }
    
    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BattleBox Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bb join <team> " + ChatColor.GRAY + "- Join a team");
        player.sendMessage(ChatColor.YELLOW + "/bb leave " + ChatColor.GRAY + "- Leave your current team");
        player.sendMessage(ChatColor.YELLOW + "/bb list " + ChatColor.GRAY + "- List all arenas");
        if (player.hasPermission("battlebox.admin")) {
            player.sendMessage(ChatColor.YELLOW + "/bb create <name> " + ChatColor.GRAY + "- Create a new arena");
            player.sendMessage(ChatColor.YELLOW + "/bb delete <name> " + ChatColor.GRAY + "- Delete an arena");
        }
    }
    
    private void handleJoin(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /bb join <team>");
            return;
        }
        
        String teamName = args[1].toLowerCase();
        this.plugin.getTeamManager().getTeam(teamName).ifPresentOrElse(
            team -> {
                this.plugin.getTeamManager().joinTeam(player, team);
                player.sendMessage(ChatColor.GREEN + "You joined " + team.getColor() + team.getName());
            },
            () -> player.sendMessage(ChatColor.RED + "Team not found!")
        );
    }
    
    private void handleLeave(Player player) {
        this.plugin.getTeamManager().getPlayerTeam(player).ifPresentOrElse(
            team -> {
                this.plugin.getTeamManager().leaveTeam(player);
                player.sendMessage(ChatColor.GREEN + "You left " + team.getColor() + team.getName());
            },
            () -> player.sendMessage(ChatColor.RED + "You are not in a team!")
        );
    }
    
    private void handleList(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Available Arenas ===");
        this.plugin.getArenaManager().getArenas().forEach((name, arena) -> {
            String status = arena.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
            player.sendMessage(ChatColor.YELLOW + name + ChatColor.GRAY + " - " + status);
        });
    }
    
    private void handleCreate(Player player, String[] args) {
        if (!player.hasPermission("battlebox.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /bb create <name>");
            return;
        }
        
        String name = args[1];
        if (this.plugin.getArenaManager().getArena(name).isPresent()) {
            player.sendMessage(ChatColor.RED + "An arena with that name already exists!");
            return;
        }
        
        // TODO: Implement arena creation wizard
        player.sendMessage(ChatColor.GREEN + "Arena creation wizard not implemented yet!");
    }
    
    private void handleDelete(Player player, String[] args) {
        if (!player.hasPermission("battlebox.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /bb delete <name>");
            return;
        }
        
        String name = args[1];
        this.plugin.getArenaManager().getArena(name).ifPresentOrElse(
            arena -> {
                this.plugin.getArenaManager().deleteArena(name);
                player.sendMessage(ChatColor.GREEN + "Arena deleted successfully!");
            },
            () -> player.sendMessage(ChatColor.RED + "Arena not found!")
        );
    }
}