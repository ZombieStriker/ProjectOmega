package com.projectomega.main.command.permission;

import java.util.function.Function;

public enum DefaultAccess {

    /**
     * Only operators have access to the permission, unless specifically
     * overriden
     */
    OP(op -> op),

    /**
     * Only non-operators have access to this permission, unless specifically
     * overridden
     */
    NOT_OP(op -> !op),

    /**
     * No one has access to this permission, unless specifically overridden.
     */
    NO_ONE(op -> false),

    /**
     * Everyone has access to this permission, unless specifically denied.
     */
    EVERYONE(op -> true);

    private final Function<Boolean, Boolean> canAccess;

    DefaultAccess(Function<Boolean, Boolean> canAccess) {
        this.canAccess = canAccess;
    }

    /**
     * Returns the default permission access for the current op state.
     *
     * @param isOp Whether is the target opped or not
     * @return True if they can access this permission by default, false
     * if otherwise.
     */
    public boolean canAccess(boolean isOp) {
        return canAccess.apply(isOp);
    }

}
