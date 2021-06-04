package com.projectomega.main.command;

import com.projectomega.main.command.permission.DefaultAccess;
import com.projectomega.main.command.permission.Permission;
import com.projectomega.main.plugin.OmegaPlugin;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public final class OmegaCommand {

    static final CommandExecutor VOID = (sender, args, plugin) -> {};

    private final @NotNull String name;
    private final @Nullable String description;
    private final @Nullable String usage;
    private final @Nullable Permission permission;
    private final @Unmodifiable List<String> aliases;
    private @NotNull CommandExecutor executor;
    private OmegaPlugin plugin;

    OmegaCommand(@Nullable String description,
                 @Nullable String usage,
                 @Nullable Permission permission,
                 List<String> aliases,
                 @NotNull CommandExecutor executor) {
        this.name = aliases.get(0);
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.aliases = Collections.unmodifiableList(aliases);
        this.executor = executor;
    }

    void setExecutor(@NotNull CommandExecutor executor) {
        this.executor = executor;
    }

    void setPlugin(OmegaPlugin plugin) {
        this.plugin = plugin;
    }

    public static Builder named(@NotNull String name) {
        return new Builder(name);
    }

    public static class Builder {

        private String description;
        private String usage;
        private Permission permission;
        private final List<String> aliases = new ArrayList<>();
        private CommandExecutor executor;

        private Builder(@NonNull String name) {
            aliases.add(0, name);
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder permission(Permission permission) {
            this.permission = permission;
            return this;
        }

        public Builder permission(String permission) {
            this.permission = Permission.operatorsOnly(permission);
            return this;
        }

        public Builder permission(String permission, DefaultAccess access) {
            this.permission = Permission.of(permission, access);
            return this;
        }

        public Builder executor(CommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        public Builder addAlias(String... aliases) {
            Collections.addAll(this.aliases, aliases);
            return this;
        }

        public Builder addAlias(@NonNull Collection<String> aliases) {
            this.aliases.addAll(aliases);
            return this;
        }

        public OmegaCommand make() {
            if (executor == null)
                executor = VOID;
            return new OmegaCommand(description, usage, permission, aliases, executor);
        }
    }
}
