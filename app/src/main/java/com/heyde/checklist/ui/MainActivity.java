package com.heyde.checklist.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;

import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.heyde.checklist.R;
import com.heyde.checklist.model.FileController;
import com.heyde.checklist.model.Task;
import com.heyde.checklist.model.TaskList;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.heyde.checklist.R.id.toolbar;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    RelativeLayout mMainLayout;
    @BindView(R.id.refreshButton)
    Button mRefreshButton;
    @BindView(R.id.addButton)
    ImageButton mAddButton;
    @BindView(R.id.DraggableListLayout)
    com.jmedeisis.draglinearlayout.DragLinearLayout mDragLinearLayout;
    @BindView(R.id.referenceTask)
    TextView mReferenceText;
    @BindView(R.id.referenceButton)
    ImageButton mReferenceButton;
    @BindView(R.id.noListsDisplay)
    ViewGroup mNoLists;
    @BindView(R.id.scroll)
    ScrollView mScrollView;
    @BindView(toolbar)
    Toolbar mToolbar;

    private TextView mTitleText;
    private boolean mTasksEditable = false;
    private TaskList mWorkingList;
    private Context mContext;
    private FileController mFileController;
    private TaskList mPreviousList;
    private ImageView mDropDownCarrot;
    private boolean mDeleteMode = false;
    private boolean mInEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(mToolbar);

        getSupportActionBar().setCustomView(R.layout.listname_actionbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        layoutInflater.inflate(R.layout.listname_actionbar, null);
        mTitleText = (TextView) findViewById(R.id.titleEditText);
        mDropDownCarrot = (ImageView) findViewById(R.id.imageDropDown);
        mDragLinearLayout.setContainerScrollView(mScrollView);

        mFileController = new FileController(this);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDeleteMode) {
                    mWorkingList.refreshList();
                    mFileController.saveList(mWorkingList);
                } else {
                    mWorkingList.deleteSelected();
                    displayTable(mWorkingList);
                    mFileController.saveList(mWorkingList);
                }
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(true);
            }
        });
        mTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWorkingList != null) {
                    //FIXME shows "New List" when clicking title (only happens after tutorial)
                    if (mInEditMode) {
                        createListTitle(false);
                    } else {
                        PopupMenu popup = new PopupMenu(mContext, mTitleText);
                        popup.getMenu().add(mWorkingList.getName());
                        for (String filename : mFileController.getAvailableFiles()) {
                            if (!filename.equals(mWorkingList.getName())) {
                                popup.getMenu().add(filename);
                            }
                        }
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switchToLoadedList(item);
                                return true;
                            }
                        });
                        try {
                            popup.show();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
            }
        });
        mTitleText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mFileController.getAvailableFiles().isEmpty()) {
                    titleOptions();
                }
                return true;
            }
        });
        mDropDownCarrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTasksEditable) {
                    createListTitle(false);
                } else {
                    PopupMenu popup = new PopupMenu(mContext, mTitleText);
                    popup.getMenu().add(mWorkingList.getName());
                    for (String filename : mFileController.getAvailableFiles()) {
                        if (!filename.equals(mWorkingList.getName())) {
                            popup.getMenu().add(filename);
                        }
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switchToLoadedList(item);
                            return false;
                        }
                    });
                    try {
                        popup.show();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        });
        mDropDownCarrot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mFileController.getAvailableFiles().isEmpty()) {
                    titleOptions();
                }
                return true;
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyLaunched = prefs.getBoolean(getString(R.string.Prev_Launch), false);

        if (!previouslyLaunched) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.Prev_Launch), Boolean.TRUE);
            edit.commit();

            mWorkingList = new TaskList(this, MainActivity.this);

            setNoListsVisible(false);
            Task newTask = new Task("Sample Task", false, this, mReferenceText, mReferenceButton);
            mWorkingList.addTask(newTask);
            mWorkingList.makeNewLine(newTask);

            displayTable(mWorkingList);
            final LinearLayout lLayout = (LinearLayout) mDragLinearLayout.getChildAt(0);


            mToolbar.inflateMenu(R.menu.action_button);
            final ShowcaseView showcase = new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setTarget(new ViewTarget(mToolbar.findViewById(R.id.add_new_list))) //Here is where you supply the id of the action bar item you want to display
                    .setContentTitle("Create a List")
                    .setContentText("Press to create a new list.")
                    .build();
            showcase.setButtonText("next");
            showcase.setStyle(R.style.CustomShowcase);


            showcase.overrideButtonClick(new View.OnClickListener() {
                int count = 0;

                @Override
                public void onClick(View v) {
                    count++;
                    switch (count) {
                        case 1:
                            showcase.setTarget(new ViewTarget(mAddButton));
                            showcase.setContentTitle("Add a Task");
                            showcase.setContentText("Press to add tasks to your list.");
                            break;

                        case 2:
                            showcase.setTarget(new ViewTarget(mToolbar.findViewById(R.id.titleEditText)));
                            showcase.setContentTitle("Access Lists");
                            showcase.setContentText("Press to access saved lists. \nHold to delete or rename the current list.");
                            break;

                        case 3:

                            showcase.setTarget(new OffsetViewTarget(lLayout, -282, 0, MainActivity.this)); // task
                            showcase.setContentTitle("Task");
                            showcase.setContentText("Tap a task to check it off. Hold it to edit or delete. Use drag handle to drag.");
                            break;

                        case 4:
                            showcase.setTarget(new ViewTarget(mRefreshButton));
                            showcase.setContentTitle("\n\nUncheck All");
                            showcase.setContentText("Press to clear all checkmarks. When in edit mode, press to delete selected tasks.");
                            break;

                        default:
                            showcase.hide();
                            mFileController.deleteList(mWorkingList.getName());
                            mWorkingList = null;
                            displayList(0);
                            break;

                    }
                }
            });
        } else {
            displayList(0);
        }


    }

    private void titleOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] options = new String[]{"Rename List", "Delete List"};
        builder.setTitle("List Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int choice) {
                if (choice == 0) { //rename list
                    createListTitle(false);
                } else { // delete list
                    confirmDelete();
                }
            }
        });

        builder.show();
    }

    private void setNoListsVisible(boolean visible) {
        if (visible) {
            mDragLinearLayout.removeAllViews();
            mTitleText.setText(R.string.app_name);
            mNoLists.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
            mAddButton.setVisibility(View.INVISIBLE);
            mDropDownCarrot.setVisibility(View.INVISIBLE);
        } else {
            mNoLists.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
            mAddButton.setVisibility(View.VISIBLE);
            mDropDownCarrot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mFileController.getAvailableFiles().isEmpty()) {
            mFileController.saveList(mWorkingList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_new_list:
                if (mInEditMode) {
                    confirmDelete();
                } else {
                    createNewList();
                }
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button, menu);
        return true;
    }

    private void createListTitle(final boolean newlist) {

        // http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        if (newlist) {
            builder.setTitle("Add a New List");
        } else {
            builder.setTitle("Rename List");
            input.setText(mWorkingList.getName());
        }

        // specify input type
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setSelection(input.getText().length());
        builder.setView(input); // takes builder, tells it to show the edittext that we're now editing


        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();

                if (newlist) { // setting a name for a new list
                    mWorkingList = new TaskList(mContext, MainActivity.this);
                    mFileController.saveList(mWorkingList);
                    mWorkingList.setName(newText);
                    mTitleText.setText(mWorkingList.getName());
                    mFileController.saveList(mWorkingList);
                    setNoListsVisible(false);
                    displayTable(mWorkingList);
                } else { // changing the name of an existing list
                    String prevName = mWorkingList.getName();
                    mWorkingList.setName(newText);
                    mFileController.saveList(mWorkingList);
                    mFileController.deleteList(prevName);
                    mTitleText.setText(mWorkingList.getName());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();// closes pop up dialog without input
                if (!mFileController.getAvailableFiles().isEmpty()) {
                    displayTable(mWorkingList);
                }
            }
        });

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(Color.GREEN);

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTextColor(Color.RED);
    }

    public void addTask(final boolean newTask) {
        addTask(newTask, null);
    }


    public void addTask(final boolean newTask, final Task oldTask) {

        // http://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(mContext);
        if (newTask) {
            builder.setTitle("Add a New Task");
        } else {
            builder.setTitle("Edit Task");
            input.setText(oldTask.getTaskText());// old task text here
        }

        // specify input type
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        input.setSelection(input.getText().length());
        builder.setView(input); // takes builder, tells it to show the edittext that we're now editing


        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newTask) {
                    String newText = input.getText().toString();
                    Task newTask = new Task(newText, false, mContext, mReferenceText, mReferenceButton);
                    mWorkingList.addTask(newTask);
                    mWorkingList.makeNewLine(newTask);
                } else {
                    oldTask.setTaskText(input.getText().toString());
                    switchToDelete();
                    mWorkingList.refreshRows();
                }
                mFileController.saveList(mWorkingList);
                displayTable(mWorkingList);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();// closes pop up dialog without input
                switchToDelete();
            }
        });

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

