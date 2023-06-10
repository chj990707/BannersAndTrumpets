package com.bunting.bannersandtrumpets.entities;

import com.bunting.bannersandtrumpets.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class MarauderEntity extends Pillager {

    public MarauderEntity(EntityType<? extends MarauderEntity> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    public boolean startRiding(Entity toRide){
        if(toRide instanceof SaddleAdapterEntity){
            return super.startRiding(toRide);
        }
        else{
            if(this.level instanceof ServerLevel){
                SaddleAdapterEntity saddleAdapterEntity = EntityRegistry.SADDLEADAPTERENTITY.get().create(this.level);
                ((ServerLevel)this.level).addFreshEntityWithPassengers(saddleAdapterEntity);
                saddleAdapterEntity.startRiding(toRide);
                this.startRiding(saddleAdapterEntity);
                return true;
            }
            return false;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33790_, DifficultyInstance p_33791_, MobSpawnType p_33792_, @Nullable SpawnGroupData p_33793_, @Nullable CompoundTag p_33794_){
        p_33793_ = super.finalizeSpawn(p_33790_,p_33791_,p_33792_,p_33793_,p_33794_);
        Horse horse = EntityType.HORSE.create(this.level);
        horse.moveTo(this.position());
        horse.setTamed(true);
        horse.setAge(0);
        horse.finalizeSpawn(p_33790_,p_33791_,p_33792_,(SpawnGroupData)null, (CompoundTag)null);
        horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
        ((ServerLevel)this.level).addFreshEntityWithPassengers(horse);
        horse.equipSaddle(null);
        this.startRiding(horse);
        return p_33793_;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }
}