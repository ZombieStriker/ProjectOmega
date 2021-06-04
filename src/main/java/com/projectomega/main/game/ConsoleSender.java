package com.projectomega.main.game;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.permission.Permission;
import com.projectomega.main.command.permission.PermissionState;
import com.projectomega.main.command.permission.PermissionStore;
import com.projectomega.main.game.chat.TextMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Map.Entry;
import java.util.logging.Level;

public class ConsoleSender implements CommandSender {

    private final PermissionStore permissionStore = new PermissionStore() {
        @Override public PermissionState getState(@NotNull String permission) {
            for (Entry<String, PermissionState> treePermissions : tree.entrySet()) {
                if (permission.startsWith(treePermissions.getKey()))
                    return treePermissions.getValue();
            }
            return permissions.getOrDefault(permission, PermissionState.ALLOW); // for console
        }
    };

    @Override
    public void sendMessage(String message) {
        Omega.getLogger().log(Level.INFO, message);
    }

    @Override
    public void issueCommand(String command) {
        try {
            Omega.getCommandHandler().execute(this, command.substring(1));
        } catch (CommandException e) {
            sendMessage(e.getMessage());
        }
    }

    @Override
    public void chat(String message) {
        Omega.broadcastJSONMessage(TextMessage.chat("Console", ": " + message).getAsJson());
    }

    @Override public boolean isOp() { return true; }

    @Override public void setOp(boolean op) {}

    @Override public void allowPermission(@NotNull String permission) {permissionStore.allowPermission(permission);}

    @Override public void allowPermission(@NotNull Permission permission) {permissionStore.allowPermission(permission);}

    @Override public void denyPermission(@NotNull String permission) {permissionStore.denyPermission(permission);}

    @Override public void denyPermission(@NotNull Permission permission) {permissionStore.denyPermission(permission);}

    @Override public PermissionState getState(@NotNull Permission permission) {return permissionStore.getState(permission);}

    @Override public PermissionState getState(@NotNull String permission) {return permissionStore.getState(permission);}

    @Override public boolean hasPermission(@NotNull Permission permission) {return permissionStore.hasPermission(permission);}

    @Override public boolean hasPermission(@NotNull String permission) {return permissionStore.hasPermission(permission);}
}
