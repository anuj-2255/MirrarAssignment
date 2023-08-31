package com.example.testassignment.di


import com.example.testassignment.model.repo.AppRepository
import org.koin.dsl.module

val repoModule = module {

    single { AppRepository(get()) }



/*


//  This Repository is for GOOGLE Places Api
    single { GooglePlacesRepository(get(named(ApiConstant.GOOGLE_API_BASE_TAG))) }

//  Following Repositories are for All Other App Functions besides Google Places Api
    single { OnboardRepository(get(named(ApiConstant.GLOBAL_LIMOS_BASE_TAG))) }
    single { HomeRepository(get(named(ApiConstant.GLOBAL_LIMOS_BASE_TAG))) }


*/



}