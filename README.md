# Master Meme - Modern Android Meme Creation App

Master Meme is a sleek Android meme creation application developed as part of the [Mobile Dev Campus Monthly Challenge](https://pl-coding.com/campus/) by Philipp Lackner for December of 2024. It demonstrates modern Android development practices and architecture while providing an intuitive meme creation and management experience.

## Features

- 🎨 Easy-to-use meme creation interface
- 📱 Draggable text boxes with customizable fonts and colors
- ⭐ Favorite memes organization
- 🔄 Undo/redo support for edits
- 💾 Save memes to device gallery
- 🔍 Searchable meme template library
- 📤 Share created memes directly to other apps
- 🎯 Material Design 3 UI with Jetpack Compose

## Technical Highlights

This project showcases modern Android development practices and technologies:

### Architecture & Design Patterns
- Clean Architecture principles
- MVVM pattern with UI States
- Repository pattern for data management
- Single Activity architecture

### Android Jetpack
- **Compose**: Modern declarative UI with gesture support
- **Room**: Local database for meme storage
- **Hilt**: Dependency injection
- **DataStore**: User preferences storage
- **Navigation**: Single Activity navigation
- **Coil**: Efficient image loading and caching

### Other Technologies & Libraries
- **Kotlin**: 100% Kotlin codebase with coroutines and flows
- **Material Design 3**: Modern and consistent UI/UX
- **Version Catalog**: Dependency management

## Implementation Details

### Meme Creation Engine
- Custom composables for draggable text elements
- Save state handling for undo/redo functionality
- Support for multiple text styles and colors

### Data Management
- Room database for created memes storage
- Efficient image caching strategy
- File system integration for saving memes
- Content provider integration for sharing

### UI Features
- Bottom sheet implementation for template selection
- Multi-select support for batch operations
- Search functionality with filtering
- Responsive grid layouts

## Building The Project

1. Clone the repository
```bash
git clone https://github.com/BLTuckerDev/MasterMeme.git
```

2. Open the project in Android Studio (latest version recommended)

3. Build and run the project

## Requirements
- Minimum SDK: 27 (Android 8.1)
- Target SDK: 35 (Android 15)
- Kotlin 1.9.24

## Credits

This project was developed as part of the Mobile Dev Campus Monthly Challenge by [Philipp Lackner](https://pl-coding.com/campus/). The challenge provided an opportunity to demonstrate Android development expertise while building a creative, user-focused application.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.