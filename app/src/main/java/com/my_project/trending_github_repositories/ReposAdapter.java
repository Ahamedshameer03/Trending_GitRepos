package com.my_project.trending_github_repositories;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> implements Filterable {

    Context context;
    List<RepoModel> data;
    List<RepoModel> dataList;

    private final onclickListener onclickListener;

    public ReposAdapter(Context context, List<RepoModel> data, onclickListener onclickListener) {
        this.context = context;
        this.data = data;
        dataList = new ArrayList<>(this.data);
        this.onclickListener = onclickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onclickListener);
        return viewHolder;
    }

    private String toUpper(String str){
        String firstLetter = str.substring(0,1);
        String remLetters = str.substring(1);
        str = firstLetter.toUpperCase() + remLetters;
        Log.d("STRING", str);
        return str;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.author.setText(toUpper(data.get(position).getAuthor()));
        holder.name.setText(toUpper(data.get(position).getName()));
        holder.language.setText(data.get(position).getLanguage());
        holder.description.setText(data.get(position).getDescription());
        holder.stars.setText("" + data.get(position).getStars());

        Glide.with(context).load(data.get(position).avatar).into(holder.avatarDp);

    }

    public interface onclickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Log.d("INSIDE", "PERF");
            List<RepoModel> filteredList = new ArrayList<RepoModel>();

            for (RepoModel repoModel : dataList) {
                try {
                    if (repoModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(repoModel);
                        Log.d("NAME", repoModel.getName().toLowerCase());
                        Log.d("CHARSEQ", charSequence.toString().toLowerCase());
                        Log.d("SIZE", "" + dataList.size());
                    }
                } catch (Exception e) {
                    Log.d("INSIDE", "ERROR");
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Log.d("INSIDE", "RESUL");
            data.clear();
            data.addAll((Collection<? extends RepoModel>) filterResults.values);
            notifyDataSetChanged();
        }

    };


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView author, name, description, language, stars, languageColor, imageUrl, username;

        ImageView avatarDp;
        Image image;

        onclickListener onclickListener;

        public ViewHolder(@NotNull View itemView, onclickListener onclickListener) {

            super(itemView);
            avatarDp = itemView.findViewById(R.id.avatarDp);
            author = itemView.findViewById(R.id.author);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            language = itemView.findViewById(R.id.language);
            stars = itemView.findViewById(R.id.stars);
            this.onclickListener = onclickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onclickListener.onClick(getAdapterPosition());
        }
    }
}
