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
@Document(indexName = "solution", shards = 10, replicas = 0)
public class SolutionSearch {
    @Id
    private Integer id;
    @Field(type = FieldType.Integer)
    private Integer pid;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String summary;
    @Field(type = FieldType.Text)
    private String tags;

    public static Solution fromEs(SolutionSearch search){
        Solution p = new Solution();
        p.setId(search.getId());
        p.setPid(search.getPid());
        p.setSummary(search.getSummary());
        p.setTags(search.getTags());
        p.setTitle(search.getTitle());
        return p;
    }
}