//        alertDialog.getAppli

        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(Color.GREEN);

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTextColor(Color.RED);


    }

    public void displayTable(TaskList taskList) { // used to display tasks from current table
        mDragLinearLayout.removeAllViews();
        for (LinearLayout layout : taskList.getLayoutRows()) {
            mDragLinearLayout.addView(layout);
        }

        for (int i = 0; i < mDragLinearLayout.getChildCount(); i++) {
            LinearLayout parent = (LinearLayout) mDragLinearLayout.getChildAt(i);
            View child = parent.getChildAt(0);
            mDragLinearLayout.setViewDraggable(parent, child);
        }

        mDragLinearLayout.setOnViewSwapListener(new DragLinearLayout.OnViewSwapListener() {
            @Override
            public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
                mWorkingList.reorderList(firstPosition, secondPosition);
            }
        });

    }

    private void displayList(int pos) { // used to call a different list or to call the initial list
        if (mFileController.getAvailableFiles().size() > 0) {
            setNoListsVisible(false);
            String initListName = mFileController.getAvailableFiles().get(pos);
            ArrayList<String> initListStrings = new ArrayList<>(mFileController.loadFile(initListName));
            mWorkingList = fileToList(initListStrings, initListName);
            mTitleText.setText(mWorkingList.getName());
            displayTable(mWorkingList);
        } else {
            setNoListsVisible(true);
        }
    }

    private void createNewList() {
        createListTitle(true);
    }

    private void switchToLoadedList(MenuItem item) {
        if (!item.getTitle().equals(mWorkingList.getName())) {
            mFileController.saveList(mWorkingList); // save old list before rewriting table
            String itemName = item.getTitle().toString();
            ArrayList<String> taskList = new ArrayList<>(mFileController.loadFile(itemName));
            mWorkingList = fileToList(taskList, itemName);
            mTitleText.setText(mWorkingList.getName());
            displayTable(mWorkingList);
        }
    }
