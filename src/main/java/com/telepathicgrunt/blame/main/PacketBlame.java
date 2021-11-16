package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;

/* @author - TelepathicGrunt
 *
 * Help modders know what a vague crash is caused by
 *
 * LGPLv3
 */
public class PacketBlame {
    public static void printError(Exception exception) {
        Blame.LOGGER.error(
                "\n****************** Blame Report Invalid Player Data Error " + Blame.VERSION + " ******************" +
                        "\n\n An error occurred with player trying to join world." +
                        "\n Here's the error message that would've been hidden:\n", exception);
    }
}
