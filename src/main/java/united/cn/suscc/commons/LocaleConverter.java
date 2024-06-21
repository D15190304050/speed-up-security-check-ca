package united.cn.suscc.commons;

import java.util.Locale;

public class LocaleConverter
{
    private LocaleConverter()
    {}

    public static Locale toLanguageCountry(String language)
    {
        return switch (language)
        {
            case "en" -> new Locale("en", "US");
            case "fr" -> new Locale("fr", "FR");
            case "zh" -> new Locale("zh", "ZH");
            default -> throw new IllegalArgumentException("Unsupported locale " + language);
        };
    }
}
