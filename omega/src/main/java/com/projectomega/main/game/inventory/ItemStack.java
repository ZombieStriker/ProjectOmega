package com.projectomega.main.game.inventory;

import com.projectomega.main.game.Material;

public class ItemStack {

    private Material material;
    private int amount = 1;


    public ItemStack(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }
}
