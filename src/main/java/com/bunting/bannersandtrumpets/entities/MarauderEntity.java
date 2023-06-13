package com.bunting.bannersandtrumpets.entities;

import com.bunting.bannersandtrumpets.entities.ai.goal.RideHorseGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;


public class MarauderEntity extends Pillager implements ISaddledHorseRider {

    public MarauderEntity(EntityType<? extends MarauderEntity> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    public void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(0, new RideHorseGoal(this, 20.0f, 1.0));
    }

    public boolean startRidingSaddledHorse(Entity vehicle) {
        return SaddleAdapterEntity.startRidingWithAdapter(this, vehicle);
    }

    public boolean startRiding(Entity vehicle){
        if (vehicle instanceof AbstractHorse && ((AbstractHorse) vehicle).isSaddled()){
            return startRidingSaddledHorse(vehicle);
        }
        else{
            return super.startRiding(vehicle);
        }
    }
}
