package it.uniba.sms222325;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.uniba.sms222325.entities.User;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.MyViewHolder> {

    private List<User> usersLeaderboard;
    private Context context;

    public UserRecyclerViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.usersLeaderboard = users;
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new UserRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.textViewUsername.setText(usersLeaderboard.get(position).getUsername());
        holder.textViewScore.setText(usersLeaderboard.get(position).getBestScore().toString());

        String userPlacement = usersLeaderboard.indexOf(usersLeaderboard.get(position)) + 1 + "°";
        holder.textViewPosition.setText(userPlacement);
    }

    @Override
    public int getItemCount() {
        return usersLeaderboard.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewPosition;
        TextView textViewUsername;
        TextView textViewScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPosition = itemView.findViewById(R.id.textViewPosition);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewScore = itemView.findViewById(R.id.textViewScore);
        }
    }
}
