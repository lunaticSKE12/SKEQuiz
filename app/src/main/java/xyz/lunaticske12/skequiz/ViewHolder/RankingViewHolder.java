package xyz.lunaticske12.skequiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import xyz.lunaticske12.skequiz.Interface.ItemClickListener;
import xyz.lunaticske12.skequiz.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_name, txt_score;
    private ItemClickListener itemClickListener;

    public RankingViewHolder(View itemView) {
        super(itemView);

        txt_name = itemView.findViewById(R.id.txt_name);
        txt_score = itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
