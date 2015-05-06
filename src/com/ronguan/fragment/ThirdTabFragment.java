package com.ronguan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ronguan.R;

public class ThirdTabFragment extends BaseFragment implements OnClickListener {
	private Context context;
	
	private EditText edtName, edtPsw;

	private Spinner mySpinner;
	
	private TextView tv;

	private Button btn;

	private List<String> list = new ArrayList<String>();

	private ArrayAdapter<String> adapter;
	
	private String workStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context=getActivity();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_third_fragment, container,
				false);
		initData();
		
		initView(view);

		return view;
	}

	void initView(View view) {
		edtName = (EditText) view.findViewById(R.id.edt_name);
		edtPsw = (EditText) view.findViewById(R.id.edt_psw);
		mySpinner = (Spinner) view.findViewById(R.id.spinner1);
		btn = (Button) view.findViewById(R.id.btn_login);
		btn.setOnClickListener(this);
		tv=(TextView)view.findViewById(R.id.tv_top_title);
		tv.setText(getResources().getString(R.string.login));
		
		adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, list);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        mySpinner.setAdapter(adapter);    
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                /* 将所选mySpinner 的值带入myTextView 中*/    
            	workStr=adapter.getItem(arg2);    
                /* 将mySpinner 显示*/    
                arg0.setVisibility(View.VISIBLE);    
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
            	workStr="NONE";    
                arg0.setVisibility(View.VISIBLE);    
            }    
        });    
        /*下拉菜单弹出的内容选项触屏事件处理*/    
        mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){    
            public boolean onTouch(View v, MotionEvent event) {    
                // TODO Auto-generated method stub    
                /** 
                 *  
                 */  
                return false;    
            }  
        });    
        /*下拉菜单弹出的内容选项焦点改变事件处理*/    
        mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
        public void onFocusChange(View v, boolean hasFocus) {    
            // TODO Auto-generated method stub    
  
        }    
        });    
	}

	void initData() {
		list.add("北京");
		list.add("上海");
		list.add("深圳");
		list.add("福州");
		list.add("厦门");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:

			break;

		default:
			break;
		}
	}
}
