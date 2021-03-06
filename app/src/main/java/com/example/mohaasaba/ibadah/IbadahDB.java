package com.example.mohaasaba.ibadah;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mohaasaba.database.DataConverter;
import com.example.mohaasaba.ibadah.bookshelf.Book;
import com.example.mohaasaba.ibadah.bookshelf.BookDao;
import com.example.mohaasaba.ibadah.tasbih.Tasbih;
import com.example.mohaasaba.ibadah.tasbih.TasbihDao;

@Database(entities = {Book.class, Tasbih.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class IbadahDB extends RoomDatabase {
    private static IbadahDB ibadahDBInstance;

    public abstract BookDao bookDao();
    public abstract TasbihDao tasbihDao();

    public static synchronized IbadahDB getInstance(Context context) {
        if (ibadahDBInstance == null) {
            ibadahDBInstance = Room.databaseBuilder(context.getApplicationContext(), IbadahDB.class, "bookDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallbacks)
                    .build();
        }

        return ibadahDBInstance;
    }


    private static RoomDatabase.Callback roomCallbacks = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Thread thread = new Thread(IbadahDB::populateDatabase);
            thread.start();
        }
    };

    private static void populateDatabase() {
        createFazrTable();
    }

    private static void createFazrTable() {
        TasbihDao tasbihDao = ibadahDBInstance.tasbihDao();

        Tasbih tasbih_1 = new Tasbih();
        tasbih_1.tasbihType = Tasbih.TasbihType.AFTER_FAZR;
        tasbih_1.label = "আয়াতুল কুরসি";
        tasbih_1.hadith_ar = "اَللّٰهُ لَاۤ اِلٰهَ اِلَّا هُوَ ۚ اَلۡحَیُّ الۡقَیُّوۡمُ ۚ لَا تَاۡخُذُهٗ سِنَةٌ وَّلَا نَوۡمٌ ؕ لَهٗ مَا فِی السَّمٰوٰتِ وَمَا فِی الۡاَرۡضِ ؕ مَنۡ ذَا الَّذِیۡ یَشۡفَعُ عِنۡدَهٗۤ اِلَّا بِاِذۡنِهٖ ؕ یَعۡلَمُ مَا بَیۡنَ اَیۡدِیۡهِمۡ وَمَا خَلۡفَهُمۡ ۚ وَلَا یُحِیۡطُوۡنَ بِشَیۡءٍ مِّنۡ عِلۡمِهٖۤ اِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرۡسِیُّهُ السَّمٰوٰتِ وَالۡاَرۡضَ ۚ وَلَا یَـُٔوۡدُهٗ حِفۡظُهُمَا ۚ وَهُوَ الۡعَلِیُّ الۡعَظِیۡمُ";
        tasbih_1.hadith_bn = "আল্লাহ ছাড়া অন্য কোন উপাস্য নেই, তিনি জীবিত, সবকিছুর ধারক। তাঁকে তন্দ্রাও স্পর্শ করতে পারে না এবং নিদ্রাও নয়। আসমান ও যমীনে যা কিছু রয়েছে, সবই তাঁর। কে আছ এমন, যে সুপারিশ করবে তাঁর কাছে তাঁর অনুমতি ছাড়া? দৃষ্টির সামনে কিংবা পিছনে যা কিছু রয়েছে সে সবই তিনি জানেন। তাঁর জ্ঞানসীমা থেকে তারা কোন কিছুকেই পরিবেষ্টিত করতে পারে না, কিন্তু যতটুকু তিনি ইচ্ছা করেন। তাঁর সিংহাসন সমস্ত আসমান ও যমীনকে পরিবেষ্টিত করে আছে। আর সেগুলোকে ধারণ করা তাঁর পক্ষে কঠিন নয়। তিনিই সর্বোচ্চ এবং সর্বাপেক্ষা মহান।";
        tasbih_1.references = "হাদীসটি হাকিম সংকলন করেছেন, ১/৫৬২। আর শাইখ আলবানী একে সহীহুত তারগীব ওয়াত-তারহীবে সহীহ বলেছেন ১/২৭৩। আর তিনি একে নাসাঈ, তাবারানীর দিকে সম্পর্কযুক্ত করেছেন এবং বলেছেন, তাবারানীর সনদ ‘জাইয়্যেদ’ বা ভালো।";
        tasbih_1.reward = "১। যে ব্যাক্তি প্রত্যেক সালাতের পর আয়াতুল কুরসী পাঠ করবে তার জান্নাতে প্রবেশের পথে কেবল মৃত্যুই বাধা। \n" +
                "২। যে ব্যাক্তি ফরয সালাতের শেষে আায়াতুল কুরসী পাঠ করবে সে পরবর্তি সালাত পর্যন্ত আল্লাহর জিম্মায় থাকবে। (রাহে বেলায়াত ৭ম/৪৫৭)";

        Tasbih tasbih_2 = new Tasbih();
        tasbih_2.tasbihType = Tasbih.TasbihType.AFTER_FAZR;
        tasbih_2.label = "حَسْبِيَ اللّٰهُ لاَ إِلَهَ إِلاَّ هُوَ";
        tasbih_2.hadith_ar = "حَسْبِيَ اللّٰهُ لاَ إِلَهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيْمِ";
        tasbih_2.hadith_bn = "আল্লাহই আমার জন্য যথেষ্ট, তিনি ছাড়া আর কোনো হক্ব ইলাহ নেই। আমি তাঁর উপরই ভরসা করি। আর তিনি মহান আরশের রব্ব।";
        tasbih_2.references = "ইবনুস সুন্নী, নং ৭১, মারফূ‘ সনদে; আবূ দাউদ ৪/৩২১; মাওকূফ সনদে, নং ৫০৮১। আর শাইখ শু‘আইব ও আব্দুল কাদের আরনাঊত এর সনদকে সহীহ বলেছেন। দেখুন, যাদুল মা‘আদ ২/৩৭৬।";
        tasbih_2.reward = "যে ব্যাক্তি সকাল ও সন্ধায় ৭ বার এটি বলবে আল্লাজ তায়ালা তার দুশ্চিন্তা, উৎকণ্ঠা ও সমস্যা মিটিয়ে দেবেন। (রাহে বেলায়াত ৭ম/৪৮৩)";

        Tasbih tasbih_3 = new Tasbih();
        tasbih_3.tasbihType = Tasbih.TasbihType.AFTER_FAZR;
        tasbih_3.label = "বিষধর প্রাণীর ক্ষতি থেকে নিরাপত্তাَ";
        tasbih_3.hadith_ar = "أَعُوْذُ بِكَلِمَاتِ اللّٰهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ";
        tasbih_3.hadith_bn = "হযরত আবু হোরাযরা (রাঃ) বর্ণনা করেনঃ\n" +
                "নবী করীম সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম এরশাদ করেন, যে ব্যাক্তি সন্ধার সময় তিনবার এই কালিমাগুলি বলবে- “আল্লাহ্\u200Cর পরিপূর্ণ কালেমাসমূহের ওসিলায় আমি তাঁর নিকট তাঁর সৃষ্টির ক্ষতি থেকে আশ্রয় চাই।\" সে রাত্রে কোনো প্রকার বিষ তার ক্ষতি করতে পারবে না।";
        tasbih_3.references = "মুন্তাখাব হাদীস ২য়/৪৬৬, আহমাদ ২/২৯০, নং ৭৮৯৮; নাসাঈ, আমালুল ইয়াওমি ওয়াল লাইলাহ, নং ৫৯০; ইবনুস সুন্নী, নং ৬৮; আরও দেখুন, সহীহুত তিরমিযী ৩/১৮৭; সহীহ ইবন মাজাহ ২/২৬৬; তুহফাতুল আখইয়ার লি ইবন বায, পৃ. ৪৫।";
        tasbih_3.reward = "যে ব্যাক্তি সন্ধার সময় তিনবার এই কালিমাগুলি বলবে- “আল্লাহ্\u200Cর পরিপূর্ণ কালেমাসমূহের ওসিলায় আমি তাঁর নিকট তাঁর সৃষ্টির ক্ষতি থেকে আশ্রয় চাই।\" সে রাত্রে কোনো প্রকার বিষ তার ক্ষতি করতে পারবে না।";

        tasbihDao.insert(tasbih_1);
        tasbihDao.insert(tasbih_2);
        tasbihDao.insert(tasbih_3);
    }
}
