package com.projectomega.main.command.permission;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A basic implementation for {@link PermissionHolder}.
 */
public class PermissionStore implements PermissionHolder {

    private final Map<String, PermissionState> permissions = new HashMap<>();
    private boolean opped;

    @Override public boolean isOp() {
        return opped;
    }

    @Override public void setOp(boolean op) {
        opped = op;
    }

    @Override public void allowPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.ALLOW);
    }

    @Override public void allowPermission(@NotNull Permission permission) {
        permissions.put(permission.getNode(), PermissionState.ALLOW);
    }

    @Override public void denyPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.DENY);
    }

    @Override public void denyPermission(@NotNull Permission permission) {
        permissions.put(permission.getNode(), PermissionState.DENY);
    }

    @Override public PermissionState getState(@NotNull Permission permission) {
        return getState(permission.getNode());
    }

    @Override public PermissionState getState(@NotNull String permission) {
        return permissions.getOrDefault(permission, PermissionState.UNSET);
    }

    @Override public boolean hasPermission(@NotNull Permission permission) {
        return getState(permission).canAccess(isOp(), permission.getAccess());
    }

    @Override public boolean hasPermission(@NotNull String permission) {
        return getState(permission).canAccess(isOp(), DefaultAccess.OP);
    }

}
