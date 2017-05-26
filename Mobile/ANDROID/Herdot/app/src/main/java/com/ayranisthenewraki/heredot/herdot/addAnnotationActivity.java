package com.ayranisthenewraki.heredot.herdot;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayranisthenewraki.heredot.herdot.model.Annotation;
import com.ayranisthenewraki.heredot.herdot.model.SelectorH;
import com.ayranisthenewraki.heredot.herdot.model.myTarget;
import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;
import com.ayranisthenewraki.heredot.herdot.util.NetworkManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.ayranisthenewraki.heredot.herdot.R.id.textView;

public class addAnnotationActivity extends AppCompatActivity {

    List<String> selectedTexts = new ArrayList<String>();
    String currentSelection = "";
    String allAnnotations = "";
    TextView existingAnnotations;
    String HYPOTHESISURL = "https://hypothes.is/";
    Annotation currentAnnotation = new Annotation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annotation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView listItemTitle = (TextView) findViewById(R.id.choName);
        final TextView listItemDescription = (TextView) findViewById(R.id.choDescription);
        existingAnnotations = (TextView) findViewById(R.id.existingAnnotations);
        final EditText annotationNotes = (EditText) findViewById(R.id.annotationNotes);
        final Button addAnnotationButton = (Button) findViewById(R.id.addAnnotationButton);
        Bundle bundle = getIntent().getExtras();
        final CulturalHeritageObject cho =  (CulturalHeritageObject) bundle.getSerializable("culturalHeritageObject");

        listItemTitle.setText(cho.getTitle());
        listItemDescription.setText(cho.getDescription());


        addAnnotationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String annotationText = annotationNotes.getText().toString();

                allAnnotations = currentSelection + ": " +  annotationText + "\n" + allAnnotations;

                existingAnnotations.setText(allAnnotations);

                annotationNotes.setVisibility(View.INVISIBLE);
                annotationNotes.setText("");
                addAnnotationButton.setVisibility(View.INVISIBLE);

                String uri = "http://www.herodot.world/heritage?id=" + cho.getId();

                myTarget target = new myTarget();
                SelectorH selector = new SelectorH();
                selector.setExact(currentSelection);
                List<SelectorH> selectorList = new ArrayList<SelectorH>();
                selectorList.add(selector);
                target.setSelector(selectorList);
                target.setSource(uri);
                List<myTarget> targets = new ArrayList<myTarget>();
                targets.add(target);
                currentAnnotation.setTarget(targets);
                currentAnnotation.setText(annotationText);
                currentAnnotation.setUri(uri);

                addAnnotation(view);
            }
        });

        listItemDescription.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback() {


            @Override
            public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                menu.clear();
                menu.add(0, 0, 0, "Annotate").setIcon(R.drawable.camera);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                int min = 0;
                int max = listItemDescription.getText().length();
                if (listItemDescription.isFocused()) {
                    final int selStart = listItemDescription.getSelectionStart();
                    final int selEnd = listItemDescription.getSelectionEnd();

                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));

                    SpannableString highlightString = new SpannableString(listItemDescription.getText());
                    highlightString.setSpan(new ForegroundColorSpan(Color.BLUE), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    listItemDescription.setText(highlightString);

                    annotationNotes.setVisibility(View.VISIBLE);
                    addAnnotationButton.setVisibility(View.VISIBLE);
                }
                // Perform your definition lookup with the selected text
                final CharSequence selectedText = listItemDescription.getText().subSequence(min, max);
                currentSelection = selectedText.toString();
                selectedTexts.add(selectedText.toString());
                return true;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode actionMode) {

            }
        });
    }

    private void addAnnotation(final View view){
        OkHttpClient client = NetworkManager.getNewClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HYPOTHESISURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
        addAnnotationActivity.addAnnoService service = retrofit.create( addAnnotationActivity.addAnnoService.class);
        Gson gson = new Gson();
        String json = gson.toJson(currentAnnotation);
        final Call<ObjectNode> addAnnoCall = service.addAnno(json);


        //make an asynchronous call.
        addAnnoCall.enqueue(new Callback<ObjectNode>() {
            @Override
            public void onResponse(Call<ObjectNode> call, Response<ObjectNode> response) {
                if (response.code() == 200) {

                    System.out.print("aa");
                }
                else {
                    System.out.print("aa");
                }
            }

            @Override
            public void onFailure(Call<ObjectNode> call, Throwable t) {
                System.out.print("aa");
            }
        });
    }

    private interface addAnnoService {

        @Headers({"Content-Type: application/json", "Authorization: Bearer 6879-gQ3825AY-yywCbMTqFJ6HxuQndT6BjdYHh9irTYTe38"})
        @POST("/api/annotations")
        Call<ObjectNode> addAnno(@Body String json);
    }

}
