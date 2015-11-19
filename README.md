
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
  

> 2.Here are 4 constructors for you to choose
  
  ```java
  
    /**
       * Constructor with special date and default min max year
       *
       * @param cxt
       * @param dataDesc like:1900-01-02
       * @param l
       */
      public DatePickerPopWin(Context cxt, String dataDesc,
                              OnDatePickedListener l) {
          this(cxt, DEFAULT_MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR),
                  dataDesc, l);
      }
  
      /**
       * Constructor with default date
       *
       * @param cxt
       * @param l
       */
      public DatePickerPopWin(Context cxt,
                              OnDatePickedListener l) {
          this(cxt, DEFAULT_MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR), l);
      }
  
      /**
       * Constructor with special date and minYear,maxYear
       *
       * @param cxt
       * @param minYear
       * @param maxYear
       * @param dataDesc like:1900-01-02
       * @param l
       */
      public DatePickerPopWin(Context cxt, int minYear, int maxYear,
                              String dataDesc, OnDatePickedListener l) {
  
          this.mContext = cxt;
          this.minYear = minYear;
          this.maxYear = maxYear;
          this.mListener = l;
  
          setSelectedDate(dataDesc);
          initView();
      }
  
      /**
       * Constructor with default date (right now)
       *
       * @param cxt
       * @param minYear
       * @param maxYear
       * @param l
       */
      public DatePickerPopWin(Context cxt, int minYear, int maxYear, OnDatePickedListener l) {
  
          this.mContext = cxt;
          this.minYear = minYear;
          this.maxYear = maxYear;
          this.mListener = l;
  
          setSelectedDate(getStrDate());
          initView();
      }
  
  ```

##TODO

- [ ] add Province pick PopWindow (if i have time)

- [ ] add to Jcenter

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