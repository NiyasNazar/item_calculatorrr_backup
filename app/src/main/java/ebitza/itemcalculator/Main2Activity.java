package ebitza.itemcalculator;

import android.animation.Animator;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Search_views.SampleSuggestionsBuilder;
import ebitza.itemcalculator.Search_views.SearchResult;
import ebitza.itemcalculator.Search_views.SearchResultAdapter;
import ebitza.itemcalculator.Search_views.SimpleAnimationListener;

public class Main2Activity extends AppCompatActivity {
    private PersistentSearchView mSearchView;
    private View mSearchTintView;
   SearchResultAdapter mResultAdapter;
    private DBManager dbManager;
    private RecyclerView mRecyclerView;
       LinearLayout search_error;
   RelativeLayout rll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dbManager=new DBManager(getApplicationContext());
        dbManager.open();
        dbManager.CreateDynamicTablesforallproducts();
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_search_result);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);

        mSearchTintView = findViewById(R.id.view_search_tint);
        search_error=(LinearLayout)findViewById(R.id.search_error_layout);
        rll=(RelativeLayout)findViewById(R.id.rlll);
        mResultAdapter = new SearchResultAdapter(new ArrayList<SearchResult>(),getApplicationContext());
        if (mResultAdapter.getItemCount()==0){
         //   Toast.makeText(getApplicationContext(),"no data on create",Toast.LENGTH_SHORT).show();
        }
    mRecyclerView.setAdapter(mResultAdapter);




        // mSearchTintView = findViewById(R.id.view_search_tint);
        mSearchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {

            @Override
            public void onHomeButtonClick() {
                //Hamburger has been clicked
                finish();
            }

        });
/*  List<SearchItem> mHistorySuggestions=dbManager.getsearchhistory();
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,mHistorySuggestions));*/
        mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {


            @Override
            public void onSearchEditOpened() {
                //Use this to tint the screen
                mSearchTintView.setVisibility(View.VISIBLE);
                mSearchTintView
                        .animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener())
                        .start();
              // List<SearchItem> mHistorySuggestions=dbManager.getsearchhistory();
               // mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(getApplicationContext(),mHistorySuggestions));
            }

            @Override
            public void onSearchEditClosed() {
                //Use this to un-tint the screen
                mSearchTintView
                        .animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSearchTintView.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {
                mResultAdapter.clear();
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSearchTermChanged(String term ) {
              //  Toast.makeText(Main2Activity.this, term + " Searched", Toast.LENGTH_LONG).show();
                List<SearchItem>newResults=dbManager.getAllitemsearchsug(term);

                mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(getApplicationContext(),newResults));
            }

            @Override
            public void onSearch(String string) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
dbManager.inserttosearch(string,date);

               // Toast.makeText(Main2Activity.this, string + " Searched", Toast.LENGTH_LONG).show();
                mRecyclerView.setVisibility(View.VISIBLE);



                fillResultToRecyclerView(string);
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });
      mSearchView.openSearch("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.populateEditText(matches);
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fillResultToRecyclerView(String query) {
        List<SearchResult>newResults=dbManager.getAllitemsearch(query);
       /* List<SearchResult> newResults = new ArrayList<>();
        for(int i =0; i< 10; i++) {
            SearchResult result = new SearchResult(query, query + Integer.toString(i), "");
            Log.i("result",result.getTitle());
            newResults.add(result);

        }*/
        mResultAdapter.replaceWith(newResults);
        if (mResultAdapter.getItemCount()==0){
            //Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_SHORT).show();
            search_error.setVisibility(View.VISIBLE);
            rll.setVisibility(View.GONE);

        }else if(mResultAdapter.getItemCount()>0){
            search_error.setVisibility(View.GONE);
            rll.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onBackPressed() {
        if(mSearchView.isEditing()) {
            mSearchView.cancelEditing();
        } else {
            super.onBackPressed();
        }
    }
}
