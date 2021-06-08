package com.projectomega.main.utils;

import me.nullicorn.nedit.SNBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import me.nullicorn.nedit.type.TagType;

public class NBTTagUtil {

    public static NBTCompound generateDimensionCodec() {
        NBTCompound compound = new NBTCompound();
        NBTCompound type_dimension = new NBTCompound();
        type_dimension.put("type", "minecraft:dimension_type");
        NBTList list = new NBTList(TagType.COMPOUND);
        list.add(generateDimensionTypeRegistryEntry("minecraft:overworld",0));
        type_dimension.put("value",list);
        compound.put("minecraft:dimension_type", type_dimension);

        NBTCompound type_biome = new NBTCompound();
        type_biome.put("type","minecraft:worldgen/biome");
        NBTList list2 = new NBTList(TagType.COMPOUND);
        list2.add(generateBiomeRegisteryEntry("minecraft:ocean",0,true,0.5f,-1.0f,0.5f,0.1f,"ocean"));
        list2.add(generateBiomeRegisteryEntry("minecraft:plains",1,true,0.4f,0.125f,0.8f,0.05f,"plains"));
        type_biome.put("value",list2);
        compound.put("minecraft:worldgen/biome",type_biome);
        return compound;
    }
    public static NBTCompound generateDimensionType(){
        NBTCompound tag = new NBTCompound();
        tag.put("piglin_safe",(byte)0);
        tag.put("natural",(byte)1);
        tag.put("ambient_light",1f);
        tag.put("infiniburn","minecraft:infiniburn_overworld");
        tag.put("respawn_anchor_works",(byte)0);
        tag.put("has_skylight",(byte)1);
        tag.put("bed_works",(byte)1);
        tag.put("effects","minecraft:overworld");
        tag.put("has_raids",(byte)1);
        tag.put("logical_height",256);
        tag.put("coordinate_scale",1.0f);
        tag.put("ultrawarm",(byte)0);
        tag.put("has_ceiling",(byte)0);
        tag.put("height",256);
        tag.put("min_y",0);
        return tag;
    }

    public static NBTCompound generateBiomeRegisteryEntry(String name, int id, boolean rain, float downfall, float depth, float temperature, float scale, String catagory){
        NBTCompound tag = new NBTCompound();
        tag.put("name", name);
        tag.put("id", id);
        NBTCompound elementData = new NBTCompound();
        elementData.put("precipitation",rain?"rain":"none");
        NBTCompound effectsData = new NBTCompound();
        effectsData.put("sky_color",8103167);
        effectsData.put("water_fog_color",329011);
        effectsData.put("fog_color",12638463);
        effectsData.put("water_color",4159204);
        /*NBTCompound moodSoundsData = new NBTCompound();
        moodSoundsData.put("tick_delay",6000);
        moodSoundsData.put("offset",2.0d);
        moodSoundsData.put("sound","minecraft:ambient.cave");
        moodSoundsData.put("block_search_extent",8);
        effectsData.put("mood_sound",moddSoundsData);*/
        elementData.put("effects",effectsData);
        elementData.put("depth",depth);
        elementData.put("temperature",temperature);
        elementData.put("scale",scale);
        elementData.put("downfall",downfall);
        elementData.put("category",catagory);
        tag.put("element",elementData);
        return tag;
    }

    public static NBTCompound generateDimensionTypeRegistryEntry(String name, int id) {
        NBTCompound tag = new NBTCompound();
        tag.put("name", name);
        tag.put("id", id);
        NBTCompound elementData = new NBTCompound();
        elementData.put("piglin_safe",(byte)0);
        elementData.put("natural",(byte)1);
        elementData.put("ambient_light",1f);
        elementData.put("infiniburn","minecraft:infiniburn_overworld");
        elementData.put("respawn_anchor_works",(byte)0);
        elementData.put("has_skylight",(byte)1);
        elementData.put("bed_works",(byte)1);
        elementData.put("effects","minecraft:overworld");
        elementData.put("has_raids",(byte)1);
        elementData.put("logical_height",256);
        elementData.put("coordinate_scale",1f);
        elementData.put("ultrawarm",(byte)0);
        elementData.put("has_ceiling",(byte)0);
        elementData.put("height",256);
        elementData.put("min_y",0);
        tag.put("element", elementData);
        return tag;
    }

}
