package com.example.uniwise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriend extends Fragment {

    private ImageButton searchButton;
    private EditText searchInputText;
    private RecyclerView searchList;
    private DatabaseReference searchUsersRef;
    private ProgressDialog loadingBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.find_friend, container, false);

        searchButton=(ImageButton)rootview.findViewById(R.id.search_button);
        searchInputText=(EditText)rootview.findViewById(R.id.search_friend);
        searchUsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        loadingBar=new ProgressDialog(getActivity());
        searchList=(RecyclerView)rootview.findViewById(R.id.search_list);
        searchList.setHasFixedSize(true);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchResult=searchInputText.getText().toString();
                searchFriends(searchResult);
            }
        });

        return rootview;
    }

    private void searchFriends(String searchResult) {

        loadingBar.setMessage("User not found, Press back.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        Query searchUsersQuery=searchUsersRef.orderByChild("Username")
                .startAt(searchResult).endAt(searchResult+"\uf8ff");

            FirebaseRecyclerAdapter<FindFriendsSearch,FindFriendsViewHolder>firebaseRecyclerAdapter
                    =new FirebaseRecyclerAdapter<FindFriendsSearch, FindFriendsViewHolder>(
                            FindFriendsSearch.class,
                            R.layout.search_list_layout,
                            FindFriendsViewHolder.class,
                            searchUsersQuery
            ) {
                @Override
                protected void populateViewHolder(FindFriendsViewHolder viewHolder, @NonNull FindFriendsSearch model, final int position)
                {
                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setDepartment(model.getDepartment());
                    viewHolder.setProfileImage(getActivity().getApplicationContext(),model.getProfileImage());
                    loadingBar.dismiss();


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String visit_user_id=getRef(position).getKey();

                            Bundle bundle=new Bundle();
                            bundle.putString("visit_user_id",visit_user_id);

                            FindFriendsProfile fragment=new FindFriendsProfile();
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container1,fragment).commit();


                        }
                    });
                }

            };
            searchList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProfileImage(Context ctx,String profileImage)
        {
            CircleImageView searchImage=(CircleImageView) mView.findViewById(R.id.search_list_profile_image);
            Picasso.with(ctx).load(profileImage).placeholder(R.drawable.profile_img).into(searchImage);
        }

        public void setUsername(String username) {
            TextView searchUsername=(TextView)mView.findViewById(R.id.search_list_username);
            searchUsername.setText(username);
        }

        public void setDepartment(String department) {
            TextView searchDepartment=(TextView)mView.findViewById(R.id.search_list_department);
            searchDepartment.setText(department);
        }
    }

}
