package com.thingm.blink1;

public class OnOffColor {

    public static void main(String[] args) {
        Blink1 blink1 = Blink1Finder.open();
        if (blink1 == null) {
            exitError("No blink1 found. Exiting.");
        }

        if (args.length == 0) {
            outputMessage("Turning off blink1.");
            blink1.off();
        } else {
            setColor(blink1, args);
        }
    }

    private static void setColor(Blink1 blink1, String[] args) {
        if (args == null || args.length < 3) {
            exitError("Pass in 3 numbers for: red, green and blue");
        }
        try {
            int r = Integer.parseInt(args[0]);
            int g = Integer.parseInt(args[1]);
            int b = Integer.parseInt(args[2]);
            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                throw new NumberFormatException("bad range");
            }
            outputMessage(String.format("Setting R(%s)G(%s)B(%s) on blink1", args[0], args[1], args[2]));
            blink1.setRGB(r, g, b);
        } catch (NumberFormatException nfe) {
            exitError(String.format(
                "One or more of the rgb params is not a number between 0 and 255: r(%s), g(%s), b(%s)",
                args[0], args[1], args[2]
            ));
        }
    }

    private static void outputMessage(String msg) {
        System.out.println(msg);
    }

    private static void exitError(String error) {
        System.out.println(error);
        System.exit(1);
    }
}
