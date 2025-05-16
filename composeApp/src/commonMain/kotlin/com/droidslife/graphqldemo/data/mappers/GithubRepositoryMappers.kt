package com.droidslife.graphqldemo.data.mappers

import com.droidslife.graphqldemo.domain.model.ReleaseDetail
import com.droidslife.graphqldemo.graphql.SearchRepositoriesQuery
import com.droidslife.graphqldemo.graphql.fragment.ReleaseFragment

fun ReleaseFragment.toReleaseDetail(): ReleaseDetail? {
    return ReleaseDetail(
        name = name?:"",
        description = description,
        isLatest = isLatest,
        url = url.toString(),
        updatedAt = updatedAt.toString(),
        releaseDate = publishedAt.toString(),
        tagName = tagName

    )
}

