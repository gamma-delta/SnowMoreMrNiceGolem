package at.petrak.snowmore.datagen;

import at.petrak.paucal.api.forge.datagen.PaucalForgeDatagenWrappers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class SnowMoreDatagen {
    @SubscribeEvent
    public static void generateData(GatherDataEvent evt) {
        var gen = evt.getGenerator();
        var efh = evt.getExistingFileHelper();

        if (evt.includeServer()) {
            gen.addProvider(PaucalForgeDatagenWrappers.addEFHToAdvancements(new SnowMoreAdvancementProvider(gen), efh));
        }
    }
}
