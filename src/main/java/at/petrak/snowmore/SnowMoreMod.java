package at.petrak.snowmore;

import at.petrak.snowmore.common.SnowballDamageBoost;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SnowMoreMod.MOD_ID)
public class SnowMoreMod {
    public static final String MOD_ID = "snowmore";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public SnowMoreMod() {
        // For things that happen in initialization
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // For everything else
        var evBus = MinecraftForge.EVENT_BUS;

        evBus.register(SnowballDamageBoost.class);

        var configSpec = new ForgeConfigSpec.Builder().configure(SnowMoreConfig::init).getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec);
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
