package com.udacity.google.popularmovies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.udacity.google.popularmovies.util.FavouriteDataSource;
import com.udacity.google.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dell on 10/4/2015.
 */
public class DetailFragment extends Fragment {

    int clickedMovieId;
    ListView reviewsLV;
    ListView trailersLv;
    RequestQueue queue;
    ArrayList<String> trailersNames;
    ArrayList<String> trailersKeys;
    private ShareActionProvider mShareActionProvider;
    Intent shareIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment, null, false);

//
//        Scanner sc = new Scanner(System.in);
//        String s ;
//        System.out.println();

        final ToggleButton favButton = (ToggleButton) rootView.findViewById(R.id.FavtoggleButton);
        //MainFragment.clickedMovie

        clickedMovieId = MainFragment.Companion.getClickedMovie().getId();

        TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieTitle.setText(MainFragment.Companion.getClickedMovie().getTitle());

        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
        Picasso.with(getActivity()).load(MainFragment.Companion.getClickedMovie().getPosterPath()).resize(300, 300).centerInside().into(moviePoster);


        Log.d("DetailDebug", MainFragment.Companion.getClickedMovie().getPosterPath());

        TextView movieDate = (TextView) rootView.findViewById(R.id.movie_date);
        movieDate.setText(MainFragment.Companion.getClickedMovie().getReleaseDate());

        TextView movieVoteAverage = (TextView) rootView.findViewById(R.id.movie_vote_average);
        movieVoteAverage.setText(MainFragment.Companion.getClickedMovie().getVoteAverage());


        TextView movieOverView = (TextView) rootView.findViewById(R.id.movie_overview);
        movieOverView.setText(MainFragment.Companion.getClickedMovie().getOverview());


        favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FavouriteDataSource favouriteDataSource = new FavouriteDataSource(getActivity());
                favouriteDataSource.open();

                if (favButton.getText().equals("MARK AS A FAVOURITE")) {

//                    save clicked movie id and poster link to db
                    favouriteDataSource.create(MainFragment.Companion.getClickedMovie());
                    Toast.makeText(getActivity(), "Marked as FAVOURITE", Toast.LENGTH_LONG).show();
                } else {

                    favouriteDataSource.delete(MainFragment.Companion.getClickedMovie().getId());
                    Toast.makeText(getActivity(), "Marked as not favourite", Toast.LENGTH_LONG).show();
                }

                favouriteDataSource.close();

            }
        });

        trailersLv = (ListView) rootView.findViewById(R.id.trailers_list_view);

        trailersLv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        queue = Volley.newRequestQueue(getActivity());


        trailersNames = new ArrayList<String>();
        trailersKeys = new ArrayList<String>();

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + clickedMovieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", getActivity().getResources().getString(R.string.api_key));

//http://api.themoviedb.org/3/movie/49026/videos?api_key=4f92be250f018aff8f3a2b5c3864aecd
        String myUrl = builder.build().toString();

        Log.d("VolleyDebug", myUrl);
        JsonObjectRequest trailersRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray resultJsonArray = null;
                try {

                    resultJsonArray = response.getJSONArray("results");


                    for (int i = 0; i < resultJsonArray.length(); i++) {

                        JSONObject jsonObject = resultJsonArray.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String key = jsonObject.getString("key");

                        trailersNames.add(name);
                        trailersKeys.add(key);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), trailersNames);

                        trailersLv.setAdapter(adapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Sorry some thing happened while retrieving trailers, please try again", Toast.LENGTH_LONG).show();

                    }
                }
        );

        queue.add(trailersRequest);


        trailersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String clickedKey = trailersKeys.get(i);

                String url = "https://www.youtube.com/watch?v=" + clickedKey;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);


            }
        });

        reviewsLV = (ListView) rootView.findViewById(R.id.reviews_list_view);

        final ArrayList<String> reviewsArrayList = new ArrayList<String>();


        builder = new Uri.Builder();

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + clickedMovieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", getActivity().getResources().getString(R.string.api_key));

        //http://api.themoviedb.org/3/movie/49026/reviews?api_key=4f92be250f018aff8f3a2b5c3864aecd

        myUrl = builder.build().toString();


        JsonObjectRequest reviewsRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray resultsJsonArray = response.getJSONArray("results");


                    for (int i = 0; i < resultsJsonArray.length(); i++) {

                        JSONObject resultJsonObj = resultsJsonArray.getJSONObject(i);

                        String author = resultJsonObj.getString("author");

                        String content = resultJsonObj.getString("content");

                        //TODO: author with content !
                        reviewsArrayList.add(author + ": " + System.getProperty("line.separator") + System.getProperty("line.separator") + content);

                        Log.d("DetailDebug", author + ": /r/n" + content);

                        ArrayAdapter<String> reviewsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, reviewsArrayList);
                        reviewsLV.setAdapter(reviewsAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(reviewsRequest);


        reviewsLV.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        FavouriteDataSource favouriteDataSource = new FavouriteDataSource(getActivity());
        favouriteDataSource.open();
        if (favouriteDataSource.isFavouriteMovie(clickedMovieId)) {

            favButton.setText("FAVOURITE");
        }


        return rootView;
    }

    public class CustomListAdapter extends ArrayAdapter {

        //ArrayList<String> images ;
        Activity context;

        ArrayList<String> stringArrayList;

        public CustomListAdapter(Activity context, ArrayList<String> resources) {
            super(context, R.layout.grid_view_item);

            stringArrayList = resources;
            this.context = context;
        }

        @Override
        public int getCount() {
            return stringArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return stringArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();

            View rowView = inflater.inflate(R.layout.list_view_item, null, true);

            TextView tv = (TextView) rowView.findViewById(R.id.list_item_text_view);

            tv.setText(stringArrayList.get(position));

            return rowView;


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_detail, menu);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(item.getItemId() == R.id.menu_item_share) {
           mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

           if (trailersKeys != null && trailersKeys.size() > 0) {
               String url = "https://www.youtube.com/watch?v=" + trailersKeys.get(0);

               shareIntent = new Intent(android.content.Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               shareIntent.putExtra(Intent.EXTRA_STREAM, url);
               shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
           }
           if (mShareActionProvider != null) {
               mShareActionProvider.setShareIntent(shareIntent);
           }
       }

        return super.onOptionsItemSelected(item);
    }


}
