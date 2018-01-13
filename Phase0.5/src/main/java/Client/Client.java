package Client;

import Server.StringProcessor;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class Client {
    public static void main(String[] args) {
        String[] input = new String[] {
                "Hello",
                "  hElLo     ",
                "3",
                "-10",
                "0",
                "hi"
        };

        for (int i = 0; i < input.length; ++i) {
            String in = input[i];

            String intConversion;
            try {
                intConversion = StringProcessorProxy_NoCommands.getInstance().parseInteger(in);
            } catch (NumberFormatException e) {
                intConversion = "Caught number format exception";
            } catch (Exception e) {
                intConversion = "Caught " + e.toString();
            }

            System.out.printf(
                    "\nOriginal:%s\nTrimmed:%s\nLowerCased:%s\nInt:%s\n",
                    in,
                    StringProcessorProxy_NoCommands.getInstance().trim(in),
                    StringProcessorProxy_NoCommands.getInstance().toLowercase(in),
                    intConversion
                    );
        }
    }
}
