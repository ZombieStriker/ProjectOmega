package com.projectomega.main.game;

import com.projectomega.main.command.permission.Permission;
import com.projectomega.main.command.permission.PermissionState;
import com.projectomega.main.game.chat.TextMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConsoleSender implements CommandSender {

    private final Map<String, PermissionState> permissions = new HashMap<>();

    @Override
    public void sendMessage(String message) {
        Omega.getLogger().log(Level.INFO, message);
    }

    @Override
    public void issueCommand(String command) {
        //TODO: Issue command
    }

    @Override
    public void chat(String message) {
        Omega.broadcastJSONMessage(TextMessage.chat("Console", ": " + message).getAsJson());
    }

    @Override public boolean isOp() {
        return true;
    }

    @Override public void setOp(boolean op) {
    }

    @Override public void allowPermission(@NotNull Permission permission) {
        permissions.put(permission.getNode(), PermissionState.ALLOW);
    }

    @Override public void allowPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.ALLOW);
    }

    @Override public void denyPermission(@NotNull Permission permission) {
        permissions.put(permission.getNode(), PermissionState.DENY);
    }

    @Override public void denyPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.DENY);
    }

    @Override public PermissionState getState(@NotNull Permission permission) {
        return getState(permission.getNode());
    }

    @Override public PermissionState getState(@NotNull String permission) {
        return permissions.getOrDefault(permission, PermissionState.ALLOW); // this is for console only.
    }

    @Override public boolean hasPermission(@NotNull Permission permission) {
        return getState(permission).canAccess(true, permission.getAccess());
    }

    @Override public boolean hasPermission(@NotNull String permission) {
        return getState(permission) == PermissionState.ALLOW; // this is for console only.
    }
}
