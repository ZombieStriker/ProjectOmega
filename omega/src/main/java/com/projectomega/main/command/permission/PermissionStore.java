package com.projectomega.main.command.permission;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A basic implementation for {@link PermissionHolder}.
 */
public class PermissionStore implements PermissionHolder {

    protected static final char TREE_CHAR = '*';
    protected final Map<String, PermissionState> permissions = new HashMap<>();
    protected final Map<String, PermissionState> tree = new HashMap<>();
    protected boolean opped;

    @Override public boolean isOp() {
        return opped;
    }

    @Override public void setOp(boolean op) {
        opped = op;
    }

    @Override public void allowPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.ALLOW);
        if (permission.indexOf(TREE_CHAR) != -1) {
            checkTreePermission(permission);
            tree.put(permission.substring(0, permission.indexOf(TREE_CHAR)), PermissionState.ALLOW);
        }
    }

    @Override public void allowPermission(@NotNull Permission permission) {
        allowPermission(permission.getNode());
    }

    @Override public void denyPermission(@NotNull String permission) {
        permissions.put(permission, PermissionState.DENY);
        if (permission.indexOf(TREE_CHAR) != -1) {
            checkTreePermission(permission);
            tree.put(permission.substring(0, permission.indexOf(TREE_CHAR)), PermissionState.DENY);
        }
    }

    @Override public void denyPermission(@NotNull Permission permission) {
        denyPermission(permission.getNode());
    }

    @Override public PermissionState getState(@NotNull Permission permission) {
        return getState(permission.getNode());
    }

    @Override public PermissionState getState(@NotNull String permission) {
        for (Entry<String, PermissionState> treePermissions : tree.entrySet()) {
            if (permission.startsWith(treePermissions.getKey()))
                return treePermissions.getValue();
        }
        return permissions.getOrDefault(permission, PermissionState.UNSET);
    }

    @Override public boolean hasPermission(@NotNull Permission permission) {
        return getState(permission).canAccess(isOp(), permission.getAccess());
    }

    @Override public boolean hasPermission(@NotNull String permission) {
        return getState(permission).canAccess(isOp(), DefaultAccess.OP);
    }

    private void checkTreePermission(@NotNull String permission) {
        if (permission.chars().filter(c -> c == TREE_CHAR).count() > 1)
            throw new IllegalArgumentException("Cannot have more than one '*' in a permission node!");
    }

}
