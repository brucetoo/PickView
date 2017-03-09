
Forked from https://github.com/brucetoo/PickView

# PickView
This is a helper lib for us to pick date or province like IOS system 
WheelView widget.
Added feature to pick time with WheelView Widget

    
   ```java
          
         TimePickerPopWin timePickerPopWin=new TimePickerPopWin.Builder(MainActivity.this, new       TimePickerPopWin.OnTimePickListener() {
                    @Override
                    public void onTimePickCompleted(int hour, int minute, String AM_PM, String time) {
                        Toast.makeText(MainActivity.this, time, Toast.LENGTH_SHORT).show();
                    }
                }).textConfirm("CONFIRM")
                        .textCancel("CANCEL")
                        .btnTextSize(16)
                        .viewTextSize(25)
                        .colorCancel(Color.parseColor("#999999"))
                        .colorConfirm(Color.parseColor("#009900"))
                        .build();
                timePickerPopWin.showPopWin(MainActivity.this);
      
   ```


