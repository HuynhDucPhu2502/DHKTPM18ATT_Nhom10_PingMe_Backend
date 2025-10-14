package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Admin 9/15/2025
 *
 **/
// Class bth muốn kế thừa interface thì phải impl
// Class interface muốn kế thừa interface thì dùng extends
public interface BlogRepository extends
        JpaRepository<Blog, Long>,
        JpaSpecificationExecutor<Blog> {

    default Page<Blog> findApprovedBlogs(
            Specification<Blog> filterSpec,
            Pageable pageable
    ) {
        Specification<Blog> approvedSpec = (root, query, cb) ->
                cb.isTrue(root.get("isApproved"));

        Specification<Blog> combinedSpec =
                (filterSpec != null) ? approvedSpec.and(filterSpec) : approvedSpec;

        return findAll(combinedSpec, pageable);

    }
    
}
