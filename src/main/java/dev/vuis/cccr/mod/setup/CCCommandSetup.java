package dev.vuis.cccr.mod.setup;

import com.mojang.brigadier.CommandDispatcher;
import dev.vuis.cccr.mod.command.CCCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class CCCommandSetup {
	private CCCommandSetup() {
		throw new AssertionError();
	}

	public static void register(
		CommandDispatcher<CommandSourceStack> dispatcher,
		CommandBuildContext context,
		Commands.CommandSelection selection
	) {
		CCCommand.register(dispatcher);
	}
}
