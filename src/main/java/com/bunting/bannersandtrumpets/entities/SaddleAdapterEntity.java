package com.bunting.bannersandtrumpets.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaddleAdapterEntity extends Mob {
    public SaddleAdapterEntity(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
        this.moveControl = new SaddleAdapterMoveControl(this);
    }

    public void tick(){
        if(tickCount > 1 && (!isPassenger() || this.getFirstPassenger() == null)){
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();
    }

    public PathNavigation getNavigation(){
        return this.navigation;
    }

    public MoveControl getMoveControl(){
        return this.moveControl;
    }

    public class SaddleAdapterMoveControl extends MoveControl {

        public SaddleAdapterMoveControl(Mob p_24983_) {
            super(p_24983_);
        }

        private boolean isWalkable(float p_24997_, float p_24998_){
            PathNavigation pathnavigation = this.mob.getNavigation();
            if (pathnavigation != null) {
                NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
                if (nodeevaluator != null && nodeevaluator.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + (double)p_24997_), this.mob.getVehicle().getBlockY(), Mth.floor(this.mob.getZ() + (double)p_24998_)) != BlockPathTypes.WALKABLE) {
                    return false;
                }
            }

            return true;
        }

        public void tick(){
            if(this.operation == Operation.MOVE_TO){
                double d_X = this.wantedX - this.mob.getX();
                double d_Z = this.wantedZ - this.mob.getZ();
                double d_Y = this.wantedY - this.mob.getY();
                double d_length_squared = d_X * d_X + d_Y * d_Y + d_Z * d_Z;
                if (d_length_squared < (double)2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                    return;
                }
                float f9 = (float)(Mth.atan2(d_Z, d_X) * (double)(180F / (float)Math.PI)) - 90.0F;
                System.out.println(f9);
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
                Vec2 planeForward = new Vec2((float)this.mob.getVehicle().getForward().x, (float)this.mob.getVehicle().getForward().z).normalized();
                this.strafeForwards = (float)(planeForward.x * d_X / Math.sqrt(d_length_squared) + planeForward.y * d_Z / Math.sqrt(d_length_squared));
                this.strafeRight = -(float)(planeForward.y * d_X / Math.sqrt(d_length_squared) - planeForward.x * d_Z / Math.sqrt(d_length_squared));
                BlockPos blockpos = this.mob.getVehicle().blockPosition();
                BlockState blockstate = this.mob.getVehicle().level.getBlockState(blockpos);
                VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level, blockpos);
                if (d_Y > (double)this.mob.getVehicle().getStepHeight() && d_X * d_X + d_Z * d_Z < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
                    this.mob.getJumpControl().jump();
                    this.operation = MoveControl.Operation.JUMPING;
                    return;
                }
                else{
                    float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    float f1 = (float)this.speedModifier * f;
                    float f2 = this.strafeForwards;
                    float f3 = this.strafeRight;
                    float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
                    if (f4 < 1.0F) {
                        f4 = 1.0F;
                    }

                    f4 = f1 / f4;
                    f2 *= f4;
                    f3 *= f4;
                    float f5 = Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F));
                    float f6 = Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F));
                    float f7 = f2 * f6 - f3 * f5;
                    float f8 = f3 * f6 + f2 * f5;
                    if (!this.isWalkable(f7, f8)) {
                        this.strafeForwards = 1.0F;
                        this.strafeRight = 0.0F;
                    }

                    this.mob.setSpeed(f1);
                    this.mob.setZza(this.strafeForwards);
                    this.mob.setXxa(this.strafeRight);
                    this.operation = MoveControl.Operation.WAIT;
                    return;
                }
            }
            super.tick();
        }
    }
}
