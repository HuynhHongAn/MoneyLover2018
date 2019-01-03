package com.nmcnpm.nhom10.moneylover;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<TransactionModel> implements View.OnClickListener{

    private ArrayList<TransactionModel> dataSet;
    Context mContext;

    NumberFormat format = NumberFormat.getCurrencyInstance();

    private static final String TAG = "TransactionAdapter";

    // View lookup cache
    private static class ViewHolder {
        TextView tvAmount;
        TextView tvName;
        TextView tvDate;
        TextView tvNote;
        ImageView tvTransactionIcon;
        TextView tvCurrency;
    }

    public TransactionAdapter(ArrayList<TransactionModel> data, Context context) {
        super(context, R.layout.transaction_row, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TransactionModel transactionModel=(TransactionModel)object;

        switch (v.getId())
        {
            case R.id.ivTransactionIcon:
                Snackbar.make(v, "Release date (add transaction wallet here)", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.transaction_row, parent, false);
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvNote = (TextView) convertView.findViewById(R.id.tvNote);
            viewHolder.tvTransactionIcon = (ImageView) convertView.findViewById(R.id.ivTransactionIcon);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        assert transactionModel != null;
        viewHolder.tvAmount.setText(format.format(transactionModel.getAmount()));
        viewHolder.tvName.setText(transactionModel.getName());
        viewHolder.tvNote.setText(transactionModel.getNote());
        viewHolder.tvDate.setText(transactionModel.getDate());
        viewHolder.tvTransactionIcon.setOnClickListener(this);
        viewHolder.tvTransactionIcon.setTag(position);

        Log.d(TAG, "position: " +position + " amount: " + (Boolean) (transactionModel.getAmount() > 0));
        if (transactionModel.getAmount() > 0) {
            viewHolder.tvAmount.setTextColor(Color.parseColor("#256CDE"));
        }
        // Return the completed view to render on screen
        return convertView;
    }
}


