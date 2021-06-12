package com.example.nasagallery.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.nasagallery.PAGING_SIZE
import com.example.nasagallery.database.dao.TheDao
import com.example.nasagallery.database.entities.NasaImageRecord

class GridViewModel(private val theDao: TheDao) : BaseViewModel() {

    val nasaImageFlow = Pager(PagingConfig(PAGING_SIZE)) {
        NasaImagePagingSource(theDao)
    }.flow.cachedIn(viewModelScope)

    inner class NasaImagePagingSource(private val theDao: TheDao) :
        PagingSource<Int, NasaImageRecord>() {


        override fun getRefreshKey(state: PagingState<Int, NasaImageRecord>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NasaImageRecord> {
            return try {
                val page = params.key ?: 0
                val data: List<NasaImageRecord> = theDao.getImages(PAGING_SIZE)

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