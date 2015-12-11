package itmir.numberbaseball;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText mInput;
    ListView mListView;
    Adapter mAdapter;

    final int number = 3; // n자리수 (1~10자리)
    int strike = 0; // 스트라이크 선언
    int ball = 0; // 볼 선언

    List<Integer> answer = new ArrayList<Integer>();
    List<Integer> input = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInput = (EditText) findViewById(R.id.mInput);
        mInput.setMaxEms(number);
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new Adapter(this);
        mListView.setAdapter(mAdapter);

        init();
    }

    public void init() {
        Random random = new Random();

        // 정답 n자리수를 랜덤으로 생성
        for (int i = 0; i < number; i++) {
            int randomNumber = random.nextInt(9) + 1;
            while (answer.contains(randomNumber)) {
                randomNumber = random.nextInt(9) + 1;
            }
            answer.add(i, randomNumber);
        }
    }

    public void inputNumber(View v) {
        String mStr = mInput.getText().toString();

        // n자리 숫자를 입력하지 않았을 경우 오류 처리
        if (mStr.length() != number) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle("에러");
            builder.setMessage(number + "자리수를 입력하세요");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();

            return;
        }

        for (int i = 0; i < number; i++) {
            int inputNumber = Integer.parseInt(mStr.substring(i, i + 1));
            if (input.contains(inputNumber)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle("에러");
                builder.setMessage("중복된 숫자 발견");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            } else {
                input.add(i, inputNumber);
            }
        }

        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                if (answer.get(i).equals(input.get(j))) {
                    if (i == j) {
                        strike += 1;
                        input.remove(j);
                        input.add(j, -1);
                    } else {
                        ball += 1;
                        input.remove(j);
                        input.add(j, -1);
                    }
                }
            }
        }

        if (strike == 0 && ball == 0) { // 모두 틀릴 경우
            mAdapter.addItem(mStr, "아웃!");
            reset();
        } else if (strike == number) { // 정답일 경우
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("정답");
            builder.setMessage(mStr + " 정답입니다, 다시하시겠습니까?");
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    init();
                    mAdapter.clearData();
                    mAdapter.notifyDataSetChanged();
                }
            });
            builder.show();
            reset();
        } else { // 부분 정답일 경우
            mAdapter.addItem(mStr, strike + " Strike" + ", " + ball + " Ball");
            reset();
        }

        mInput.setText("");
        mAdapter.notifyDataSetChanged();
        keyboardHide();
    }

    public void keyboardHide() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
    }

    public void reset() {
        strike = 0;
        ball = 0;
        input.clear();
    }
}
