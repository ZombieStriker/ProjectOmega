package com.projectomega.main.command.permission;

import org.jetbrains.annotations.NotNull;

public interface PermissionHolder {

    boolean isOp();

    void setOp(boolean op);

    void allowPermission(@NotNull Permission permission);

    void allowPermission(@NotNull String permission);

    void denyPermission(@NotNull Permission permission);

    void denyPermission(@NotNull String permission);

    PermissionState getState(@NotNull Permission permission);

    PermissionState getState(@NotNull String permission);

    boolean hasPermission(@NotNull Permission permission);

    boolean hasPermission(@NotNull String permission);

}
