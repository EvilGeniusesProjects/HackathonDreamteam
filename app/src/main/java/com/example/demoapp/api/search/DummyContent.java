package com.example.demoapp.api.search;

import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {

    public static final List<Persons> ITEMS = new ArrayList<Persons>();


    static {
        // Add some sample items.
            ITEMS.add(new Persons(1, "Азат", "Aллабердин", "Уралович", R.drawable.prof1,  1, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(2, "Богдан", "Горбунов", "Уралович", R.drawable.prof3,  2, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(3, "Руслан", "Яхин", "Уралович", R.drawable.prof2,  3, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(4, "Кирилл", "Ахмадиев", "Уралович", R.drawable.prof3,  3, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(5, "Марат", "Юремин", "Уралович", R.drawable.prof2,  3, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(6, "Олег", "Меркулов", "Уралович", R.drawable.prof1,  2, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(7, "Азамат", "Одинцов", "Уралович", R.drawable.prof3,  2, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(8, "Илья", "Пирожков", "Уралович", R.drawable.prof2,  1, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(9, "Артур", "Кирюхин", "Уралович", R.drawable.prof3,  2, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
            ITEMS.add(new Persons(10, "Ренат", "Касимов", "Уралович", R.drawable.prof1,  1, "demo@mail.ru", "8 (800)-555-35-35", "Младший инженер"));
    }

    public static class Persons {
        public final int id;
        public final String name;
        public final String sureName;
        public final String secondName;
        public final int image;
        public int status;
        public String email;
        public String number;
        public String role;


        public Persons(int id, String name, String sureName, String secondName, int image, int status, String email, String number, String role) {
            this.id = id;
            this.name = name;
            this.sureName = sureName;
            this.secondName = secondName;
            this.image = image;
            this.status = status;
            this.email = email;
            this.number = number;
            this.role = role;
        }
    }
}
