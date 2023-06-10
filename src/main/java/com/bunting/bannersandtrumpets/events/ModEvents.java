package com.bunting.bannersandtrumpets.events;

import com.bunting.bannersandtrumpets.BannersAndTrumpets;
import com.bunting.bannersandtrumpets.EntityRegistry;
import com.bunting.bannersandtrumpets.entities.MarauderEntity;
import com.bunting.bannersandtrumpets.entities.SaddleAdapterEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = BannersAndTrumpets.MOD_ID)
    public static class forgeEvents{

    }

    @Mod.EventBusSubscriber(modid = BannersAndTrumpets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event){
            event.put(EntityRegistry.MARAUDER.get(), MarauderEntity.createAttributes().build());
            event.put(EntityRegistry.SADDLEADAPTERENTITY.get(), SaddleAdapterEntity.createMobAttributes().build());
        }

    }
}
