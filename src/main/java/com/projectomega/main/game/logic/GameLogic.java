package com.projectomega.main.game.logic;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.game.logic.types.ChunkLoadOnPlayerWalkIntoChunkListener;
import com.projectomega.main.game.logic.types.LoadChunksOnJoinListener;

public class GameLogic {

    public static void init(){
        EventBus.INSTANCE.register(new ChunkLoadOnPlayerWalkIntoChunkListener());
        EventBus.INSTANCE.register(new LoadChunksOnJoinListener());
    }

}
