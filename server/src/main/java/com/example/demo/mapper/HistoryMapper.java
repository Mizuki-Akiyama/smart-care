package com.example.demo.mapper;
import com.example.demo.model.History;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface HistoryMapper {

    int update(History history);

    int insert(History history);

    Optional<History> findByUserId(String userId);

}
