package Client;

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

        System.out.println("***** No commands *****");
        for (int i = 0; i < input.length; ++i) {
            String in = input[i];

            String intConversion;
            try {
                intConversion = StringProcessorProxy_NoCommands.getInstance().parseInteger(in);
            } catch (NumberFormatException e) {
                intConversion = "Caught number format exception";
            }

            System.out.printf(
                    "\nOriginal:%s\nTrimmed:%s\nLowerCased:%s\nInt:%s\n",
                    in,
                    StringProcessorProxy_NoCommands.getInstance().trim(in),
                    StringProcessorProxy_NoCommands.getInstance().toLowercase(in),
                    intConversion
                    );
        }

        System.out.println("***** With commands *****");
        for (int i = 0; i < input.length; ++i) {
            String in = input[i];

            String intConversion;
            try {
                intConversion = StringProcessorProxy_Commands.getInstance().parseInteger(in);
            } catch (NumberFormatException e) {
                intConversion = "Caught number format exception";
            } catch (Exception e) {
                intConversion = "Caught " + e.toString();
            }

            System.out.printf(
                    "\nOriginal:%s\nTrimmed:%s\nLowerCased:%s\nInt:%s\n",
                    in,
                    StringProcessorProxy_Commands.getInstance().trim(in),
                    StringProcessorProxy_Commands.getInstance().toLowercase(in),
                    intConversion
            );
        }
    }
}