# Audiophile

A simulated e-commerce application for musical tools. Created with the use of [Java](https://www.java.com/en/), [Android Studio](https://developer.android.com/studio),
[Firebase](https://firebase.google.com/).

<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/n-e-t-a-n/Audiophile">
    <img src="app/src/main/res/drawable/logo1.png" alt="Logo" height="80">
  </a>

<h3 align="center">Audiophile</h3>

  <p align="center">
    A feature-rich Android e-commerce application for browsing, shopping, and managing orders.
    <br />
    <br />
    <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/n-e-t-a-n/Audiophile">View Demo</a>
    ·
    <a href="https://github.com/n-e-t-a-n/Audiophile/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    ·
    <a href="https://github.com/n-e-t-a-n/Audiophile/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

About The Project
Audiophile is a fully functional Android-based e-commerce application that enables users to browse products relaetd to instruments and music, manage their shopping cart, view orders, and leave product reviews. The app is structured around modern Android development principles using Firebase for backend services like authentication and Firestore for data storage. The following features are available to users:

- Browse products based on categories.
- Add products to the shopping cart and manage cart items.
- Place orders and view their order history.
- Leave reviews and ratings on products.
- Manage user profiles and login securely via Firebase.
- This app showcases Firebase Integration for authentication and data storage, clean UI design, and efficient performance for seamless shopping experiences.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![Java][Java.com]][Java-url]
* [![Android Studio][AndroidStudio.com]][AndroidStudio-url]
* [![Firebase][Firebase.com]][Firebase-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

Follow these instructions to get a copy set up in your local machine.
It is recommended that you use Android Studio's native set-up for Github projects.

[Online Tutorial for Cloning Using Android Studio](https://www.geeksforgeeks.org/how-to-clone-android-project-from-github-in-android-studio/)

<!-- USAGE EXAMPLES -->
## Usage

Usage
- Launch the App

Users start on the Home screen, showing featured products.
- Shop for Products

Navigate to the Shop tab to browse and filter products.
- Manage Cart

Add items to the cart and view the total price.
- Place Orders

Checkout to place an order, which is saved in order history.
- Leave Reviews

Open any product page to submit reviews.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Features

1. **User Authentication**  
   - Secure login and registration using **Firebase Authentication**.  
   - Validation of input fields to ensure proper credentials.

2. **Product Listing and Filtering**  
   - View products dynamically fetched from **Firestore** and displayed in a **RecyclerView**.  
   - **Category Filtering**: Filter products by categories such as **Guitar**, **Bass**, **Keyboard**, and **Drums**.  
   - **Search Functionality**: Real-time search to filter product listings based on user input.

3. **Product Details**  
   - Tap on any product to view:  
     - Product image, name, description, price, and stock availability.  
   - Option to **add the product to the cart**.

4. **Shopping Cart**  
   - Add and manage items in the shopping cart:  
     - Increase or decrease item quantities.  
     - Delete items.  
   - View the **total price** and proceed to checkout.

5. **Order Management**  
   - Place orders and view the order history in a structured list format.  
   - Each order shows product details, price, and the date of purchase.

6. **Product Reviews**  
   - Users can submit:  
     - **Star Ratings** using a rating bar.  
     - Written reviews for each product.  
   - Reviews are displayed in a **RecyclerView** on the product details page.

7. **User Profile**  
   - View and update user details like email.  
   - Change the profile picture using file selection.  
   - Secure **logout functionality**.

8. **Navigation**  
   - A bottom navigation bar provides quick access to:  
     - **Home**: View featured products.  
     - **Shop**: Browse and filter products.  
     - **Orders**: Access order history.  
     - **Profile**: Manage user settings.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Technical Highlights

1. **Firebase Integration**  
   - **Firebase Authentication**: For secure user login, registration, and session management.  
   - **Firebase Firestore**: Real-time database for storing products, user orders, reviews, and cart data.

2. **RecyclerView for Lists**  
   - Efficiently handles product listings, cart items, order history, and reviews with custom adapters for each.

3. **Image Management**  
   - **Glide Library** is used to efficiently load and display product and profile images.

4. **Dynamic UI Design**  
   - Built with a combination of:  
     - **ConstraintLayout** for responsive and clean layouts.  
     - **LinearLayout** and **RelativeLayout** for modular design.

5. **Custom Drawables**  
   - Implemented custom drawable resources for:  
     - Rounded buttons, search bars, and backgrounds.  
     - Improved UI consistency and aesthetics.

6. **Input Validation**  
   - Ensures fields such as email, password, and review content are correctly formatted before submission.

7. **File Selection**  
   - Integrated with file picker to allow users to change profile images using `ActivityResultLauncher`.

8. **Bottom Navigation Bar**  
   - Seamless navigation between major sections: Home, Shop, Orders, and Profile.

9. **Modular Architecture**  
   - Separate **activities** for key features:  
     - **LoginActivity**, **CartActivity**, **OrderActivity**, **ReviewActivity**, etc.  
   - Custom **adapters** for modular UI rendering.

10. **Android Permissions**  
    - Internet and network permissions configured in `AndroidManifest.xml`.
      
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->
## Roadmap

 - Add payment gateway integration (Stripe/PayPal)
 - Implement push notifications for order status
 - Introduce a wishlist feature
 - Improve UI for better user experience

See the [open issues](https://github.com/github_username/repo_name/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Nathan Arriesgado - nathan.arriesgado0g@gmail.com

Project Link: [https://github.com/n-e-t-a-n/Audiophile](https://github.com/n-e-t-a-n/Audiophile)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the project_license. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
[Java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/en/
[AndroidStudio.com]: https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=AndroidStudio&logoColor=white
[AndroidStudio-url]: https://developer.android.com/studio
[Firebase.com]: https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black
[Firebase-url]: https://firebase.google.com/
