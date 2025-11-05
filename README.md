# 🍳 Lazzat — Your Smart Recipe Finder App

Lazzat is a modern recipe discovery Android app that helps you find recipes based on the ingredients you already have.  
Just enter your ingredients, and Lazzat will fetch delicious recipes — complete with instructions, ingredients list, YouTube tutorials, and more!

---

## 🚀 Features

### 🏠 Home
- Enter ingredients manually or select from your pantry.  
- Fetch recipes using a recipe API.  
- Apply filters like cuisine, region, diet type, and cooking time.  
- Get recipe results with:
  - Full summary  
  - List of ingredients  
  - Step-by-step instructions  
  - YouTube link for video tutorials  

### 🧺 Pantry
- Save ingredients for quick access in the future.  
- Helps speed up recipe search.  
- Perfect for everyday cooking or when you’re short on time.

### ❤️ Favourites
- Save your favourite recipes.  
- Access them anytime offline.  
- Simple and elegant Room Database integration.

### 👤 Profile
- Manage user preferences.  
- Change language, theme, or other personalization options (coming soon).

---

## 🛠️ Tech Stack

| Layer | Technologies |
|-------|---------------|
| **Frontend** | Kotlin, Jetpack Components (Navigation, ViewBinding, LiveData) |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Backend / Data** | REST API Integration (Retrofit), Room Database |
| **Other Tools** | Coroutines, Glide for image loading, SharedPreferences for settings |

---

## 🌐 API Integration
Lazzat uses a recipe API to fetch real-time recipes based on entered ingredients.  
It parses data such as:
- Recipe summary  
- Ingredients and instructions  
- YouTube cooking tutorials  

---

## 📸 Screenshots

| Home Screen | Pantry | Favourites | Result | Recipe Details | Recipe Details | Recipe Details | 
|-------------|-------------|-------------|-------------|-------------|-------------|-------------|
| ![Home](screenshots/homepage.png) | ![Pantry](screenshots/pantrypage.png) | ![Favourites](screenshots/favouritepage.png) | ![Result](screenshots/resultpage.png) | ![Details](screenshots/detailpage1.png) | ![Details](screenshots/detailpage2.png) | ![Details](screenshots/detailpage3.png) 

---

## ⚙️ Installation

1. Clone this repository  
   ```bash
   git clone https://github.com/Ariifff/Lazzat.git
2. Open in Android Studio
3. Build and run the app on an emulator or physical device.

---

## 📱 Future Enhancements

- Add login & user authentication (Firebase or Clerk).
- Multi-language support.
- Personalized recipe recommendations using AI.
- Dark mode theme.

---

## 🤝 Contributing

Contributions are always welcome!
If you’d like to improve features, fix bugs, or enhance UI — feel free to fork the repo and create a pull request.

---

## 🧑‍💻 Author

Mohd Arif Rainee
- 🎓 B.Tech Computer Engineering, Aligarh Muslim University
- 📍 Android Developer | Firebase | Jetpack | Retrofit

---

⭐ If you like this project, don’t forget to give it a star!
