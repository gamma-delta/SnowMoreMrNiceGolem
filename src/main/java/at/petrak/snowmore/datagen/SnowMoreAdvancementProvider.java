package at.petrak.snowmore.datagen;

import at.petrak.paucal.api.datagen.PaucalAdvancementProvider;
import at.petrak.snowmore.SnowMoreMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SnowMoreAdvancementProvider extends PaucalAdvancementProvider {
    public SnowMoreAdvancementProvider(DataGenerator generatorIn,
        ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn, SnowMoreMod.MOD_ID);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Supplier<EntityPredicate.Builder> snowmanPred = () -> EntityPredicate.Builder.entity()
            .of(EntityType.SNOW_GOLEM);
        Supplier<DamageSourcePredicate.Builder> snowballPred = () -> DamageSourcePredicate.Builder.damageType().direct(
            EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.SNOWBALL)));

        var root = Advancement.Builder.advancement()
            .display(simpleDisplay(Items.CARVED_PUMPKIN, "root", FrameType.TASK))
            .parent(new ResourceLocation("minecraft:adventure/root"))
            .addCriterion("snowman",
                SummonedEntityTrigger.TriggerInstance.summonedEntity(snowmanPred.get()))
            .save(consumer, modLoc("root"), fileHelper);

        Advancement.Builder.advancement()
            .display(simpleDisplay(Items.PLAYER_HEAD, "hit_friend", FrameType.TASK))
            .parent(root)
            .addCriterion("calvin", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(
                DamagePredicate.Builder.damageInstance().type(snowballPred.get()),
                EntityPredicate.Builder.entity().of(EntityType.PLAYER).build()
            ))
            .save(consumer, modLoc("hit_friend"), fileHelper);

        var masklessTag = new CompoundTag();
        masklessTag.putBoolean("Pumpkin", false);
        Advancement.Builder.advancement()
            .display(simpleDisplay(Items.PUMPKIN, "maskless", FrameType.TASK))
            .parent(root)
            .addCriterion("evil", KilledTrigger.TriggerInstance.playerKilledEntity(
                snowmanPred.get().nbt(new NbtPredicate(masklessTag)), snowballPred.get()))
            .save(consumer, modLoc("maskless"), fileHelper);

        var bosses = Advancement.Builder.advancement()
            .display(simpleDisplay(Items.SNOWBALL, "bosses", FrameType.CHALLENGE))
            .parent(root);
        var bossTypes = new EntityType<?>[]{
            EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.ELDER_GUARDIAN
        };
        for (var type : bossTypes) {
            bosses.addCriterion(EntityType.getKey(type).toString(), KilledTrigger.TriggerInstance.playerKilledEntity(
                EntityPredicate.Builder.entity().of(type),
                snowballPred.get()
            ));
        }
        bosses
            .rewards(AdvancementRewards.Builder.experience(100))
            .save(consumer, modLoc("bosses"), fileHelper);
    }
}
