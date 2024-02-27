# Tasks To-Do App

A basic to-do android application to store tasks with a due date and completion status using Room DB and Jetpack Compose UI.

## Features

The Android app lets you:
- Create & Update tasks with a title, due date and completion status.
- Order tasks by their due date or completion status.
- Delete all completed tasks on single click.

## Screenshots

[<img src="https://github.com/avishekdas128/tasks-todo-compose/assets/43132866/74de403c-c8a3-4c3e-b8bb-5b13fbf7fc94" align="left"
width="180"
hspace="1" vspace="1">](ss1.png)
[<img src="https://github.com/avishekdas128/tasks-todo-compose/assets/43132866/c987cd2e-7692-4514-8354-e268abf1fd41" align="center"
width="180"
hspace="1" vspace="1">](ss2.png)
[<img src="https://github.com/avishekdas128/tasks-todo-compose/assets/43132866/1df5c15d-e991-4627-9aa1-34c60356bd65" align="left"
width="180"
hspace="1" vspace="1">](ss3.png)


## Tech stack
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- Dagger-Hilt for dependency injection.
- JetPack
    - State - Notify domain layer data to views.
    - Flows - To emit and collect UI state changes.
    - Lifecycle - Dispose of observing data when the lifecycle state changes.
    - ViewModel - UI-related data holder, lifecycle aware.
    - [Compose](https://developer.android.com/jetpack/compose) - Render UI.
- Architecture
    - MVVM Architecture (Model - View - ViewModel)
    - Repository pattern
    - data ~ domain ~ presentation
- [Room Database](https://developer.android.com/jetpack/androidx/releases/room) - For offline caching of data
