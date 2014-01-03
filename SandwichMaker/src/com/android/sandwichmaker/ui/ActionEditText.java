package com.android.sandwichmaker.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ActionEditText extends EditText {

    public ActionEditText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ActionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ActionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        if (!text.toString().isEmpty())
            //updateSuggestions(text);


            // TODO Auto-generated method stub
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

}
