# CarouselView

[![](https://jitpack.io/v/KyawSoeW1n/CarouselView.svg)](https://jitpack.io/#KyawSoeW1n/CarouselView)

CarouselView is a image slider library.Effect are describe as the following.
   - ANTI_CLOCK_SPIN
   - CAROUSEL_EFFECT
   - CLOCK_SPIN
   - CUBE_IN_ROTATION
   - CUBE_OUT_ROTATION
   - DEPTH
   - FADE_OUT
   - HORIZONTAL_FLIP
   - SIMPLE
   - SPINNER
   - VERTICAL_FLIP
   - VERTICAL_SHUT

#### Add Dependency
Add that dependency to project gradle
```sh
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add that dependency to app gradle
```sh
dependencies {
	       implementation 'com.github.KyawSoeW1n:CarouselView:1.0.0'
	}
```

### Usages
```sh
<com.kurio.carousel.ui.Slider
    android:layout_width="match_parent"
    android:layout_height="@dimen/slider_card_height"
    app:animateIndicators="true"
    app:defaultIndicators="circle"
    app:indicatorSize="@dimen/spacing_8dp"
    app:intervalSecond="5"
    app:loopSlides="true"
    app:sliderImageHeight="@dimen/slider_card_height"/>
```

| Attributes   |      Values      |       Reason      | 
|----------|:-------------:|:-------------:|
|indicatorSize|dimension| Indicator Size
|pageMargin|dimension| Margin Between Indicator 
|paddingTop|dimension| Padding Top
|paddingLeft|dimension| Padding Left
|paddingRight|dimension| Padding Right
|paddingBottom|dimension| Padding Bottom
|defaultIndicators|integer|  Indicator Style      
|animateIndicators|boolean| Animate Indicator
|intervalSecond|integer| Interval time for image change
|loopSlides|boolean| Rotate Image
|hideIndicators|boolean| Hide/Show Indicator
|sliderPlaceholderImage|reference| Place Holder During Loading Image
|sliderErrorImage|reference| For Error Case
|sliderImageHeight|dimension| Image Height
|defaultTransition|integer |  Effect 
|defaultTransform|integer|  Scale Type
|indicatorActiveColor|color| Select Indicator Color
|indicatorInactiveColor|color| Unselect Indicator Color

There are 4 indicator style
   - Dash
   - Circle
   - Square
   - Round Square

For sliderPlaceholderImage, you can use drawable image or color.
    

