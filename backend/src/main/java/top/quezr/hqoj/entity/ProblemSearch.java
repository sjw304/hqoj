package top.quezr.hqoj.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "problem", shards = 10, replicas = 0)
public class ProblemSearch{
    @Id
    private Integer id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Integer)
    private Integer point;
    @Field(type = FieldType.Integer)
    private Integer level;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Text)
    private String tags;

}