package com.bunting.bannersandtrumpets.entities.ai.goal;

import com.bunting.bannersandtrumpets.entities.ISaddledHorseRider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;

import java.util.List;

public class RideHorseGoal<T extends Mob&ISaddledHorseRider> extends Goal {

    private final T mob;
    private AbstractHorse horse;
    protected final float maxDist;
    protected final PathNavigation pathNav;
    protected final TargetingConditions targetingCondition;
    protected final double speedModifier;

    public RideHorseGoal(T mob, float maxDist, double speedModifier){
        this.mob = mob;
        this.maxDist = maxDist;
        this.speedModifier = speedModifier;
        this.horse = null;
        this.pathNav = mob.getNavigation();
        this.targetingCondition = TargetingConditions.forNonCombat().range((double)maxDist);
    }

    public boolean canUse() {
        if(this.mob.isPassenger()) return false;
        List<AbstractHorse> nearbyHorses = this.mob.level.getEntitiesOfClass(AbstractHorse.class, this.mob.getBoundingBox().inflate((double)this.maxDist, (double)this.maxDist, (double)this.maxDist), (testedEntity)-> true);
        AbstractHorse tempHorse;
        while(nearbyHorses.size() > 0){
            tempHorse = this.mob.level.getNearestEntity(nearbyHorses, targetingCondition, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
            if(tempHorse == null){
                break;
            }
            else if(!tempHorse.hasPassenger((entity)-> entity != null) && tempHorse.isTamed() && tempHorse.getAge() >= 0){
                Path path = this.pathNav.createPath(tempHorse, 0);
                if(path != null){
                    if(path.getEndNode().asBlockPos().equals(tempHorse.blockPosition())) {
                        horse = tempHorse;
                        break;
                    }
                }
            }
            nearbyHorses.remove(tempHorse);
        }
        if(this.horse != null) return true;
        else return false;
    }

    public boolean canContinueToUse(){
        return !this.pathNav.isDone() && this.mob.getRootVehicle() == this.mob && this.horse != null && !this.horse.hasPassenger((entity)->{return entity != null;});
    }

    public void start(){
        this.mob.getNavigation().moveTo(this.horse, this.speedModifier);
    }

    public void stop(){
        this.horse = null;
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick(){
        return true;
    }

    public void tick(){
        if(this.horse != null && !horse.hasPassenger((entity)-> entity != null)){
            this.mob.getLookControl().setLookAt(this.horse, 30.0F, 30.0F);
            if(this.mob.distanceToSqr(horse) <= 2.0){
                if(!horse.isSaddled()) horse.equipSaddle(this.mob.getSoundSource());
                this.mob.startRiding(horse);
                System.out.println(this.mob.position());
                return;
            }
            this.mob.getNavigation().moveTo(horse, this.speedModifier);
        }
    }
}
