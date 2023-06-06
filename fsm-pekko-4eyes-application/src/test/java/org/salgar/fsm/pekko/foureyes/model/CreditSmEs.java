package org.salgar.fsm.pekko.foureyes.model;

import lombok.Data;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "creditsm")
public class CreditSmEs {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String state;
    @Field(type = FieldType.Boolean)
    private Boolean addressCheckResult;
    @Field(type = FieldType.Boolean)
    private Boolean fraudPreventionResult;
    @Field(type = FieldType.Nested)
    private CreditTenantScoreResult creditScoreTenantResults;


}
