package com.bunting.bannersandtrumpets.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Stream;

public class CustomPathNavigation extends GroundPathNavigation {

    private boolean createPathisCalledbyMob = false;

    public CustomPathNavigation(Mob p_26515_, Level p_26516_) {
        super(p_26515_, p_26516_);
    }

    Entity prevTargetEntity = null;
    int prevReachRange = 0;

    public Path createPath(Entity p_26534_, int p_26535_) {
        createPathisCalledbyMob = true;
        prevTargetEntity = p_26534_;
        prevReachRange = p_26535_;
        Path createdPath = super.createPath(p_26534_, p_26535_);
        createPathisCalledbyMob = false;
        return createdPath;
    }

    public Path createPath(Set<BlockPos> p_26549_, int p_26550_) {
        if(!createPathisCalledbyMob) prevTargetEntity = null;
        return super.createPath(p_26549_, p_26550_);
    }

    public Path createPath(Stream<BlockPos> p_26557_, int p_26558_) {
        if(!createPathisCalledbyMob) prevTargetEntity = null;
        return super.createPath(p_26557_, p_26558_);
    }

    public Path createPath(BlockPos p_26475_, int p_26476_) {
        if(!createPathisCalledbyMob) prevTargetEntity = null;
        return super.createPath(p_26475_, p_26476_);
    }

    public Path createPath(BlockPos p_148219_, int p_148220_, int p_148221_) {
        if(!createPathisCalledbyMob) prevTargetEntity = null;
        return super.createPath(p_148219_, p_148220_, p_148221_);
    }

    protected void followThePath(){
        Vec3i nextNodePos = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - ((double)nextNodePos.getX() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        double d1 = Math.abs(this.mob.getY() - (double)nextNodePos.getY());
        double d2 = Math.abs(this.mob.getZ() - ((double)nextNodePos.getZ() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        Vec3 nextNodeDirection = new Vec3(d0, d1, d2);
        if(this.mob.getForward().dot(nextNodeDirection) / nextNodeDirection.length() < 0.8){
            this.path.advance();
            if(this.path.isDone() && this.prevTargetEntity != null){
                this.path = createPath(prevTargetEntity, prevReachRange);
            }
            if(this.path.getNextNodeIndex() < this.path.getNodeCount() - 1){
                this.followThePath();
            }
        }
        if(this.path.isDone()) return;
        super.followThePath();
    }
}
