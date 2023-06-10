package com.bunting.bannersandtrumpets;

import com.bunting.bannersandtrumpets.entities.MarauderEntity;
import com.bunting.bannersandtrumpets.entities.SaddleAdapterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BannersAndTrumpets.MOD_ID);

    public static final RegistryObject<EntityType<MarauderEntity>> MARAUDER =
            ENTITY_TYPES.register("marauder",
                    () -> EntityType.Builder.of(MarauderEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(new ResourceLocation(BannersAndTrumpets.MOD_ID,"marauder").toString()));

    public static final RegistryObject<EntityType<SaddleAdapterEntity>> SADDLEADAPTERENTITY =
            ENTITY_TYPES.register("saddleadapterentity",
                    () -> EntityType.Builder.of(SaddleAdapterEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .build(new ResourceLocation(BannersAndTrumpets.MOD_ID,"saddleadpaterentity").toString()));
    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
