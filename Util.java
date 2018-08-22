public class Util {

    public static String breackInLine(String inp, int maxLineLenght)
    {
        String[] words = inp.split(" ");

        String res = "";
        int length = 0;

        for (String word : words)
        {
            if (length > 0 && word.length() + length + 1 > maxLineLenght)
            {
                res += "\n";
                length = 0;
            }
            else
            {
                res += " ";
                length ++;
            }


            res += word;
            length += word.length();
        }
        return res;
    }

    public static String noSpace(String input)
    {
        String[] words = input.split(" ");

        String res = "";

        for (String word : words)
        {
            res += word;
        }

        return res;
    }

    public static double round(double value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }
}
