package com.bunting.bannersandtrumpets.entities.ai.navigation;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class CustomPathNavigation extends GroundPathNavigation {
    public CustomPathNavigation(Mob p_26515_, Level p_26516_) {
        super(p_26515_, p_26516_);
    }

    Entity prevTargetEntity = null;
    int prevReachRange = 0;

    public Path createPath(Entity p_26534_, int p_26535_) {
        prevTargetEntity = p_26534_;
        prevReachRange = p_26535_;
        return super.createPath(p_26534_, p_26535_);
    }

    protected void followThePath(){
        Vec3i vec3i = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - ((double)vec3i.getX() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        double d1 = Math.abs(this.mob.getY() - (double)vec3i.getY());
        double d2 = Math.abs(this.mob.getZ() - ((double)vec3i.getZ() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        Vec3 nextNodeDirection = new Vec3(d0, d1, d2);
        if(this.mob.getForward().dot(nextNodeDirection) / nextNodeDirection.length() < 0.5){
            this.path.advance();
            if(this.path.isDone() && this.prevTargetEntity != null){
                this.path = createPath(prevTargetEntity, prevReachRange);
                this.path.advance();
            }
        }
        if(this.path.isDone()) return;
        super.followThePath();
    }
}
