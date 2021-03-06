package com.projectomega.main.utils;

public class ExperiencePointsUtil {

    public static int getXPRequired(int level) {
        if (level >= 0 && level <= 15) {
            return 2 * level + 7;
        } else if (level >= 16 && level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

    public static int getTotalXP(int level) {
        if (level >= 0 && level <= 16) {
            return (level * level) + (6 * level);
        } else if (level >= 0 && level <= 16) {
            return (int) ((2.5 * level * level )- (40.5 * level) + 360);
        } else {
            return (int) ((4.5 * level * level )- (152.5 * level) + 2220);
        }
    }
}
