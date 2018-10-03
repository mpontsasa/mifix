import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

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
        return input.replace(" ", "");
    }

    public static double round(double value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }

    public static EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Lengh) {
                    e.consume();
                }
                if(e.getCharacter().matches("[0-9]")){
                    /*if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                        e.consume();
                    }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                        e.consume();
                    }*/
                }else{
                    e.consume();
                }

                if(max_Lengh == 2 && !(txt_TextField.getText() + e.getCharacter()).isEmpty())  //month
                {
                    try
                    {

                        if (Integer.parseInt(txt_TextField.getText() + e.getCharacter()) > 12 || Integer.parseInt(txt_TextField.getText() + e.getCharacter()) < 1)
                            e.consume();
                    }
                    catch(NumberFormatException numEx)
                    {
                        //hehhe, do nothing(this try-catch is for backspace)
                    }
                }

            }
        };
    }   // copy paste, I didnt make this (dont blame me for naming convention)

}
