# MirrarAssignment

# NASA Image of the Day App

This Android app allows you to view NASA's "Image of the Day" along with its description. It uses NASA's API to fetch daily image data.

## Setup

1. **Clone the Repository:**
- git clone [repository_url].

2. **Obtain NASA API Key:**
- Visit the [NASA API Portal](https://api.nasa.gov) to obtain an API key.
- Copy the API key as you will need it for the app.

3. **Add API Key:**
- Open the project in Android Studio.
- Locate the file `Constants.kt` in the project's root directory.
- Add your NASA API key as follows:
  ```
  NASA_API_KEY="your_api_key_here"
  ```
- Replace `"your_api_key_here"` with the API key you obtained from NASA.

4. **Build and Run:**
- Build and run the app on your Android device or emulator using Android Studio.

## Usage

- Upon launching the app, you will see the NASA "Image of the Day" along with its title, date, and description.
- You can also refresh the image and data by pulling down to refresh or tapping a refresh button if implemented.

## Features

- Daily image retrieval from NASA's API.
- Caching of image data for faster loading and offline access.
- Graceful error handling for API request failures.
- UI includes an ImageView for displaying the image, and TextViews for title, date, and description.
- PlayerVuew for playing video content if applicable.

## Contributing

Contributions to this project are welcome. Feel free to open issues and pull requests to suggest improvements or report bugs.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


