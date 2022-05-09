package at.petrak.snowmore.mixin;

import at.petrak.snowmore.SnowMoreConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Snowball.class)
public class SnowballMixin {
    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean damage(Entity target, DamageSource pSource, float pAmount) {
        var difficulty = target.level.getDifficulty();
        var newDamage = SnowMoreConfig.getSnowballDamageFor(target.getType().getRegistryName(),
            difficulty);
        return target.hurt(pSource, (float) newDamage);
    }
}
