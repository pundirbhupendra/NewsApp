package com.portfolio.newsapp.data.mapper

import com.portfolio.newsapp.data.local.entity.ArticleEntity
import com.portfolio.newsapp.data.remote.dto.ArticleDto
import com.portfolio.newsapp.domain.model.Article
import java.util.UUID

// DTO -> Domain
fun ArticleDto.toDomainModel(): Article {
    return Article(
        id = url.hashCode().toString(),
        title = title,
        description = description,
        content = content,
        imageUrl = urlToImage,
        publishedAt = publishedAt,
        source = source.name,
        url = url,
        author = author
    )
}

// Domain -> Entity
fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = source,
        url = url,
        author = author
    )
}

// Entity -> Domain
fun ArticleEntity.toDomainModel(): Article {
    return Article(
        id = id,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = source,
        url = url,
        author = author
    )
}
