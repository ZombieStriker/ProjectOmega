package com.projectomega.main.game.logic;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.game.logic.types.ChunkManagerListener;

public class GameLogic {

    public static void init(){
        EventBus.INSTANCE.register(new ChunkManagerListener());
    }

}
