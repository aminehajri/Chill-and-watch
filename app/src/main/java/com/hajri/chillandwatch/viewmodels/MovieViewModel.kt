package com.hajri.chillandwatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hajri.chillandwatch.models.Movie
import com.hajri.chillandwatch.repositories.IMovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class MovieViewModel(
    private val repository: IMovieRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val _listOfMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    val listOfMovies: LiveData<List<Movie>> get() = _listOfMovies

    fun getMovieList() {
        disposables.add(
            repository.getLatestMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _listOfMovies.postValue(it)
                    }, { error ->
                        Timber.e(error)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}