//
//    private void switchToPreviousList() { // should I set it straight to workinglist?
//        mFileController.saveList(mWorkingList); // save old list before rewriting table
//        if (mFileController.getAvailableFiles().contains(mPreviousList.getName())) {
//            ArrayList<String> taskList = new ArrayList<>(mFileController.loadFile(mPreviousList.getName()));
//            mWorkingList = fileToList(taskList, mPreviousList.getName());
//        }
//        mTitleText.setText(mWorkingList.getName());
//        displayTable(mWorkingList);
//    }

    private TaskList fileToList(ArrayList<String> inputList, String name) {
        TaskList newList = new TaskList(mContext, MainActivity.this);

        for (String taskText : inputList) {
            String[] taskProps;
            String taskName;
            boolean isChecked;

            taskProps = taskText.split(":::");
            if (taskProps[1].equals("false")) {
                isChecked = false;
            } else {
                isChecked = true;
            }

            taskName = taskProps[0].trim();

            Task newTask = new Task(taskName, isChecked, mContext, mReferenceText, mReferenceButton);

            newList.addTask(newTask);
            newList.makeNewLine(newTask);
        }

        newList.setName(name);

        return newList;
    }

    private void confirmDelete() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want to delete " + "\"" + mWorkingList.getName() + "\"?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = mWorkingList.getName();
//                if (mFileController.getAvailableFiles().contains(mPreviousList.getName()) && !listName.equals(mPreviousList.getName())) { // switch to mPreviousList if prev list exists and is not mWorkingList
//                    switchToPreviousList();
//                } else { // if the last open list is no longer there
                int num = 0;
                if (mFileController.getAvailableFiles().size() > 1) { // if there are other lists available, pick the first one that is not the list to be deleted
                    while (mFileController.getAvailableFiles().get(num).equals(listName)) {
                        num++;
                    }
                    displayList(num);
                }
//                }
                mFileController.deleteList(listName);
                if (mFileController.getAvailableFiles().size() == 0) {
                    mWorkingList = null;
                    setNoListsVisible(true);
                }
                Toast toast;
                if (!mFileController.getAvailableFiles().contains(listName)) {
                    toast = Toast.makeText(mContext, "\"" + listName + "\"" + " successfully deleted", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(mContext, "Error while deleting list.", Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
                toast.show(); //perhaps later, a snackbar instead of toast
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(Color.GREEN);

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setTextColor(Color.RED);


    }

    public void switchToDelete() {
        if (mWorkingList != null) { // will be null if there is no working list, ie. all lists are deleted or app has been opened for first time
            int deletableTasks = mWorkingList.getDeleteListSize();
            if (deletableTasks > 0) {
                if (deletableTasks == 1) {
                    mRefreshButton.setText("Delete " + deletableTasks + " item");

                } else {
                    mRefreshButton.setText("Delete " + deletableTasks + " items");
                }

                mAddButton.setVisibility(View.INVISIBLE);
                mDeleteMode = true;
            } else {
                mDeleteMode = false;
                mAddButton.setVisibility(View.VISIBLE);
                mRefreshButton.setText(R.string.Clear_Progress);
            }
        }
    }


}



