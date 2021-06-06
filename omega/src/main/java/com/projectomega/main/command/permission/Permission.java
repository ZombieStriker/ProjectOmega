package com.projectomega.main.command.permission;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Represents a permission to do something.
 * <p>
 * Accessible through {@link PermissionHolder}.
 */
@EqualsAndHashCode(of = "node")
public final class Permission {

    /**
     * The permission address / node
     */
    private final String node;

    /**
     * The default access for the permission.
     */
    private final DefaultAccess access;

    private Permission(String node, DefaultAccess access) {
        this.node = node;
        this.access = access;
    }

    /**
     * Returns the address / node for this permission
     *
     * @return The permission node
     */
    public String getNode() {
        return node;
    }

    /**
     * Returns the default access for the permission
     *
     * @return The default permission access
     */
    public DefaultAccess getAccess() {
        return access;
    }

    /**
     * Tests whether does the specified {@link PermissionHolder} have
     * this permission
     *
     * @param holder Holder to test against
     * @return True if they can use, false if otherwise.
     */
    public boolean canAccess(@NonNull PermissionHolder holder) {
        return holder.hasPermission(this);
    }

    /**
     * Operators only can access the permission by default
     *
     * @param permission The permission node
     * @return The permission object
     */
    public static Permission operatorsOnly(@NonNull String permission) {
        return new Permission(permission, DefaultAccess.OP);
    }

    /**
     * Non-operators only can access the permission by default
     *
     * @param permission The permission node
     * @return The permission object
     */
    public static Permission nonOperatorsOnly(@NonNull String permission) {
        return new Permission(permission, DefaultAccess.NOT_OP);
    }

    /**
     * Everyone can access the permission by default
     *
     * @param permission The permission node
     * @return The permission object
     */
    public static Permission everyone(@NonNull String permission) {
        return new Permission(permission, DefaultAccess.EVERYONE);
    }

    /**
     * No one can access the permission by default
     *
     * @param permission The permission node
     * @return The permission object
     */
    public static Permission noOne(@NonNull String permission) {
        return new Permission(permission, DefaultAccess.NO_ONE);
    }

    /**
     * Creates a {@link Permission} with the specified node and access.
     *
     * @param node   The permission node
     * @param access The permission access
     * @return The permission object
     */
    public static Permission of(@NonNull String node, @NonNull DefaultAccess access) {
        return new Permission(node, access);
    }

}
