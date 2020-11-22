package vue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.animation.Timeline;

public class Jeux {
    
    public static final List<String> LISTE_JEUX = Arrays.asList("Connect 4");

    public static final String P4_NOM = "Connect 4";
    public static final String P4_TYPE = "Board game";
    public static final String P4_NOMBRE_LIKES = "69k";
    public static final String P4_DESCRIPTION = "To win a Connect 4 game, you just have to be the first to align 4 chips of its color horizontally, vertically or diagonally. If during a game, all the chips are played without any alignment of chips, the game is declared a draw.";
    
    public static final String P4_MINIATURE = "connect4.png";
    public static final String[] P4_CAPTURES = {"capture1.png"};

    public static HashMap<String, Timeline> TIMELINES = new HashMap<>();

}