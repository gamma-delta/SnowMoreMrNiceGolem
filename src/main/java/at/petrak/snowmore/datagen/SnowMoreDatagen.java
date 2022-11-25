package at.petrak.snowmore.datagen;

import at.petrak.paucal.api.forge.datagen.PaucalForgeDatagenWrappers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SnowMoreDatagen {
    @SubscribeEvent
    public static void generateData(GatherDataEvent evt) {
        var gen = evt.getGenerator();
        var efh = evt.getExistingFileHelper();

        gen.addProvider(evt.includeServer(),
            PaucalForgeDatagenWrappers.addEFHToAdvancements(new SnowMoreAdvancementProvider(gen), efh));
    }
}
