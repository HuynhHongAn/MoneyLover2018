package com.nmcnpm.nhom10.moneylover;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<TransactionModel> implements View.OnClickListener{

    private ArrayList<TransactionModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvTransactionName;
        TextView tvTransactionDate;
        TextView tvTransactionAmount;
        TextView tvNote;
        ImageView tvTransactionIcon;
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
        TransactionModel TransactionModel=(TransactionModel)object;

        switch (v.getId())
        {
            case R.id.ivTransactionIcon:
                Snackbar.make(v, "Release date " + TransactionModel.getIcon(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TransactionModel TransactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.transaction_row, parent, false);
            viewHolder.tvTransactionName = (TextView) convertView.findViewById(R.id.tvTransactionName);
            viewHolder.tvTransactionDate = (TextView) convertView.findViewById(R.id.tvTransactionDate);
            viewHolder.tvTransactionAmount = (TextView) convertView.findViewById(R.id.tvTransactionAmount);
            viewHolder.tvNote = (TextView) convertView.findViewById(R.id.etNote);
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

        viewHolder.tvTransactionName.setText(TransactionModel.getName());
        viewHolder.tvTransactionDate.setText(TransactionModel.getDate());
        viewHolder.tvNote.setText(TransactionModel.getNote());
        viewHolder.tvTransactionAmount.setText(TransactionModel.getAmount().toString());
        viewHolder.tvTransactionIcon.setOnClickListener(this);
        viewHolder.tvTransactionIcon.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}


