# PayNearby Android Task
# Android-Wardrobe-App

Wardrobe is an app that suggests you combinations of clothes for daily wear. The home screen is divided into
two sections, each displaying a shirt (top) and a pair of trousers (bottoms). You can add more shirts or pants
by clicking on the ‘+’ button on the right corner of each section which will give you an option to select an image
from Gallery or use the camera to upload the top & trousers.Once uploaded it is stored in local database. You
can also swipe left and right each section if you don’t like the combination of top & trousers. In the centre,
there's a SHUFFLE button which will shuffle both the sections and a display a random combination. On its right
is a ‘favourite’ button if you like an outfit combination and whenever the combination selected as favourite
appears the favourite icon changes to red.

Functionality:
1. Home Screen (the above wireframes) with a neat UI.
2. Support for both camera and gallery to add shirts and pants (need DB support).
3. You can swipe through both shirts and pants horizontally to see new combination. You can use
ViewPager/(UIScrollView & UIPageControl) class for that.
4. Shuffle button to display random combination of shirt and pants. Need a basic algorithm to show a new
combination every time. Basically when to choose from the favourite section or giving completely new
combination and all.
5. 'Favourite' button to mark combination as favourite
