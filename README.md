
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickView-green.svg?style=true)](https://android-arsenal.com/details/1/2811)

# PickView
This is a helper lib for us to pick date or province like IOS system 
WheelView widget.

# Here is date pick effect by gif

![picker](./datepick.gif)

##How to use
> 1. Pick date is even with single step...

  ```java
      
    DatePickerPopWin pickerPopWin = new DatePickerPopWin(MainActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                       @Override
                       public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                       //handler the result here
                           Toast.makeText(MainActivity.this,dateDesc,Toast.LENGTH_SHORT).show();
                       }
                   });
  
  ```

#Dependencies

```java 

   compile 'com.brucetoo:pickview:1.0.0'
 
```

##TODO

- [ ] add Province pick PopWindow (if i have time)

- [x] add to Jcenter (Wait Jcenter manager verify)

------

#THANKS
 [androidWheelView](https://github.com/weidongjian/androidWheelView) 
 
## License

Copyright 2015 Bruce too

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See [LICENSE](LICENSE) file for details.