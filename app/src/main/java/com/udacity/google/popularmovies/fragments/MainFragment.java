package com.udacity.google.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.google.popularmovies.DetailActivity;
import com.udacity.google.popularmovies.util.FavouriteContract;
import com.udacity.google.popularmovies.util.FavouriteDataSource;
import com.udacity.google.popularmovies.util.CustomGridAdapter;
import com.udacity.google.popularmovies.util.FavouriteOpenHelper;
import com.udacity.google.popularmovies.util.FavouriteProvider;
import com.udacity.google.popularmovies.util.Movie;
import com.udacity.google.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by Dell on 9/29/2015.
 */
public class MainFragment extends Fragment {


    public static Movie clickedMovie;
    GridView gridView ;

    View rootView = null;

    ArrayList<Movie> popularMovies;
    ArrayList<String> popularMoviePosters ;
    String poster;
    RequestQueue queue;

    String POP_DESC = "popularity.desc";
    String VOTE_DSEC= "vote_average.desc";
    String FAV = "favourite";

    String API_KEY = null;

    String TAG = "VolleyDebug";

    String sort_by = POP_DESC;
    CustomGridAdapter adapter ;
    SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        API_KEY = getActivity().getResources().getString(R.string.api_key);

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        queue = Volley.newRequestQueue(getActivity());

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        sort_by = sp.getString("sort_by",POP_DESC);

        if(sort_by.equals(FAV))
            getFavouriteFromDB();
        else {
            jsonObjectRequest(sort_by);

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LinearLayout layout = (LinearLayout) view;
                ImageView clickedItem = (ImageView) layout.findViewById(R.id.imageView);
                clickedMovie = (Movie) clickedItem.getTag();


                if ((getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {


                    //if tablet, add detailFragment


                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.container_detail, new DetailFragment());

                    transaction.commit();
                } else {
                    //if mobile device, open DetailActivity
                    Intent in = new Intent(getActivity(), DetailActivity.class);
                    //in.putExtra("movie", movie); ???!
                    startActivity(in);

                }
            }
        });

            return rootView;
    }

    private void jsonObjectRequest(String sort_by) {


        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sort_by)
                .appendQueryParameter("api_key", API_KEY);

// http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=4f92be250f018aff8f3a2b5c3864aecd

        String myUrl = builder.build().toString();

        Log.d("popularMovies", myUrl);

        //TODO:

        popularMoviePosters = new ArrayList<String>();
        popularMovies = new ArrayList<Movie>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArrayResult = response.getJSONArray("results");

                            for (int i = 0; i < jsonArrayResult.length(); i++) {
                                Movie movie = new Movie();


                                JSONObject jsonObjectMovie = jsonArrayResult.getJSONObject(i);

                                movie.setId(jsonObjectMovie.getInt("id"));
                                movie.setOverview(jsonObjectMovie.getString("overview"));
                                movie.setPosterPath("https://image.tmdb.org/t/p/w185/"+jsonObjectMovie.getString("poster_path"));
                                movie.setReleaseDate(jsonObjectMovie.getString("release_date"));
                                movie.setTitle(jsonObjectMovie.getString("title"));
                                movie.setVoteAverage(jsonObjectMovie.getString("vote_average"));
                                popularMovies.add(movie);

                                poster = "https://image.tmdb.org/t/p/w185/";
                                poster += jsonObjectMovie.getString("poster_path");
                                popularMoviePosters.add(poster);
                                Log.d(TAG, popularMoviePosters.get(i));
                            }


                            adapter = new CustomGridAdapter(getActivity(), popularMovies);

                            gridView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "Sorry some thing happened while retrieving data, please try again", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_most_popular){


            sp.edit().putString("sort_by", POP_DESC).commit();


            jsonObjectRequest(POP_DESC);

           return true;
        }
        if(id == R.id.action_highest_rated){


            sp.edit().putString("sort_by", VOTE_DSEC).commit();

            jsonObjectRequest(VOTE_DSEC);

            return true;
        }
        if(id == R.id.action_favourite){
            sp.edit().putString("sort_by", "favourite").commit();

            getFavouriteFromDB();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFavouriteFromDB() {

        //retrieve movies from db and show them on grid view
        FavouriteDataSource dataSource = new FavouriteDataSource(getActivity());

        dataSource.open();
        ArrayList<Movie> favoriteMovies = dataSource.getFavouriteMovies();
        dataSource.close();

        if (favoriteMovies.size() == 0){
            Toast.makeText(getActivity(), "You don't have any favourite movies yet, mark a movie as favourite first", Toast.LENGTH_LONG).show();

        }else{

            CustomGridAdapter adapter = new CustomGridAdapter(getActivity(), favoriteMovies);

            gridView.setAdapter(adapter);

        }
    }

}
