package at.petrak.snowmore;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowMoreConfig {
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> snowballDamage;
    private static ForgeConfigSpec.ConfigValue<Double> defaultSnowballDamage;

    public static Unit init(ForgeConfigSpec.Builder builder) {
        snowballDamage = builder.comment(
                "A list of entries defining how much damage a snowball does to the given entity.",
                "The format is: `minecraft:entity damage`")
            .defineList("snowballDamage", List.of("minecraft:blaze 3"),
                o -> o instanceof String s && getDamage(s) != null);
        defaultSnowballDamage = builder.comment("How much damage a snowball does to an entity not in the above list.")
            .defineInRange("defaultSnowballDamage", 1d, 0d, Double.POSITIVE_INFINITY);

        return Unit.INSTANCE;
    }

    public static double getSnowballDamageFor(ResourceLocation entity) {
        for (var s : snowballDamage.get()) {
            var entry = getDamage(s);
            if (entry == null) {
                SnowMoreMod.LOGGER.warn("Invalid snowball damage entry made its way past the validator?! {}", s);
                continue;
            }
            if (entry.getFirst().equals(entity)) {
                return entry.getSecond();
            }
        }
        return defaultSnowballDamage.get();
    }

    private static @Nullable Pair<ResourceLocation, Double> getDamage(String line) {
        var split = line.split(" ");
        if (split.length != 2) {
            return null;
        }
        var resloc = ResourceLocation.tryParse(split[0]);
        if (resloc == null) {
            return null;
        }
        double damage;
        try {
            damage = Double.parseDouble(split[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Pair<>(resloc, damage);
    }
}
