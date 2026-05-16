package dev.vuis.cccr.mod.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.vuis.cccr.mod.command.arg.MatchArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class CCCommand {
	private CCCommand() {
		throw new AssertionError();
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			literal("cccr").then(
				literal("join").then(
					argument("match", MatchArgument.match(context))
				)
			)
		);
	}
}
