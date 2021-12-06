package com.pixelninja.namecolor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class NameColorCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("namecolor")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("color", ColorArgumentType.color())
                                .executes(context ->
                                        executeModifyColor(context.getSource(), EntityArgumentType.getPlayer(context, "player"), ColorArgumentType.getColor(context, "color"))))));
    }

    public static int executeModifyColor(ServerCommandSource source, PlayerEntity entity, Formatting color) throws CommandSyntaxException {
        ((ColoredPlayerEntity)entity).setColor(color);
        source.sendFeedback(entity.getDisplayName(), true);
        return 1;
    }
}
