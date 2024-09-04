# Movie Database
## 🎬 An Android application built using Kotlin, Data binding, MVVM architecture, LiveData, and ViewModel.

Movie Database is a fully functional Android app built entirely with Kotlin and Jetpack Compose. Using MVVM Architecture pattern with View model,Life data and dependency injection with Hilt. For seamless experience between online and offline state, cahcing data with Room local database and for load network image and caching with Glide. Using latest recommended best practices tech stack for Android development. 

# Tech stack & Open-source libraries
- MVVM Architecture: Separation of concerns and testability.
- Data binding: Bind UI components in XML layouts
- LiveData: Observable data holder class.
- ViewModel: Manage UI-related data in a lifecycle-conscious way.
- Kotlin
- Room: For offline caching
- Glide: For Network image and cache
- Retrofit: For http request
- OkHttp3: implementing interceptor, logging
- Core Splash Api
- Paging3: pagination load data from network
- Youtube player: for load movie and tv series trailer
- Di: Hilt
- Kotlin Coroutines: Asynchronous computation

Architecture
This project follows the MVVM architecture pattern:
- Model: Represents the data layer. This includes data models and repository classes.
- View: Represents the UI layer. This includes Jetpack Compose UI components.
- ViewModel: Manages the UI-related data and handles the business logic.
- LiveData
- ViewModel
- Kotlin Coroutines

Code Structure
- model: Contains data models.
- repository: Contains repository classes.
- dao: Contains data access object for room.
- view: Contains UI components built with Jetpack Compose.
- viewmodel: Contains ViewModel classes.
- paging: Pagination data source.
- di: App module
- adapter: Recycler view adapter
- db: Room database
