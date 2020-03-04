package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    @Query(value = "select tag.name from Tag as tag where tag.name like :key")
    List<String> findAllLike(@Param("key")String key);
}
