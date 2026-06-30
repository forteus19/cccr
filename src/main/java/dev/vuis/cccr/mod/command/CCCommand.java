package dev.vuis.cccr.mod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public final class CCCommand {
	private CCCommand() {
		throw new AssertionError();
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
	}
}
