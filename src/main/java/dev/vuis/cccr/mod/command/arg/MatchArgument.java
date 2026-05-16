package dev.vuis.cccr.mod.command.arg;

import com.mojang.brigadier.context.CommandContext;
import dev.vuis.cccr.mod.match.Match;
import dev.vuis.cccr.mod.registry.CCRegistryKeys;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.core.Holder;

public class MatchArgument extends ResourceOrIdArgument<Match<?>> {
	protected MatchArgument(CommandBuildContext context) {
		super(context, CCRegistryKeys.MATCH, Match.CODEC);
	}

	public static MatchArgument match(CommandBuildContext context) {
		return new MatchArgument(context);
	}

	public static Holder<Match<?>> getMatch(CommandContext<CommandSourceStack> context, String name) {
		//noinspection unchecked
		return context.getArgument(name, Holder.class);
	}
}
