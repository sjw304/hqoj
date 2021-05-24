package top.quezr.hqoj.dao.esdao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.quezr.hqoj.entity.SolutionSearch;

@Repository
public interface EsSolutionDao extends ElasticsearchRepository<SolutionSearch,Integer> {
}
