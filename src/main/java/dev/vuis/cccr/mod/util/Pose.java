package dev.vuis.cccr.mod.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record Pose(
	Vec3 position,
	Vec2 rotation
) {
	public static final Codec<Pose> CODEC = RecordCodecBuilder.create(instance -> instance
		.group(
			Vec3.CODEC.fieldOf("position").forGetter(Pose::position),
			Vec2.CODEC.fieldOf("rotation").forGetter(Pose::rotation)
		).apply(
			instance, Pose::new
		));
}
