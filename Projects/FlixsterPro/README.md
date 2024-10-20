# Android Project 4 - *FlixsterPro*

Submitted by: **Tsewang Sherpa**

**FlixsterPro** is a movie browsing app that allows users to explore upcoming movies, learn about them, and check out popular actors.

**Time spent:** **4** hours in total

## Required Features

The following **required** functionality has been completed:

- [X] **Choose any endpoint on The MovieDB API except `now_playing`**
    - Chosen Endpoints: [Upcoming](https://developer.themoviedb.org/reference/movie-upcoming-list) and [Popular](https://developer.themoviedb.org/reference/person-popular-list)

- [X] **Make a request to your chosen endpoint and implement a RecyclerView to display all entries**
- [X] **Use Glide to load and display at least one image per entry**
- [X] **Click on an entry to view specific details about that entry using Intents**

The following **optional** features have been implemented:

- [X] **Add another API call and RecyclerView that lets the user interact with different data**
- [X] **Add rounded corners to the images using Glide transformations**
- [X] **Implement a shared element transition when the user clicks into the details of a movie**

## Video Walkthrough

Here's a walkthrough of the implemented user stories: the chosen endpoints on The MovieDB API are implemented, requests are made and displayed in a RecyclerView, images are loaded using Glide, and clicking on an entry shows detailed information. Additionally, the following optional features are also implemented: an additional API call with its own RecyclerView, rounded corners for images with Glide transformations, and a shared element transition for movie details, all demonstrated in the following GIF.

![Video Walkthrough](./FinalGifAnimation.gif)

GIF created with [ScreenToGif](https://www.screentogif.com/) for Windows.

## Notes

The beginning was a bit challenging due to dependency issues. I had to clone a repository with compatible dependencies already added, which allowed me to start the project and complete it smoothly.

## License

    Copyright [2024] [TsewangSherpa]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.