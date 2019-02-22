package ebitza.itemcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ebitza.itemcalculator.Db_Helper.DBManager;

public class Addcategory extends AppCompatActivity implements View.OnClickListener {

    private Button addTodoBtn;
    private EditText categoryEditText;
    private EditText descEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.addcat);

        categoryEditText = (EditText) findViewById(R.id.category_edittext);

/*categoryEditText.setFilters(new InputFilter[]{getEditTextFilter()});*/
        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String name = categoryEditText.getText().toString();
                if (!name.matches("[a-zA-Z? ]*")) {

                    categoryEditText.setError("Special characters not allowed");

                }else{

                    dbManager.addcategory(name);
                    categoryEditText.setText("");
                }


                /*Intent main = new Intent(Addcategory.this, MainActivity.class);


                startActivity(main);
                finish();*/
                break;
        }
    }
    @Override
    public void onBackPressed() {

        Intent is=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(is);
        finish();
    }
    public static InputFilter getEditTextFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }
}