package at.petrak.snowmore.common;

import at.petrak.snowmore.SnowMoreConfig;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SnowballDamageBoost {
    @SubscribeEvent
    public static void boostSnowballDamage(LivingHurtEvent evt) {
        var damageSource = evt.getSource();
        if (!(damageSource instanceof IndirectEntityDamageSource ieds) || !(ieds.getDirectEntity() instanceof Snowball)) {
            return;
        }

        var target = evt.getEntity();
        var newDamage = SnowMoreConfig.getSnowballDamageFor(target.getType().getRegistryName());
        evt.setAmount((float) newDamage);
    }
}
