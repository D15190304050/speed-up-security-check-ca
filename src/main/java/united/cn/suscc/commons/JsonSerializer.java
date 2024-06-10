package united.cn.suscc.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonSerializer
{
    private JsonSerializer()
    {
    }

    private static final ObjectMapper MAPPER;
    private static final TypeFactory TYPE_FACTORY;

    static
    {
        MAPPER = new ObjectMapper();
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        TYPE_FACTORY = MAPPER.getTypeFactory();
    }

    public static String serialize(Object object)
    {
        try
        {
            return MAPPER.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            return null;
        }
    }

    public static <T> T deserialize(String value, Class<T> clazz)
    {
        try
        {
            return MAPPER.readValue(value, clazz);
        }
        catch (JsonProcessingException e)
        {
            return null;
        }
    }

    public static <T> T deserialize(JsonNode jsonNode, Class<T> clazz)
    {
        return MAPPER.convertValue(jsonNode, clazz);
    }

    public static <T> T deserialize(InputStream inputStream, Class<T> clazz)
    {
        try
        {
            return MAPPER.readValue(inputStream, clazz);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static <T> List<T> deserializeList(String value, Class<T> clazz)
    {
        try
        {
            return MAPPER.readValue(value, TYPE_FACTORY.constructParametricType(List.class, clazz));
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
