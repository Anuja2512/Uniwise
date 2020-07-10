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

public class SearchQuestionFragment extends Fragment {

    private ImageButton searchButton;
    private EditText searchInputText;
    private RecyclerView searchList;
    private DatabaseReference searchUsersRef;
    private ProgressDialog loadingBar;
    String selectedSubject,selectedDept;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_search_question, container, false);

        if(getActivity()!=null) {
            Intent intent = getActivity().getIntent();
            selectedSubject = intent.getStringExtra("SelectedSubject");
            selectedDept=intent.getStringExtra("dept");

        }
        searchButton=(ImageButton)rootview.findViewById(R.id.search_button);
        searchInputText=(EditText)rootview.findViewById(R.id.search_friend);
        searchUsersRef= FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject);
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

        loadingBar.setMessage("Question not found, Press back.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        Query searchUsersQuery=searchUsersRef.orderByChild("question")
                .startAt(searchResult).endAt(searchResult+"\uf8ff");

        FirebaseRecyclerAdapter<SearchQuestionHolder,FindFriendsViewHolder>firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<SearchQuestionHolder, FindFriendsViewHolder>(
                SearchQuestionHolder.class,
                R.layout.search_question_layout,
                FindFriendsViewHolder.class,
                searchUsersQuery
        ) {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, @NonNull SearchQuestionHolder model, final int position)
            {
                viewHolder.setQuestion(model.getQuestion());
                loadingBar.dismiss();

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

        public void setQuestion(String question) {
            TextView searchUsername=(TextView)mView.findViewById(R.id.search_list_username);
            searchUsername.setText(question);
        }


    }

}
