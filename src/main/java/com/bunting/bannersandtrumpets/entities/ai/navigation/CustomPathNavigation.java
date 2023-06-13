package com.bunting.bannersandtrumpets.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

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
        if(!createPathisCalledbyMob) {
            prevTargetEntity = null;
            prevReachRange = 0;
        }
        return super.createPath(p_26549_, p_26550_);
    }

    public Path createPath(Stream<BlockPos> p_26557_, int p_26558_) {
        if(!createPathisCalledbyMob) {
            prevTargetEntity = null;
            prevReachRange = 0;
        }
        return super.createPath(p_26557_, p_26558_);
    }

    public Path createPath(BlockPos p_26475_, int p_26476_) {
        if(!createPathisCalledbyMob) {
            prevTargetEntity = null;
            prevReachRange = 0;
        }
        return super.createPath(p_26475_, p_26476_);
    }

    public Path createPath(BlockPos p_148219_, int p_148220_, int p_148221_) {
        if(!createPathisCalledbyMob) {
            prevTargetEntity = null;
            prevReachRange = 0;
        }
        return super.createPath(p_148219_, p_148220_, p_148221_);
    }

    protected void followThePath(){
        Vec3i nextNodePos = this.path.getNextNodePos();
        double dx = Math.abs(this.mob.getX() - ((double)nextNodePos.getX() + (this.mob.getBbWidth() + 1) / 2D));
        double dz = Math.abs(this.mob.getZ() - ((double)nextNodePos.getZ() + (this.mob.getBbWidth() + 1) / 2D));
        Vec3 nextNodeDirection = new Vec3(dx, 0, dz).normalize();
        Vec3 planarForward = new Vec3(this.mob.getForward().x, 0, this.mob.getForward().z).normalize();
        double directionDotForward = planarForward.dot(nextNodeDirection);
        if(directionDotForward < 0.9){
            boolean isSharpTurn = false;
            if(this.path.getNodeCount() > this.path.getNextNodeIndex() + 2 && this.path.getNextNodeIndex() > 0){
                Vec3[] directions = new Vec3[3];
                for(int i = 0; i < 3; i++){
                    double direction_x = this.path.getNode(this.path.getNextNodeIndex() - 1 + i).x - this.path.getNode(this.path.getNextNodeIndex() + i).x;
                    double direction_z = this.path.getNode(this.path.getNextNodeIndex() - 1 + i).z - this.path.getNode(this.path.getNextNodeIndex() + i).z;
                    directions[i] = new Vec3(direction_x,0,direction_z).normalize();
                }
                isSharpTurn = directions[0].dot(directions[1]) < 0.9 && directions[0].dot(directions[2]) < 0.1;
                if(isSharpTurn){
                    for(int i = 0; i < 3; i++){
                        System.out.println(i + "," + directions[i]);
                    }
                }
            }
            if(!isSharpTurn){
                this.path.advance();
                if(this.path.isDone() && this.prevTargetEntity != null){
                    this.path = createPath(prevTargetEntity, prevReachRange);
                    if(this.path.getNodeCount() > 1){
                        this.followThePath();
                    }
                }
            }
            if(this.path.isDone()) return;
        }
        super.followThePath();
    }
}
