package com.example.uniwise;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumFragment extends Fragment {
    String selectedSubject;
    String selectedDept;
    FirebaseAuth mAuth;
    private RecyclerView queList;
    private DatabaseReference Questionref,Likesref;
    Boolean LikeChecker = false;
    String currentuserID;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_forum, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentuserID = mAuth.getCurrentUser().getUid();
        if(getActivity()!=null) {
            Intent intent = getActivity().getIntent();
            selectedSubject = intent.getStringExtra("SelectedSubject");
            selectedDept=intent.getStringExtra("dept");
        }
        queList = (RecyclerView) rootview.findViewById(R.id.all_que_list);
        queList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        queList.setLayoutManager(linearLayoutManager);
        Questionref = FirebaseDatabase.getInstance().getReference().child("Departments").child(selectedDept).child(selectedSubject);
        Likesref = FirebaseDatabase.getInstance().getReference().child("Likes");

        DisplayAllQuestion();


        return rootview;
    }

    private void DisplayAllQuestion() {
        FirebaseRecyclerAdapter<Questionss, QuestionViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Questionss, QuestionViewHolder>(Questionss.class,R.layout.all_ques_ans,QuestionViewHolder.class,Questionref) {
                    @Override
                    protected void populateViewHolder(QuestionViewHolder questionViewHolder, Questionss questionss, int i) {

                        final String  PostKey = getRef(i).getKey();

                        questionViewHolder.setQuestion(questionss.getQuestion());
                        questionViewHolder.setProfileImage(getContext(),questionss.getProfileImage());
                        questionViewHolder.setUsernamee(questionss.getUsernamee());
                        questionViewHolder.setTime(questionss.getTime());
                        questionViewHolder.setDate(questionss.getDate());

                        questionViewHolder.setLikeButtonStatus(PostKey);

                        questionViewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent commentsIntent = new Intent(getActivity(),AnswaersActivity.class);
                               // commentsIntent.putExtra("Que",)
                                commentsIntent.putExtra("Postkey",PostKey);
                                commentsIntent.putExtra("selectedSub",selectedSubject);
                                commentsIntent.putExtra("Dept",selectedDept);
                                startActivity(commentsIntent);

                            }
                        });

                        questionViewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LikeChecker = true;
                                Likesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(LikeChecker.equals(true)){
                                            if(dataSnapshot.child(PostKey).hasChild(currentuserID))
                                            {
                                                Likesref.child(PostKey).child(currentuserID).removeValue();
                                                LikeChecker = false;
                                            }
                                            else {
                                                Likesref.child(PostKey).child(currentuserID).setValue(true);
                                                LikeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                };
        queList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton LikePostButton,CommentPostButton;
        TextView DisplaynoOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference Likesref;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            LikePostButton = (ImageButton) mView.findViewById(R.id.like_button);
            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            DisplaynoOfLikes = (TextView) mView.findViewById(R.id.no_likes);

            Likesref = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikeButtonStatus(final String PostKey){
            Likesref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId)){
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.like);

                        DisplaynoOfLikes.setText((Integer.toString(countLikes)+(" Likes")));
                    }
                    else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.dislike);

                        DisplaynoOfLikes.setText((Integer.toString(countLikes)+(" Likes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setUsernamee(String usernamee){
            TextView u = (TextView) mView.findViewById(R.id.post_que_user_name);
            u.setText(usernamee);
        }
        public void setProfileImage(Context ctx, String profileImage) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
            Picasso.with(ctx).load(profileImage).into(image);
        }
        public void setQuestion(String question) {
            TextView userq = (TextView) mView.findViewById(R.id.user_que);
            userq.setText(question);
        }
        public void setTime(String time){
            TextView ptime = (TextView) mView.findViewById(R.id.post_time);
            ptime.setText("  "+time);
        }
        public void setDate(String date){
            TextView pdate = (TextView) mView.findViewById(R.id.post_date);
            pdate.setText("  "+date);
        }
    }
}
