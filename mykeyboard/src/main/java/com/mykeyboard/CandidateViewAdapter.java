package com.mykeyboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.openwnn.legacy.CLOUDSONG.OnCandidateSelected;
import com.googlecode.openwnn.legacy.WnnWord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.mykeyboard.CandidateViewAdapter.java
 * @author: myName
 * @date: 2020-01-08 18:11
 */
class CandidateViewAdapter extends RecyclerView.Adapter<CandidateViewAdapter.ViewHolder> {
    private List<WnnWord> mList;
    private OnCandidateSelected mOnCandidateSelected;

    public CandidateViewAdapter(OnCandidateSelected candidateSelected) {
        mList =new ArrayList<>();
        this.mOnCandidateSelected = candidateSelected;
    }
    public void addData(List<WnnWord> mList){
        this.mList.clear();
        this.mList=mList;
        notifyDataSetChanged();
    }
    public void delData(){
        this.mList.clear();
        notifyDataSetChanged();
    }
    public void setOnCandidateSelected(OnCandidateSelected candidateSelected) {
        this.mOnCandidateSelected = candidateSelected;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final  int position) {
        final WnnWord bean = mList.get(position);
        holder.item_name.setText( bean.candidate);
        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCandidateSelected!=null){
                    mOnCandidateSelected.candidateSelected(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        else
            return mList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);

        }
    }
}

