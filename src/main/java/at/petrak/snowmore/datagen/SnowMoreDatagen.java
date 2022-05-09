package at.petrak.snowmore.datagen;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class SnowMoreDatagen {
    @SubscribeEvent
    public static void generateData(GatherDataEvent evt) {
        var gen = evt.getGenerator();
        var efh = evt.getExistingFileHelper();

        if (evt.includeServer()) {
            gen.addProvider(new SnowMoreAdvancementProvider(gen, efh));
        }
    }
}
