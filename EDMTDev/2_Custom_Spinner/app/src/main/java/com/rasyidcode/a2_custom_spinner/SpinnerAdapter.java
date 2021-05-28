package com.rasyidcode.a2_custom_spinner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater inflater;

    public SpinnerAdapter(List<String> listData, Activity activity) {
        this.listData = listData;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View _view = view;

        if (view == null) {
            _view = inflater.inflate(R.layout.item_spinner, null);
        }

        TextView spinnerItemText = _view.findViewById(R.id.spinner_item_text);
        spinnerItemText.setText(listData.get(i));

        return _view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        LinearLayout linearLayout = (LinearLayout) view;
        TextView spinnerItemText = linearLayout.findViewById(R.id.spinner_item_text);
        spinnerItemText.setGravity(Gravity.START);
        spinnerItemText.setTextColor(Color.parseColor("#FA331A"));
        spinnerItemText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        return view;
    }
}
