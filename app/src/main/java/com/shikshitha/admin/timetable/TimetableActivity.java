package com.shikshitha.admin.timetable;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.ClassDao;
import com.shikshitha.admin.dao.SectionDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.dao.TimetableDao;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.NetworkUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableActivity extends AppCompatActivity implements TimetableView,
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.spinner_class)
    Spinner classSpinner;
    @BindView(R.id.spinner_section) Spinner sectionSpinner;
    @BindView(R.id.section_layout) LinearLayout sectionLayout;
    @BindView(R.id.tableLayout)
    FrameLayout tableLayout;
    @BindView(R.id.noTimetable)
    LinearLayout noTimetable;

    LinkedHashMap<String, List<Timetable>> timetableMap = new LinkedHashMap<>();
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private int noOfPeriods;

    private TimetablePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new TimetablePresenterImpl(this, new TimetableInteractorImpl());

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getClassList(TeacherDao.getTeacher().getSchoolId());
        } else {
            showOfflineClass();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void loadData(MenuItem item) {
        presenter.getClassList(TeacherDao.getTeacher().getSchoolId());
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void showOffline(String tableName) {
        switch (tableName){
            case "class":
                showOfflineClass();
                break;
            case "section":
                showOfflineSection();
                break;
            case "timetable":
                showOfflineTimetable();
                break;
            default:
                break;
        }
    }

    @Override
    public void showClass(List<Clas> clasList) {
        ArrayAdapter<Clas> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
        backupClass(clasList);
    }

    private void backupClass(final List<Clas> classList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClassDao.delete(TeacherDao.getTeacher().getSchoolId());
                ClassDao.insert(classList);
            }
        }).start();
    }

    private void showOfflineClass() {
        List<Clas> clasList = ClassDao.getClassList(TeacherDao.getTeacher().getSchoolId());
        ArrayAdapter<Clas> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showSection(List<Section> sectionList) {
        setSectionAdapter(sectionList);
        backupSection(sectionList);
    }

    private void backupSection(final List<Section> sectionList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SectionDao.delete(((Clas) classSpinner.getSelectedItem()).getId());
                SectionDao.insert(sectionList);
            }
        }).start();
    }

    private void showOfflineSection() {
        List<Section> sectionList = SectionDao.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
        setSectionAdapter(sectionList);
    }

    private void setSectionAdapter(List<Section> sectionList) {
        ArrayAdapter<Section> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        sectionSpinner.setOnItemSelectedListener(this);
        if(sectionList.size() == 1 && sectionList.get(0).getSectionName().equals("none")) {
            sectionLayout.setVisibility(View.INVISIBLE);
            sectionLayout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        } else {
            sectionLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int px = Conversion.dpToPx(10, getApplicationContext());
            sectionLayout.setPadding(px, px, px, px);
            sectionLayout.setLayoutParams(params);
        }
    }

    @Override
    public void showTimetable(List<Timetable> timetableList) {
        if(timetableList.size() == 0) {
            TimetableDao.delete(((Section) sectionSpinner.getSelectedItem()).getId());
            noTimetable.setVisibility(View.VISIBLE);
        } else {
            noTimetable.setVisibility(View.INVISIBLE);
            for(String day: days) {
                List<Timetable> timtableList = new ArrayList<>();
                for(Timetable timetable: timetableList) {
                    if(timetable.getDayOfWeek().equals(day)) {
                        timtableList.add(timetable);
                        if(timetable.getPeriodNo() > noOfPeriods) noOfPeriods = timetable.getPeriodNo();
                    }
                }
                timetableMap.put(day, timtableList);
            }
            tableLayout.addView(new TableMainLayout(this));
            backupTimetable(timetableList);
        }
    }

    private void backupTimetable(final List<Timetable> timetableList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TimetableDao.delete(((Section) sectionSpinner.getSelectedItem()).getId());
                TimetableDao.insert(timetableList);
            }
        }).start();
    }

    private void showOfflineTimetable() {
        List<Timetable> timetableList = TimetableDao.getTimetable(((Section) sectionSpinner.getSelectedItem()).getId());
        if(timetableList.size() == 0) {
            noTimetable.setVisibility(View.VISIBLE);
        } else {
            noTimetable.setVisibility(View.INVISIBLE);
            for(String day: days) {
                List<Timetable> timtableList = new ArrayList<>();
                for(Timetable timetable: timetableList) {
                    if(timetable.getDayOfWeek().equals(day)) {
                        timtableList.add(timetable);
                        if(timetable.getPeriodNo() > noOfPeriods) noOfPeriods = timetable.getPeriodNo();
                    }
                }
                timetableMap.put(day, timtableList);
            }
            tableLayout.addView(new TableMainLayout(this));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.spinner_class:
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
                } else {
                    showOfflineSection();
                }
                break;
            case R.id.spinner_section:
                tableLayout.removeAllViews();
                getTimetable();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getTimetable() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getTimetable(((Section) sectionSpinner.getSelectedItem()).getId());
        } else {
            showOfflineTimetable();
        }
    }

    public class TableMainLayout extends RelativeLayout {
        TableLayout tableA;
        TableLayout tableB;
        TableLayout tableC;
        TableLayout tableD;

        HorizontalScrollView horizontalScrollViewB;
        HorizontalScrollView horizontalScrollViewD;

        ScrollView scrollViewC;
        ScrollView scrollViewD;

        Context context;

        int headerCellsWidth[] = new int[noOfPeriods+1];

        public TableMainLayout(Context context) {
            super(context);

            this.context = context;

            // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
            this.initComponents();
            this.setComponentsId();
            this.setScrollViewAndHorizontalScrollViewTag();

            // no need to assemble component A, since it is just a table
            this.horizontalScrollViewB.addView(this.tableB);

            this.scrollViewC.addView(this.tableC);

            this.scrollViewD.addView(this.horizontalScrollViewD);
            this.horizontalScrollViewD.addView(this.tableD);

            // add the components to be part of the main layout
            this.addComponentToMainLayout();
            //this.setBackgroundColor(Color.RED);

            // add some table rows
            this.addTableRowToTableA();
            this.addTableRowToTableB();

            this.resizeHeaderHeight();

            this.getTableRowHeaderCellWidth();

            this.generateTableC_AndTable_D();

            this.resizeBodyTableRowHeight();
        }

        private void initComponents(){
            this.tableA = new TableLayout(this.context);
            this.tableB = new TableLayout(this.context);
            this.tableC = new TableLayout(this.context);
            this.tableD = new TableLayout(this.context);

            this.horizontalScrollViewB = new TableMainLayout.MyHorizontalScrollView(this.context);
            this.horizontalScrollViewD = new TableMainLayout.MyHorizontalScrollView(this.context);
            this.horizontalScrollViewB.setHorizontalScrollBarEnabled(false);

            this.scrollViewC = new TableMainLayout.MyScrollView(this.context);
            this.scrollViewD = new TableMainLayout.MyScrollView(this.context);

            this.tableA.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            this.horizontalScrollViewB.setBackgroundColor(Color.WHITE);
        }

        private void setComponentsId(){
            this.tableA.setId(R.id.tableA_id);
            this.horizontalScrollViewB.setId(R.id.horizontalScrollViewB_id);
            this.scrollViewC.setId(R.id.scrollViewC_id);
            this.scrollViewD.setId(R.id.scrollViewD_id);
        }

        // set tags for some horizontal and vertical scroll view
        private void setScrollViewAndHorizontalScrollViewTag(){
            this.horizontalScrollViewB.setTag("horizontal scroll view b");
            this.horizontalScrollViewD.setTag("horizontal scroll view d");

            this.scrollViewC.setTag("scroll view c");
            this.scrollViewD.setTag("scroll view d");
        }

        // we add the components here in our TableMainLayout
        private void addComponentToMainLayout(){
            // RelativeLayout params were very useful here
            // the addRule method is the key to arrange the components properly
            LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

            LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

            LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
            componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

            // 'this' is a relative layout,
            // we extend this table layout as relative layout as seen during the creation of this class
            this.addView(this.tableA);
            this.addView(this.horizontalScrollViewB, componentB_Params);
            this.addView(this.scrollViewC, componentC_Params);
            this.addView(this.scrollViewD, componentD_Params);
        }

        private void addTableRowToTableA(){
            this.tableA.addView(this.componentATableRow());
        }

        private void addTableRowToTableB(){
            this.tableB.addView(this.componentBTableRow());
        }

        TableRow componentATableRow(){
            TableRow componentATableRow = new TableRow(this.context);
            TextView textView = this.headerTextView("Timetable");
            textView.setPadding(36, 24, 36, 24);
            textView.setTextSize(18);
            textView.setTextColor(ContextCompat.getColor(context, R.color.default_white));
            componentATableRow.addView(textView);
            return componentATableRow;
        }

        TableRow componentBTableRow(){
            TableRow componentBTableRow = new TableRow(this.context);

            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 0, 0);

            for(int x=1; x<=noOfPeriods; x++){
                TextView textView = this.headerTextView("Period  -  " + x );
                textView.setPadding(64, 16, 64, 16);
                textView.setTextSize(16);
                textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
                //textView.setLayoutParams(params);
                componentBTableRow.addView(textView, params);
            }
            return componentBTableRow;
        }

        // generate table row of table C and table D
        private void generateTableC_AndTable_D(){
            for (Map.Entry<String, List<Timetable>> entry : timetableMap.entrySet()) {
                TableRow tableRowForTableC = this.tableRowForTableC(entry.getKey());
                TableRow taleRowForTableD = this.taleRowForTableD(entry.getValue());

                tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                this.tableC.addView(tableRowForTableC);
                this.tableD.addView(taleRowForTableD);
            }
        }

        TableRow tableRowForTableC(String day){
            TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0],LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);

            TableRow tableRowForTableC = new TableRow(this.context);
            TextView textView = this.bodyTextView(day);
            textView.setPadding(0, 32, 0, 32);
            textView.setTextSize(16);
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            tableRowForTableC.addView(textView,params);

            return tableRowForTableC;
        }

        TableRow taleRowForTableD(List<Timetable> timetables){
            TableRow taleRowForTableD = new TableRow(this.context);

            int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

            SparseArray<String> sparseArray = new SparseArray<>();
            for(Timetable timtbl: timetables){
                sparseArray.put(timtbl.getPeriodNo(), timtbl.getSubjectName() + "\n [ " + timtbl.getTeacherName() + " ]");
            }

            for(int x=1 ; x<=loopCount; x++){
                TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x],LayoutParams.MATCH_PARENT);
                params.setMargins(2, 2, 0, 0);

                TextView textViewB = this.bodyTextView(sparseArray.get(x)!=null?sparseArray.get(x):"");
                textViewB.setPadding(8, 32, 8, 32);
                textViewB.setTextSize(14);
                textViewB.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
                taleRowForTableD.addView(textViewB, params);
            }
            return taleRowForTableD;

        }

        // table cell standard TextView
        TextView bodyTextView(String label){
            TextView bodyTextView = new TextView(this.context);
            bodyTextView.setBackgroundColor(Color.WHITE);
            bodyTextView.setText(label);
            bodyTextView.setGravity(Gravity.CENTER);
            bodyTextView.setPadding(5, 5, 5, 5);
            return bodyTextView;
        }

        // header standard TextView
        TextView headerTextView(String label){
            TextView headerTextView = new TextView(this.context);
            //   headerTextView.setBackgroundColor(Color.WHITE);
            headerTextView.setText(label);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setPadding(5, 5, 5, 5);

            return headerTextView;
        }

        // resizing TableRow height starts here
        void resizeHeaderHeight() {
            TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
            TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

        void getTableRowHeaderCellWidth(){
            int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
            int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();;

            for(int x=0; x<(tableAChildCount+tableBChildCount); x++){
                if(x==0){
                    this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
                }else{
                    this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
                }
            }
        }

        // resize body table row height
        void resizeBodyTableRowHeight(){
            int tableC_ChildCount = this.tableC.getChildCount();

            for(int x=0; x<tableC_ChildCount; x++){
                TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
                TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

                int rowAHeight = this.viewHeight(productNameHeaderTableRow);
                int rowBHeight = this.viewHeight(productInfoTableRow);

                TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
                int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

                this.matchLayoutHeight(tableRow, finalHeight);
            }
        }

        // match all height in a table row
        // to make a standard TableRow height
        private void matchLayoutHeight(TableRow tableRow, int height) {
            int tableRowChildCount = tableRow.getChildCount();

            // if a TableRow has only 1 child
            if(tableRow.getChildCount()==1){
                View view = tableRow.getChildAt(0);
                TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
                params.height = height - (params.bottomMargin + params.topMargin);
                return ;
            }

            // if a TableRow has more than 1 child
            for (int x = 0; x < tableRowChildCount; x++) {
                View view = tableRow.getChildAt(x);
                TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
                if (!isTheHeighestLayout(tableRow, x)) {
                    params.height = height - (params.bottomMargin + params.topMargin);
                    return;
                }
            }
        }

        // check if the view has the highest height in a TableRow
        private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {
            int tableRowChildCount = tableRow.getChildCount();
            int heighestViewPosition = -1;
            int viewHeight = 0;

            for (int x = 0; x < tableRowChildCount; x++) {
                View view = tableRow.getChildAt(x);
                int height = this.viewHeight(view);

                if (viewHeight < height) {
                    heighestViewPosition = x;
                    viewHeight = height;
                }
            }
            return heighestViewPosition == layoutPosition;
        }

        // read a view's height
        private int viewHeight(View view) {
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            return view.getMeasuredHeight();
        }

        // read a view's width
        private int viewWidth(View view) {
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            return view.getMeasuredWidth();
        }

        // horizontal scroll view custom class
        class MyHorizontalScrollView extends HorizontalScrollView{
            public MyHorizontalScrollView(Context context) {
                super(context);
            }
            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                String tag = (String) this.getTag();
                if(tag.equalsIgnoreCase("horizontal scroll view b")){
                    horizontalScrollViewD.scrollTo(l, 0);
                }else{
                    horizontalScrollViewB.scrollTo(l, 0);
                }
            }
        }

        // scroll view custom class
        class MyScrollView extends ScrollView{
            public MyScrollView(Context context) {
                super(context);
            }
            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                String tag = (String) this.getTag();
                if(tag.equalsIgnoreCase("scroll view c")){
                    scrollViewD.scrollTo(0, t);
                }else{
                    scrollViewC.scrollTo(0,t);
                }
            }
        }
    }

}
