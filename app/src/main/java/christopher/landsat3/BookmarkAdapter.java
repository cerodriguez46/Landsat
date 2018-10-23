package christopher.landsat3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import christopher.landsat3.Networking.LandsatModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context mContext;

    private List<LandsatModel> satelliteList;

    private List<LandsatModel> newList;

    String poster;


    public BookmarkAdapter(Context mContext, List<LandsatModel> satelliteList) {

        this.mContext = mContext;
        this.satelliteList = satelliteList;
    }


    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final BookmarkAdapter.ViewHolder holder, int position) {

        LandsatModel recordEntries = satelliteList.get(position);

        String poster = recordEntries.url;



        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.posterthumbnail);
    }


    @Override
    public int getItemCount() {
        if (satelliteList == null) {
            return 0;
        }
        return satelliteList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterthumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            posterthumbnail = (ImageView) itemView.findViewById(R.id.rv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    LandsatModel clickedItem = satelliteList.get(clickedPosition);

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("detailLat", clickedItem);
                    intent.putExtra("detailLon", clickedItem);
                    intent.putExtra("detailImage", poster);

                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void setRecords(List<LandsatModel> recordEntries) {
        satelliteList = recordEntries;
        notifyDataSetChanged();
    }
}
