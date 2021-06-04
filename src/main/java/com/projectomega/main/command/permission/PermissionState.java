package com.projectomega.main.command.permission;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the overridden state of a permission for a {@link PermissionHolder}.
 */
public enum PermissionState {

    /**
     * The holder is allowed to use the permission with no exceptions
     */
    ALLOW,


    /**
     * The holder is denied to use the permission with no exceptions
     */
    DENY,

    /**
     * The holder may or may not be able to use the permission, depending on
     * their {@link PermissionHolder#isOp()} state and the {@link Permission#getAccess()}.
     */
    UNSET;

    /**
     * Tests whether does this state allow or deny. If {@link PermissionState#UNSET},
     * this will use {@link DefaultAccess#canAccess(boolean)}
     *
     * @param op     Whether is the holder opped or not
     * @param access The permission access
     * @return True if they can access, false if otherwise.
     */
    public boolean canAccess(boolean op, @Nullable DefaultAccess access) {
        switch (this) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            default:
                return access == null || access.canAccess(op);
        }
    }

}
