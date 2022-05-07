package at.petrak.snowmore.common;

import at.petrak.snowmore.SnowMoreConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DamageChanges {
    @SubscribeEvent
    public static void ouchie(LivingHurtEvent evt) {
        var damageSource = evt.getSource();
        var target = evt.getEntity();
        Difficulty difficulty = target.level.getDifficulty();

        if (damageSource instanceof IndirectEntityDamageSource ieds && ieds.getDirectEntity() instanceof Snowball) {
            var newDamage = SnowMoreConfig.getSnowballDamageFor(target.getType().getRegistryName(),
                difficulty);
            evt.setAmount((float) newDamage);

            if (!SnowMoreConfig.snowballDoIFrames.get(difficulty).get()) {
                target.invulnerableTime = 0;
            }
        }
        if (target instanceof SnowGolem) {
            var damageAllowed = SnowMoreConfig.snowGolemMaxDamages.get(difficulty).get().floatValue();
            if (damageAllowed >= 0) {
                evt.setAmount(Math.min(damageAllowed, evt.getAmount()));
            }
        }
    }
}
