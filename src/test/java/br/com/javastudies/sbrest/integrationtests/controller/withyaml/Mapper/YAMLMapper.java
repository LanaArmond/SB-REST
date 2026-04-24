package br.com.javastudies.sbrest.integrationtests.controller.withyaml.Mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YAMLMapper implements ObjectMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper mapper;
    protected TypeFactory typeFactory;

    public YAMLMapper() {
        mapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }

    /**
     * Permite acesso direto ao ObjectMapper interno do Jackson,
     * útil para navegação em árvores JSON/YAML.
     */
    public com.fasterxml.jackson.databind.ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        String content = context.getDataToDeserialize().asString();
        Class<?> type = (Class<?>) context.getType();

        try {
            // 1. Converte o YAML em árvore
            JsonNode rootNode = mapper.readTree(content);

            // 2. Se existir um envelope "body", extrai apenas o conteúdo interno
            JsonNode contentNode = rootNode;
            if (rootNode.isObject() && rootNode.has("body")) {
                contentNode = rootNode.get("body");
            }

            // 3. Converte o nó (original ou interno) para o tipo de destino
            return mapper.readValue(
                    contentNode.traverse(),
                    mapper.constructType(type)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e);
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return mapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing YAML content", e);
        }
    }
}