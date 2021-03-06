package com.zylbaby.app.view;

import java.util.Calendar;




import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickSelect implements OnClickListener {
	
	
	
	public interface DateSelectListener{
		void ReturnDate(String text);
	}
	
	public  DateSelectListener dateselectlistener=null;
	
	
//	private int year, monthOfYear, dayOfMonth;
	private EditText t;
	private Context _context;
	public DatePickSelect(){
		Init();
	}
	public DatePickSelect(Context c){
//		setT(t);
		set_context(c);
		Init();
	}
	private int year, monthOfYear, dayOfMonth;
	private void Init(){
      
	Calendar calendar = Calendar.getInstance();
      year = calendar.get(Calendar.YEAR);
      monthOfYear = calendar.get(Calendar.MONTH);
      dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

	}
	
	@Override
	public void onClick(View view)
    {
        /**
         * ʵ��һ��DatePickerDialog�Ķ���
         * �ڶ���������һ��DatePickerDialog.OnDateSetListener�����ڲ��࣬���û�ѡ������ڵ��done����������onDateSet����
         */
        DatePickerDialog datePickerDialog = new DatePickerDialog(get_context(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth)
            {
                //getT().setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                if(DatePickSelect.this.dateselectlistener!=null){
					try
					{
						DatePickSelect.this.dateselectlistener.ReturnDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
            }
        }, year, monthOfYear, dayOfMonth);
        
        datePickerDialog.show();
    }
//	public EditText getT() {
//		return t;
//	}
//	public void setT(EditText t) {
//		this.t = t;
//	}
	public Context get_context() {
		return _context;
	}
	public void set_context(Context _context) {
		this._context = _context;
	}

}
