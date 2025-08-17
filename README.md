# MyAnime App

A simple, local-first Android application that fetches and displays a list of top anime from the [Jikan API](https://jikan.moe/). The app is built with modern Android development best practices, including clean architecture, offline support, and robust error handling.

---

Demo & Download
<video src="https://raw.githubusercontent.com/amanroy3901/my-anime-app/main/release/demo_video.mp4" controls loop muted width="100%"></video>

Download the APK: MyAnime.apk

## Features Implemented

This project fulfills all the core requirements and bonus features outlined in the assignment:

- **Anime List Page**: Displays a paginated list of top-rated anime with their title, image, and score.
- **Anime Detail Page**: Tapping on an anime opens a details page that displays the synopsis, genres, score, episode count, trailer (if available), and the main Japanese voice cast.
- **Local Database with Room**: All fetched anime and detail data are stored locally in a Room database, ensuring data persistence and fast loading.
- **Offline Mode & Syncing**:
  - The app functions seamlessly without an internet connection, loading data from the local Room database.
  - It intelligently detects network connectivity changes and automatically triggers a data sync when the device comes back online, ensuring the data is always up-to-date.
- **Error Handling**:
  - Graceful handling of network failures and empty data states.
  - Displays a user-friendly message when there is no internet connection and no local data to show.
- **Design Patterns & Architecture**:
  - The project follows the **MVVM (Model-View-ViewModel)** architecture, ensuring a clear separation of concerns, testability, and maintainability.
  - Uses a sealed class for UI state management, allowing the Composables to react to different states (Loading, Success, Error).
- **Design Constraint Handling**: The UI is designed to be flexible. The Cast section includes a toggle to show/hide voice actor profile images, demonstrating the ability to handle design constraints (like a legal change) without re-writing the layout.

### Problem-Solving & Personal Input
- **Network Resilience**: API calls are conditionally executed only when a network connection is available.
- **Automatic Data Syncing**: The ViewModel automatically refetches data when the device's network status changes from offline to online.
- **Pagination**: The `LazyVerticalGrid` on the main screen automatically loads the next page of data when the user scrolls to the end of the list.
- **UI Framework**: The entire UI is developed using **Jetpack Compose**, as no specific UI framework was mentioned in the requirements.

---

## Core Libraries

| Library                | Purpose                                                                 |
|------------------------|-------------------------------------------------------------------------|
| Jetpack Compose        | Declarative UI toolkit for building native UIs.                        |
| Hilt                   | Dependency Injection framework to manage dependencies and simplify testing. |
| Retrofit               | Type-safe HTTP client for making API requests to the Jikan API.         |
| Room                   | Persistence library that provides an abstraction layer over SQLite.    |
| Glide (via ExperimentalGlideComposeApi) | Efficient image loading library for displaying images. Note: Glide support in Compose is still in beta, and while there are more stable third-party alternatives, the official library was chosen for professional alignment and requirement compliance. |
| Kotlin Coroutines & Flows | For asynchronous and reactive data handling.                        |

---

## Assumptions Made

- The primary source of truth is the local Room database.
- The app only syncs with the API on initial load or when the network connection is restored. A pull-to-refresh mechanism was not implemented but would be a logical next step.
- The "main cast" is determined by filtering for characters with the role "Main" and a Japanese voice actor. **Note**: The Jikan API does not provide comprehensive cast data as per the requirements, so you may need to explore other APIs for complete cast information.
- The `NoDataAndNoNetworkScreen` provides a "Retry" button that triggers a re-fetch, which will succeed only when a network connection is available.
- **Trailer Playback**: Most trailers available in the API are YouTube links. Due to YouTube's policies, these cannot be played directly in Media3 or ExoPlayer. A third-party library was used for video playback to handle user interaction and control limitations.

---

## Known Limitations

- The current synchronization strategy is a simple fetch-and-sync. A more advanced solution might involve timestamps or other mechanisms to prevent unnecessary data fetches.
- More granular error handling could be implemented for specific API error codes (e.g., 404, 500) to display more precise error messages to the user.
- Glide's Compose support is in beta, which may introduce instability as per the official documentation.

