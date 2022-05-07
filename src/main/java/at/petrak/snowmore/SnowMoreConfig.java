package at.petrak.snowmore;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;

public class SnowMoreConfig {
    private static final EnumMap<Difficulty, ForgeConfigSpec.ConfigValue<List<? extends String>>> snowballDamages =
        new EnumMap<>(Difficulty.class);
    private static final EnumMap<Difficulty, ForgeConfigSpec.ConfigValue<Double>> defaultSnowballDamages =
        new EnumMap<>(Difficulty.class);
    public static final EnumMap<Difficulty, ForgeConfigSpec.ConfigValue<Double>> snowGolemMaxDamages =
        new EnumMap<>(Difficulty.class);
    public static final EnumMap<Difficulty, ForgeConfigSpec.ConfigValue<Boolean>> snowballDoIFrames =
        new EnumMap<>(Difficulty.class);

    public static Unit init(ForgeConfigSpec.Builder builder) {
        for (var difficulty : Difficulty.values()) {
            builder.push(difficulty.getKey());

            var snowballDamage = builder.comment(
                    "A list of entries defining how much damage a snowball does to the given entity in " + difficulty.getKey() + " mode.",
                    "The format is: `minecraft:entity damage`")
                .defineList("snowballDamage", List.of("minecraft:blaze 3"),
                    o -> o instanceof String s && getDamage(s) != null);
            snowballDamages.put(difficulty, snowballDamage);

            var defaultSnowballDamage = builder.comment(
                    "How much damage a snowball does to an entity not in the above list in " + difficulty.getKey() + " mode.")
                .defineInRange("defaultSnowballDamage", 1d, 0d, Double.POSITIVE_INFINITY);
            defaultSnowballDamages.put(difficulty, defaultSnowballDamage);

            var maxDamage = builder.comment(
                    "The maximum amount of damage a snow golem can take from any attack in " + difficulty.getKey() + " mode.",
                    "Set below 0 to allow any amount of damage.")
                .defineInRange("snowGolemMaxDamage", -1.0, -1.0, Double.POSITIVE_INFINITY);
            snowGolemMaxDamages.put(difficulty, maxDamage);

            var iframe = builder.comment(
                    "Whether snowballs should still cause invincibility frames in " + difficulty.getKey() + " mode.")
                .define("snowballDoIFrames", true);
            snowballDoIFrames.put(difficulty, iframe);

            builder.pop();
        }

        return Unit.INSTANCE;
    }

    public static double getSnowballDamageFor(ResourceLocation entity, Difficulty difficulty) {
        var dmges = snowballDamages.get(difficulty);
        for (var s : dmges.get()) {
            var entry = getDamage(s);
            if (entry == null) {
                SnowMoreMod.LOGGER.warn("Invalid snowball damage entry made its way past the validator?! {}", s);
                continue;
            }
            if (entry.getFirst().equals(entity)) {
                return entry.getSecond();
            }
        }
        return defaultSnowballDamages.get(difficulty).get();
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
