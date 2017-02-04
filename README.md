#Material Wheel View 
>:ferris_wheel: Prettier and simpler IOS like WheelView and other built-in WheelView UIs

[![License](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://opensource.org/licenses/Apache-2.0) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Material%20WheelView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5184) [![](https://jitpack.io/v/BlackBoxVision/material-wheel-view.svg)](https://jitpack.io/#BlackBoxVision/material-wheel-view)

##Installation

**Gradle**

- Add it in your root build.gradle at the end of repositories:

```java
repositories {
	maven { 
	    url "https://jitpack.io"
	}
}
```

- Add the dependency:

```java
dependencies {
    compile 'com.github.BlackBoxVision:material-wheel-view:v0.0.1'
}
```
**Maven**

- Add the JitPack repository to your maven file. 

```xml
<repository>
     <id>jitpack.io</id>
     <url>https://jitpack.io</url>
</repository>
```
- Add the dependency in the form

```xml
<dependency>
    <groupId>com.github.BlackBoxVision</groupId>
    <artifactId>>material-wheel-view</artifactId>
    <version>v0.0.1</version>
</dependency>
```
**SBT**

- Add it in your build.sbt at the end of resolvers:

```java
resolvers += "jitpack" at "https://jitpack.io"
```

- Add the dependency in the form:

```java
libraryDependencies += "com.github.BlackBoxVision" % "material-wheel-view" % "v0.0.1"	
```

##Usage example

In your layout.xml:

```xml
<io.blackbox_vision.wheelview.view.WheelView
	android:id="@+id/loop_view"
	android:layout_width="200dp"
	android:layout_height="180dp"
	app:canLoop="true"
	app:centerTextColor="#ff000000"
	app:drawItemCount="7"
	app:initPosition="3"
	app:lineColor="@color/colorPrimary"
	app:textSize="25sp"
	app:topBottomTextColor="#ffafafaf"/>
```

In your activity class: 

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	setSupportActionBar(toolbar);

	final WheelView wheelView = (WheelView) findViewById(R.id.loop_view);

	wheelView.setInitPosition(2);
	wheelView.setCanLoop(false);
	wheelView.setLoopListener(item -> {});
	wheelView.setTextSize(12);
	wheelView.setItems(getList());
 }
 ```
 
 Also, there is a built-in UI called DatePickerPopUpWindow, you can use it like these: 
 
 ```java
 	findViewById(R.id.date).setOnClickListener(v -> {
		final DatePickerPopUpWindow datePicker = new DatePickerPopUpWindow.Builder(getApplicationContext())
			.setMinYear(1990)
			.setMaxYear(2550)
			.setSelectedDate("2013-11-11")
			.setOnDateSelectedListener(this::onDateSelected)
			.setConfirmButtonText("CONFIRM")
			.setCancelButtonText("CANCEL")
			.setConfirmButtonTextColor(Color.parseColor("#999999"))
			.setCancelButtonTextColor(Color.parseColor("#009900"))
			.setButtonTextSize(16)
			.setViewTextSize(15)
			.setShowDayMonthYear(true)
			.build();

		datePicker.show(this);
	});			
```			

##Issues

If you found a bug, or you have an answer, or whatever. Please, open an [issue](https://github.com/BlackBoxVision/material-wheel-view/issues). I will do the best to fix it, or help you.

##Contributing

Of course, if you see something that you want to upgrade from this library, or a bug that needs to be solved, **PRs are welcome!**

##TODO 
- [ ] Custom **DatePicker** component based on **WheelView**
- [ ] Custom **TimePicker** component based on **WheelView**
- [ ] Custom **DatePickerDialog** based on DatePicker **WheelView**
- [ ] Custom **TimePickerDialog** based on DatePicker **WheelView**
