package com.projectomega.main.game.entity;

public enum EntityType {

    AREA_EFFECT_CLOUD(false),
    ARMOR_STAND(true),
    ARROW(false),
    BAT(true),
    BEE(true),
    BLAZE(true),
    BOAT(false),
    CAT(true),
    CAVESPIDER(true),
    CHICKEN(true),
    COD(true),
    COW(true),
    CREEPER(true),
    DONKEY(true),
    DOLPHIN(true),
    DRAGON_FIREBALL(true),
    DROWNED(true),
    ELDER_GUADIAN(true),
    ENDER_CRYSTAL(false),
    ENDER_DRAGON(true),
    ENDERMAN(true),
    ENDERMITE(true),
    EVOCATION_FANG(false),
    EVOCATIONILLAGER(true),
    XP_ORB(false),
    EYE_IF_ENDER_SIGNAL(false),
    FALLING_SAND(false),
    FIREWORK_ROCKET(false),
    FOX(true),
    GHAST(true),
    GIANT(true),
    GUARDIAN(true),
    HORSE(true),
    HUSK(true),
    ILLUSION_ILLAGER(true),
    ITEM(false),
    ITEM_FRAME(false),
    FIREBALL(false),
    LEASH_KNOT(false),
    LLAMA(true),
    LLAMA_SPIT(false),
    MAGMA_CUBE(true),
    MINECART(false),
    MINECART_CHEST(false),
    MINECART_COMMANDBLOCK(false),
    MINECART_FURNACE(false),
    MINECART_HOPPER(false),
    MINECART_SPAWNER(false),
    MINECART_TNT(false),
    MULE(true),
    MUSHROOM_COW(true),
    OZELOT(true),
    PAINTING(false),
    PANDA(true),
    PARROT(true),
    PIG(true),
    PUFFERFISH(true),
    PIG_ZOMBIE(true),
    POLAR_BEAR(true),
    PRIMED_TNT(false),
    RABBIT(true),
    SALMON(true),
    SHEEP(true),
    SHULKER(true),
    SHULKER_BULLET(false),
    SILVERFISH(true),
    SKELETON(true),
    SKELETON_HORSE(true),
    SLIME(true),
    SMALL_FIREBALL(false),
    SNOWMAN(true),
    SNOWBALL(false),
    SPECTRAL_ARROW(false),
    SPIDER(true),
    SQUID(true),
    STRAY(true),
    TRADER_LLAMA(true),
    TROPICAL_FISH(true),
    TURTLE(true),
    THROWN_EGG(false),
    THROWN_ENDERPEARL(false),
    THROWN_EXP_BOTTLE(false),
    THROWN_POTION(false),
    THROWN_TRIDENT(false),
    VEX(true),
    VILLAGER(true),
    IRON_GOLEM(true),
    VINDICATER(true),
    PILLAGER(true),
    WANDERING_TRADER(true),
    WITCH(true),
    WITHER_BOSS(true),
    WITHER_SKELETON(true),
    WITHER_SKULL(false),
    WOLF(true),
    ZOMBIE(true),
    ZOMBIE_HORSE(true),
    ZOMBIE_VILLAGER(true),
    PHANTOM(true),
    RAVAGER(true),
    LIGHTNING_BOLT(false),
    PLAYER(false),
    FISHING_BOBBER(false),
    HOGLIN(true),
    ZOMBIE_PIGMAN(true),
    ZOGLIN(true),
    STRIDER(true),
    PIGLIN(true),
    PIGLIN_BRUTE(true)
    ;

    private boolean living;

    EntityType(boolean living) {
        this.living = living;
    }


    public boolean isLiving() {
        return living;
    }

    public static EntityType getEntityType(String params) {
        for(EntityType entityType : EntityType.values()) {
            if(entityType.equals(params)) {
                return entityType;
            }
        }
        return null;
    }
}
