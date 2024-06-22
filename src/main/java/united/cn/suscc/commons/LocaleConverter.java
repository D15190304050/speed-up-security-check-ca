package united.cn.suscc.commons;

import java.util.Locale;

public class LocaleConverter
{
    public static final Locale EN_US;
    public static final Locale FR_FR;
    public static final Locale ZH_CN;

    private LocaleConverter()
    {}

    static
    {
        EN_US = new Locale("en", "US");
        FR_FR = new Locale("fr", "FR");
        ZH_CN = new Locale("zh", "CN");
    }

    public static Locale toLanguageCountry(String language)
    {
        return switch (language)
        {
            case "en" -> EN_US;
            case "fr" -> FR_FR;
            case "zh" -> ZH_CN;
            default -> throw new IllegalArgumentException("Unsupported locale " + language);
        };
    }
}
