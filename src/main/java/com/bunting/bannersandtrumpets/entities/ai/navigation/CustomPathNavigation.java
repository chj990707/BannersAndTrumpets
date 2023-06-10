package com.bunting.bannersandtrumpets.entities.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class CustomPathNavigation extends GroundPathNavigation {
    public CustomPathNavigation(Mob p_26515_, Level p_26516_) {
        super(p_26515_, p_26516_);
    }

    public void tick(){
        if(this.path != null){
            this.mob.getMoveControl().setWantedPosition(this.path.getEndNode().x, this.path.getEndNode().y,this.path.getEndNode().z, this.mob.getMoveControl().getSpeedModifier());
        }
    }
}
