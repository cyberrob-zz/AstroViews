package com.example.astroviews.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.astroviews.PAGING_SIZE
import com.example.astroviews.database.dao.TheDao
import com.example.astroviews.database.entities.AstroImageRecord

class GridViewModel(private val theDao: TheDao) : BaseViewModel() {

    val nasaImageFlow = Pager(PagingConfig(PAGING_SIZE)) {
        NasaImagePagingSource(theDao)
    }.flow.cachedIn(viewModelScope)

    inner class NasaImagePagingSource(private val theDao: TheDao) :
        PagingSource<Int, AstroImageRecord>() {


        override fun getRefreshKey(state: PagingState<Int, AstroImageRecord>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AstroImageRecord> {
            return try {
                val page = params.key ?: 0
                val data: List<AstroImageRecord> =
                    theDao.getImages(offset = page * PAGING_SIZE, size = PAGING_SIZE)

                LoadResult.Page(
                    data = data,
                    prevKey = if (page > 0) page - 1 else null,
                    nextKey = if (data.isNullOrEmpty()) null else page + 1
                )

            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

    }


}