package com.example.mohaasaba.helper;

import com.example.mohaasaba.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThemeUtils {

    public static final int THEME_RED = -11001;
    public static final int THEME_PINK = -11002;
    public static final int THEME_PURPLE = -11003;
    public static final int THEME_PURPLE_DARK = -11004;
    public static final int THEME_INDIGO = -11005;
    public static final int THEME_BLUE = -11006;
    public static final int THEME_LIGHT_BLUE = -11007;
    public static final int THEME_CYAN = -11008;
    public static final int THEME_TEAL = -11009;
    public static final int THEME_GREEN = -11010;
    public static final int THEME_LIGHT_GREEN = -11011;
    public static final int THEME_LIME = -11012;
    public static final int THEME_YELLOW = -11013;
    public static final int THEME_AMBER = -11014;
    public static final int THEME_ORANGE = -11015;
    public static final int THEME_DEEP_ORANGE = -11016;
    public static final int THEME_BROWN = -11017;
    public static final int THEME_GRAY = -11018;
    public static final int THEME_BLUE_GRAY = -11019;

    public static List<Integer> getThemeList() {
        List<Integer> themeList = new ArrayList<>();

        themeList.add(THEME_RED);
        themeList.add(THEME_PINK);
        themeList.add(THEME_PURPLE);
        themeList.add(THEME_PURPLE_DARK);
        themeList.add(THEME_INDIGO);
        themeList.add(THEME_BLUE);
        themeList.add(THEME_LIGHT_BLUE);
        themeList.add(THEME_CYAN);
        themeList.add(THEME_TEAL);
        themeList.add(THEME_GREEN);
        themeList.add(THEME_LIGHT_GREEN);
        themeList.add(THEME_LIME);
        themeList.add(THEME_YELLOW);
        themeList.add(THEME_AMBER);
        themeList.add(THEME_ORANGE);
        themeList.add(THEME_DEEP_ORANGE);
        themeList.add(THEME_BROWN);
        themeList.add(THEME_GRAY);
        themeList.add(THEME_BLUE_GRAY);

        return themeList;
    }
    public static List<String> getThemeNames() {
        List<String> themeNames = new ArrayList<>();

        themeNames.add("Red");
        themeNames.add("Pink");
        themeNames.add("Purple");
        themeNames.add("Purple Dark");
        themeNames.add("Indigo");
        themeNames.add("Blue");
        themeNames.add("Light Blue");
        themeNames.add("Cyan");
        themeNames.add("Teal");
        themeNames.add("Green");
        themeNames.add("Light Green");
        themeNames.add("Lime");
        themeNames.add("Yellow");
        themeNames.add("Amber");
        themeNames.add("Orange");
        themeNames.add("Deep Orange");
        themeNames.add("Brown");
        themeNames.add("Gray");
        themeNames.add("Blue Gray");

        return themeNames;
    }
    public static int getResourceID(int theme) {
        switch (theme) {
            case THEME_RED:
                return R.style.ThemeRed;
            case THEME_PINK:
                return R.style.ThemePink;
            case THEME_PURPLE:
                return R.style.ThemePurple;
            case THEME_PURPLE_DARK:
                return R.style.ThemePurpleDark;
            case THEME_INDIGO:
                return R.style.ThemeIndigo;
            case THEME_BLUE:
                return R.style.ThemeBlue;
            case THEME_LIGHT_BLUE:
                return R.style.ThemeLightBlue;
            case THEME_CYAN:
                return R.style.ThemeCyan;
            case THEME_TEAL:
                return R.style.ThemeTeal;
            case THEME_GREEN:
                return R.style.ThemeGreen;
            case THEME_LIGHT_GREEN:
                return R.style.ThemeLightGreen;
            case THEME_LIME:
                return R.style.ThemeLime;
            case THEME_YELLOW:
                return R.style.ThemeYellow;
            case THEME_AMBER:
                return R.style.ThemeAmber;
            case THEME_ORANGE:
                return R.style.ThemeOrange;
            case THEME_DEEP_ORANGE:
                return R.style.ThemeDeepOrange;
            case THEME_BROWN:
                return R.style.ThemeBrown;
            case THEME_GRAY:
                return R.style.ThemeGray;
            case THEME_BLUE_GRAY:
                return R.style.ThemeBlueGray;
            default:
                return R.style.AppTheme;
        }
    }

    /**
     * Generates a random theme ID
     * @return a random number that indicates a theme ID from above specified themes
     */
    public static int getRandomThemeID() {
        Random random = new Random();
        int i = random.nextInt(19);
        return - 11000 - i;
    }
}
