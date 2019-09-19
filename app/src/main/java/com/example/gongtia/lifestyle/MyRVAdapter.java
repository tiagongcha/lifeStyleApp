package com.example.gongtia.lifestyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder> {
    private List<ModuleButton> mbtn_img_ListItems;
    private Context mContext;
    private OnTransferListener mTransferListener;

    public static int positionGetter;


    public MyRVAdapter(List<ModuleButton> inputList) {
        mbtn_img_ListItems = inputList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View itemLayout;
        protected ImageButton btn_image_itemData;
        protected TextView tv_btn_label;

        public ViewHolder(View view){
            super(view);
            itemLayout = view;
            btn_image_itemData = view.findViewById(R.id.btn_img_dashboard_item);
            tv_btn_label = view.findViewById(R.id.tv_btn_label);
        }
    }
    @NonNull
    @Override
    public MyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.module_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRVAdapter.ViewHolder holder, int position) {
        try {
            mTransferListener = (OnTransferListener) mContext;
        } catch (Exception err) {

        }

        holder.btn_image_itemData.setImageDrawable(mbtn_img_ListItems.get(position).getImage());
        holder.tv_btn_label.setText(mbtn_img_ListItems.get(position).getText());

        holder.btn_image_itemData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mTransferListener.onTransferPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mbtn_img_ListItems.size();
    }

    /**create an interface then transfer the position from the adapter to the activity.
     *
     */
    public interface OnTransferListener {
        void onTransferPosition(int position);
    }
}
