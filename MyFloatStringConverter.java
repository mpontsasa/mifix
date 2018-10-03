import javafx.util.StringConverter;

public class MyFloatStringConverter  extends StringConverter<Float> {

    @Override
    public Float fromString(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return 0f;
        }

        try
        {

            return Float.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            return 0f;
        }
    }

    @Override
    public String toString(Float value) {

        if (value == null) {
            return "";
        }

        return Float.toString(((Float)value).floatValue());
    }
}
