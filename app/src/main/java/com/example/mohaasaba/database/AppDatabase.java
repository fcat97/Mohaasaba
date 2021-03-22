package com.example.mohaasaba.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mohaasaba.helper.IdGenerator;
import com.example.mohaasaba.helper.ThemeUtils;
import com.example.mohaasaba.models.Note;
import com.example.mohaasaba.models.Notify;
import com.example.mohaasaba.models.Reminder;
import com.example.mohaasaba.models.Schedule;
import com.example.mohaasaba.models.Task;
import com.example.mohaasaba.models.Transaction;
import com.example.mohaasaba.models.TransactionAccount;
import com.example.mohaasaba.models.TransactionPage;
import com.example.mohaasaba.plans.Plan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Database(entities = {Schedule.class, Note.class, Reminder.class,
        Transaction.class, TransactionPage.class, TransactionAccount.class,
        Plan.class},
        version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase appDatabaseInstance;

    public abstract ScheduleDao scheduleDao();
    public abstract NoteDao noteDao();
    public abstract ReminderDao reminderDao();
    public abstract TransactionAccountDao accountDao();
    public abstract TransactionPageDao transactionPageDao();
    public abstract TransactionDao transactionDao();
    public abstract PlanDao planDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabaseInstance == null) {
            appDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallbacks)
                    .build();
        }

        return appDatabaseInstance;
    }

    private static RoomDatabase.Callback roomCallbacks = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Thread thread = new Thread(AppDatabase::populateDatabase);
            thread.start();
        }
    };

    static final Migration MIGRATION_48_49 = new Migration(48, 49) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `transaction_table` (`entryKey` TEXT NOT NULL, " +
                    "`commitTime` TEXT, " +
                    "`note` TEXT, " +
                    "`tags` TEXT, " +
                    "`amount` REAL NOT NULL, " +
                    "`unit` TEXT, " +
                    "`account` TEXT, " +
                    "`page` TEXT, " +
                    " PRIMARY KEY(`entryKey`))");

            database.execSQL("CREATE TABLE `transaction_account` (`accountID` TEXT NOT NULL, " +
                    "`name` TEXT, " +
                    "`balance` REAL NOT NULL, " +
                    " PRIMARY KEY(`accountID`))");
        }
    };
    static final Migration MIGRATION_49_50 = new Migration(49, 50) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            String id = IdGenerator.getNewID();
            String name = "Cash";
            float balance = 0.0f;
            database.execSQL("INSERT INTO transaction_account (accountID, name, balance) " +
                    "VALUES ('" + id + "', '" + name + "', " + balance + ")");
        }
    };


    private static void populateDatabase() {
        ScheduleDao scheduleDao = appDatabaseInstance.scheduleDao();

        Schedule schedule = new Schedule("Quick Tutorial");
        schedule.setThemeID(ThemeUtils.THEME_GREEN);

        Notify notify1 = new Notify(0,0);
        notify1.label = schedule.getTitle();
        notify1.message = "You can add Reminders here!";

        Notify notify2 = new Notify(0,0);
        notify2.label = schedule.getTitle();
        notify2.message = "Click on the + icon in top right corner";

        schedule.getNotifyList().add(notify1);
        schedule.getNotifyList().add(notify2);


        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("You can add new task from bottom"));
        taskList.add(new Task("Each can contain certain specification"));
        taskList.add(new Task("Task Progress is saved per day"));
        taskList.add(new Task("New Day New Progress"));
        taskList.add(new Task("Tap and hold to progress task"));
        taskList.add(new Task("the trash icon will delete task"));
        taskList.add(new Task("Tapping circular Progress View on top left you can go to specific date"));
        taskList.add(new Task("Click 'Daily Schedule' text to change schedule type"));
        taskList.add(new Task("Remember to save your change"));

        schedule.getHistory().commitTodo(Calendar.getInstance(), taskList);

        scheduleDao.insertSchedule(schedule);

        // Add default Transaction Account to Database
        TransactionAccountDao accountDao = appDatabaseInstance.accountDao();
        TransactionAccount transactionAccount = new TransactionAccount();
        transactionAccount.name = TransactionAccount.DEFAULT_ACCOUNT;
        transactionAccount.balance = TransactionAccount.DEFAULT_BALANCE;
        accountDao.insert(transactionAccount);

        // Create A default page
        TransactionPageDao pageDao = appDatabaseInstance.transactionPageDao();
        TransactionPage transactionPage = new TransactionPage(Transaction.DEFAULT_PAGE);
        pageDao.insert(transactionPage);
    }
}
