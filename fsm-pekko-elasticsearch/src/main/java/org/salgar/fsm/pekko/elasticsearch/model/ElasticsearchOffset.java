package org.salgar.fsm.pekko.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "offset")
public class ElasticsearchOffset {
    @Id
    private final String projectionId;
    @Field(type = FieldType.Text)
    private final String projectionName;
    @Field(type = FieldType.Text)
    private final String projectionKey;
    @Field(type = FieldType.Text)
    private final String offset;
}