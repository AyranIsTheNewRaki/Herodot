package com.ayranisthenewraki.heredot.herdot;

import android.content.ClipboardManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;

import java.util.ArrayList;
import java.util.List;

import static com.ayranisthenewraki.heredot.herdot.R.id.textView;

public class addAnnotationActivity extends AppCompatActivity {

    List<String> selectedTexts = new ArrayList<String>();
    String currentSelection = "";
    String allAnnotations = "";
    TextView existingAnnotations;
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
        CulturalHeritageObject cho =  (CulturalHeritageObject) bundle.getSerializable("culturalHeritageObject");

        listItemTitle.setText(cho.getTitle());
        listItemDescription.setText(cho.getDescription());


        addAnnotationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                allAnnotations = currentSelection + ": " +  annotationNotes.getText() + "\n" + allAnnotations;

                existingAnnotations.setText(allAnnotations);

                annotationNotes.setVisibility(View.INVISIBLE);
                annotationNotes.setText("");
                addAnnotationButton.setVisibility(View.INVISIBLE);
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

}
