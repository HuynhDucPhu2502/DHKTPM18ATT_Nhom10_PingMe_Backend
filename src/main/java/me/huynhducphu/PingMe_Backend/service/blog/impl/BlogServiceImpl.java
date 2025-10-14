package me.huynhducphu.PingMe_Backend.service.blog.impl;

import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.blog.CreateBlogRequest;
import me.huynhducphu.PingMe_Backend.dto.response.blog.BlogReviewResponse;
import me.huynhducphu.PingMe_Backend.model.Blog;
import me.huynhducphu.PingMe_Backend.repository.BlogRepository;
import me.huynhducphu.PingMe_Backend.service.common.CurrentUserProvider;
import me.huynhducphu.PingMe_Backend.service.integration.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin 10/13/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements me.huynhducphu.PingMe_Backend.service.blog.BlogService {
    private static final long MAX_BLOG_IMAGE_SIZE = 2 * 1024 * 1024L;

    private final CurrentUserProvider currentUserProvider;

    private final BlogRepository blogRepository;

    private final S3Service s3Service;

    private final ModelMapper modelMapper;


    @Override
    public BlogReviewResponse saveBlog(
            CreateBlogRequest dto,
            MultipartFile blogImg
    ) {
        var currentUser = currentUserProvider.get();

        var blog = new Blog(
                dto.getTitle(), dto.getDescription(),
                dto.getContent(), dto.getCategory()
        );
        blog.setUser(currentUser);

        var savedBlog = blogRepository.saveAndFlush(blog);

        if (blogImg != null && !blogImg.isEmpty()) {
            String url = s3Service.uploadFile(
                    blogImg,
                    "blog-images",
                    savedBlog.getId().toString(),
                    true,
                    MAX_BLOG_IMAGE_SIZE
            );

            blog.setImgPreviewUrl(url);
        }

        return modelMapper.map(blog, BlogReviewResponse.class);
    }

    @Override
    public Page<BlogReviewResponse> getAllApprovedBlogs(
            Specification<Blog> spec,
            Pageable pageable
    ) {
        return blogRepository
                .findApprovedBlogs(spec, pageable)
                .map(blog -> modelMapper.map(blog, BlogReviewResponse.class));
    }


}
