package org.salgar.fsm.pekko.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Data
@Document(indexName = "creditsm")
public class CreditSMQueryDocument {
    @Field(type = FieldType.Text)
    private final String state;
    @Field(type = FieldType.Nested)
    private final Map<String, Object> payload;
}
